package com.inovasialfatih.klinikbwcc.services.infrastructure;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inovasialfatih.klinikbwcc.BuildConfig;
import com.inovasialfatih.klinikbwcc.extensions.Cooky;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCall {
    public Context context;
    private Retrofit.Builder builder;

    private String baseUri = BuildConfig.URL_BASE;
    private boolean enableLog = true;

    public RetrofitCall(Context context, boolean enableLog){
        this.context = context;
        this.enableLog = enableLog;
        builder = new Retrofit.Builder();

    }

    public Retrofit call(){
        return execute(true);
    }

    public Retrofit callIgnoreAuth(){
        return execute(false);
    }

    private Retrofit execute(boolean isAuth){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        builder.baseUrl(baseUri).addConverterFactory(GsonConverterFactory.create(gson));

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(20, TimeUnit.SECONDS);
        clientBuilder.readTimeout(60, TimeUnit.SECONDS);

        if(enableLog) clientBuilder.addInterceptor(InterceptorHelper.createLog());

        if(isAuth)
            clientBuilder.addInterceptor(
                    InterceptorHelper.authorize(new Cooky(context).token())
            );

        builder.client(clientBuilder.build());
        return builder.build();
    }

    public Retrofit callIdentityChecker(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        builder.baseUrl("http://139.59.219.156").addConverterFactory(GsonConverterFactory.create(gson));

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(20, TimeUnit.SECONDS);
        clientBuilder.readTimeout(60, TimeUnit.SECONDS);

        if(enableLog) clientBuilder.addInterceptor(InterceptorHelper.createLog());

        builder.client(clientBuilder.build());
        return builder.build();
    }

    public Retrofit callOcr(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        builder.baseUrl("https://api.ocr.space").addConverterFactory(GsonConverterFactory.create(gson));

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(20, TimeUnit.SECONDS);
        clientBuilder.readTimeout(60, TimeUnit.SECONDS);

        if(enableLog) clientBuilder.addInterceptor(InterceptorHelper.createLog());

        clientBuilder.addInterceptor(
                InterceptorHelper.authorizeOcr(BuildConfig.OCR_API_KEY)
        );

        builder.client(clientBuilder.build());
        return builder.build();
    }
}
