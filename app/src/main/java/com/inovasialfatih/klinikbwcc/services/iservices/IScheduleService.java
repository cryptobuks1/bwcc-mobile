package com.inovasialfatih.klinikbwcc.services.iservices;

import com.inovasialfatih.klinikbwcc.model.ScheduleClassResponse;
import com.inovasialfatih.klinikbwcc.model.ScheduleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IScheduleService {
    @GET("booking/get_schedule1")
    Call<ScheduleResponse> getSchedule(
            @Query("key") String key
    );

    @GET("class/get_dataclass")
    Call<ScheduleClassResponse> getScheduleClass(
            @Query("key") String key
    );
}
