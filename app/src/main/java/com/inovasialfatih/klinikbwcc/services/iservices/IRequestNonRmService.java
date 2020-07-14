package com.inovasialfatih.klinikbwcc.services.iservices;


import com.inovasialfatih.klinikbwcc.model.DataOldPatientResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IRequestNonRmService {
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("request/non_rm")
    Call<DataOldPatientResponse> requestNonRm(
            @Query("key") String key,
            @Field("nik") String nik,
            @Field("nama") String nama,
            @Field("tanggal") String tanggal,
            @Field("status_pasien") String status_pasien,
            @Field("jenis_pasien") String jenis_pasien,
            @Field("id_bpjs") String id_bpjs,
            @Field("id_poli") String id_poli,
            @Field("id_dokter") String id_dokter,
            @Field("schedule_id") String schedule_id
    );

}