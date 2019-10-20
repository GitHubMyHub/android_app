package com.toolbardemo.mytestapp.adapter;

import android.content.Context;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.util.SparseBooleanArray;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.toolbardemo.mytestapp.DAO.UnitDAO;
import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.data.ArticleItem;
import com.toolbardemo.mytestapp.image.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by V-Windows on 10.05.2017.
 *
 */

public class CustomRecyclerStock extends RecyclerView.Adapter<CustomRecyclerStock.MyViewHolder>{

    public static final String TAG = "CustomRecyclerAdapter";
    private ArrayList<ArticleItem> web;
    private static AdapterCallback callback;
    private static SparseBooleanArray selectedItems = new SparseBooleanArray();
    private ImageLoader imageLoader;
    private static Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        @BindView(R.id.imageArticleItemView) ImageView imageArticleItemView;
        @BindView(R.id.txtViewTextShoppingListArticleName) TextView txtViewTextShoppingListArticleName;
        @BindView(R.id.cardViewShoppingListItem) CardView cardView;
        @BindView(R.id.imgEditButton) ImageButton imgEditButton;
        @BindView(R.id.imgHistoryButton) ImageButton imgHistoryButton;
        @BindView(R.id.imgShoppingButton) ImageButton imgShoppingButton;
        @BindView(R.id.imgCartButton) ImageButton imgCartButton;
        @BindView(R.id.txtViewTextShoppingListQuanity) TextView txtViewTextShoppingListQuantity;
        @BindView(R.id.txtViewTextShoppingListDurability) TextView txtViewTextShoppingListDurability;
        @BindView(R.id.txtViewTextShoppingListTemp) TextView txtViewTextShoppingListTemp;
        @BindView(R.id.txtViewTextShoppingListTempTitle) TextView txtViewTextShoppingListTempTitle;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            //cardView.setOnClickListener(this);
            cardView.setOnLongClickListener(this);

