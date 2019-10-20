package com.toolbardemo.mytestapp;

import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.os.Vibrator;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.util.SparseBooleanArray;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.toolbardemo.mytestapp.DAO.FavoritesDAO;
import com.toolbardemo.mytestapp.DAO.ShoppingListsDAO;
import com.toolbardemo.mytestapp.DAO.StoreDAO;
import com.toolbardemo.mytestapp.DAO.StorePositionDAO;
import com.toolbardemo.mytestapp.DAO.UnitDAO;
import com.toolbardemo.mytestapp.adapter.CustomPlacePosition;
import com.toolbardemo.mytestapp.adapter.CustomRecyclerFavorites;
import com.toolbardemo.mytestapp.adapter.CustomStore;
import com.toolbardemo.mytestapp.adapter.CustomStorePosition;
import com.toolbardemo.mytestapp.adapter.CustomUnit;
import com.toolbardemo.mytestapp.data.ArticleItem;
import com.toolbardemo.mytestapp.data.ArticleStore;
import com.toolbardemo.mytestapp.data.Favorites;
import com.toolbardemo.mytestapp.data.ShoppingLists;
import com.toolbardemo.mytestapp.image.ImageLoader;
import com.toolbardemo.mytestapp.widgets.MyEditTextDatePicker;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.toolbardemo.mytestapp.MainActivity.context;
import static com.toolbardemo.mytestapp.ShoppingListsActivity.getDatabase;

