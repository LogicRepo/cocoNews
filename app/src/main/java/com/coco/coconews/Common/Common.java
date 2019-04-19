package com.coco.coconews.Common;

import com.coco.coconews.Interface.IconsBetterIdeaService;
import com.coco.coconews.Interface.NewsService;
import com.coco.coconews.Remote.IconsBetterIdeaClient;
import com.coco.coconews.Remote.RetrofitClient;

public class Common {
    private static final String BASE_URL = "https://newsapi.org/";
    public static final String API_KEY = "95e082c3da224aa4b0cc42d4355ff6f9";

    public static NewsService getNewsService(){
        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }

    public static IconsBetterIdeaService getIconService(){
        return IconsBetterIdeaClient.getClient().create(IconsBetterIdeaService.class);
    }
}
