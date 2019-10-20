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

import com.toolbardemo.mytestapp.data.Unit;

import java.util.ArrayList;

/**
 * Created by V-Windows on 05.03.2018.
 *
 */

public class CustomUnit extends ArrayAdapter<Unit> implements AdapterView.OnItemSelectedListener {

    private Activity context;
    private ArrayList<Unit> web;
    private AdapterCallback callback;

    public CustomUnit(AdapterCallback callback, Activity context, ArrayList<Unit> web){
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

        Log.d("getView", web.get(position).getUnitName());

        rowView.setText(web.get(position).getUnitName());

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
        rowView.setText(web.get(position).getUnitName());
        return rowView;

    }

    public int getSelectedItemPosition(String name){
        int i=0;
        int match = 0;

        while(i < web.size()){
            if(web.get(i).getUnitName() == name){
                match = i;
            }
            i++;
        }
        return match;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("onItemSelected", String.valueOf(l));
        callback.onUnitSelect(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface AdapterCallback {
        void onUnitSelect(int i);
    }
}
