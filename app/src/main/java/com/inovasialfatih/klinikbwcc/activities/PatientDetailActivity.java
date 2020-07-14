package com.inovasialfatih.klinikbwcc.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.activities.BaseActivity;
import com.inovasialfatih.klinikbwcc.extensions.Cooky;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.PatientDeleteResponse;
import com.inovasialfatih.klinikbwcc.model.PatientDetailItem;
import com.inovasialfatih.klinikbwcc.model.PatientEditResponse;
import com.inovasialfatih.klinikbwcc.model.PatientListItem;
import com.inovasialfatih.klinikbwcc.views.Loadie;
import com.inovasialfatih.klinikbwcc.views.Toasty;
import com.joanzapata.iconify.widget.IconTextView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;

    @BindView(R.id.txt_name) TextView txtName;
    @BindView(R.id.txt_gender) TextView txtGender;
    @BindView(R.id.patient_id) TextView txtPatientId;
    @BindView(R.id.medical_record) TextView txtMedicalRecord;
    @BindView(R.id.place_of_birth) TextView txtPlaceofBirth;
    @BindView(R.id.date_of_birth) TextView txtDateofBirth;
    @BindView(R.id.patient_address) TextView txtAddress;
    @BindView(R.id.phone_number) TextView txtPhoneNumber;
    @BindView(R.id.email_address) TextView txtEmail;
    @BindView(R.id.payment_type) TextView txtPaymentType;

    @BindView(R.id.img_profile) CircularImageView imgProfile;

    private String mTrxId = "";

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_patient_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTrxId = extras.getString("TRX_ID");
        }

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.green_700);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPatientDetail();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },1000);
            }
        });

        initControl();
        getPatientDetail();
    }

    private void getPatientDetail() {
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Get patient details data.");
        loader.show();

        Call<PatientDetailItem> call = _gate.patient().getPatientDetail(
                mTrxId,
                _cooky.token());
        call.enqueue(new Callback<PatientDetailItem>() {
            @Override
            public void onResponse(Call<PatientDetailItem> call, Response<PatientDetailItem> response) {
                PatientDetailItem result = response.body();

                if(result != null) {
                    if(result.isStatus()) {
                        if (result.data != null){
                            txtName.setText(result.data.nama);
                            txtPatientId.setText(result.data.pasien_id);
                            txtMedicalRecord.setText(result.data.kode_rm);
                            txtPlaceofBirth.setText(result.data.tempat_lahir);
                            txtDateofBirth.setText(result.data.tanggal_lahir);
                            txtAddress.setText(result.data.alamat);
                            txtPhoneNumber.setText(result.data.no_hp);
                            txtEmail.setText(result.data.email);
                            txtPaymentType.setText(result.data.jenis_pembayaran);

                            if (result.data.jenis_kelamin.equals("LK")){
                                Glide.with(_ctx).load(R.drawable.man_patient).into(imgProfile);
                                txtGender.setText("MAN");
                            } else if (result.data.jenis_kelamin.equals("PR")){
                                Glide.with(_ctx).load(R.drawable.woman_patient).into(imgProfile);
                                txtGender.setText("WOMAN");
                            }

                        }
                    }
                }

                loader.dismiss();
            }

            @Override
            public void onFailure(Call<PatientDetailItem> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    private void initControl() {
        mToolbarTitle.setText("Patient Detail");
    }

    public void deletedPatient() {
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Deleting Patient.");
        loader.show();

        Call<PatientDeleteResponse> call = _gate.patient().patientDelete(
                _cooky.token(),
                txtPatientId.getText().toString());

        call.enqueue(new Callback<PatientDeleteResponse>() {
            @Override
            public void onResponse(Call<PatientDeleteResponse> call, Response<PatientDeleteResponse> response) {
                PatientDeleteResponse result = response.body();

                if(result != null) {
                    if(result.isStatus()) {
                        PatientDetailActivity.super.onBackPressed();
                        Toasty.success(_ctx, HRes.string(_ctx, R.string.success_delete_patient));
                    } else {
                        Toasty.error(_ctx, HRes.string(_ctx, R.string.failed_delete_patient));
                    }
                }
            }

            @Override
            public void onFailure(Call<PatientDeleteResponse> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
            }
        });
    }

    @OnClick(R.id.btn_delete) void deletePatient(){
        new android.support.v7.app.AlertDialog.Builder(_ctx)
                .setTitle(R.string.confirm_delete_patient)
                .setMessage(R.string.confirm_delete_patient_desc)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deletedPatient();



                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    @OnClick(R.id.btn_edit) void edit(){
        Bundle b = new Bundle();
        b.putString("TRX_ID", mTrxId);
        _nav.navigate(EditPatientActivity.class, b);

    }

    @OnClick(R.id.toolbar_back) void back() {
        super.onBackPressed();
    }

    @Override
    public void onResume(){
        super.onResume();
        getPatientDetail();
    }
}
