package com.coco.coconews;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.coco.coconews.Adapter.ListNewsAdapter;
import com.coco.coconews.Common.Common;
import com.coco.coconews.Interface.NewsService;
import com.coco.coconews.Model.Article;
import com.coco.coconews.Model.News;
import com.coco.coconews.Model.Source;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNewsActivity extends AppCompatActivity {

    KenBurnsView kenBurnsView;
    DiagonalLayout diagonalLayout;
    AlertDialog alertDialog;
    NewsService newsService;
    TextView top_author;
    TextView top_title;
    SwipeRefreshLayout swipeRefreshLayout;

    String source = "";
    String sortBy = "";
    String webHotUrl = "";

    ListNewsAdapter listNewsAdapter;
    RecyclerView lstNews;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        //Service
        newsService = Common.getNewsService();

        alertDialog = new SpotsDialog.Builder().setContext(ListNewsActivity.this).build();

        //View
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(source,true);
            }
        });

        diagonalLayout =findViewById(R.id.diagonallayout);
        diagonalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent detail = new Intent(getBaseContext(), DetailArticleActivity.class);
                detail.putExtra("webURL",webHotUrl);
                startActivity(detail);

            }
        });
        kenBurnsView = findViewById(R.id.top_image);
        top_author = findViewById(R.id.top_author);
        top_title = findViewById(R.id.top_title);

        lstNews = findViewById(R.id.lstNews);
        lstNews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstNews.setLayoutManager(layoutManager);

        //Intent
        if (getIntent() != null){
            source = getIntent().getStringExtra("source");

            if (!source.isEmpty() ){
                loadNews(source, false);
            }
        }

    }

    private void loadNews(String source, boolean isRefreshed) {
        if (!isRefreshed){
            alertDialog.show();
            newsService.getNewestArticles(Common.getAPIUrl(source,sortBy,Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            alertDialog.dismiss();

                            //get first article
                            Picasso.get()
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(kenBurnsView);

                            top_title.setText(response.body().getArticles().get(0).getTitle());
                            top_author.setText(response.body().getArticles().get(0).getAuthor());

                            webHotUrl = response.body().getArticles().get(0).getUrl();

                            //load remaining article
                            List<Article> removeFirstItem = response.body().getArticles();
                            //Because we have already load first item to show on Diagonal layout
                            //so we need to remove it.
                            removeFirstItem.remove(0);
                            listNewsAdapter = new ListNewsAdapter(removeFirstItem,getBaseContext());
                            listNewsAdapter.notifyDataSetChanged();
                            lstNews.setAdapter(listNewsAdapter);
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });
        }else {
            alertDialog.show();
            newsService.getNewestArticles(Common.getAPIUrl(source,sortBy,Common.API_KEY))
                    .enqueue(new Callback<News>() {
                        @Override
                        public void onResponse(Call<News> call, Response<News> response) {
                            alertDialog.dismiss();

                            //get first article
                            Picasso.get()
                                    .load(response.body().getArticles().get(0).getUrlToImage())
                                    .into(kenBurnsView);

                            top_title.setText(response.body().getArticles().get(0).getTitle());
                            top_author.setText(response.body().getArticles().get(0).getAuthor());

                            webHotUrl = response.body().getArticles().get(0).getUrl();

                            //load remaining article
                            List<Article> removeFirstItem = response.body().getArticles();
                            //Because we have already load first item to show on Diagonal layout
                            //so we need to remove it.
                            removeFirstItem.remove(0);
                            listNewsAdapter = new ListNewsAdapter(removeFirstItem,getBaseContext());
                            listNewsAdapter.notifyDataSetChanged();
                            lstNews.setAdapter(listNewsAdapter);
                        }

                        @Override
                        public void onFailure(Call<News> call, Throwable t) {

                        }
                    });
            swipeRefreshLayout.setRefreshing(false);
        }

    }
}
