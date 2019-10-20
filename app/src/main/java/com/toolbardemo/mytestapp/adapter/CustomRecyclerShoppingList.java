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

public class CustomRecyclerShoppingList extends RecyclerView.Adapter<CustomRecyclerShoppingList.MyViewHolder>{

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
        @BindView(R.id.editButton) ImageButton imageButton;
        @BindView(R.id.txtViewTextShoppingListQuanity) TextView txtViewTextShoppingListQuantity;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            cardView.setOnClickListener(this);
            cardView.setOnLongClickListener(this);
            imageButton.setOnClickListener(this);
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

            Log.d("View", v.toString());

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
                        v.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                    }else{
                        Log.d("Key", "Not found.");
                        selectedItems.delete(itemPosition);
                        v.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                    }
                }

            }else if(v instanceof ImageButton){
                int itemPosition = getAdapterPosition();
                Log.d(TAG, "ImageButton");
                callback.editShoppingListEntry(itemPosition);
            }

        }

    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        Log.d(TAG, "onViewAttachedToWindow");
        super.onViewAttachedToWindow(holder);

        CardView cardItem = holder.itemView.findViewById(R.id.cardViewShoppingListItem);
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

        CardView cardItem = holder.itemView.findViewById(R.id.cardViewShoppingListItem);
        if(isItemSelected(holder.getLayoutPosition())){
            cardItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        }else{
            cardItem.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
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

    public CustomRecyclerShoppingList(AdapterCallback callback, Context context, ArrayList<ArticleItem> data) {
        this.callback = callback;
        mContext = context;
        imageLoader = new ImageLoader(context);
        this.web = data;
    }

    public interface AdapterCallback{
        void changeEditMode();
        int getEditMode();
        void setEditMode(int mode);
        void transferItem(ArticleItem item, int placeId);
        void editShoppingListEntry(int position);
        void transferToStock(ArticleItem item, int placeId);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_single_shopping_list_item, parent, false);
        Log.d("onCreateViewHolder", "JA");

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        Log.d("onBindViewHolder", holder.toString());
        Log.d("Merkel", String.valueOf(listPosition));

        if(!web.get(listPosition).getShoppingListPicture().equals("")){
            Log.d("IMAGE", web.get(listPosition).getShoppingListPicture());
            ImageView imageArticleItemView = holder.imageArticleItemView;
            this.imageLoader.DisplayImage(web.get(listPosition).getShoppingListPicture(), imageArticleItemView);
        }else{
            ImageView imageArticleItemView = holder.imageArticleItemView;
            imageArticleItemView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_cart_black));
        }

        // Article-Name if Article is Not defined as Barcode
        TextView txtViewTextShoppingListArticleName = holder.txtViewTextShoppingListArticleName;
        txtViewTextShoppingListArticleName.setText(web.get(listPosition).getShoppingListName());

        UnitDAO units = new UnitDAO();

        // Shopping-List-Article-Quantity
        TextView txtViewTextShoppingListQuantity = holder.txtViewTextShoppingListQuantity;
        txtViewTextShoppingListQuantity.setText(String.valueOf(web.get(listPosition).getShoppingListQuantity()) + " " + units.getUnitIndexToName(web.get(listPosition).getUnitId()));

    }

    /**
     * getItemCount
     * Return the quantity of ArticleItems
     */
    public int getItemCount() {
        Log.d("getItemCount", String.valueOf(web.size()));
        return web.size();
    }

    public ArrayList<ArticleItem> getShoppingList(){
        return web;
    }

    /**
     * setShoppingList
     * Adds a new ArticleItem to the Adapter
     * @param item
     */
    public void setShoppingList(ArticleItem item){
        Log.d("setShoppingList", String.valueOf(web.size()));
        web.add(item);
    }

    /**
     * changeShoppingListItem
     * Method to change the Item in Adapter
     * @param key Item in ArrayList<ArticleItem>
     * @param articleItem Item which should be updated
     * */
    public void changeShoppingListItem(String key, ArticleItem articleItem){
        int count = 0;

        while (web.size() > count) {
            if(web.get(count).getShoppingListKey().equals(key)){
                web.set(count, articleItem);
            }
            count++;
        }
    }

    /**
     * deleteShoppingListItem
     * Method to delete the Item
     * @param key Item in ArrayList<ArticleItem>
     * */
    public void deleteShoppingListItem(String key){
        Log.d(TAG, "deleteShoppingListItem");

        int count = 0;

        while (web.size() > count) {
            if(web.get(count).getShoppingListKey().equals(key)){
                web.remove(count);
            }
            count++;
        }
    }

    /**
     * deleteShoppingListItem
     * Method to delete the Item
     * @param position Item in ArrayList<ArticleItem>
     * */
    public void deleteShoppingListItem(int position){
        Log.d(TAG, "deleteShoppingListItem");

        web.remove(position);
    }

    /**
     * deleteShoppingListItemSelected
     * Method to delete the Item
     * */
    public void deleteShoppingListItemSelected(){
        Log.d(TAG, "deleteShoppingListItemSelected");

        for(int i=0;i<web.size(); i++) {
            if (selectedItems.get(i))
                web.remove(i);
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

        if(web.size() > 0){
            while (web.size() > count) {
                Log.d("LARS", key.toString());
                Log.d("LARS", web.get(count).getShoppingListKey());
                if(web.get(count).getShoppingListKey().equals(key)){
                    if(web.get(count).getPlaceId() == 1 || web.get(count).getPlaceId() == 2){
                        match = true;
                    }
                }
                count++;
            }
        }

        return match;
    }

    /**
     * getShoppingListItem
     * @return ArticleItem
     * @param position Item Position
     */
    public ArticleItem getShoppingListItem(int position){
        Log.d("getShoppingListItem", String.valueOf(web.size()));
        return web.get(position);
    }

    /**
     * getArticleItems
     * @return ArrayList<ArticleItem>
     */
    public ArrayList<ArticleItem> getArticleItems(){
        return web;
    }

    /**
     * getSelectedItems
     * Returns all Items which were selected to delete
     * @return SparseBooleanArray
     */
    public SparseBooleanArray getSelectedItems(){
        return this.selectedItems;
    }

    /**
     * isItemSelected
     * Returns if the Item is selected
     * @return boolean
     * @param position Position
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
     * setTransferToStock
     * Transfers ONE Item to (placeId)
     * @param item ArticleItem
     * @param placeId int
     */
    public void setTransferArticle(ArticleItem item, int placeId){
        Log.d(TAG, "setTransferArticle");
        this.callback.transferItem(item, placeId);
    }


    /**
     * setTransferToStock
     * Transfers ALL Items to Stock
     * @param placeId
     */
    public void setTransferToStock(int placeId){
        Log.d(TAG, "setTransferToStock");

        for(int i=0; i<web.size(); i++){
            this.callback.transferToStock(web.get(i), placeId);
        }
    }

    /**
     * deleteItems
     * Delets all Items in web
     */
    public void deleteItems(){
        web.clear();
    }

}
