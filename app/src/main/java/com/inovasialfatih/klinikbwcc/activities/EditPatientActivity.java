package com.inovasialfatih.klinikbwcc.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.activities.BaseActivity;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.DataNewPatientResponse;
import com.inovasialfatih.klinikbwcc.model.PatientDetailItem;
import com.inovasialfatih.klinikbwcc.model.PatientEditResponse;
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

public class EditPatientActivity extends BaseActivity {

    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;

    @BindView(R.id.et_patient) EditText mPatient;
    @BindView(R.id.et_place_of_birth) EditText mPlaceOfBirth;
    @BindView(R.id.et_date_of_birth) EditText mDateOfBirth;
    @BindView(R.id.et_sex_type) EditText mSexType;
    @BindView(R.id.et_address) EditText mAddress;
    @BindView(R.id.et_handphone) EditText mHandphone;
    @BindView(R.id.et_email) EditText mEmail;
    @BindView(R.id.et_payment_type) EditText mPayType;

    private String mTrxId = "";
    private String patientID = "";

    private String selectedDate = "";
    private String selectedSexType ="";
    private String selectedPayType ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_edit_patient);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTrxId = extras.getString("TRX_ID");
        }

        initControl();
        getPatientDetail();
    }

    private void initControl(){
        mToolbarTitle.setText("Edit Patient");

        mDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDatePickerBorn(v);

            }
        });

        mSexType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSexType(v);

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

                        selectedDate = String.format("%d-%02d-%02d", year, monthOfYear+1, dayOfMonth);
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

    private void showSexType(final View v) {
        final String[] array = new String[]{
                "MAN", "WOMAN"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Gender");
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedSexType = array[i];
                ((EditText) v).setText(array[i]);
                switch (selectedSexType) {
                    case "MAN":
                        selectedSexType = "LK";
                        break;
                    case "WOMAN":
                        selectedSexType = "PR";
                        break;
                }
                dialogInterface.dismiss();
            }
        });
        builder.show();
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
                            patientID = result.data.pasien_id;
                            mPatient.setText(result.data.nama);
                            mPlaceOfBirth.setText(result.data.tempat_lahir);
                            mDateOfBirth.setText(result.data.tanggal_lahir);
                            mAddress.setText(result.data.alamat);
                            mHandphone.setText(result.data.no_hp);
                            mEmail.setText(result.data.email);
                            mPayType.setText(result.data.jenis_pembayaran);

                            selectedSexType = result.data.jenis_kelamin;
                            selectedDate = result.data.tanggal_lahir;
                            selectedPayType = result.data.jenis_pembayaran;


                            if (result.data.jenis_kelamin.equals("LK")){
                                mSexType.setText("MAN");
                            } else if (result.data.jenis_kelamin.equals("PR")){
                                mSexType.setText("WOMAN");
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

    public void editedPatient() {
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Editing Patient.");
        loader.show();

        Call<PatientEditResponse> call = _gate.patient().patientEdit(
                _cooky.token(),
                patientID,
                mPatient.getText().toString(),
                mPlaceOfBirth.getText().toString(),
                selectedDate,
                selectedSexType,
                mAddress.getText().toString(),
                mHandphone.getText().toString(),
                mEmail.getText().toString(),
                selectedPayType);

        call.enqueue(new Callback<PatientEditResponse>() {
            @Override
            public void onResponse(Call<PatientEditResponse> call, Response<PatientEditResponse> response) {
                PatientEditResponse result = response.body();

                if(result != null) {
                    if (result.isStatus()){
                        EditPatientActivity.super.onBackPressed();
                        Toasty.success(_ctx, HRes.string(_ctx, R.string.success_edit_patient));
                    } else if (result.is_show_message){
                        Toasty.info(_ctx, result.data);
                    }
                    else {
                        Toasty.info(_ctx, HRes.string(_ctx, R.string.failed_edit_patient));
                    }
                }

                loader.dismiss();

            }

            @Override
            public void onFailure(Call<PatientEditResponse> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    @OnClick(R.id.btn_save) void savePatient(){
        mPatient.setError(null);
        mPlaceOfBirth.setError(null);
        mDateOfBirth.setError(null);
        mSexType.setError(null);
        mAddress.setError(null);
        mHandphone.setError(null);
        mEmail.setError(null);
        mPayType.setError(null);

        String name = mPatient.getText().toString();
        String placeofbirht = mPlaceOfBirth.getText().toString();
        String dateofbirth = mDateOfBirth.getText().toString();
        String sextype = mSexType.getText().toString();
        String address = mAddress.getText().toString();
        String handphone = mHandphone.getText().toString();
        String email = mEmail.getText().toString();
        String paytype = mPayType.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(name)) {
            mPatient.setError(HRes.string(_ctx, R.string.error_name_empty));
            focusView = mPatient;
            cancel = true;
        } else if (TextUtils.isEmpty(placeofbirht)) {
            mPlaceOfBirth.setError(HRes.string(_ctx, R.string.error_tpt_lahir_empty));
            focusView = mPlaceOfBirth;
            cancel = true;
        } else if (TextUtils.isEmpty(dateofbirth)) {
            mDateOfBirth.setError(HRes.string(_ctx, R.string.error_tgl_lahir_empty));
            focusView = mDateOfBirth;
            cancel = true;
        } else if (TextUtils.isEmpty(sextype)) {
            mSexType.setError(HRes.string(_ctx, R.string.error_gender_empty));
            focusView = mSexType;
            cancel = true;
        } else if (TextUtils.isEmpty(address)) {
            mAddress.setError(HRes.string(_ctx, R.string.error_address_empty));
            focusView = mAddress;
            cancel = true;
        } else if (TextUtils.isEmpty(handphone)) {
            mHandphone.setError(HRes.string(_ctx, R.string.error_phone_empty));
            focusView = mHandphone;
            cancel = true;
        } else if (handphone.length() < 10) {
            mHandphone.setError(HRes.string(_ctx, R.string.error_min_phone_number));
            focusView = mHandphone;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            mEmail.setError(HRes.string(_ctx, R.string.error_email_empty));
            focusView = mEmail;
            cancel = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError(HRes.string(_ctx, R.string.enter_validate_email));
            focusView = mEmail;
            cancel = true;
        } else if (TextUtils.isEmpty(paytype)) {
            mPayType.setError(HRes.string(_ctx, R.string.error_payment_type_empty));
            focusView = mPayType;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            editedPatient();
        }
    }

    @OnClick(R.id.toolbar_back) void back() {
        super.onBackPressed();
    }
}
