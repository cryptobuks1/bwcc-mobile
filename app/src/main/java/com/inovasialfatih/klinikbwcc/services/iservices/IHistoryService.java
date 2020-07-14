package com.inovasialfatih.klinikbwcc.services.iservices;


import com.inovasialfatih.klinikbwcc.model.HistoryClassItem;
import com.inovasialfatih.klinikbwcc.model.HistoryItem;
import com.inovasialfatih.klinikbwcc.model.PatientDetailItem;
import com.inovasialfatih.klinikbwcc.model.RequestBooking;
import com.inovasialfatih.klinikbwcc.model.RequestHistoryDetail;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IHistoryService {
    @GET("booking/history")
    Call<HistoryItem> getHistoryList(
            @Query("key") String key

    );

    @GET("class/list_booking_class")
    Call<HistoryClassItem> getHistoryClassList(
            @Query("key") String key

    );
}