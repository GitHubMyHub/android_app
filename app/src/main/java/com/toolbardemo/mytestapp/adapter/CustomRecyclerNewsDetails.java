package com.toolbardemo.mytestapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.data.News;

import java.util.ArrayList;

/**
 * Created by V-Windows on 10.05.2017.
 *
 */

public class CustomRecyclerNewsDetails extends RecyclerView.Adapter<CustomRecyclerNewsDetails.MyViewHolder>{

    private ArrayList<News> web;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtViewTitleDetailNews;
        TextView txtViewTextDetailNews;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.txtViewTitleDetailNews = itemView.findViewById(R.id.txtViewTitleDetailNews);
            this.txtViewTextDetailNews = itemView.findViewById(R.id.txtViewTextDetailNews);
        }
    }

    public CustomRecyclerNewsDetails(ArrayList<News> data) {
        this.web = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        // Title
        TextView txtTitle = holder.txtViewTitleDetailNews;
        txtTitle.setText(web.get(listPosition).getTitle());

        // Text
        TextView txtText = holder.txtViewTextDetailNews;
        txtText.setText(Html.fromHtml(web.get(listPosition).getText()));

    }

    @Override
    public int getItemCount() {
        return web.size();
    }
}
