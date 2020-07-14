package com.inovasialfatih.klinikbwcc.services.iservices;

import com.inovasialfatih.klinikbwcc.model.DetailNewsItem;
import com.inovasialfatih.klinikbwcc.model.NewsItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface INewsService {
    @GET("news/detail/{id}")
    Call<DetailNewsItem> getDetailNews(
            @Path("id") String id,
            @Query("key") String key

    );

    @GET("news/list")
    Call<NewsItem> getNews(
            @Query("key") String key
    );
}
