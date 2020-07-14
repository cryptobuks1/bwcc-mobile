package com.inovasialfatih.klinikbwcc.services.iservices;


import com.inovasialfatih.klinikbwcc.model.DetailNewsItem;
import com.inovasialfatih.klinikbwcc.model.RequestBooking;
import com.inovasialfatih.klinikbwcc.model.RequestBookingClass;
import com.inovasialfatih.klinikbwcc.model.RequestHistoryClassDetail;
import com.inovasialfatih.klinikbwcc.model.RequestHistoryDetail;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRequestBookingService {
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("booking/request")
    Call<RequestBooking> requestBooking(
            @Query("key") String key,
            @Field("schedule_id") String schedule_id,
            @Field("patient_id") String patient_id
    );

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("booking/cancel_and_request")
    Call<RequestBooking> cancelAndrequestBooking(
            @Query("key") String key,
            @Field("schedule_id") String schedule_id,
            @Field("patient_id") String patient_id,
            @Field("old_req_id") String old_req_id
    );

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("booking/history/detail")
    Call<RequestHistoryDetail> requestHistoryDetail(
            @Query("key") String key,
            @Field("booking_id") String booking_id
    );

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("booking/cancel")
    Call<RequestBooking> cancelBooking(
            @Query("key") String key,
            @Field("req_id") String req_id
    );

    // Booking Class
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("class/registration_newclass")
    Call<RequestBookingClass> requestBookingClass(
            @Query("key") String key,
            @Field("id_schedule") String id_schedule,
            @Field("pasien_id") String pasien_id
    );

    @GET("class/list_booking_class/detail_booking/{booking_class_id}")
    Call<RequestHistoryClassDetail> getDetailClass(
            @Path("booking_class_id") String booking_class_id,
            @Query("key") String key

    );

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("class/booking_class/cancel")
    Call<RequestBooking> cancelBookingClass(
            @Query("key") String key,
            @Field("booking_class_id") String booking_class_id
    );

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("class/edit_class_booking/{id}")
    Call<RequestBookingClass> cancelAndrequestBookingClass(
            @Path("id") String id,
            @Query("key") String key,
            @Field("id_schedule") String id_schedule,
            @Field("patient_id") String patient_id
    );
}