public class FavoritesActivity extends AppCompatActivity implements CustomRecyclerFavorites.AdapterCallback,
                                                                    CustomUnit.AdapterCallback,
                                                                    CustomStore.AdapterCallback,
                                                                    CustomStorePosition.AdapterCallback,
                                                                    CustomPlacePosition.AdapterCallback {
    private final String TAG = "FavoritesActivity";

    public Activity mActivity;
    public Context mContext;
    private FavoritesDAO mFavoritesDAO = new FavoritesDAO();

    private static int mEditMode = 0;
    private boolean isAllSelected = false;
    private Menu menu;
    private Spinner spinner;
    private Spinner spinnerStore;
    private Spinner spinnerStorePosition;
    private Spinner spinnerStorePlace;

    private CustomUnit dataAdapter;
    private CustomStore dataStoreAdapter;
    private CustomStorePosition dataStorePositionAdapter;
    private CustomPlacePosition dataPlaceAdapter;

    private ShoppingListsDAO shoppingListsDAO = new ShoppingListsDAO();
    private StorePositionDAO mStorePositionDAO;

    private UnitDAO mUnitDAO = new UnitDAO();
    private StoreDAO mStoreDAO = new StoreDAO();

    private int mUnitSelected;

    // FireBase
    public FirebaseDatabase database = getDatabase();
    public DatabaseReference mRootRef = database.getReference();
    private DatabaseReference mFavorites = mRootRef.child("favorites").child(MainActivity.mProfile.getUid());
    private DatabaseReference mConditionChildRef = mRootRef.child("shopping_list").child(MainActivity.mProfile.getUid());
    public DatabaseReference mShoppingLists = mRootRef.child("shopping_lists").child(MainActivity.mProfile.getUid());

    // CardView
    private CustomRecyclerFavorites adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ImageLoader imageLoader;

    @BindView(R.id.toolbarFavorites) Toolbar toolbar;
    @BindView(R.id.recyclerViewFavorites) RecyclerView recyclerView;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        ButterKnife.bind(this);

        mRootRef.keepSynced(true);

        // Toolbar
        toolbar.setTitle(R.string.favorite_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mActivity = this;
        mContext = this;

        imageLoader = new ImageLoader(mContext);


        //VectorDrawable navIcon = (VectorDrawable) toolbar.getNavigationIcon();
        //navIcon.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);


        // CardView-Layout
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        // CardView-Content
        adapter = new CustomRecyclerFavorites(this,this, mFavoritesDAO.getFavorites());

        Query queryShoppingLists = mShoppingLists;
        queryShoppingLists.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("mShoppingLists", dataSnapshot.toString());

                Log.d("Priol", dataSnapshot.child("shoppingListsBought").getValue().toString());

                if(Integer.parseInt(dataSnapshot.child("shoppingListsBought").getValue().toString()) == 2){
                    ShoppingLists item = dataSnapshot.getValue(ShoppingLists.class);
                    item.setShoppingListsKey(dataSnapshot.getKey());
                    shoppingListsDAO.setShoppingListItem(item);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mFavorites.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("dataSnapshot", dataSnapshot.toString());
                Favorites favorites = dataSnapshot.getValue(Favorites.class);
                adapter.setFavorites(favorites);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.deleteFavoriteItem(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        this.menu = menu;

        if(mEditMode != 0) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.toolbar_submenu_delete, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.selectItems) {
            Log.d(TAG, "selectItems");
            for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; ++i) {
                final RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                View cardItem = holder.itemView.findViewById(R.id.cardViewFavoriteItem);

                Log.d("EDDYYY", String.valueOf(isAllSelected));

                if(!isAllSelected){
                    cardItem.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    adapter.setSelectedPosition(i);
                }else{
                    this.setEditModeBackColor();
                    adapter.setSelectedPosition(i);
                }
            }
            if(!isAllSelected){
                isAllSelected = true;
            }else{
                isAllSelected = false;
            }
            //
        }else if(id == R.id.trashCan) {
            Log.d(TAG, "trashCan");
            deleteItems(adapter.getSelectedItems());
            Log.d("trashCan", adapter.getSelectedItems().toString());
            adapter.deleteAllSelectedItems();
            mEditMode = 0;
            this.setEditModeBackColor();
            this.changeEditMode();

        }else if (id == android.R.id.home){
            if(mEditMode == 1){
                mEditMode = 0;
                this.changeEditMode();
                this.setEditModeBackColor();
            }else{
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void changeEditMode() {
        Log.d(TAG, "changeEditMode");

        if(mEditMode == 1){
            Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(100);

            this.menu.clear();

            getMenuInflater().inflate(R.menu.toolbar_submenu_delete, this.menu);
        }else{
            this.menu.clear();
            getMenuInflater().inflate(R.menu.toolbar_submenu_shoppinglist, menu);
        }
    }

    @Override
    public int getEditMode(){
        Log.d(TAG, "getEditMode");
        return mEditMode;
    }

    @Override
    public void setEditMode(int mode){
        Log.d(TAG, "setEditMode");
        this.mEditMode = mode;
    }

    @Override
    public void onUnitSelect(int i) {
        Log.d(TAG, "onTest");
        Log.d(TAG, String.valueOf(i));

        mUnitSelected = i;
    }


    public void deleteItems(SparseBooleanArray selectedItems){

        Log.d("selectedItems", String.valueOf(selectedItems));

        for (int i = 0; i < adapter.getItemCount(); ++i) {
            final RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            TextView txtItemKey = holder.itemView.findViewById(R.id.txtViewTextFavoriteKey);
            if(selectedItems.get(i) == true) {
                Log.d(TAG, String.valueOf(txtItemKey.getText()));
                Log.d("selectedItems", "deleteItems");
                mFavorites.child(String.valueOf(txtItemKey.getText())).removeValue();
            }
        }
    }

    @Override
    public void createToShopping(final int position, final int movePosition) {
        Log.d(TAG, "createToShoppingList");

        LayoutInflater inflater = getLayoutInflater();

        final View viewMessage = inflater.inflate(R.layout.dialog_no_shoppinglist, null);
        addItemsOnSpinner(viewMessage);

        Log.d("shoppingListsDAO", String.valueOf(shoppingListsDAO.getCount()));
        if(shoppingListsDAO.getCount() == 0){

            // Click action
            new AlertDialog.Builder(mContext)
                    .setTitle(R.string.shopping_no_list)
                    .setView(viewMessage)
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d(TAG, "Ja");

                            Log.d("KENKEN", String.valueOf(shoppingListsDAO.getCount()));

                            Intent intent = new Intent(mActivity, ShoppingListsActivity.class);
                            mActivity.startActivity(intent);

                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Log.d(TAG, "Nein");
                        }
                    }).setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                                dialog.dismiss();
                            return false;
                        }
                    }).create().show();

        }else{
            doCreateToShoppingList(position, movePosition);
        }

    }

    // add items into spinner dynamically
    public void addItemsOnSpinner(View view) {
        Log.d(TAG, "addItemsOnSpinner");

        // UNIT
        spinner = view.findViewById(R.id.txtShoppingListArticleUnit);

        if(spinner != null) {
            dataAdapter = new CustomUnit(this, this, mUnitDAO.getUnits());
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spinner.setOnItemSelectedListener(dataAdapter);
            spinner.setAdapter(dataAdapter);
        }

        // STORE
        spinnerStore = view.findViewById(R.id.txtShoppingListArticleStore);

        if(spinnerStore != null){
            dataStoreAdapter = new CustomStore(this, this, mStoreDAO.getStore());
            dataStoreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spinnerStore.setOnItemSelectedListener(dataStoreAdapter);
            spinnerStore.setAdapter(dataStoreAdapter);
        }

        // STORE-POSITION
        spinnerStorePosition = view.findViewById(R.id.txtShoppingListArticleStoreSub);

        if(spinnerStorePosition != null) {
            dataStorePositionAdapter = new CustomStorePosition(this, this, mStorePositionDAO.getStorePosition());
            dataStorePositionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spinnerStorePosition.setOnItemSelectedListener(dataStorePositionAdapter);
            spinnerStorePosition.setAdapter(dataStorePositionAdapter);
        }

        // PLACE
        spinnerStorePlace = view.findViewById(R.id.txtShoppingListArticleNewList);

        if(spinnerStorePlace != null){
            dataPlaceAdapter = new CustomPlacePosition(this, this, shoppingListsDAO.getShoppingLists());
            dataPlaceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spinnerStorePlace.setOnItemSelectedListener(dataPlaceAdapter);
            spinnerStorePlace.setAdapter(dataPlaceAdapter);
        }
    }

    private void doCreateToShoppingList(int position, final int movePosition){

        LayoutInflater inflater = getLayoutInflater();

        final ArticleItem data = new ArticleItem();
        final Favorites dataFavorite;

        dataFavorite = adapter.getArticleItems().get(position);
        //data = xxx.getArticleItems().get(position);


        final View viewEdit = inflater.inflate(R.layout.dialog_stock_new_list, null);
        addItemsOnSpinner(viewEdit);

        final ImageView imageView = viewEdit.findViewById(R.id.imageViewArticle);
        this.imageLoader.DisplayImage(dataFavorite.getPicturePath(), imageView);


        final EditText textArticleName = viewEdit.findViewById(R.id.txtShoppingListArticleName);
        textArticleName.setText(dataFavorite.getArticleName());
        textArticleName.setSelection(textArticleName.getText().length());

        final EditText textArticleQuantity = viewEdit.findViewById(R.id.txtShoppingListArticleQuantity);
        //textArticleQuantity.setText(String.valueOf(data.getShoppingListQuantity()));

        final Spinner spinnerLocal = viewEdit.findViewById(R.id.txtShoppingListArticleUnit);
        //spinnerLocal.setSelection(data.getUnitId());

        final Spinner spinnerStorePlace = viewEdit.findViewById(R.id.txtShoppingListArticleNewList);
        //spinnerStorePlace.setSelection(dataPlaceAdapter.getItemPosition(data.getShoppingListsKey()));

        //Sets the ArticleId for the Article
        data.setArticleId(dataFavorite.getArticleId());

        /*final Spinner spinnerStoreLocal = viewEdit.findViewById(R.id.txtShoppingListArticleStore);
        Log.d("SpinnerNo2", String.valueOf(data.getStore().getStoreId()));
        spinnerStoreLocal.setSelection(dataStoreAdapter.getSelectedItemPosition(data.getStore().getStoreId()));

        final Spinner spinnerStorePositionLocal = viewEdit.findViewById(R.id.txtShoppingListArticleStoreSub);
        Log.d("SpinnerNo3", String.valueOf(data.getSelectedStorePlace()));
        spinnerStorePositionLocal.setSelection(data.getSelectedStorePlace());*/

        ImageButton btnCalender = viewEdit.findViewById(R.id.btnCalender);
        final MyEditTextDatePicker calenderBtn = new MyEditTextDatePicker(btnCalender, mContext);

        final EditText textArticleDurability = viewEdit.findViewById(R.id.txtShoppingListArticleDurability);
        //textArticleDurability.setText(data.getShoppingListDurability());
        calenderBtn.setDurabilityField(textArticleDurability);

        final ArticleStore articleStore = dataFavorite.getStore();
        //articleStore.

        /*********************************
         *  ARTICLE-STORE anpassen
         *
         */

        ImageButton btnPlus = viewEdit.findViewById(R.id.btnCalenderPlus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textArticleQuantity.getText().toString().equals("")){
                    textArticleQuantity.setText("1.0");
                }else{
                    double quantity = Double.parseDouble(textArticleQuantity.getText().toString());
                    double result = quantity + 1;
                    textArticleQuantity.setText(String.valueOf(result));
                }
            }
        });

        ImageButton btnMinus = viewEdit.findViewById(R.id.btnCalenderMinus);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textArticleQuantity.getText().toString().equals("")){
                    textArticleQuantity.setText("1.0");
                }else{
                    double quantity = Double.parseDouble(textArticleQuantity.getText().toString());

                    if(quantity != 1){
                        double result = quantity - 1;
                        textArticleQuantity.setText(String.valueOf(result));
                    }
                }
            }
        });

        // Click action
        new AlertDialog.Builder(mContext)
                .setTitle(R.string.shopping_list_article_new)
                .setView(viewEdit)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "Ja");

                        Log.d("textArticleName", textArticleName.getText().toString());
                        Log.d("textArticleQuantity", textArticleQuantity.getText().toString());
                        Log.d("textArticleDurability", textArticleDurability.getText().toString());

                        data.setShoppingListName(textArticleName.getText().toString());
                        Double dblArticleQuantity = Double.parseDouble(textArticleQuantity.getText().toString());
                        data.setShoppingListQuantity(dblArticleQuantity);
                        data.setUnitId(spinnerLocal.getSelectedItemPosition());
                        data.setShoppingListDurability(textArticleDurability.getText().toString());

                        //Log.d("XXXX", data.getShoppingListsKey());

                        createArticleItem(textArticleName.getText().toString(),
                                dblArticleQuantity,
                                textArticleDurability.getText().toString(),
                                data.getArticleId(),
                                data.getShoppingListPicture(),
                                articleStore,
                                dataPlaceAdapter.getItemPositionKey(spinnerStorePlace.getSelectedItemPosition()),
                                movePosition);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d(TAG, "Nein");
                    }
                }).setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                            dialog.dismiss();
                        return false;
                    }
                }).create().show();

    }

    public void createArticleItem(String textArticleName,
                                  Double dblArticleQuantity,
                                  String textArticleDurability,
                                  int articleId,
                                  String articleImage,
                                  ArticleStore store,
                                  String shoppingListsKey,
                                  int movePosition){
        Log.d(TAG, "createArticleItem");

        String uniqueId = mConditionChildRef.push().getKey();

        ArticleItem articleItem = new ArticleItem();
        //articleItem.setShoppingListsKey(SHOPPING_LIST_ID);
        articleItem.setUserId(MainActivity.mProfile.getUid());
        if(articleId == 0){
            articleItem.setArticleId(0);
        }else{
            articleItem.setArticleId(articleId);
        }

        if(articleId != 0 && articleImage != ""){
            articleItem.setShoppingListPicture(articleImage);
        }else{
            articleItem.setShoppingListPicture("");
        }

        articleItem.setShoppingListName(textArticleName);
        articleItem.setShoppingListQuantity(dblArticleQuantity);
        articleItem.setUnitId(mUnitSelected);
        articleItem.setShoppingListDurability(textArticleDurability);
        articleItem.setShoppingListBought(2);
        articleItem.setPlaceId(movePosition);
        articleItem.setShoppingListKey(uniqueId);
        articleItem.setShoppingListsKey(shoppingListsKey);
        articleItem.setStore(store);

        mConditionChildRef.child(shoppingListsKey).child(uniqueId).setValue(articleItem);

    }

    @Override
    public void onBackPressed() {
        // your code.
        Log.d(TAG, "onBackPressed");
        if(mEditMode == 1){
            mEditMode = 0;
            adapter.deleteAllSelectedItems();
            this.changeEditMode();
            this.setEditModeBackColor();
        }else{
            finish();
        }
    }

    @Override
    public void onPlacePositionSelect(int i) {

    }

    @Override
    public void onStoreSelect(int i) {

    }

    @Override
    public void onStorePositionSelect(int i) {

    }



    public void setEditModeBackColor(){

        for (int childCount = adapter.getItemCount(), i = 0; i < childCount; ++i) {
            final RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            View test = holder.itemView.findViewById(R.id.cardViewFavoriteItem);
            test.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }
    }
}
