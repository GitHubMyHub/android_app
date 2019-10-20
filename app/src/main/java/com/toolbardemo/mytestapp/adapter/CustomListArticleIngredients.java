package com.toolbardemo.mytestapp.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.toolbardemo.mytestapp.data.Ingredients;
import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.image.ImageLoader;


public class CustomListArticleIngredients extends ArrayAdapter<Ingredients>{

    private final Activity context;
    private final ArrayList<Ingredients> web;

    public CustomListArticleIngredients(Activity context, ArrayList<Ingredients> web) {
        super(context, R.layout.list_single_article_ingredients, web);
        this.context = context;
        this.web = web;
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_single_article_ingredients, parent, false);

        //Log.d("getComponentsName", web.get(position).getComponentsName());
        //Log.d("getComponentsQuantity", Integer.toString(web.get(position).getComponentsQuantity()));
        //Log.d("getUnitTag", web.get(position).getUnitTag());

        // Ingredients-Name
        TextView txtIngredientsViewName = rowView.findViewById(R.id.txtIngredientsViewName);
        txtIngredientsViewName.setText(web.get(position).getComponentsName());

        // Ingredients-Quantity
        TextView txtIngredientsViewQuantity = rowView.findViewById(R.id.txtIngredientsViewQuantity);
        txtIngredientsViewQuantity.setText(String.valueOf(web.get(position).getComponentsQuantity()));

        // Ingredients-Unit-Tag
        TextView txtIngredientsViewUnit = rowView.findViewById(R.id.txtIngredientsViewUnit);
        txtIngredientsViewUnit.setText(web.get(position).getUnitTag());

        return rowView;

    }

}


