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
import com.toolbardemo.mytestapp.data.Certificate;
import com.toolbardemo.mytestapp.image.ImageLoader;


/**
 * Created by V-Windows on 11.04.2017.
 *
 */

public class CustomListArticleCertificate extends ArrayAdapter<Certificate>{

    private final Activity context;
    private final ArrayList<Certificate> web;
    private ImageLoader imageLoader = new ImageLoader(getContext());

    public CustomListArticleCertificate(Activity context, ArrayList<Certificate> web) {
        super(context, R.layout.list_single_article_zertificate, web);
        this.context = context;
        this.web = web;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_article_zertificate, parent, false);

        // Certificate-Image
        ImageView imgViewCertificate = rowView.findViewById(R.id.imgViewZertificate);
        this.imageLoader.DisplayImage(web.get(position).getZertificateImagePath(), imgViewCertificate);

        // Certificate-Name
        TextView txtCertificateViewName = rowView.findViewById(R.id.txtZertificateViewName);
        txtCertificateViewName.setText(web.get(position).getZertificateName());

        // Certificate-Description
        TextView txtCertificateViewDescription = rowView.findViewById(R.id.txtZertificateViewDescription);
        txtCertificateViewDescription.setText(web.get(position).getZertificateDescription());

        return rowView;

    }

}