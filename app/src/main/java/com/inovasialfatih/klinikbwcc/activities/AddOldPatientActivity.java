package com.inovasialfatih.klinikbwcc.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.DataOldPatientResponse;
import com.inovasialfatih.klinikbwcc.model.RequestCekRmResult;
import com.inovasialfatih.klinikbwcc.utils.Tools;
import com.inovasialfatih.klinikbwcc.views.Loadie;
import com.inovasialfatih.klinikbwcc.views.Toasty;
import com.joanzapata.iconify.widget.IconTextView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOldPatientActivity extends BaseActivity {

    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;

    @BindView(R.id.patient_id) EditText mPatientId;
    @BindView(R.id.date_of_birth) EditText mDateOfBirth;

    @BindView(R.id.id_patient) TextView mIDPatinet;
    @BindView(R.id.pasien_name) TextView mPatientName;
    @BindView(R.id.place_of_birth) TextView mPlaceOfBirth;
    @BindView(R.id.dob_patient) TextView mDobPatient;

    @BindView(R.id.result_layout) LinearLayout mResultLayout;

    @BindView(R.id.et_address) EditText mAddress;
    @BindView(R.id.et_handphone) EditText mHandphone;
    @BindView(R.id.et_payment_type) EditText mPayType;


    private String selectedDate = "";
    private String selectedPayType ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_add_old_patient);

        initControl();

    }

    private void initControl(){
        mToolbarTitle.setText("Add Old Patient");

        mDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDatePickerBorn(v);

            }
        });

        mPayType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayType(v);

            }
        });
    }

    private void dialogDatePickerBorn(final View v) {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date = calendar.getTimeInMillis();
                        ((EditText) v).setText(Tools.getFormattedDateShort(date));

                        selectedDate = String.format("%02d-%02d-%d", dayOfMonth, monthOfYear+1, year);
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.setMaxDate(cur_calender);
        datePicker.show(getFragmentManager(), "Date of Birth");
    }

    private void showPayType(final View v) {
        final String[] array = new String[]{
                "UMUM", "ASURANSI"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Payment Type");
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedPayType = array[i];
                ((EditText) v).setText(array[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void requestRmResponse(){
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Checking Data.");
        loader.show();

        Call<RequestCekRmResult> call = _gate.requestRm().requestRm(
                _cooky.token(),
                mPatientId.getText().toString(),
                selectedDate);

        call.enqueue(new Callback<RequestCekRmResult>() {
            @Override
            public void onResponse(Call<RequestCekRmResult> call, Response<RequestCekRmResult> response) {
                RequestCekRmResult result = response.body();

                if(result != null) {
                    if(result.isStatus()) {
                        if (result.data != null){
                            mIDPatinet.setText(result.data.kode_rm);
                            mPatientName.setText(result.data.nama);
                            mPlaceOfBirth.setText(result.data.tempat_lahir);
                            mDobPatient.setText(result.data.tanggal_lahir);

                            mResultLayout.setVisibility(View.VISIBLE);
                        } else {
                            Toasty.error(_ctx, HRes.string(_ctx, R.string.error_patient_data_));
                        }
                    }
                }

                loader.dismiss();
            }

            @Override
            public void onFailure(Call<RequestCekRmResult> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    private void requestRm(){
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Saving Patient Data.");
        loader.show();

        Call<DataOldPatientResponse> call = _gate.requestRm().requestRmResult(
                _cooky.token(),
                mIDPatinet.getText().toString(),
                mAddress.getText().toString(),
                mHandphone.getText().toString(),
                selectedPayType);

        call.enqueue(new Callback<DataOldPatientResponse>() {
            @Override
            public void onResponse(Call<DataOldPatientResponse> call, Response<DataOldPatientResponse> response) {
                DataOldPatientResponse result = response.body();

                if(response.isSuccessful()) {
                    if(result != null) {
                        if (result.isStatus()){
                            AddOldPatientActivity.super.onBackPressed();
                            Toasty.success(_ctx, HRes.string(_ctx, R.string.success_save_patient));
                        }
                    }
                } else {
                    Toasty.info(_ctx, HRes.string(_ctx, R.string.info_patient_data));
                }

                loader.dismiss();
            }

            @Override
            public void onFailure(Call<DataOldPatientResponse> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    @OnClick(R.id.btn_check) void checkRm() {
        mPatientId.setError(null);
        mDateOfBirth.setError(null);

        String rm = mPatientId.getText().toString();
        String tgl_lahir = mDateOfBirth.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(rm)) {
            mPatientId.setError(HRes.string(_ctx, R.string.error_rm_empty));
            focusView = mPatientId;
            cancel = true;
        } else if (TextUtils.isEmpty(tgl_lahir)) {
            mDateOfBirth.setError(HRes.string(_ctx, R.string.error_tgl_lahir_empty));
            focusView = mDateOfBirth;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            requestRmResponse();
        }
    }

    @OnClick(R.id.btn_save) void savePatient(){

        mAddress.setError(null);
        mHandphone.setError(null);

        String address = mAddress.getText().toString();
        String handphone = mHandphone.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(address)) {
            mAddress.setError(HRes.string(_ctx, R.string.error_address_empty));
            focusView = mAddress;
            cancel = true;
        } else if (TextUtils.isEmpty(handphone)) {
            mHandphone.setError(HRes.string(_ctx, R.string.error_phone_empty));
            focusView = mHandphone;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            requestRm();
        }
    }

    @OnClick(R.id.toolbar_back) void back() {
        super.onBackPressed();
    }
}
