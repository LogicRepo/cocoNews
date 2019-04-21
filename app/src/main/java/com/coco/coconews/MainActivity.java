package com.coco.coconews;

import android.app.AlertDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.coco.coconews.Adapter.ListSourceAdapter;
import com.coco.coconews.Common.Common;
import com.coco.coconews.Interface.NewsService;
import com.coco.coconews.Model.NewsData;
import com.google.gson.Gson;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView listWebsite;
    RecyclerView.LayoutManager layoutManager;
    NewsService newsService;
    ListSourceAdapter adapter;
    public AlertDialog dialog;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init Cache
        Paper.init(this);

        //Init Service
        newsService = Common.getNewsService();

        //Init view
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebsiteSource(true);
            }
        });

        listWebsite = findViewById(R.id.list_source);
        listWebsite.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listWebsite.setLayoutManager(layoutManager);

        dialog = new SpotsDialog.Builder().setContext(MainActivity.this).build();

        loadWebsiteSource(false);
    }

    private void loadWebsiteSource(boolean isRefreshed) {
        if (!isRefreshed){
            String cache  = Paper.book().read("cache");
            if (cache != null && !cache.isEmpty() && !cache.equals("null")){ //if have empty

                NewsData newsData = new Gson().fromJson(cache, NewsData.class); // convert cache from json to object.
                adapter = new ListSourceAdapter(getBaseContext(), newsData);
                adapter.notifyDataSetChanged();
                listWebsite.setAdapter(adapter);
            }else { //if not have cache.

                dialog.show();
                //swipeRefreshLayout.setRefreshing(true);
                //fetch new data;
                newsService.getSources().enqueue(new Callback<NewsData>() {
                    @Override
                    public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                        adapter = new ListSourceAdapter(getBaseContext(), response.body());
                        adapter.notifyDataSetChanged();
                        listWebsite.setAdapter(adapter);

                        //save to cache;
                        Paper.book().write("Cache", new Gson().toJson(response.body()));

                        //swipeRefreshLayout.setRefreshing(false);
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<NewsData> call, Throwable t) {

                    }
                });

            }
        }else {
            //dialog.show();
            swipeRefreshLayout.setRefreshing(true);
            //fetch new data;
            newsService.getSources().enqueue(new Callback<NewsData>() {
                @Override
                public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                    adapter = new ListSourceAdapter(getBaseContext(), response.body());
                    adapter.notifyDataSetChanged();
                    listWebsite.setAdapter(adapter);

                    //save to cache;
                    Paper.book().write("Cache", new Gson().toJson(response.body()));

                    // dismiss refresh progressing
                    swipeRefreshLayout.setRefreshing(false);
                    //dialog.dismiss();
                }

                @Override
                public void onFailure(Call<NewsData> call, Throwable t) {

                }
            });
        }
    }
}
