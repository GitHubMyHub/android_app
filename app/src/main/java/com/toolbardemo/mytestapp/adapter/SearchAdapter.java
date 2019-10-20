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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.data.SearchArticle;
import com.toolbardemo.mytestapp.image.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Miguel Catalan Bañuls
 */
public class SearchAdapter extends ArrayAdapter<SearchArticle> implements Filterable, AdapterView.OnItemSelectedListener {

    private static final String TAG = "SearchAdapter";
    private Activity mContext;
    private int viewResourceId;
    private ArrayList<SearchArticle> web;
    private List<SearchArticle> mDepartments;
    private List<SearchArticle> mDepartmentsAll;
    private ImageLoader imageLoader = new ImageLoader(getContext());
    private SearchAdapter.AdapterCallback callback;

    public SearchAdapter(SearchAdapter.AdapterCallback callback, Activity context, int viewResourceId, ArrayList<SearchArticle> web) {
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

            ImageView imageView = rowView.findViewById(R.id.suggestion_image);
            this.imageLoader.DisplayImage(web.get(position).getArticleImagePath(), imageView);

            TextView suggestion_article_id = rowView.findViewById(R.id.suggestion_article_id);
            suggestion_article_id.setText(web.get(position).getArticleName());

            TextView suggestion_text = rowView.findViewById(R.id.suggestion_text);
            suggestion_text.setText(web.get(position).getArticleName());

        return rowView;
    }

    public interface AdapterCallback {
        void getArticleSearchPosition(int position);
    }

    @Override
    public int getPosition(@Nullable SearchArticle item) {
        return super.getPosition(item);
    }

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
        Log.d("onItemSelected", String.valueOf(i));
        callback.getArticleSearchPosition(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    @NonNull
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                Log.d(TAG, "convertResultToString");
                return ((SearchArticle) resultValue).getArticleName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.d(TAG, "performFiltering");
                FilterResults filterResults = new FilterResults();
                List<SearchArticle> departmentsSuggestion = new ArrayList<>();
                if (constraint != null) {
                    for (SearchArticle department : mDepartmentsAll) {
                        Log.d("Brown", department.getArticleName().toLowerCase());
                        Log.d("Brown2", constraint.toString().toLowerCase());
                        if (department.getArticleName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            departmentsSuggestion.add(department);
                        }else if(department.getArticleBarcode().equals(constraint.toString())){
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
                        if (object instanceof SearchArticle) {
                            mDepartments.add((SearchArticle) object);
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