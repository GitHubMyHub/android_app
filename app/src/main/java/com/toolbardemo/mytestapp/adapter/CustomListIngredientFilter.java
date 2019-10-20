package com.toolbardemo.mytestapp.adapter;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.util.SparseBooleanArray;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.toolbardemo.mytestapp.IngredientFiltersActivity;
import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.data.IngredientFilter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomListIngredientFilter extends RecyclerView.Adapter<CustomListIngredientFilter.MyViewHolder>{

    public static final String TAG = "CustomListFilter";
    private ArrayList<IngredientFilter> web;
    private static AdapterCallback callback;

    private static SparseBooleanArray selectedItems = new SparseBooleanArray();

    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        @BindView(R.id.txtViewTextIngredientFilterName) TextView txtViewTextIngredientFilterName;
        @BindView(R.id.cardViewIngredientFilterItem) CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            cardView.setOnClickListener(this);
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

                    /*Log.d("Dummdreist", "KAKA");

                    TextView sKey = v.findViewById(R.id.txtViewTextIngredientFiltersId);
                    Log.d("sKey", sKey.getText().toString());

                    Intent intent = new Intent(MainActivity.context, IngredientFilterActivity.class);
                    String message = sKey.getText().toString();
                    intent.putExtra("INGREDIENT_FILTER_ID", message);
                    MainActivity.context.startActivity(intent);*/

                }

                Log.d("selectedItems", selectedItems.toString());

            }
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        Log.d(TAG, "onViewAttachedToWindow");
        super.onViewAttachedToWindow(holder);

        CardView cardItem = holder.itemView.findViewById(R.id.cardViewIngredientFilterItem);
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

        CardView cardItem = holder.itemView.findViewById(R.id.cardViewIngredientFilterItem);
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
    }


    public CustomListIngredientFilter(AdapterCallback callback, Context context, ArrayList<IngredientFilter> data){
        this.callback = callback;
        this.mContext = context;
        this.web = data;
    }



    @Override
    public CustomListIngredientFilter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_single_ingredient_filter, parent, false);
        Log.d("onCreateViewHolder", "JA");

        return new CustomListIngredientFilter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomListIngredientFilter.MyViewHolder holder, final int listPosition) {
        Log.d("onBindViewHolder", holder.toString());

        // Ingredient-Filter-ID->FK (ingredient_FilterS
        TextView txtViewTextIngredientFilterName = holder.txtViewTextIngredientFilterName;
        txtViewTextIngredientFilterName.setText(String.valueOf(web.get(listPosition).getComponentsName()));

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
    public void setIngredientList(IngredientFilter item){
        web.add(item);
    }

    /**
     * getItem
     * Method to return ArrayList<IngredientFilter> Item
     * @param position Item in IngredientFilter
     * */
    public IngredientFilter getItem(int position){
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
     * deleteAllSelectedItems
     * Deletes all selected Items
     */
    public void deleteAllSelectedItems(){
        this.selectedItems.clear();
    }

    /**
     * deleteSelectedItems
     * Method to clear the selected-Items
     * */
    public void deleteSelectedItems(){
        this.selectedItems.clear();
    }

    /**
     * changeShoppingListsItem
     * Method to change the Item in Adapter
     * @param key Item in ArrayList<ShoppingLists>
     * @param ingredientFilterItem Item which should be updated
     * */
    public void changeIngredientFiltersItem(int key, IngredientFilter ingredientFilterItem){
        int count = 0;

        while (web.size() > count) {
            if(web.get(count).getFilterId() == key){
                web.set(count, ingredientFilterItem);
            }
            count++;
        }
    }

    /**
     * deleteIngredientFilterItem
     * Method to delete the Item in Adapter
     * @param position Item in position
     * */
    public void deleteIngredientFilterItem(int position){
        web.remove(position);
    }
}
