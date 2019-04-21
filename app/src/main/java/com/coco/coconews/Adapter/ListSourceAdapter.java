package com.coco.coconews.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coco.coconews.Common.Common;
import com.coco.coconews.Interface.IconsBetterIdeaService;
import com.coco.coconews.Interface.ItemClickListener;
import com.coco.coconews.ListNewsActivity;
import com.coco.coconews.Model.IconBetterIdea;
import com.coco.coconews.Model.NewsData;
import com.coco.coconews.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ListSourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ItemClickListener itemClickListener;

    TextView source_title;
    CircleImageView source_image;

    ListSourceViewHolder(View itemView){
        super(itemView);

        source_image = (CircleImageView) itemView.findViewById(R.id.source_image);
        source_title = (TextView) itemView.findViewById(R.id.source_name);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}

public class ListSourceAdapter extends  RecyclerView.Adapter<ListSourceViewHolder> {

    private Context context;
    private NewsData newsData;
    private IconsBetterIdeaService service;


    public ListSourceAdapter(Context context, NewsData newsData) {
        this.context = context;
        this.newsData = newsData;

        service = Common.getIconService();
    }

    @NonNull
    @Override
    public ListSourceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.source_layout, viewGroup, false);

        return new ListSourceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListSourceViewHolder listSourceViewHolder, int position) {
        /*StringBuilder iconBetterApi = new StringBuilder("https://besticon-demo.herokuapp.com/allicons.json?url=");
        iconBetterApi.append(newsData.getSources().get(position).getUrl());

        service.getIconUrl(iconBetterApi.toString())
                .enqueue(new Callback<IconBetterIdea>() {
                    @Override
                    public void onResponse(Call<IconBetterIdea> call, Response<IconBetterIdea> response) {
                        if (response.body().getIcons().size() > 0){
                            Picasso.get()
                                    .load(response.body().getIcons().get(0).getUrl())
                                    .into(listSourceViewHolder.source_image);
                        }
                    }

                    @Override
                    public void onFailure(Call<IconBetterIdea> call, Throwable t) {

                    }
                });*/

        listSourceViewHolder.source_title.setText(newsData.getSources().get(position).getName());

        listSourceViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                Intent intent = new Intent(context, ListNewsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("source", newsData.getSources().get(position).getId());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return newsData.getSources().size();
    }
}
