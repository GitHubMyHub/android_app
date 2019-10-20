package com.toolbardemo.mytestapp.adapter;

import android.app.Activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.toolbardemo.mytestapp.data.StorePosition;

import java.util.ArrayList;

/**
 * Created by V-Windows on 05.03.2018.
 *
 */

public class CustomStorePosition extends ArrayAdapter<StorePosition> implements AdapterView.OnItemSelectedListener {

    private Activity context;
    private ArrayList<StorePosition> web;
    private AdapterCallback callback;

    public CustomStorePosition(AdapterCallback callback, Activity context, ArrayList<StorePosition> web){
        super(context, android.R.layout.simple_spinner_item, web);
        this.context = context;
        this.web = web;
        this.callback = callback;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = this.context.getLayoutInflater();
        TextView rowView = (TextView) inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);

        Log.d("getView", web.get(position).getStorePositionName());

        rowView.setText(web.get(position).getStorePositionName());

        return rowView;
    }

    @Override
    public void setDropDownViewResource(int resource) {
        super.setDropDownViewResource(resource);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = this.context.getLayoutInflater();
        TextView rowView = (TextView) inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        rowView.setText(web.get(position).getStorePositionName());
        return rowView;

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("onItemSelected", String.valueOf(l));
        callback.onStorePositionSelect(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public StorePosition getItem(int position){
        return web.get(position);
    }

    public interface AdapterCallback {
        void onStorePositionSelect(int i);
    }
}
