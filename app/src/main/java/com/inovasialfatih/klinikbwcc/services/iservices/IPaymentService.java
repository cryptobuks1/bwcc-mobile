package com.inovasialfatih.klinikbwcc.services.iservices;

import com.inovasialfatih.klinikbwcc.model.IdentityUploadResult;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface IPaymentService {

    @Multipart
    @POST("booking/payment_confirmation")
    Call<IdentityUploadResult> upload(
            @Query("key") String key,
            @Part("booking_id") RequestBody booking_id,
            @Part MultipartBody.Part file
    );

    @Multipart
    @POST("class/submit_payment_bookingclass")
    Call<IdentityUploadResult> uploadPaymentClass(
            @Query("key") String key,
            @Part("booking_class_id") RequestBody booking_class_id,
            @Part MultipartBody.Part file
    );
}
