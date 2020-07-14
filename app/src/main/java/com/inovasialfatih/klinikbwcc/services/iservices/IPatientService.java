package com.inovasialfatih.klinikbwcc.services.iservices;

import com.inovasialfatih.klinikbwcc.model.PatientDeleteResponse;
import com.inovasialfatih.klinikbwcc.model.PatientDetailItem;
import com.inovasialfatih.klinikbwcc.model.PatientEditResponse;
import com.inovasialfatih.klinikbwcc.model.PatientListItem;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IPatientService {
    @GET("patient/details/{pasien_id}")
    Call<PatientDetailItem> getPatientDetail(
            @Path("pasien_id") String id,
            @Query("key") String key

    );

    @GET("patient/list")
    Call<PatientListItem> getPatientList(
            @Query("key") String key
    );

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("patient/delete")
    Call<PatientDeleteResponse> patientDelete(
            @Query("key") String key,
            @Field("pasien_id") String pasien_id
    );

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("patient/edit")
    Call<PatientEditResponse> patientEdit(
            @Query("key") String key,
            @Field("pasien_id") String pasien_id,
            @Field("nama") String nama,
            @Field("lahir_tempat") String lahir_tempat,
            @Field("lahir_tanggal") String lahir_tanggal,
            @Field("jenis_kelamin") String jenis_kelamin,
            @Field("alamat") String alamat,
            @Field("no_hp") String no_hp,
            @Field("email") String email,
            @Field("jenis_pembayaran") String jenis_pembayaran
    );
}
