package com.toolbardemo.mytestapp;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.text.TextUtils;
import android.transition.Slide;
import android.util.Log;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.toolbardemo.mytestapp.DAO.ArticleDetailsDAO;
import com.toolbardemo.mytestapp.DAO.ShoppingListsDAO;
import com.toolbardemo.mytestapp.DAO.StoreDAO;
import com.toolbardemo.mytestapp.DAO.StorePositionDAO;
import com.toolbardemo.mytestapp.DAO.UnitDAO;
import com.toolbardemo.mytestapp.adapter.CustomListArticleDetails;
import com.toolbardemo.mytestapp.adapter.CustomPlacePosition;
import com.toolbardemo.mytestapp.adapter.CustomStore;
import com.toolbardemo.mytestapp.adapter.CustomStorePosition;
import com.toolbardemo.mytestapp.adapter.CustomUnit;
import com.toolbardemo.mytestapp.data.ArticleItem;
import com.toolbardemo.mytestapp.data.ArticleStore;
import com.toolbardemo.mytestapp.data.Favorites;
import com.toolbardemo.mytestapp.data.ShoppingLists;
import com.toolbardemo.mytestapp.data.Store;
import com.toolbardemo.mytestapp.image.ImageLoader;
import com.toolbardemo.mytestapp.widgets.MyEditTextDatePicker;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.toolbardemo.mytestapp.ShoppingListsActivity.getDatabase;
import static java.security.AccessController.getContext;


