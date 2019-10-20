package com.toolbardemo.mytestapp;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.toolbardemo.mytestapp.DAO.ArticleSearchDAO;
import com.toolbardemo.mytestapp.DAO.ShoppingListDAO;
import com.toolbardemo.mytestapp.DAO.ShoppingListsDAO;
import com.toolbardemo.mytestapp.DAO.StoreDAO;
import com.toolbardemo.mytestapp.DAO.StorePositionDAO;
import com.toolbardemo.mytestapp.DAO.UnitDAO;
import com.toolbardemo.mytestapp.adapter.CustomPlacePosition;
import com.toolbardemo.mytestapp.adapter.CustomRecyclerStock;
import com.toolbardemo.mytestapp.adapter.CustomStore;
import com.toolbardemo.mytestapp.adapter.CustomStorePosition;
import com.toolbardemo.mytestapp.adapter.CustomUnit;
import com.toolbardemo.mytestapp.adapter.SearchAdapter;
import com.toolbardemo.mytestapp.adapter.ViewPagerAdapter;
import com.toolbardemo.mytestapp.data.ArticleItem;
import com.toolbardemo.mytestapp.data.ArticleStore;
import com.toolbardemo.mytestapp.data.ShoppingLists;
import com.toolbardemo.mytestapp.fragment.FragmentViewPagerCooler;
import com.toolbardemo.mytestapp.fragment.FragmentViewPagerFridge;
import com.toolbardemo.mytestapp.fragment.FragmentViewPagerPantry;
import com.toolbardemo.mytestapp.image.ImageLoader;
import com.toolbardemo.mytestapp.widgets.MyEditTextDatePicker;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.toolbardemo.mytestapp.ShoppingListsActivity.getDatabase;

