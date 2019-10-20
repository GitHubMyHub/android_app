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

import com.toolbardemo.mytestapp.data.StoreSpinner;

import java.util.ArrayList;

/**
 * Created by V-Windows on 05.03.2018.
 *
 */

public class CustomStore extends ArrayAdapter<StoreSpinner> implements AdapterView.OnItemSelectedListener {

    private Activity context;
    private ArrayList<StoreSpinner> web;
    private AdapterCallback callback;

    public CustomStore(AdapterCallback callback, Activity context, ArrayList<StoreSpinner> web){
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

        Log.d("PositionX", String.valueOf(position));

        rowView.setText(web.get(position).getStoreName());

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
        rowView.setText(web.get(position).getStoreName());
        return rowView;

    }

    /*public int getSelectedItemPosition(int storeId){
        switch(storeId){
            case 1:
                return 3;
            case 2:
                return 1;
            default:
                return 2;
        }
    }*/

    /**
     * getSelectedItemPosition
     * @param storeId
     * DB -> SpinnerIndex
     * 2 - Fridge - 0
     * 3 - Cooler - 1
     * 1 - Pantry - 2
     */
    public int getSelectedItemPosition(int storeId){
        switch(storeId){
            case 1:
                return 2;
            case 2:
                return 0;
            default:
                return 1;
        }
    }

    /**
     * getSelectedItemIndex
     * @param storeId
     * DB -> SpinnerIndex
     * 2 - Fridge - 0
     * 3 - Cooler - 1
     * 1 - Pantry - 2
     */
    public int getSelectedItemIndex(int storeId){
        switch(storeId){
            case 0:
                return 2;
            case 1:
                return 3;
            default:
                return 1;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("onItemSelected", String.valueOf(l));
        callback.onStoreSelect(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public StoreSpinner getItem(int position){
        return web.get(position);
    }

    public interface AdapterCallback {
        void onStoreSelect(int i);
    }
}
