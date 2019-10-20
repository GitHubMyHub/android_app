package com.toolbardemo.mytestapp.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.data.Recycling;
import com.toolbardemo.mytestapp.image.ImageLoader;


/**
 * Created by V-Windows on 11.04.2017.
 *
 */

public class CustomListArticleRecycling extends ArrayAdapter<Recycling>{

    private final Activity context;
    private final ArrayList<Recycling> web;
    private ImageLoader imageLoader = new ImageLoader(getContext());

    public CustomListArticleRecycling(Activity context, ArrayList<Recycling> web) {
        super(context, R.layout.list_single_article_recycling, web);
        this.context = context;
        this.web = web;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_article_recycling, parent, false);

        // Recycling-Image
        ImageView imgViewRecycling = rowView.findViewById(R.id.imgViewRecycling);
        imageLoader.DisplayImage(web.get(position).getRecyclingImagePath(), imgViewRecycling);

        // Recycling-Tag
        TextView txtRecyclingViewName = rowView.findViewById(R.id.txtRecyclingViewName);
        txtRecyclingViewName.setText(web.get(position).getRecyclingNameTag());

        // Recycling-Description
        TextView txtRecyclingViewDescription = rowView.findViewById(R.id.txtRecyclingViewDescription);
        txtRecyclingViewDescription.setText(web.get(position).getRecyclingDescription());

        // Trash-Image
        ImageView imgViewTrash = rowView.findViewById(R.id.imgViewTrash);
        this.imageLoader.DisplayImage(web.get(position).getTrashImagePath(), imgViewTrash);

        // Trash-Name
        TextView txtTrashViewName = rowView.findViewById(R.id.txtTrashViewName);
        txtTrashViewName.setText(web.get(position).getTrashName());

        return rowView;

    }

}