public class StockActivity extends AppCompatActivity  implements CustomRecyclerStock.AdapterCallback,
                                                                 CustomUnit.AdapterCallback,
                                                                 CustomStore.AdapterCallback,
                                                                 CustomStorePosition.AdapterCallback,
                                                                 CustomPlacePosition.AdapterCallback,
                                                                 SearchAdapter.AdapterCallback {
    private final String TAG = "ShoppingListActivity";

    public Activity mActivity;
    public Context mContext;
    public SearchAdapter.AdapterCallback mCallback;

    private ShoppingListDAO mShoppingListDAO = new ShoppingListDAO();
    private ShoppingListDAO mShoppingListDAO2 = new ShoppingListDAO();
    private ShoppingListDAO mShoppingListDAO3 = new ShoppingListDAO();
    private ArticleSearchDAO articleSearchDAO = new ArticleSearchDAO();
    private ShoppingListsDAO shoppingListsDAO = new ShoppingListsDAO();
    private UnitDAO mUnitDAO = new UnitDAO();
    private StoreDAO mStoreDAO = new StoreDAO();
    private CustomStore dataStoreAdapter;
    private CustomStorePosition dataStorePositionAdapter;
    private CustomPlacePosition dataPlaceAdapter;
    private StorePositionDAO mStorePositionDAO;
    private String SHOPPING_LIST_ID;

    private int mUnitSelected;
    private int mStoreSelected;
    private int mStorePositionSelected;

    // Firebase
    public FirebaseDatabase database = getDatabase();
    public DatabaseReference mRootRef = database.getReference();
    public DatabaseReference mConditionChildRef = mRootRef.child("shopping_list").child(MainActivity.mProfile.getUid());
    public DatabaseReference mShoppingLists = mRootRef.child("shopping_lists").child(MainActivity.mProfile.getUid());

    public static MenuInflater inflater;
    private Menu menu;
    private Spinner spinner;
    private Spinner spinnerStore;
    private Spinner spinnerStorePosition;
    private Spinner spinnerStorePlace;
    private Menu search_menu;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapterViewPager;
    private static int mViewPagerPage = 0;

    private CustomRecyclerStock adapterFridge;
    private CustomRecyclerStock adapterCooler;
    private CustomRecyclerStock adapterPantry;
    private CustomUnit dataAdapter;

    private static int mEditMode = 0;
    private static boolean mSearchMode = false;

    private SearchAdapter searchAdapter;
    private ImageLoader imageLoader;

    boolean isFABOpen=false;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fabBGLayout) View fabBGLayout;
    @BindView(R.id.fabLayout1) LinearLayout fabLayout1;
    @BindView(R.id.fabLayout2) LinearLayout fabLayout2;
    @BindView(R.id.fab_shopping_list) FloatingActionButton mFabAdd;
    @BindView(R.id.fab_shopping_cart) FloatingActionButton mFabCart;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.textview_shopping_list) TextView textviewShoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        ButterKnife.bind(this);

        Log.d(TAG, "onCreate");

        // Toolbar
        toolbar.setTitle(R.string.stock_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        search_menu = toolbar.getMenu();
        //setSearchable();

        mActivity = this;
        mContext = this;
        mCallback = this;
        mStorePositionDAO = new StorePositionDAO(this);

        imageLoader = new ImageLoader(mContext);

        // Set Adapter
        adapterFridge = new CustomRecyclerStock(this, this, mShoppingListDAO.getShoppingList());
        adapterCooler = new CustomRecyclerStock(this, this, mShoppingListDAO2.getShoppingList());
        adapterPantry = new CustomRecyclerStock(this, this, mShoppingListDAO3.getShoppingList());


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

        adapterFridge.deleteItems();
        adapterCooler.deleteItems();
        adapterPantry.deleteItems();

        Query query = mConditionChildRef;
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("query:onChildAdded", dataSnapshot.toString());

                /*adapterFridge.deleteItems();
                adapterCooler.deleteItems();
                adapterPantry.deleteItems();*/

                if(dataSnapshot.exists()){
                    Log.d("ShoppingList-Key", dataSnapshot.getKey());

                    for(DataSnapshot d : dataSnapshot.getChildren()){
                        Log.d("dParam", d.toString());

                        ArticleItem item = new ArticleItem();
                        ArticleStore store = new ArticleStore();

                        for(DataSnapshot e : d.getChildren()){
                            //Log.d("eParam", d.toString());
                            if (e.getKey().equals("store") && Integer.parseInt(d.child("placeId").getValue().toString()) == 3) {

                                Log.d("Gregor", d.toString());



                                //Log.d("Storeeeee", e.getValue().toString());
                                store = e.getValue(ArticleStore.class);

                                item = d.getValue(ArticleItem.class);
                                item.setShoppingListsKey(dataSnapshot.getKey());
                                item.setShoppingListKey(d.getKey());
                                item.setStore(store);

                                Log.d("Gregor2", String.valueOf(store.getStoreId()));


                            }
                        }

                        if (store.getStoreId() == 2) {
                            adapterFridge.setShoppingList(item);
                            adapterFridge.notifyDataSetChanged();
                        } else if (store.getStoreId() == 3) {
                            adapterCooler.setShoppingList(item);
                            adapterCooler.notifyDataSetChanged();
                        } else if (store.getStoreId() == 1) {
                            adapterPantry.setShoppingList(item);
                            adapterPantry.notifyDataSetChanged();
                        }


                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded");
                Log.d(TAG, dataSnapshot.toString());

                for(DataSnapshot d : dataSnapshot.getChildren()){
                    //Log.d("dParam", d.toString());

                    //if(e.child("store").exists() && Integer.valueOf(d.child(e.getKey()).child("placeId").getValue().toString()) == 3) {
                    //Log.d("moveItemHistory", d.getKey());

                    if (d.child("store").exists() && Integer.parseInt(d.child("placeId").getValue().toString()) == 4) {
                        //Log.d("store", String.valueOf(d.child("store").exists()));
                        //Log.d("placeId", d.child("placeId").getValue().toString());

                        if (mViewPagerPage == 0) {
                            adapterFridge.deleteShoppingListItem(d.getKey());
                            adapterFridge.notifyDataSetChanged();
                        } else if (mViewPagerPage == 1) {
                            adapterCooler.deleteShoppingListItem(d.getKey());
                            adapterCooler.notifyDataSetChanged();
                        } else if (mViewPagerPage == 2) {
                            adapterPantry.deleteShoppingListItem(d.getKey());
                            adapterPantry.notifyDataSetChanged();
                        }
                    }else{
                        Log.d("DUMBATZZZ2", d.getValue().toString());
                        ArticleItem item = d.getValue(ArticleItem.class);

                        if (mViewPagerPage == 0) {
                            //Log.d("Mama_1", d.getKey());
                            //Log.d("Papa_1", d.getValue().toString());
                            adapterFridge.changeShoppingListItem(d.getKey(), item);
                            adapterFridge.notifyDataSetChanged();
                        } else if (mViewPagerPage == 1) {
                            adapterCooler.changeShoppingListItem(d.getKey(), item);
                            adapterCooler.notifyDataSetChanged();
                        } else if (mViewPagerPage == 2) {
                            adapterPantry.changeShoppingListItem(d.getKey(), item);
                            adapterPantry.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved");
                Log.d("onChildRemoved", dataSnapshot.toString());

                /*if(mViewPagerPage == 0) {
                    adapterFridge.deleteShoppingListItem(dataSnapshot.getKey());
                    adapterFridge.notifyDataSetChanged();
                }else if(mViewPagerPage == 1){
                    adapterCooler.deleteShoppingListItem(dataSnapshot.getKey());
                    adapterCooler.notifyDataSetChanged();
                }else if(mViewPagerPage == 2){
                    adapterPantry.deleteShoppingListItem(dataSnapshot.getKey());
                    adapterPantry.notifyDataSetChanged();
                }*/
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.containerViewpagerPantry);
        adapterViewPager = new ViewPagerAdapter(getSupportFragmentManager());

        adapterViewPager.AddFragment(new FragmentViewPagerFridge().newInstance(adapterFridge));
        adapterViewPager.AddFragment(new FragmentViewPagerCooler().newInstance(adapterCooler));
        adapterViewPager.AddFragment(new FragmentViewPagerPantry().newInstance(adapterPantry));

        viewPager.setAdapter(adapterViewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_fridge_white);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_cooler_white);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_pantry_white);

        setListeners();
        setFabListeners();

    }

    private void setFabListeners(){

        fabBGLayout.setBackgroundColor(Color.parseColor("#55D0D0D0"));

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });

        fabLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Test", Toast.LENGTH_LONG).show();
            }
        });

        fabLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), R.string.shopping_list_cart_moved, Toast.LENGTH_LONG).show();
                /*adapterShoppingListCart.setTransferToStock(3);

                if(adapterShoppingListCart.getArticleItems().size() == 0){
                    mConditionCart.child("shoppingListsBought").setValue(1);
                }

                adapterShoppingListCart.notifyDataSetChanged();*/
            }
        });
    }

    @Override
    public void onUnitSelect(int i) {
        Log.d(TAG, "onTest");
        Log.d(TAG, String.valueOf(i));

        mUnitSelected = i;

    }

    private void setListeners(){
        Log.d(TAG, "setListeners");

        final LayoutInflater inflater = getLayoutInflater();

        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closeFABMenu();

                final View viewEdit = inflater.inflate(R.layout.dialog_shopping_list, null);
                addItemsOnSpinner(viewEdit);

                ImageButton btnCalender = viewEdit.findViewById(R.id.btnCalender);
                final MyEditTextDatePicker calenderBtn = new MyEditTextDatePicker(btnCalender, mContext);

                final EditText textArticleDurability = viewEdit.findViewById(R.id.txtShoppingListArticleDurability);
                calenderBtn.setDurabilityField(textArticleDurability);

                final Spinner spinnerStoreLocal = viewEdit.findViewById(R.id.txtShoppingListArticleStore);
                final Spinner spinnerStorePositionLocal = viewEdit.findViewById(R.id.txtShoppingListArticleStoreSub);


                // Click action
                new AlertDialog.Builder(mContext)
                        .setTitle(R.string.shopping_list_article_new)
                        .setView(viewEdit)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "Ja");
                                //EditText text = findViewById(R.id.txtShoppingListsName);
                                EditText textArticleName= viewEdit.findViewById(R.id.txtShoppingListArticleName);
                                EditText textArticleQuantity = viewEdit.findViewById(R.id.txtShoppingListArticleQuantity);

                                Log.d("textArticleName", textArticleName.getText().toString());
                                Log.d("textArticleQuantity", textArticleQuantity.getText().toString());
                                Log.d("textArticleDurability", textArticleDurability.getText().toString());

                                Double dblArticleQuantity = Double.parseDouble(textArticleQuantity.getText().toString());

                                //ArticleStore articleStore = new ArticleStore();
                                //articleStore.setStoreId(dataStoreAdapter.getItem(spinnerStoreLocal.getSelectedItemPosition()).getStoreId());
                                Log.d("POOOOP", String.valueOf(spinnerStoreLocal.getSelectedItemPosition()));

                                createArticleItem(textArticleName.getText().toString(),
                                                  dblArticleQuantity, textArticleDurability.getText().toString(),
                                                 0,
                                                 "",
                                                 setArticleStore(spinnerStoreLocal.getSelectedItemPosition(), spinnerStorePositionLocal.getSelectedItemPosition()),
                                                 "0",
                                                 1);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d(TAG, "Nein");
                    }
                }).create().show();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /*if (position == MANDATORY_PAGE_LOCATION && positionOffset > 0.5) {
                    viewPager.setCurrentItem(MANDATORY_PAGE_LOCATION, true);
                }*/
                if(mEditMode == 1) {
                    if (viewPager.getAdapter().getCount() > 1) {
                        viewPager.setCurrentItem(0);
                    }
                }
            }
            public void onPageSelected(int position) {
                // Check if this is the page you want.
                Log.d("onPageSelected", String.valueOf(position));

                mViewPagerPage = position;

                Log.d("position", String.valueOf(position));
                Log.d("mEditMode", String.valueOf(mEditMode));

                if(position == 0 && mEditMode == 1){
                    setVisibleAddButton(View.GONE);
                }else{
                    setVisibleAddButton(View.VISIBLE);
                }
            }
        });

    }

    private ArticleStore setArticleStore(int positionStore, int positionStorePosition){

        ArticleStore articleStore = new ArticleStore();
        articleStore.setStoreId(dataStoreAdapter.getSelectedItemIndex(positionStore));

        Log.d("mStorePositionSelected", String.valueOf(positionStorePosition));

        if(dataStoreAdapter.getItem(positionStore).getStoreName().equals("Fridge")){
            switch(dataStorePositionAdapter.getItem(positionStorePosition).getStorePositionId()){
                case 0:
                    articleStore.setStorageLevel_3(true);
                    break;
                case 1:
                    articleStore.setStorageLevel_2(true);
                    break;
                case 2:
                    articleStore.setStorageLevel_1(true);
                    break;
                case 3:
                    articleStore.setStorageLevelVegDrawer(true);
                    break;
                case 4:
                    articleStore.setStorageLevelDoorLevel_3(true);
                    break;
                case 5:
                    articleStore.setStorageLevelDoorLevel_2(true);
                    break;
                case 6:
                    articleStore.setStorageLevelDoorLevel_1(true);
                    break;
            }
        }else{
            articleStore.setAllToFalse();
        }

        return articleStore;
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

    public final void setVisibleAddButton(final int STATUS){
        Animation anim;

        Log.d("Kaiba", String.valueOf(mEditMode));

        if(STATUS == View.VISIBLE){
            anim = AnimationUtils.loadAnimation(
                    mFabAdd.getContext(), R.anim.design_fab_in);
        }else{
            anim = AnimationUtils.loadAnimation(
                    mFabAdd.getContext(), R.anim.design_fab_out);
            textviewShoppingList.setVisibility(View.INVISIBLE);
        }

        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(200);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mFabAdd.setVisibility(STATUS);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mFabAdd.startAnimation(anim);

    }

    private void showFABMenu(){
        isFABOpen=true;
        fabLayout1.setVisibility(View.VISIBLE);
        //fabLayout2.setVisibility(View.VISIBLE);
        fabBGLayout.setVisibility(View.VISIBLE);

        mFab.animate().rotationBy(180);
        fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        //fabLayout2.animate().translationX(-getResources().getDimension(R.dimen.standard_55));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fabBGLayout.setVisibility(View.GONE);
        mFab.animate().rotationBy(-180);
        fabLayout1.animate().translationY(0);
        fabLayout2.animate().translationX(0);
        mFab.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!isFABOpen){
                    fabLayout1.setVisibility(View.GONE);
                    fabLayout2.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void editShoppingListEntry(int position){
        final ArticleItem data;
        if(mViewPagerPage == 0){
            data = adapterFridge.getArticleItems().get(position);
        }else if(mViewPagerPage == 1){
            data = adapterCooler.getArticleItems().get(position);
        }else {
            data = adapterPantry.getArticleItems().get(position);
        }

        LayoutInflater inflater = getLayoutInflater();

        final View viewEdit = inflater.inflate(R.layout.dialog_shopping_list, null);
        addItemsOnSpinner(viewEdit);

        final ImageView imageView = viewEdit.findViewById(R.id.imageViewArticle);
        this.imageLoader.DisplayImage(data.getShoppingListPicture(), imageView);

        final EditText textArticleName = viewEdit.findViewById(R.id.txtShoppingListArticleName);
        textArticleName.setText(data.getShoppingListName());

        final EditText textArticleQuantity = viewEdit.findViewById(R.id.txtShoppingListArticleQuantity);
        textArticleQuantity.setText(String.valueOf(data.getShoppingListQuantity()));

        final Spinner spinnerLocal = viewEdit.findViewById(R.id.txtShoppingListArticleUnit);
        spinnerLocal.setSelection(data.getUnitId());

        ImageButton btnCalender = viewEdit.findViewById(R.id.btnCalender);
        final MyEditTextDatePicker calenderBtn = new MyEditTextDatePicker(btnCalender, mContext);

        final EditText textArticleDurability = viewEdit.findViewById(R.id.txtShoppingListArticleDurability);
        textArticleDurability.setText(data.getShoppingListDurability());
        calenderBtn.setDurabilityField(textArticleDurability);

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
                .setTitle(R.string.shopping_list_article_edit)
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

                        editArticleItem(data);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.d(TAG, "Nein");
            }
        }).create().show();
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

    @Override
    public void onPlacePositionSelect(int i) {

    }

    public void editArticleItem(ArticleItem articleItem){
        Log.d(TAG, "editArticleItem");
        Log.d(TAG, articleItem.getShoppingListsKey());
        Log.d(TAG, articleItem.getShoppingListKey());

        /* shopping_list|user_id|shoppingListsKey|shoppingListKey */
        mConditionChildRef.child(articleItem.getShoppingListsKey()).child(articleItem.getShoppingListKey()).setValue(articleItem);

    }

    @Override
    public void onStoreSelect(int i) {
        Log.d(TAG, "onStoreSelect");

        mStoreSelected = i;
        if(mStoreSelected == 0){
            spinnerStorePosition.setEnabled(true);
        }else{
            spinnerStorePosition.setEnabled(false);
        }

    }

    @Override
    public void onStorePositionSelect(int i) {
        Log.d(TAG, "onStorePositionSelect");
        Log.d(TAG, String.valueOf(i));

        mStorePositionSelected = i;

    }

    public void moveArticleItemToHistory(ArticleItem articleItem){
        Log.d(TAG, "moveArticleItemToHistory");
        Log.d(TAG, articleItem.getShoppingListKey());

        mConditionChildRef.child(articleItem.getShoppingListsKey()).child(articleItem.getShoppingListKey()).child("placeId").setValue(4);

    }

    @Override
    public void getArticleSearchPosition(int position) {

    }

    @Override
    public void moveToHistory(int placeId){
        Log.d(TAG, "moveToHistory");

        final ArticleItem data;
        if(mViewPagerPage == 0){
            data = adapterFridge.getArticleItems().get(placeId);
        }else if(mViewPagerPage == 1){
            data = adapterCooler.getArticleItems().get(placeId);
        }else {
            data = adapterPantry.getArticleItems().get(placeId);
        }

        moveArticleItemToHistory(data);
    }

    @Override
    public void createToShopping(final int position, final int movePosition) {
        Log.d(TAG, "createToShoppingList");

        LayoutInflater inflater = getLayoutInflater();

        final View viewMessage = inflater.inflate(R.layout.dialog_no_shoppinglist, null);
        addItemsOnSpinner(viewMessage);

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
                    }).create().show();

        }else{
            doCreateToShoppingList(position, movePosition);
        }

    }

    private void doCreateToShoppingList(int position, final int movePosition){

        LayoutInflater inflater = getLayoutInflater();

        final ArticleItem data;
        if(mViewPagerPage == 0){
            data = adapterFridge.getArticleItems().get(position);
        }else if(mViewPagerPage == 1){
            data = adapterCooler.getArticleItems().get(position);
        }else {
            data = adapterPantry.getArticleItems().get(position);
        }

        final View viewEdit = inflater.inflate(R.layout.dialog_stock_new_list, null);
        addItemsOnSpinner(viewEdit);

        final ImageView imageView = viewEdit.findViewById(R.id.imageViewArticle);
        this.imageLoader.DisplayImage(data.getShoppingListPicture(), imageView);


        final EditText textArticleName = viewEdit.findViewById(R.id.txtShoppingListArticleName);
        textArticleName.setText(data.getShoppingListName());

        final EditText textArticleQuantity = viewEdit.findViewById(R.id.txtShoppingListArticleQuantity);
        textArticleQuantity.setText(String.valueOf(data.getShoppingListQuantity()));



        final Spinner spinnerLocal = viewEdit.findViewById(R.id.txtShoppingListArticleUnit);
        spinnerLocal.setSelection(data.getUnitId());

        final Spinner spinnerStorePlace = viewEdit.findViewById(R.id.txtShoppingListArticleNewList);
        Log.d("Schrammmm1", String.valueOf(data.getShoppingListsKey()));
        Log.d("Schrammmm2", String.valueOf(dataPlaceAdapter.getItemPosition(data.getShoppingListsKey())));
        spinnerStorePlace.setSelection(dataPlaceAdapter.getItemPosition(data.getShoppingListsKey()));


        /*final Spinner spinnerStoreLocal = viewEdit.findViewById(R.id.txtShoppingListArticleStore);
        Log.d("SpinnerNo2", String.valueOf(data.getStore().getStoreId()));
        spinnerStoreLocal.setSelection(dataStoreAdapter.getSelectedItemPosition(data.getStore().getStoreId()));

        final Spinner spinnerStorePositionLocal = viewEdit.findViewById(R.id.txtShoppingListArticleStoreSub);
        Log.d("SpinnerNo3", String.valueOf(data.getSelectedStorePlace()));
        spinnerStorePositionLocal.setSelection(data.getSelectedStorePlace());*/


        ImageButton btnCalender = viewEdit.findViewById(R.id.btnCalender);
        final MyEditTextDatePicker calenderBtn = new MyEditTextDatePicker(btnCalender, mContext);

        final EditText textArticleDurability = viewEdit.findViewById(R.id.txtShoppingListArticleDurability);
        textArticleDurability.setText(data.getShoppingListDurability());
        calenderBtn.setDurabilityField(textArticleDurability);

        final ArticleStore articleStore = data.getStore();

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

                        Log.d("XXXX", data.getShoppingListsKey());

                        moveArticleItemToHistory(data);
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
                }).create().show();

    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");

        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");
        int id = item.getItemId();

        if (id == android.R.id.home){
            Log.d(TAG, "android.R.id.home");
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
