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
import com.toolbardemo.mytestapp.data.Making;
import com.toolbardemo.mytestapp.image.ImageLoader;

import java.util.ArrayList;


/**
 * Created by V-Windows on 11.04.2017.
 *
 */

public class CustomListArticleMake extends ArrayAdapter<Making>{

    private final Activity context;
    private final ArrayList<Making> web;
    private ImageLoader imageLoader = new ImageLoader(getContext());

    public CustomListArticleMake(Activity context, ArrayList<Making> web) {
        super(context, R.layout.list_single_article_making, web);
        this.context = context;
        this.web = web;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_article_making, parent, false);

        // Making-Image
        ImageView imgViewMakingImage = rowView.findViewById(R.id.imgViewMakingImage);
        this.imageLoader.DisplayImage(web.get(position).getMakingImagePath(), imgViewMakingImage);

        // Making-Image-2
        ImageView imgViewMakingStepPicture = rowView.findViewById(R.id.imgViewMakingImageStep);
        this.imageLoader.DisplayImage(web.get(position).getMakingImageStepPath(), imgViewMakingStepPicture);

        // Making-Device
        TextView txtDeviceViewName = rowView.findViewById(R.id.txtDeviceViewName);
        txtDeviceViewName.setText(web.get(position).getDeviceName());

        // Making-Step
        TextView txtMakingViewStep = rowView.findViewById(R.id.txtMakingViewStep);
        txtMakingViewStep.setText(web.get(position).getMakingStep());

        // Making-Time
        TextView txtMakingViewTime = rowView.findViewById(R.id.txtMakingViewTime);
        txtMakingViewTime.setText(web.get(position).getMakingStepTime());

        // Making-Description
        TextView txtMakingViewDescription = rowView.findViewById(R.id.txtMakingViewDescription);
        txtMakingViewDescription.setText(web.get(position).getMakingDescription());

        return rowView;

    }

}