package com.inovasialfatih.klinikbwcc.services.iservices;

import com.inovasialfatih.klinikbwcc.model.ChangePasswordResponse;
import com.inovasialfatih.klinikbwcc.model.ForgotPasswordResponse;
import com.inovasialfatih.klinikbwcc.model.LoginModel;
import com.inovasialfatih.klinikbwcc.model.LoginResult;
import com.inovasialfatih.klinikbwcc.model.OpResult;
import com.inovasialfatih.klinikbwcc.model.RegisterResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IUserService {
    @Headers("Content-Type: application/x-www-form-urlencoded" )
    @FormUrlEncoded
    @POST("user/login")
    Call<LoginModel> login(
            @Query("key") String key,
            @Field("email") String email,
            @Field("password") String password
    );

    @Headers("Content-Type: application/x-www-form-urlencoded" )
    @FormUrlEncoded
    @POST("user/registration")
    Call<RegisterResult> register(
            @Query("key") String key,
            @Field("nama") String nama,
            @Field("no_hp") String no_hp,
            @Field("email") String email,
            @Field("password") String password,
            @Field("confirmation_password") String confirmation_password
    );

    @Headers("Content-Type: application/x-www-form-urlencoded" )
    @FormUrlEncoded
    @POST("user/reset_password")
    Call<ForgotPasswordResponse> forgotPassword(
            @Query("key") String key,
            @Field("email") String email
    );

    @Headers("Content-Type: application/x-www-form-urlencoded" )
    @FormUrlEncoded
    @POST("user/change_password/")
    Call<ChangePasswordResponse> changePassword(
            @Query("key") String key,
            @Field("new_password") String new_password,
            @Field("confirmation_password") String confirmation_password
    );

//    Call<OpResult> updateTokenNotif(String token);

    @Headers("Content-Type: application/x-www-form-urlencoded" )
    @FormUrlEncoded
    @POST("user/update_token_notification")
    Call<OpResult> updateTokenNotif(
            @Query("key") String key,
            @Field("token_notification") String token_notification
    );
}
