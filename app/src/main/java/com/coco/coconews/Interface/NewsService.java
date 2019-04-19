package com.coco.coconews.Interface;

import com.coco.coconews.Model.NewsData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsService {

    @GET("v1/sources?language=en")
    Call<NewsData> getSources();
}
