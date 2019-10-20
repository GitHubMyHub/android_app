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

import android.widget.ImageButton;
import android.widget.TextView;

import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.ShoppingListsActivity;
import com.toolbardemo.mytestapp.data.ShoppingLists;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by V-Windows on 10.05.2017.
 *
 */

public class CustomRecyclerShoppingLists extends RecyclerView.Adapter<CustomRecyclerShoppingLists.MyViewHolder>{

    public static final String TAG = "CustomRecyclerAdapter";
    private ArrayList<ShoppingLists> web;
    private static AdapterCallback callback;
    private static SparseBooleanArray selectedItems = new SparseBooleanArray();

    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        @BindView(R.id.txtViewTextshoppingListsName) TextView txtViewShoppingListsName;
        @BindView(R.id.cardViewShoppingListsItem) CardView cardView;
        @BindView(R.id.editButton) ImageButton imageButton;
        @BindView(R.id.txtViewRoundedLetter) TextView txtViewRoundedLetter;

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
            if(v instanceof CardView) {
                // use getAdapterPosition() instead of getLayoutPosition()
                int itemPosition = getAdapterPosition();
                Log.d("ITEM", String.valueOf(itemPosition));
                Log.d("getEditMode", String.valueOf(callback.getEditMode()));
                Log.d("selectedItems", selectedItems.toString());
                //removeItem(itemPosition);

                if(callback.getEditMode() == 1){
                    Log.d("valueKeyNotFound", String.valueOf(selectedItems.get(itemPosition, false)));
                    if (!selectedItems.get(itemPosition, false)) {
                        selectedItems.put(itemPosition, true);
                        v.setBackgroundColor(ShoppingListsActivity.mContext.getResources().getColor(R.color.colorPrimary));
                    }else{
                        Log.d("Key", "Not found.");
                        selectedItems.delete(itemPosition);
                        v.setBackgroundColor(ShoppingListsActivity.mContext.getResources().getColor(R.color.colorWhite));
                    }
                }else{

                    callback.openShoppingList(itemPosition);

                }

            }else if(v instanceof ImageButton){
                int itemPosition = getAdapterPosition();
                Log.d(TAG, "ImageButton");
                //callback.editShoppingListsEntry(web.get(itemPosition).getShoppingListsKey(), web.get(itemPosition).getShoppingListsName());
                callback.editShoppingListsEntry(itemPosition);
            }
        }

    }

    @Override
    public void onViewAttachedToWindow(@NonNull CustomRecyclerShoppingLists.MyViewHolder holder) {
        Log.d(TAG, "onViewAttachedToWindow");
        super.onViewAttachedToWindow(holder);

        CardView cardItem = holder.itemView.findViewById(R.id.cardViewShoppingListsItem);
        if(isItemSelected(holder.getLayoutPosition())){
            cardItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        }else{
            cardItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CustomRecyclerShoppingLists.MyViewHolder holder) {
        Log.d(TAG, "onViewDetachedFromWindow");
        super.onViewDetachedFromWindow(holder);

        Log.d("Item", String.valueOf(holder.getLayoutPosition()));
        Log.d("Item", String.valueOf(getSelectedItems()));

        CardView cardItem = holder.itemView.findViewById(R.id.cardViewShoppingListsItem);
        if(isItemSelected(holder.getLayoutPosition())){
            cardItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        }else{
            cardItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
        }
    }

    public ShoppingLists getItem(int position){
        return web.get(position);
    }

    public CustomRecyclerShoppingLists(AdapterCallback callback, Context context, ArrayList<ShoppingLists> data) {
        this.callback = callback;
        this.mContext = context;
        this.web = data;
    }

    public interface AdapterCallback{
        void changeEditMode();
        int getEditMode();
        void setEditMode(int mode);
        void editShoppingListsEntry(int position);
        void openShoppingList(int position);

    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_single_shopping_list, parent, false);
        Log.d("onCreateViewHolder", "JA");

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        Log.d("onBindViewHolder", holder.toString());

        TextView txtViewRoundedLetter = holder.txtViewRoundedLetter;
        txtViewRoundedLetter.setText(String.valueOf(web.get(listPosition).getShoppingListsName().toUpperCase().charAt(0)));

        // Shopping-List-Name
        TextView txtViewShoppingListsName = holder.txtViewShoppingListsName;
        txtViewShoppingListsName.setText(String.valueOf(web.get(listPosition).getShoppingListsName()));

    }

    public int getItemCount() {
        Log.d("getItemCount", String.valueOf(web.size()));
        return web.size();
    }

    public ArrayList<ShoppingLists> getShoppingLists(){
        return web;
    }

    /**
     * setShoppingLists
     * Method to add a new Entry to the Shopping-Lists
     * @param item Item in ShoppingLists
     * */
    public void setShoppingLists(ShoppingLists item){
        web.add(item);
    }

    public SparseBooleanArray getSelectedItems(){
        return this.selectedItems;
    }

    /**
     * isItemSelected
     * Returns if the Item is selected
     * @return boolean
     * @param position
     */
    public boolean isItemSelected(int position){
        boolean isSelected = false;

        if(selectedItems.get(position)){
            isSelected = true;
        }
        return isSelected;
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
     * deleteShoppingListsItem
     * Method to delete the Item in Adapter
     * @param key Item in String key
     * */
    public void deleteShoppingListsItem(String key){
        int count = 0;

        while (web.size() > count) {
            if(web.get(count).getShoppingListsKey().equals(key)){
                web.remove(count);
            }
            count++;
        }
    }

    /**
     * changeShoppingListsItem
     * Method to change the Item in Adapter
     * @param key Item in ArrayList<ShoppingLists>
     * @param shoppingListsItem Item which should be updated
     * */
    public void changeShoppingListsItem(String key, ShoppingLists shoppingListsItem){
        int count = 0;

        while (web.size() > count) {
            if(web.get(count).getShoppingListsKey().equals(key)){
                web.set(count, shoppingListsItem);
            }
            count++;
        }
    }

    /**
     * deleteShoppingLists
     * Method to clear the Shopping-Lists
     * */
    public void deleteShoppingLists(){
        web.clear();
    }


}
