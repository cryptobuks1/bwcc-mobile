package com.inovasialfatih.klinikbwcc.services.iservices;

import com.inovasialfatih.klinikbwcc.model.DoctorScheduleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IDoctorScheduleService {

//    @GET("doctor/status")
//    Call<DoctorScheduleResponse> getDoctorSchedule(
//            @Query("key") String key
//    );

    @GET("doctor/info_absent")
    Call<DoctorScheduleResponse> getDoctorSchedule(
            @Query("key") String key
    );
}
