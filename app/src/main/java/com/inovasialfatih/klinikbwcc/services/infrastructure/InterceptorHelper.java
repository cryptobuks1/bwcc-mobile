package com.inovasialfatih.klinikbwcc.services.infrastructure;

import com.inovasialfatih.klinikbwcc.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class InterceptorHelper {
    public static HttpLoggingInterceptor createLog(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return loggingInterceptor;
    }

    public static Interceptor authorize(final String token){
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", token)
                        .addHeader("X-API-KEY", BuildConfig.X_API_KEY)
                        .build();
                return chain.proceed(newRequest);
            }
        };

        return interceptor;
    }

    public static Interceptor authorizeOcr(final String apiKey){
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("apikey", apiKey)
                        .build();
                return chain.proceed(newRequest);
            }
        };

        return interceptor;
    }
}