            imgEditButton.setOnClickListener(this);
            imgHistoryButton.setOnClickListener(this);
            imgShoppingButton.setOnClickListener(this);
            imgCartButton.setOnClickListener(this);
        }


        @Override
        public boolean onLongClick(View view) {
            Log.d(TAG, "onLongClick");
            return true;
        }

        @Override
        public void onClick(View v) {
            if(v instanceof ImageButton){
                int itemPosition = getAdapterPosition();
                Log.d(TAG, "ImageButton");
                Log.d("DUMMDREIST", String.valueOf(v.getId()));
                if(v.getId() == R.id.imgEditButton){
                    callback.editShoppingListEntry(itemPosition);
                }else if(v.getId() == R.id.imgHistoryButton){
                    Log.d("Button","imgHistoryButton");
                    Log.d("ItemPosition", String.valueOf(itemPosition));
                    callback.moveToHistory(itemPosition);
                }else if(v.getId() == R.id.imgShoppingButton){
                    Log.d("Button","imgShoppingButton");
                    callback.createToShopping(itemPosition, 1);
                }else if(v.getId() == R.id.imgCartButton){
                    Log.d("Button","imgCartButton");
                    callback.createToShopping(itemPosition, 2);
                }
            }
        }
    }

    public ArticleItem removeItem(int position) {
        ArticleItem mItem = web.get(position);
        web.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, web.size());

        return mItem;
    }

    public void addItem(ArticleItem articleItem) {
        Log.d(TAG, "addItem");
        web.add(articleItem);
        notifyItemInserted(web.size());
    }

    public CustomRecyclerStock(AdapterCallback callback, Context context, ArrayList<ArticleItem> data) {
        this.callback = callback;
        this.mContext = context;
        this.imageLoader = new ImageLoader(context);
        this.web = data;
    }

    public interface AdapterCallback{
        /**
         * editShoppingListEntry
         * Gets the ArticleItem which should be edited
         * @param position Item in ArrayList<ArticleItem>
         */
        void editShoppingListEntry(int position); // benötigt und drin
        void moveToHistory(int position); // drin
        void createToShopping(int position, int movePosition); // drin
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_single_stock_item, parent, false);
        Log.d("onCreateViewHolder", "JA");

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        Log.d("onBindViewHolder", holder.toString());
        //Log.d("IMAGE", web.get(listPosition).getShoppingListPicture());

        if(web.get(listPosition).getShoppingListPicture() != null && !web.get(listPosition).getShoppingListPicture().equals("")){
            ImageView imageArticleItemView = holder.imageArticleItemView;
            this.imageLoader.DisplayImage(web.get(listPosition).getShoppingListPicture(), imageArticleItemView);
        }

        // Article-Name if Article is Not defined as Barcode
        TextView txtViewTextShoppingListArticleName = holder.txtViewTextShoppingListArticleName;
        txtViewTextShoppingListArticleName.setText(web.get(listPosition).getShoppingListName());

        UnitDAO units = new UnitDAO();

        // Shopping-List-Article-Quantity
        TextView txtViewTextShoppingListQuantity = holder.txtViewTextShoppingListQuantity;
        txtViewTextShoppingListQuantity.setText(String.valueOf(web.get(listPosition).getShoppingListQuantity()) + " " + units.getUnitIndexToName(web.get(listPosition).getUnitId()));

        if(Integer.parseInt(web.get(listPosition).getStore().isStorageLevelTemp()) == 0){

            TextView txtViewTextShoppingListTemp = holder.txtViewTextShoppingListTemp;
            txtViewTextShoppingListTemp.setVisibility(View.GONE);

            TextView txtViewTextShoppingListTempTitle = holder.txtViewTextShoppingListTempTitle;
            txtViewTextShoppingListTempTitle.setVisibility(View.GONE);
        }else{

            TextView txtViewTextShoppingListTemp = holder.txtViewTextShoppingListTemp;
            txtViewTextShoppingListTemp.setText(String.valueOf(web.get(listPosition).getStore().isStorageLevelTemp()));

        }

        // Shopping-List-Article-Unit
        //TextView txtViewTextShoppingListUnitId = holder.txtViewTextShoppingListUnitId;
        //txtViewTextShoppingListUnitId.setText(String.valueOf(web.get(listPosition).getUnitId()));
        //txtViewTextShoppingListUnitId.setText(String.valueOf(units.getUnitIndexToName(web.get(listPosition).getUnitId())));


        // Shopping-List-Article-Durability
        TextView txtViewTextShoppingListDurability = holder.txtViewTextShoppingListDurability;
        txtViewTextShoppingListDurability.setText(String.valueOf(web.get(listPosition).getShoppingListDurability()));

    }

    public int getItemCount() {
        Log.d("getItemCount", String.valueOf(web.size()));
        return web.size();
    }

    public ArrayList<ArticleItem> getShoppingList(){
        return web;
    }

    public void setShoppingList(ArticleItem item){
        Log.d("setShoppingList", String.valueOf(web.size()));
        web.add(item);
    }

    /**
     * changeShoppingListItem
     * Method to change (update) the Item in Adapter
     * @param key Item in ArrayList<ArticleItem>
     * @param articleItem Item which should be updated
     * */
    public void changeShoppingListItem(String key, ArticleItem articleItem){
        int count = 0;

        if(web.size() > 0){
            while (web.size() > count) {
                Log.d("LARS", key.toString());
                Log.d("LARS", web.get(count).getShoppingListKey());
                if(web.get(count).getShoppingListKey().equals(key)){
                    web.set(count, articleItem);
                }
                count++;
            }
        }
    }

    /**
     * deleteShoppingListItem
     * Method to delete the Item
     * @param key Item in ArrayList<ArticleItem>
     * */
    public void deleteShoppingListItem(String key){
        int count = 0;

        while (count < web.size()) {
            if(web.get(count).getShoppingListKey().equals(key)){
                web.remove(count);
                break;
            }
            count++;
        }
    }

    /**
     * isInAdapter
     * Method to verify if the article is in Adapter
     * @param key String in ArrayList<ArticleItem>
     * */
    public boolean isInAdapter(String key){
        int count = 0;
        boolean match = false;

        while (web.size() > count) {
            if(web.get(count).getShoppingListKey().equals(key)){
                if(web.get(count).getPlaceId() == 1 || web.get(count).getPlaceId() == 2){
                    match = true;
                }
            }
            count++;
        }

        return match;
    }

    public void getShoppingList(int position){
        Log.d("getShoppingList", String.valueOf(web.size()));
        web.get(position);
    }

    public ArrayList<ArticleItem> getArticleItems(){
        return web;
    }

    public SparseBooleanArray getSelectedItems(){
        return this.selectedItems;
    }

    /**
     * deleteItems
     * Deletes all Items in web
     */
    public void deleteItems(){
        web.clear();
    }

}
