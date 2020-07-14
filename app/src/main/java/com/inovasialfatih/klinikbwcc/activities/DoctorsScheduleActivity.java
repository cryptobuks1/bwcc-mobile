package com.inovasialfatih.klinikbwcc.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter;
import com.inovasialfatih.klinikbwcc.R;
import com.inovasialfatih.klinikbwcc.helper.HRes;
import com.inovasialfatih.klinikbwcc.model.DoctorScheduleResponse;
import com.inovasialfatih.klinikbwcc.views.Loadie;
import com.inovasialfatih.klinikbwcc.views.Toasty;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorsScheduleActivity extends BaseActivity {

    @BindView(R.id.toolbar_title) IconTextView mToolbarTitle;
    @BindView(R.id.et_poli) EditText etPoli;
    @BindView(R.id.et_dokter) EditText etDokter;
    @BindView(R.id.doctor_name) TextView mDoctorName;
    @BindView(R.id.specialist) TextView mSpecialist;
    @BindView(R.id.experience) TextView mExperience;
    @BindView(R.id.img_doctor) ImageView imgDoctor;
    @BindView(R.id.schedule_area) LinearLayout areaSchedule;

    @BindView(R.id.ic_not_found) ImageView mNotFound;

    private String selectedPoli = "";
    private String selectedDoctor = "";
    private RendererRecyclerViewAdapter mAdapter;
    private ArrayList<DoctorScheduleResponse.DoctorScheduleList> childItems = new ArrayList<>();
    private ArrayList<DoctorScheduleResponse.DoctorScheduleList> scheduleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this, R.layout.activity_doctors_schedule);

        initControl();
        getDoctorSchedule();
    }

    private void initControl(){
        mToolbarTitle.setText("Doctor Name");

        etPoli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPoliDialog(v);
            }
        });

        etDokter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDoctorDialog(v);
            }
        });

    }

    private void showPoliDialog(final View v) {
        final String[] arrayPoli = new String[scheduleList.size()];

        for(int i=0; i < scheduleList.size(); i++) {
            arrayPoli[i] = scheduleList.get(i).poly_name;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Specialty");
        builder.setSingleChoiceItems(arrayPoli, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedPoli = arrayPoli[i];
                ((EditText) v).setText(arrayPoli[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void showDoctorDialog(final View v) {
        if(TextUtils.isEmpty(selectedPoli)) {
            Toasty.error(_ctx, "Choose specialty first.");
            return;
        }

        final List<DoctorScheduleResponse.DoctorResult> doctors = new ArrayList<>();
        for(int i=0; i < scheduleList.size(); i++) {
            if(scheduleList.get(i).poly_name.equals(selectedPoli)) {
                doctors.addAll(scheduleList.get(i).doctors);
                break;
            }
        }

        if(doctors == null || doctors.size() == 0) {
            Toasty.info(_ctx, "Doctors were not found on this specialty.");
            return;
        }

        final String[] arrayDoctors = new String[doctors.size()];
        for(int i=0; i < doctors.size(); i++) {
            arrayDoctors[i] = doctors.get(i).name;
        }

        Arrays.sort(arrayDoctors);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Doctor Name");
        builder.setSingleChoiceItems(arrayDoctors, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedDoctor = arrayDoctors[i];
                ((EditText) v).setText(arrayDoctors[i]);
                dialogInterface.dismiss();

                DoctorScheduleResponse.DoctorResult doctor = null;
                for(int j=0; j < doctors.size(); j++) {
                    if(doctors.get(j).name.equals(selectedDoctor))
                        doctor = doctors.get(j);
                }

                if (doctor != null)
                {
                    areaSchedule.setVisibility(View.VISIBLE);
                    mNotFound.setVisibility(View.GONE);

                    mDoctorName.setText(doctor.name);
                    mSpecialist.setText(doctor.specialist);
                    mExperience.setText(doctor.experience);

                    Glide.with(_ctx).load(doctor.image_url).into(imgDoctor);

                    LinearLayout parentLayout = findViewById(R.id.layout_schedule);
                    parentLayout.removeAllViews();
                    for(int k=0; k<doctor.jadwal_absen.size(); k++) {

                        DoctorScheduleResponse.DoctorScheduleTime docSchedule = doctor.jadwal_absen.get(k);
                        View layout = LayoutInflater.from(_ctx).inflate(R.layout.item_doctor_hour, null);
                        TextView txtDayName = layout.findViewById(R.id.date);
                        TextView txtHour = layout.findViewById(R.id.time);

                        txtDayName.setText(docSchedule.day);
                        txtHour.setText(docSchedule.jam_absen);

                        parentLayout.addView(layout);
                    }
                }
            }
        });
        builder.show();
    }

    private void getDoctorSchedule(){
        final Loadie loader = new Loadie(_ctx, "Loading ...", "Get Doctor Schedule.");
        loader.show();

        Call<DoctorScheduleResponse> call = _gate.doctorSchedule().getDoctorSchedule(_cooky.token());
        call.enqueue(new Callback<DoctorScheduleResponse>() {
            @Override
            public void onResponse(Call<DoctorScheduleResponse> call, Response<DoctorScheduleResponse> response) {
                DoctorScheduleResponse result = response.body();

                if(result != null) {
                    if(result.data.schedule != null) {
                        scheduleList.addAll(result.data.schedule);
                    }
                }

                loader.dismiss();
            }

            @Override
            public void onFailure(Call<DoctorScheduleResponse> call, Throwable t) {
                Toasty.error(_ctx, HRes.string(_ctx, R.string.error_request_failed));
                loader.dismiss();
            }
        });
    }

    @OnClick(R.id.toolbar_back) void back() {
        super.onBackPressed();
    }
}
