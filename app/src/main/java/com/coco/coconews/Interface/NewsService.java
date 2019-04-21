package com.coco.coconews.Interface;

import com.coco.coconews.Common.Common;
import com.coco.coconews.Model.News;
import com.coco.coconews.Model.NewsData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface NewsService {

    @GET("v2/sources?language=en&apiKey="+ Common.API_KEY)
    Call<NewsData> getSources();

    @GET
    Call<News> getNewestArticles(@Url String url);
}
