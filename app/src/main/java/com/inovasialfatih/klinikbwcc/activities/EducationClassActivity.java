package com.inovasialfatih.klinikbwcc.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.activities.BaseActivity;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.PatientListItem;
import com.inovasialfatih.klinikbwcc.model.RequestBookingClass;
import com.inovasialfatih.klinikbwcc.model.ScheduleClassResponse;
import com.inovasialfatih.klinikbwcc.utils.Tools;
import com.inovasialfatih.klinikbwcc.views.Loadie;
import com.inovasialfatih.klinikbwcc.views.Toasty;
import com.joanzapata.iconify.widget.IconTextView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EducationClassActivity extends BaseActivity {

    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;

    @BindView(R.id.et_select_patient) EditText etPatient;
    @BindView(R.id.et_select_date) EditText etDate;
    @BindView(R.id.et_select_class) EditText etClass;
    @BindView(R.id.et_select_instructor) EditText etInstructor;

    private String selectedDate = "";

    private PatientListItem.PatientDetail selectedPatient = null;
    private ScheduleClassResponse.ScheduleClass selectedClass = null;
    private ScheduleClassResponse.DataInstructor selectedInstructor = null;

    private List<PatientListItem.PatientDetail> patientList = new ArrayList<>();
    private List<ScheduleClassResponse.ScheduleResult> scheduleList = new ArrayList<>();

    private String mBookingId = "";
    private String reqID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_education_class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            reqID = extras.getString("TRX_ID");
            mBookingId = extras.getString("BOOKING_ID");
        }

        initControl();
        getPatients();

    }

    private void initControl() {
        mToolbarTitle.setText("Education Class");

        etPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPatientDialog(v);

            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDatePickerLight(v);

            }
        });

        etClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClassDialog(v);

            }
        });

        etInstructor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInstructorDialog(v);
            }
        });
    }

    private void showPatientDialog(final View v) {

        if(patientList.size() == 0) {
            Toasty.info(_ctx,"Patient not found.");
            return;
        }

        final String[] array = new String[patientList.size()];
        for (int i =0; i < patientList.size(); i++) {
            array[i] = patientList.get(i).nama;
        }

        Arrays.sort(array);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Patient");

        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);

                for (int j = 0; j < patientList.size(); j++) {
                    if(array[i].equals(patientList.get(j).nama))
                        selectedPatient = patientList.get(j);
                }
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void dialogDatePickerLight(final View v) {
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
                        selectedClass = null;
                        selectedInstructor = null;
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        Calendar tomorrow = Calendar.getInstance();
        //tomorrow.add(Calendar.DAY_OF_YEAR, 0);
        datePicker.setMinDate(tomorrow);
        Calendar oneWeek = Calendar.getInstance();
        oneWeek.add(Calendar.DAY_OF_YEAR, 29);

//        oneWeek.add(Calendar.DATE, 1);
        datePicker.setMaxDate(oneWeek);
        datePicker.show(getFragmentManager(), "Date");
    }

    private void showClassDialog(final View v) {
        if(TextUtils.isEmpty(selectedDate)) {
            Toast.makeText(_ctx, "Choose a date first", Toast.LENGTH_LONG).show();
            return;
        }

        List<ScheduleClassResponse.ScheduleClass> kelas = new ArrayList<>();
        for(int i = 0; i < scheduleList.size(); i++) {
            ScheduleClassResponse.ScheduleDate day = scheduleList.get(i).info_date;
            if(day.date_start.equals(selectedDate))
                kelas = scheduleList.get(i).schedule;
        }

        if(kelas.size() == 0) {
            Toasty.info(_ctx,"Schedule not found on this date.");
            return;
        }

        final String[] array = new String[kelas.size()];
        for (int i =0; i < kelas.size(); i++) {
            array[i] = kelas.get(i).class_name;
        }

        Arrays.sort(array);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Class");

        final List<ScheduleClassResponse.ScheduleClass> finalSchedules = kelas;
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);

                for (int j = 0; j < finalSchedules.size(); j++) {
                    if(array[i].equals(finalSchedules.get(j).class_name))
                        selectedClass = finalSchedules.get(j);
                }
                selectedInstructor = null;

                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void showInstructorDialog(final View v) {
        if(TextUtils.isEmpty(selectedDate)) {
            Toast.makeText(_ctx, "Choose a date first.", Toast.LENGTH_LONG).show();
            return;
        }

        if(selectedClass == null) {
            Toast.makeText(_ctx, "Choose class first.", Toast.LENGTH_LONG).show();
            return;
        }

        if(selectedClass.info_instructor.size() == 0) {
            Toast.makeText(_ctx, "Instructor were not found on this class.", Toast.LENGTH_LONG).show();
            return;
        }

        final List<ScheduleClassResponse.DataInstructor> instructors = selectedClass.info_instructor;

        final String[] array = new String[instructors.size()];
        for (int i =0; i < instructors.size(); i++) {
            array[i] = instructors.get(i).name_instructor;
        }

        Arrays.sort(array);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Instructor");
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);

                for (int j = 0; j < instructors.size(); j++) {
                    if(array[i].equals(instructors.get(j).name_instructor))
                        selectedInstructor = instructors.get(j);
                }

                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void getPatients() {
        Call<PatientListItem> call = _gate.patient().getPatientList(_cooky.token());
        call.enqueue(new Callback<PatientListItem>() {
            @Override
            public void onResponse(Call<PatientListItem> call, Response<PatientListItem> response) {
                PatientListItem result = response.body();

                if(result != null) {
                    if(result.isStatus()) {
                        if (result.data != null){
                            patientList.addAll(result.data);
                        }
                    }
                }

                getSchedules();
            }

            @Override
            public void onFailure(Call<PatientListItem> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                getSchedules();
            }
        });
    }

    private void getSchedules(){
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Get Schedule Data.");
        loader.show();

        Call<ScheduleClassResponse> call = _gate.schedule().getScheduleClass(
                "m3svkHTbtMPiuIHybgdjDjsW2hEE29YN");
        call.enqueue(new Callback<ScheduleClassResponse>() {
            @Override
            public void onResponse(Call<ScheduleClassResponse> call, Response<ScheduleClassResponse> response) {
                ScheduleClassResponse result = response.body();

                if(result != null) {
                    if(result.status.equals("Success")) {
                        scheduleList = result.data.result;
                    }
                }

                loader.dismiss();
            }

            @Override
            public void onFailure(Call<ScheduleClassResponse> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    private void requestClass(){
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Registering queue.");
        loader.show();

        Call<RequestBookingClass> call;
        if(!TextUtils.isEmpty(mBookingId))
            call = _gate.requestBooking().cancelAndrequestBookingClass(
                    mBookingId,
                    _cooky.token(),
                    selectedInstructor.id_schedule,
                    selectedPatient.pasien_id
                    );
        else
            call = _gate.requestBooking().requestBookingClass(
                _cooky.token(),
                    selectedInstructor.id_schedule,
                    selectedPatient.pasien_id);
        call.enqueue(new Callback<RequestBookingClass>() {
            @Override
            public void onResponse(Call<RequestBookingClass> call, Response<RequestBookingClass> response) {
                RequestBookingClass result = response.body();

                if(result != null) {
                    if(result.isStatus()) {
                        Bundle b = new Bundle();
                        b.putString("BOOKING_ID", result.data.booking_id);
                        b.putBoolean("FROM_REQUEST", true );
                        _nav.navigate(BookClassDetailActivity.class, b);
                        Toasty.success(_ctx, HRes.string(_ctx, R.string.success_registration_queue));
                    } else {
                        Toasty.error(_ctx, HRes.string(_ctx, R.string.error_schedule_booked));
                    }
                }

                loader.dismiss();
            }

            @Override
            public void onFailure(Call<RequestBookingClass> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    @OnClick(R.id.btn_register) void next(){
        etPatient.setError(null);
        etDate.setError(null);
        etClass.setError(null);
        etInstructor.setError(null);

        String name = etPatient.getText().toString();
        String clas = etClass.getText().toString();
        String instructor = etInstructor.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(name)) {
            etPatient.setError(HRes.string(_ctx, R.string.error_patient_empty));
            Toast.makeText(_ctx, "Choose the patient first.", Toast.LENGTH_LONG).show();
            focusView = etPatient;
            cancel = true;
        } else if (TextUtils.isEmpty(selectedDate)) {
            etDate.setError(HRes.string(_ctx, R.string.error_date_empty));
            Toast.makeText(_ctx, "Date cannot be empty", Toast.LENGTH_LONG).show();
            focusView = etDate;
            cancel = true;
        } else if (TextUtils.isEmpty(clas)) {
            etClass.setError(HRes.string(_ctx, R.string.error_class_empty));
            Toast.makeText(_ctx, "Class cannot be empty.", Toast.LENGTH_LONG).show();
            focusView = etClass;
            cancel = true;
        } else if(TextUtils.isEmpty(instructor)) {
            etInstructor.setError(HRes.string(_ctx,R.string.error_instructor_empty));
            Toast.makeText(_ctx, "Instructor name cannot be empty.", Toast.LENGTH_LONG).show();
            focusView = etInstructor;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            requestClass();
        }
    }

    @OnClick(R.id.toolbar_back) void back() {
        super.onBackPressed();
    }
}
