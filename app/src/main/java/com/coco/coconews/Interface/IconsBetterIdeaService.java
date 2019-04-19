package com.coco.coconews.Interface;

import com.coco.coconews.Model.IconBetterIdea;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IconsBetterIdeaService {

    @GET
    Call<IconBetterIdea> getIconUrl(@Url String url);

}
