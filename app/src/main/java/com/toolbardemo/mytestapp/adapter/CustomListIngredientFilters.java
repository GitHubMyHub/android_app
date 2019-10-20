package com.toolbardemo.mytestapp.adapter;

import android.content.Context;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.util.SparseBooleanArray;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.TextView;

import com.toolbardemo.mytestapp.IngredientFilterActivity;
import com.toolbardemo.mytestapp.IngredientFiltersActivity;
import com.toolbardemo.mytestapp.MainActivity;
import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.data.IngredientFilters;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomListIngredientFilters extends RecyclerView.Adapter<CustomListIngredientFilters.MyViewHolder>{

    public static final String TAG = "CustomListFilters";
    private ArrayList<IngredientFilters> web;
    private static AdapterCallback callback;

    private static SparseBooleanArray selectedItems = new SparseBooleanArray();

    private static Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        @BindView(R.id.txtViewTextIngredientFiltersName) TextView txtViewTextIngredientFiltersName;
        @BindView(R.id.cardViewIngredientFiltersItem) CardView cardView;
        @BindView(R.id.editButton) ImageButton imageButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            cardView.setOnClickListener(this);
            imageButton.setOnClickListener(this);
            cardView.setOnLongClickListener(this);

        }

        @Override
        public boolean onLongClick(View view) {
            Log.d(TAG, "onLongClick");
            callback.setEditMode(1);
            callback.changeEditMode();
            return true;
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick");

            if(v instanceof CardView) {
                // use getAdapterPosition() instead of getLayoutPosition()
                int itemPosition = getAdapterPosition();
                Log.d("ITEM", String.valueOf(itemPosition));
                Log.d("getEditMode", String.valueOf(callback.getEditMode()));

                if(callback.getEditMode() == 1){
                    Log.d("valueKeyNotFound", String.valueOf(selectedItems.get(itemPosition, false)));
                    if (!selectedItems.get(itemPosition, false)) {
                        selectedItems.put(itemPosition, true);
                        v.setBackgroundColor(IngredientFiltersActivity.mContext.getResources().getColor(R.color.colorPrimary));
                    }else{
                        Log.d("Key", "Not found.");
                        selectedItems.delete(itemPosition);
                        v.setBackgroundColor(IngredientFiltersActivity.mContext.getResources().getColor(R.color.colorWhite));
                    }
                }else{

                    Log.d("Dummdreist", "KAKA");

                    callback.openIngredientList(itemPosition);
                    
                }

                Log.d("selectedItems", selectedItems.toString());

            }else if(v instanceof ImageButton){
                int itemPosition = getAdapterPosition();
                Log.d(TAG, "ImageButton");
                //callback.editIngredientsEntry(web.get(itemPosition).getShoppingListsKey(), web.get(itemPosition).getShoppingListsName());
                callback.editIngredientsEntry(itemPosition);
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        Log.d(TAG, "onViewAttachedToWindow");
        super.onViewAttachedToWindow(holder);

        CardView cardItem = holder.itemView.findViewById(R.id.cardViewIngredientFiltersItem);
        if(isItemSelected(holder.getLayoutPosition())){
            cardItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        }else{
            cardItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MyViewHolder holder) {
        Log.d(TAG, "onViewDetachedFromWindow");
        super.onViewDetachedFromWindow(holder);

        Log.d("Item", String.valueOf(holder.getLayoutPosition()));
        Log.d("Item", String.valueOf(getSelectedItems()));

        CardView cardItem = holder.itemView.findViewById(R.id.cardViewIngredientFiltersItem);
        if(isItemSelected(holder.getLayoutPosition())){
            cardItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        }else{
            cardItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        }
    }

    public interface AdapterCallback{
        void changeEditMode();
        int getEditMode();
        void setEditMode(int mode);
        void editIngredientsEntry(int position);
        void openIngredientList(int position);
    }


    public CustomListIngredientFilters(AdapterCallback callback, Context context, ArrayList<IngredientFilters> data){
        this.callback = callback;
        this.mContext = context;
        this.web = data;
    }



    @Override
    public CustomListIngredientFilters.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_single_ingredient_filters, parent, false);
        Log.d("onCreateViewHolder", "JA");

        return new CustomListIngredientFilters.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomListIngredientFilters.MyViewHolder holder, final int listPosition) {
        Log.d("onBindViewHolder", holder.toString());

        // Ingredient-Filters-Name
        TextView txtViewTextIngredientFiltersName = holder.txtViewTextIngredientFiltersName;
        txtViewTextIngredientFiltersName.setText(String.valueOf(web.get(listPosition).getFiltersName()));

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        return web.size();
    }



    /**
     * setShoppingLists
     * Method to add a new Entry to the IngredientFilters-Lists
     * @param item Item in IngredientFilters
     * */
    public void setIngredientLists(IngredientFilters item){
        web.add(item);
    }

    /**
     * getItem
     * Method to return ArrayList<IngredientFilters> Item
     * @param position Item in IngredientFilters
     * */
    public IngredientFilters getItem(int position){
        return web.get(position);
    }

    /**
     * getSelectedItems
     * Method to get the selected-Items
     * */
    public SparseBooleanArray getSelectedItems(){
        return this.selectedItems;
    }

    /**
     * isItemSelected
     * Returns if the Item is selected
     * @return boolean
     * @param position Position of Item
     */
    public boolean isItemSelected(int position){
        boolean isSelected = false;

        if(selectedItems.get(position)){
            isSelected = true;
        }
        return isSelected;
    }

    /**
     * deleteSelectedItems
     * Method to clear the selected-Items
     * */
    public void deleteSelectedItems(){
        this.selectedItems.clear();
    }

    /**
     * setAllSelectedItems
     * Sets all Items to selected Items
     */
    public void setAllSelectedItems(){

        for(int i=0; i<web.size(); i++){
            if (!selectedItems.get(i, false)) {
                this.selectedItems.put(i, true);
            }
        }

        Log.d("selectedItems", selectedItems.toString());
    }

    /**
     * deleteAllSelectedItems
     * Deletes all selected Items
     */
    public void deleteAllSelectedItems(){
        this.selectedItems.clear();
    }

    /**
     * changeShoppingListsItem
     * Method to change the Item in Adapter
     * @param key Item in ArrayList<ShoppingLists>
     * @param ingredientFiltersItem Item which should be updated
     * */
    public void changeIngredientFiltersItem(int key, IngredientFilters ingredientFiltersItem){
        int count = 0;

        while (web.size() > count) {
            if(web.get(count).getFiltersId() == key){
                web.set(count, ingredientFiltersItem);
            }
            count++;
        }
    }

    /**
     * deleteIngredientFiltersItem
     * Method to delete the Item in Adapter
     * @param position Item in position
     * */
    public void deleteIngredientFiltersItem(int position){
        web.remove(position);
    }
}
