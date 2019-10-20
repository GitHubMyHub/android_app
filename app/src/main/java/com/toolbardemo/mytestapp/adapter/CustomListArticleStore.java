package com.toolbardemo.mytestapp.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.data.Store;
import com.toolbardemo.mytestapp.image.ImageLoader;

import java.util.ArrayList;


/**
 * Created by V-Windows on 11.04.2017.
 *
 */

public class CustomListArticleStore extends ArrayAdapter<Store>{

    private final Activity context;
    private final ArrayList<Store> web;
    private ImageLoader imageLoader = new ImageLoader(getContext());

    public CustomListArticleStore(Activity context, ArrayList<Store> web) {
        super(context, R.layout.list_single_article_store, web);
        this.context = context;
        this.web = web;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_article_store, parent, false);

        // Store-Image
        ImageView imgViewStore = rowView.findViewById(R.id.imgViewStore);
        this.imageLoader.DisplayImage(web.get(position).getStoreImagePath(), imgViewStore);

        // Storage
        TextView txtStoreViewName = rowView.findViewById(R.id.txtStoreViewName);
        txtStoreViewName.setText(web.get(position).getStorage());

        // Store-Temp
        TextView txtStoreViewTemp = rowView.findViewById(R.id.txtStoreViewTemp);
        txtStoreViewTemp.setText(web.get(position).getStoreTemp());

        // Store-Type
        TextView txtStoreViewType = rowView.findViewById(R.id.txtStoreViewType);
        txtStoreViewType.setText(web.get(position).getStoreType());

        return rowView;

    }

}