public class ArticleDetailActivity extends AppCompatActivity implements CustomUnit.AdapterCallback,
                                                                        CustomStore.AdapterCallback,
                                                                        CustomStorePosition.AdapterCallback,
                                                                        CustomPlacePosition.AdapterCallback {

    private static final String TAG = "ArticleDetailActivity";
    private static final String EXTRA_IMAGE = "drawer_background";
    private static final String EXTRA_TITLE = "Article";
    private CollapsingToolbarLayout collapsingToolbarLayout;


    private ArticleDetailsDAO mArticleDetailsDAO = new ArticleDetailsDAO();
    private ShoppingListsDAO shoppingListsDAO = new ShoppingListsDAO();
    private StorePositionDAO mStorePositionDAO;

    public Activity mActivity;
    public Context mContext;

    // Spinner
    private Spinner spinner;
    private Spinner spinnerStore;
    private Spinner spinnerStorePosition;
    private Spinner spinnerStorePlace;

    private CustomUnit dataAdapter;
    private CustomStore dataStoreAdapter;
    private CustomStorePosition dataStorePositionAdapter;
    private CustomPlacePosition dataPlaceAdapter;

    private UnitDAO mUnitDAO = new UnitDAO();
    private StoreDAO mStoreDAO = new StoreDAO();

    // FireBase
    public FirebaseDatabase database = getDatabase();
    public DatabaseReference mRootRef = database.getReference();
    private DatabaseReference mFavorites;
    public DatabaseReference mShoppingLists = mRootRef.child("shopping_lists").child(MainActivity.mProfile.getUid());
    private DatabaseReference mConditionChildRef = mRootRef.child("shopping_list").child(MainActivity.mProfile.getUid());

    boolean boolLoadContent = true;
    int imageNumber = 0;
    boolean isFABOpen = false;
    private static boolean isFavoriteIn = false;
    private String favoriteIdKey;
    private int mUnitSelected;

    private ImageLoader imageLoader;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fabBGLayoutDetail) View fabBGLayoutDetail;
    @BindView(R.id.fabLayout1) LinearLayout fabLayout1;
    @BindView(R.id.fabLayout2) LinearLayout fabLayout2;
    @BindView(R.id.fabLayout3) LinearLayout fabLayout3;
    @BindView(R.id.fab) FloatingActionButton mFab;
    @BindView(R.id.fab_add_favorite) FloatingActionButton fab_add_favorite;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();

        //Log.d("JOPO", MainActivity.mProfile.getUid());

        mActivity = this;
        mContext = this;

        imageLoader = new ImageLoader(mContext);

        if(MainActivity.mProfile.getUid() != null){
            mFavorites = mRootRef.child("favorites").child(MainActivity.mProfile.getUid());
        }

        String articleID = getIntent().getStringExtra("BARCODE_ID");
        Log.d("BARCODE_ID", articleID);
        mArticleDetailsDAO.getArticle(articleID);

        isFavoriteIn = false;

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }*/


        if(!TextUtils.isEmpty(mArticleDetailsDAO.getArticle().getNoAuthentification()) && mArticleDetailsDAO.getArticle().getNoAuthentification().compareTo("API Key invalid") == 0) {
            Log.d("boolLoadContent", Boolean.toString(false));
            boolLoadContent = false;
            setContentView(R.layout.activity_article_no_authentication);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }else if (!TextUtils.isEmpty(mArticleDetailsDAO.getArticle().getNoEntry()) && mArticleDetailsDAO.getArticle().getNoEntry().compareTo("No Article") == 0){
            Log.d("boolLoadContent", Boolean.toString(false));
            boolLoadContent = false;
            setContentView(R.layout.activity_article_no_entry);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        }else{
            setContentView(R.layout.activity_article_detail);
            ButterKnife.bind(this);
            ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_IMAGE);
            supportPostponeEnterTransition();

            // Toolbar
            toolbar.setTitle(R.string.article_title);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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




        //String itemTitle = getIntent().getStringExtra(EXTRA_TITLE);


        if (boolLoadContent == true){
            collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
            //collapsingToolbarLayout.setTitle(itemTitle); <-- auskommentiert
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        }

        //------------------------
        if (boolLoadContent == true){

            CustomListArticleDetails articleDetails = new CustomListArticleDetails(this, mArticleDetailsDAO.getArticleArrayList());
            articleDetails.setContent(articleDetails.ARTICLE_POSITION);

            ListView listViewIngredients = this.findViewById(R.id.listViewIngredients);
            setListViewHeightBasedOnChildren(listViewIngredients, "Ingredients");

            ListView listViewZertificate = this.findViewById(R.id.listViewZertificate);
            setListViewHeightBasedOnChildren(listViewZertificate, "Certificate");

            ListView listViewMaking = this.findViewById(R.id.listViewMaking);
            setListViewHeightBasedOnChildren(listViewMaking, "Making");

            ListView listViewStore = this.findViewById(R.id.listViewStore);
            setListViewHeightBasedOnChildren(listViewStore, "Store");

            ListView listViewRecycling = this.findViewById(R.id.listViewRecycling);
            setListViewHeightBasedOnChildren(listViewRecycling, "Recycling");
        }



        //------------------------

        if (boolLoadContent == true) {

            View test = this.findViewById(R.id.progressBarArticleDetailMain);

            /*imageView = (SquareImageView) findViewById(R.id.imageViewArticleDetail);
            imageLoader.DisplayImage(mArticleDetailsDAO.getArticle().getArticleImage(imageNumber), imageView);
            test.setVisibility(View.GONE);*/

            final ViewPager viewpager = findViewById(R.id.imageViewArticleDetail);

            Fragment fragment = new Fragment();
            //fragment.getChildFragmentManager()


            //getSupportFragmentManager()

            final PagerAdapter pAdapter = new PagerAdapter(getSupportFragmentManager(), mArticleDetailsDAO.getArticle().getArticleImages());
            viewpager.setAdapter(pAdapter);


            test.setVisibility(View.GONE);


            View viewButtonLeft = findViewById(R.id.imageViewArticleDetailLeft);
            viewButtonLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("imageNumber", Integer.toString(imageNumber));
                    if(imageNumber >= 1) {
                        imageNumber = imageNumber - 1;
                        viewpager.setCurrentItem(imageNumber);
                    }

                }
            });

            View viewButtonRight = findViewById(R.id.imageViewArticleDetailRight);
            viewButtonRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("imageNumber", Integer.toString(imageNumber));
                    Log.d("imageNumber", Integer.toString(mArticleDetailsDAO.getArticle().getArticleImages().size()-1));
                    if(imageNumber < mArticleDetailsDAO.getArticle().getArticleImages().size()-1) {
                        imageNumber = imageNumber + 1;
                        viewpager.setCurrentItem(imageNumber);
                    }
                }
            });

        }

        if(MainActivity.mProfile.getUid() != null){
            setFabListeners();
        }else{
            FloatingActionButton mFab = findViewById(R.id.fab);
            mFab.setVisibility(View.GONE);
        }


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }*/
    }

    private void setFabListeners(){

        fabBGLayoutDetail.setBackgroundColor(Color.parseColor("#55D0D0D0"));

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



        fabBGLayoutDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });


        mFavorites.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("dataSnapshot", dataSnapshot.toString());

                for(DataSnapshot d : dataSnapshot.getChildren()){
                    if(d.hasChild("articleId")){
                        Log.d("hasChild", "Yes");
                        Log.d("articleId", d.child("articleId").getValue().toString());
                        Log.d("articleId", String.valueOf(mArticleDetailsDAO.getArticle().getId()));
                        if(Integer.parseInt(d.child("articleId").getValue().toString()) == mArticleDetailsDAO.getArticle().getId()){
                            // Artikel ist drin
                            isFavoriteIn = true;
                            favoriteIdKey = d.getKey();
                            fab_add_favorite.setImageResource(R.drawable.ic_favortie_white);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DatabaseError", databaseError.toString());
            }
        });


        fabLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getApplicationContext(), "Gedrückt", Toast.LENGTH_LONG).show();
                Log.d("Schröck", String.valueOf(mArticleDetailsDAO.getArticle().getId()));

                Log.d("DREK", "DRECK");
                Log.d("DREK", String.valueOf(isFavoriteIn));
                if(!isFavoriteIn){
                    Toast.makeText(getApplicationContext(), R.string.favorite_added, Toast.LENGTH_LONG).show();
                    String uniqueId = mFavorites.push().getKey();

                    // new Favorite
                    Favorites favorites = new Favorites();
                    favorites.setFavoriteKey(uniqueId);
                    favorites.setArticleId(mArticleDetailsDAO.getArticle().getId());
                    favorites.setArticleName(mArticleDetailsDAO.getArticle().getName());
                    favorites.setUserKey(MainActivity.mProfile.getUid());
                    favorites.setPicturePath(mArticleDetailsDAO.getArticle().getArticleImages().get(0));

                    ArticleStore articleStore = mArticleDetailsDAO.getArticle().getArticleStore();
                    favorites.setStore(articleStore);

                    mFavorites.child(uniqueId).setValue(favorites);

                    //mFavorites.child(uniqueId).child("articleId").setValue(mArticleDetailsDAO.getArticle().getId());
                    fab_add_favorite.setImageResource(R.drawable.ic_favortie_white);
                }else{
                    mFavorites.child(favoriteIdKey).removeValue();
                    fab_add_favorite.setImageResource(R.drawable.ic_favortie_empty_white);
                    Toast.makeText(getApplicationContext(), R.string.favorite_removed, Toast.LENGTH_LONG).show();
                }
            }
        });


        fabLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Gedrückt-List", Toast.LENGTH_LONG).show();
                createToShopping(1);
            }
        });

        fabLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Gedrückt-Cart", Toast.LENGTH_LONG).show();
                createToShopping(2);
            }
        });
    }

    public void createToShopping(final int movePosition){
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
            doCreateToShoppingList(movePosition);
        }
    }

    private void doCreateToShoppingList(final int movePosition){

        LayoutInflater inflater = getLayoutInflater();

        final ArticleItem data = new ArticleItem();


        //dataFavorite = adapter.getArticleItems().get(position);
        //data = xxx.getArticleItems().get(position);


        final View viewEdit = inflater.inflate(R.layout.dialog_stock_new_list, null);
        addItemsOnSpinner(viewEdit);

        final ImageView imageView = viewEdit.findViewById(R.id.imageViewArticle);
        this.imageLoader.DisplayImage(mArticleDetailsDAO.getArticle().getArticleImage(0), imageView);
        data.setShoppingListPicture(mArticleDetailsDAO.getArticle().getArticleImage(0));


        final EditText textArticleName = viewEdit.findViewById(R.id.txtShoppingListArticleName);
        textArticleName.setText(mArticleDetailsDAO.getArticle().getName());
        textArticleName.setSelection(textArticleName.getText().length());

        final EditText textArticleQuantity = viewEdit.findViewById(R.id.txtShoppingListArticleQuantity);
        //textArticleQuantity.setText(String.valueOf(data.getShoppingListQuantity()));

        final Spinner spinnerLocal = viewEdit.findViewById(R.id.txtShoppingListArticleUnit);
        //spinnerLocal.setSelection(data.getUnitId());

        final Spinner spinnerStorePlace = viewEdit.findViewById(R.id.txtShoppingListArticleNewList);
        //spinnerStorePlace.setSelection(dataPlaceAdapter.getItemPosition(data.getShoppingListsKey()));

        //Sets the ArticleId for the Article
        data.setArticleId(mArticleDetailsDAO.getArticle().getId());

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

        final ArticleStore articleStore = mArticleDetailsDAO.getArticle().getArticleStore();

        /*********************************
         *  ARTICLE-STORE anpassen
         *
         */

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

    private void showFABMenu(){
        isFABOpen = true;
        fabLayout1.setVisibility(View.VISIBLE);
        fabLayout2.setVisibility(View.VISIBLE);
        fabLayout3.setVisibility(View.VISIBLE);
        fabBGLayoutDetail.setVisibility(View.VISIBLE);

        mFab.animate().rotationBy(180);
        fabLayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        fabLayout3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fabBGLayoutDetail.setVisibility(View.GONE);
        mFab.animate().rotationBy(-180);
        fabLayout1.animate().translationY(0);
        fabLayout2.animate().translationY(0);
        fabLayout3.animate().translationY(0);
        mFab.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!isFABOpen){
                    fabLayout1.setVisibility(View.GONE);
                    fabLayout2.setVisibility(View.GONE);
                    fabLayout3.setVisibility(View.GONE);
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


    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView, String source) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        //int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        Log.d("getWidth", Integer.toString(listView.getWidth()));
        Log.d("desiredWidth", Integer.toString(desiredWidth));
        int totalHeight = 200;
        //int totalHeight = 0;
        View view = null;

        Log.d("listAdapter-Items", Integer.toString(listAdapter.getCount()));

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);



            totalHeight += view.getMeasuredHeight();
        }

        //Log.d("getMeasuredHeight: " + source, Integer.toString(view.getMeasuredHeight()));

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
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

    @Override
    public void onUnitSelect(int i) {
        Log.d(TAG, "onTest");
        Log.d(TAG, String.valueOf(i));

        mUnitSelected = i;
    }
}
