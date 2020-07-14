package com.inovasialfatih.klinikbwcc.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.PatientListItem;
import com.inovasialfatih.klinikbwcc.model.RequestBooking;
import com.inovasialfatih.klinikbwcc.model.ScheduleResponse;
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

public class BookingOnlineActivity extends BaseActivity {

    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;
    @BindView(R.id.et_select_patient) EditText etPatient;
    @BindView(R.id.et_select_date) EditText etDate;
    @BindView(R.id.et_select_poli) EditText etPoli;
    @BindView(R.id.et_select_doctor) EditText etDokter;
    @BindView(R.id.et_select_hours) EditText etHours;

    private String selectedDate = "";

    private PatientListItem.PatientDetail selectedPatient = null;
    private ScheduleResponse.ScheduleDataPoly selectedPoli = null;
    private ScheduleResponse.ScheduleDataDoctors selectedDoctor = null;
    private ScheduleResponse.ScheduleTime selectedTime = null;

    private List<PatientListItem.PatientDetail> patientList = new ArrayList<>();
    private List<ScheduleResponse.ScheduleDataResult> scheduleList = new ArrayList<>();

    private String mBookingId = "";
    private String reqID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_booking_online);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            reqID = extras.getString("TRX_ID");
            mBookingId = extras.getString("BOOKING_ID");
        }

        initControl();
        getSchedules();
        getPatients();
    }

    private void initControl(){
        mToolbarTitle.setText("Online Booking");

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

        etPoli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPoliDialog(v);

            }
        });

        etDokter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDokterDialog(v);

            }
        });

        etHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHoursDialog(v);

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

        final List<PatientListItem.PatientDetail> finalPatients = patientList;
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);

                for (int j = 0; j < finalPatients.size(); j++) {
                    if(array[i].equals(finalPatients.get(j).nama))
                        selectedPatient = finalPatients.get(j);
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
                        selectedPoli = null;
                        selectedDoctor = null;
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

    private void showPoliDialog(final View v) {
        if(TextUtils.isEmpty(selectedDate)) {
            Toast.makeText(_ctx, "Choose a date first.", Toast.LENGTH_LONG).show();
            return;
        }

        List<ScheduleResponse.ScheduleDataPoly> poly = new ArrayList<>();
        for(int i = 0; i < scheduleList.size(); i++) {
            ScheduleResponse.ScheduleDataDay day = scheduleList.get(i).day;
            if(day.date.equals(selectedDate))
                poly = scheduleList.get(i).poly;
        }

        if(poly.size() == 0) {
            Toasty.info(_ctx,"Schedule not found on this date.");
            return;
        }

        final String[] array = new String[poly.size()];
        for (int i =0; i < poly.size(); i++) {
            array[i] = poly.get(i).poly_name;
        }

        Arrays.sort(array);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Specialty");

        final List<ScheduleResponse.ScheduleDataPoly> finalSchedules = poly;
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);

                for (int j = 0; j < finalSchedules.size(); j++) {
                    if(array[i].equals(finalSchedules.get(j).poly_name))
                        selectedPoli = finalSchedules.get(j);
                }
                selectedDoctor = null;

                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void showDokterDialog(final View v) {
        if(TextUtils.isEmpty(selectedDate)) {
            Toast.makeText(_ctx, "Choose a date first.", Toast.LENGTH_LONG).show();
            return;
        }

        if(selectedPoli == null) {
            Toast.makeText(_ctx, "Choose specialty first.", Toast.LENGTH_LONG).show();
            return;
        }

        if(selectedPoli.doctors.size() == 0) {
            Toast.makeText(_ctx, "Doctors were not found on this specialty.", Toast.LENGTH_LONG).show();
            return;
        }

        final List<ScheduleResponse.ScheduleDataDoctors> doctors = selectedPoli.doctors;

        final String[] array = new String[doctors.size()];
        for (int i =0; i < doctors.size(); i++) {
            array[i] = doctors.get(i).doctor.name;
        }

        Arrays.sort(array);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Doctor Name");
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(array[i]);

                for (int j = 0; j < doctors.size(); j++) {
                    if(array[i].equals(doctors.get(j).doctor.name))
                        selectedDoctor = doctors.get(j);
                }

                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void showHoursDialog(final View v) {
        if(TextUtils.isEmpty(selectedDate)) {
            Toast.makeText(_ctx, "Choose a date first", Toast.LENGTH_LONG).show();
            return;
        }

        if(selectedPoli == null) {
            Toast.makeText(_ctx, "Choose specialty first", Toast.LENGTH_LONG).show();
            return;
        }

        if(selectedDoctor == null) {
            Toast.makeText(_ctx, "Choose doctor name first", Toast.LENGTH_LONG).show();
            return;
        }

        if(selectedDoctor.schedule.size() == 0) {
            Toast.makeText(_ctx, "Schedule were not found on this doctor.", Toast.LENGTH_LONG).show();
            return;
        }

        final List<ScheduleResponse.ScheduleTime> schedule = selectedDoctor.schedule;

        final String[] array = new String[schedule.size()];
        for (int i =0; i < schedule.size(); i++) {
            array[i] = schedule.get(i).queue_number + (schedule.get(i).status.equals( "booked") ? " (" + schedule.get(i).status.toUpperCase() + ")" : "");
        }

//        Arrays.sort(array);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Queue Number");
        builder.setSingleChoiceItems(array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int j = 0; j < schedule.size(); j++) {
                    String text = schedule.get(j).queue_number +(schedule.get(j).status.equals( "booked") ? " (" + schedule.get(j).status.toUpperCase() + ")" : "");
                    if(array[i].equals(text)) {
                        if(schedule.get(j).status.equals("booked")) {
                            Toasty.info(_ctx, "This schedule has been booked.");
                            ((EditText) v).setText("");
                            selectedTime = null;
                        } else {
                            ((EditText) v).setText(array[i]);
                            selectedTime = schedule.get(j);
                        }
                        break;
                    }
                }

                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void getSchedules(){
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Get Schedule Data.");
        loader.show();

        Call<ScheduleResponse> call = _gate.schedule().getSchedule(_cooky.token());
        call.enqueue(new Callback<ScheduleResponse>() {
            @Override
            public void onResponse(Call<ScheduleResponse> call, Response<ScheduleResponse> response) {
                ScheduleResponse result = response.body();

                if(result != null) {
                    if(result.status.equals("Success")) {
                        scheduleList = result.data.result;
                    }
                }

                loader.dismiss();
            }

            @Override
            public void onFailure(Call<ScheduleResponse> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    private void getPatients(){
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
            }

            @Override
            public void onFailure(Call<PatientListItem> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
            }
        });
    }

    private void requestBooking(){
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Registering queue.");
        loader.show();

        Call<RequestBooking> call;
        if(!TextUtils.isEmpty(mBookingId))
            call = _gate.requestBooking().cancelAndrequestBooking(
                    _cooky.token(),
                    selectedTime.schedule_id,
                    selectedPatient.pasien_id,
                    mBookingId);
        else
            call = _gate.requestBooking().requestBooking(
                    _cooky.token(),
                    selectedTime.schedule_id,
                    selectedPatient.pasien_id);
        call.enqueue(new Callback<RequestBooking>() {
            @Override
            public void onResponse(Call<RequestBooking> call, Response<RequestBooking> response) {
                RequestBooking result = response.body();

                if(result != null) {
                    if(result.isStatus()) {
                        Bundle b = new Bundle();
                        b.putString("BOOKING_ID", result.data.booking_id);
                        b.putBoolean("FROM_REQUEST", true);
                        _nav.navigate(BookSpecialtyDetailActivity.class, b);
                        Toasty.success(_ctx, HRes.string(_ctx, R.string.success_registration_queue));
                    } else {
                        Toasty.error(_ctx, HRes.string(_ctx, R.string.error_schedule_booked));
                    }
                }

                loader.dismiss();
            }

            @Override
            public void onFailure(Call<RequestBooking> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    @OnClick(R.id.btn_register) void next(){
        etPatient.setError(null);
        etDate.setError(null);
        etPoli.setError(null);
        etDokter.setError(null);
        etHours.setError(null);

        String name = etPatient.getText().toString();
        String poly = etPoli.getText().toString();
        String doctor = etDokter.getText().toString();
        String schedule = etHours.getText().toString();

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
        } else if (TextUtils.isEmpty(poly)) {
            etPoli.setError(HRes.string(_ctx, R.string.error_specialty_empty));
            Toast.makeText(_ctx, "Specialty cannot be empty.", Toast.LENGTH_LONG).show();
            focusView = etPoli;
            cancel = true;
        } else if(TextUtils.isEmpty(doctor)) {
            etDokter.setError(HRes.string(_ctx,R.string.error_dokter_empty));
            Toast.makeText(_ctx, "Doctor name cannot be empty.", Toast.LENGTH_LONG).show();
            focusView = etDokter;
            cancel = true;
        } else if(TextUtils.isEmpty(schedule)) {
            etHours.setError(HRes.string(_ctx,R.string.error_hours_empty));
            Toast.makeText(_ctx, "Time cannot be empty.", Toast.LENGTH_LONG).show();
            focusView = etHours;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            requestBooking();
        }
    }

    @OnClick(R.id.toolbar_back) void back() {
        super.onBackPressed();
    }
}
