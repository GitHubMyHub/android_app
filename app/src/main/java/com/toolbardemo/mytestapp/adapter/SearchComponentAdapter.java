package com.toolbardemo.mytestapp.adapter;


import android.app.Activity;

import android.support.annotation.Nullable;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.data.SearchComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel Catalan Bañuls
 */
public class SearchComponentAdapter extends ArrayAdapter<SearchComponent> implements Filterable, AdapterView.OnItemSelectedListener {

    private static final String TAG = "SearchAdapter";
    private Activity mContext;
    private int viewResourceId;
    private ArrayList<SearchComponent> web;
    private List<SearchComponent> mDepartments;
    private List<SearchComponent> mDepartmentsAll;
    private SearchComponentAdapter.AdapterCallback callback;

    public SearchComponentAdapter(SearchComponentAdapter.AdapterCallback callback, Activity context, int viewResourceId, ArrayList<SearchComponent> web) {
        super(context, viewResourceId, android.R.id.text1, web);
        this.mContext = context;
        this.viewResourceId = viewResourceId;
        this.web = web;
        this.mDepartments = new ArrayList<>(web);
        this.mDepartmentsAll = new ArrayList<>(web);
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return web.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            Log.d(TAG, "getView");

            LayoutInflater inflater = mContext.getLayoutInflater();
            View rowView= inflater.inflate(viewResourceId, null, true);

            TextView suggestion_article_id = rowView.findViewById(R.id.suggestion_component_id);
            suggestion_article_id.setText(String.valueOf(web.get(position).getComponentId()));

            TextView suggestion_article_name = rowView.findViewById(R.id.suggestion_component_name);
            suggestion_article_name.setText(web.get(position).getComponentName());

            TextView suggestion_text = rowView.findViewById(R.id.suggestion_text);
            suggestion_text.setText(web.get(position).getComponentDescription());

        return rowView;
    }

    public interface AdapterCallback {
        void getArticleSearchPosition(int position);
    }

    @Override
    public int getPosition(@Nullable SearchComponent item) {
        return super.getPosition(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("onItemSelected", String.valueOf(i));
        callback.getArticleSearchPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public SearchComponent getItem(int position){
        return web.get(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                Log.d(TAG, "convertResultToString");
                return ((SearchComponent) resultValue).getComponentName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.d(TAG, "performFiltering");
                FilterResults filterResults = new FilterResults();
                List<SearchComponent> departmentsSuggestion = new ArrayList<>();
                if (constraint != null) {
                    for (SearchComponent department : mDepartmentsAll) {

                        Log.d("POPP2", department.getComponentName().toLowerCase());
                        Log.d("POPP", constraint.toString().toLowerCase());

                        if (department.getComponentName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            departmentsSuggestion.add(department);
                        }

                    }
                    filterResults.values = departmentsSuggestion;
                    filterResults.count = departmentsSuggestion.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.d(TAG, "publishResults");
                mDepartments.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                    for (Object object : (List<?>) results.values) {
                        if (object instanceof SearchComponent) {
                            mDepartments.add((SearchComponent) object);
                        }
                    }
                    notifyDataSetChanged();
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    mDepartments.addAll(mDepartmentsAll);
                    notifyDataSetInvalidated();
                }
            }
        };
    }

}