package com.inovasialfatih.klinikbwcc.services.iservices;



import com.inovasialfatih.klinikbwcc.model.DataNewPatientResponse;
import com.inovasialfatih.klinikbwcc.model.DataOldPatientResponse;
import com.inovasialfatih.klinikbwcc.model.RequestCekRmResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IRequestRmService {
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("patient/check")
    Call<RequestCekRmResult> requestRm(
            @Query("key") String key,
            @Field("no_rm") String no_rm,
            @Field("tanggal_lahir") String tanggal_lahir

    );

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("patient/add/old")
    Call<DataOldPatientResponse> requestRmResult(
            @Query("key") String key,
            @Field("no_rm") String no_rm,
            @Field("alamat") String alamat,
            @Field("no_hp") String no_hp,
            @Field("jenis_pembayaran") String jenis_pembayaran
    );

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("patient/add/new")
    Call<DataNewPatientResponse> requestNewPatientResult(
            @Query("key") String key,
            @Field("nama") String nama,
            @Field("tempat_lahir") String tempat_lahir,
            @Field("tanggal_lahir") String tanggal_lahir,
            @Field("jenis_kelamin") String jenis_kelamin,
            @Field("alamat") String alamat,
            @Field("no_hp") String no_hp,
            @Field("email") String email,
            @Field("jenis_pembayaran") String jenis_pembayaran
    );

}