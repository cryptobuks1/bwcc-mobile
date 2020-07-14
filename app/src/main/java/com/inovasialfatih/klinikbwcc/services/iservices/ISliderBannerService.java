package com.inovasialfatih.klinikbwcc.services.iservices;

import com.inovasialfatih.klinikbwcc.model.ContactUsResponse;
import com.inovasialfatih.klinikbwcc.model.ImageSlider;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ISliderBannerService {
    @GET("user/slide_content_image")
    Call<ImageSlider> getSlider(
            @Query("key") String key

    );
}
