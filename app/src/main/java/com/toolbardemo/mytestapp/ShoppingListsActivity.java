package com.toolbardemo.mytestapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.os.Vibrator;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.toolbardemo.mytestapp.DAO.ShoppingListsDAO;
import com.toolbardemo.mytestapp.adapter.CustomRecyclerShoppingLists;
import com.toolbardemo.mytestapp.data.ShoppingLists;
import com.toolbardemo.mytestapp.image.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.toolbardemo.mytestapp.MainActivity.context;

public class ShoppingListsActivity extends AppCompatActivity implements CustomRecyclerShoppingLists.AdapterCallback {

    private final String TAG = "ShoppingListsActivity";
    public static Context mContext;

    private ShoppingListsDAO mShoppingListsDAO = new ShoppingListsDAO();
    private ImageLoader imageLoader = new ImageLoader(this);

    private static int mEditMode = 0;

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }

    private FirebaseDatabase database = getDatabase();
    private DatabaseReference mRootRef = database.getReference();

    private DatabaseReference mConditionRef = mRootRef.child("shopping_lists");
    private DatabaseReference mConditionChildRef = mRootRef.child("shopping_lists").child(MainActivity.mProfile.getUid());


    public static MenuInflater inflater;
    private Menu menu;

    // CardView
    private CustomRecyclerShoppingLists adapter;
    private RecyclerView.LayoutManager layoutManager;

    @BindView(R.id.toolbarShoppingLists) Toolbar toolbar;
    @BindView(R.id.recyclerViewShoppingLists) RecyclerView recyclerView;
    @BindView(R.id.fab_shopping_lists) FloatingActionButton mFab;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_lists);
        ButterKnife.bind(this);

        mContext = this;
        inflater = getMenuInflater();
        mConditionChildRef.keepSynced(true);

        // Toolbar
        toolbar.setTitle(R.string.shopping_lists_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //VectorDrawable navIcon = (VectorDrawable) toolbar.getNavigationIcon();
        //navIcon.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);


        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        // Set Adapter
        adapter = new CustomRecyclerShoppingLists(this, this,mShoppingListsDAO.getShoppingLists());
        recyclerView.setAdapter(adapter);

        final LayoutInflater inflater = getLayoutInflater();

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            final View viewEdit = inflater.inflate(R.layout.dialog_shopping_lists, null);
            final EditText text = viewEdit.findViewById(R.id.txtShoppingListsName);

            Log.d("MERDA", text.getText().toString());

                    // Click action
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                            .setTitle(R.string.shopping_list_new)
                            .setView(viewEdit)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(TAG, "Ja");

                                    if(!text.getText().toString().equals("")) {
                                        Log.d("Input", text.getText().toString());
                                        createListItem(text.getText().toString());
                                    }else{
                                        Toast.makeText(mContext, R.string.shopping_lists_no_name, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Log.d(TAG, "Nein");
                                }
                            }).setOnKeyListener(new DialogInterface.OnKeyListener() {
                                @Override
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                                        dialog.dismiss();
                                    return false;
                                }
                            });

                final AlertDialog d = builder.show();
                d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                text.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        Log.d("Idioten", editable.toString());
                        if(!editable.toString().equals("")){
                            d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                        }else{
                            d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        }

                    }
                });

            }
        });

        mConditionChildRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.d(TAG, "onChildAdded");
                Log.d("dataSnapshot", dataSnapshot.toString());

                Log.d("Ergebnis", dataSnapshot.child("shoppingListsBought").getValue().toString());

                if(Integer.valueOf(dataSnapshot.child("shoppingListsBought").getValue().toString()) == 2){
                    ShoppingLists item = dataSnapshot.getValue(ShoppingLists.class);
                    item.setShoppingListsKey(dataSnapshot.getKey());

                    adapter.setShoppingLists(item);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.d(TAG, "onChildChanged");
                Log.d("dataSnapshot", dataSnapshot.toString());

                if(dataSnapshot.child("shoppingListsBought").getValue().toString().equals("1")){
                    adapter.deleteShoppingListsItem(dataSnapshot.getKey());
                    adapter.notifyDataSetChanged();
                }else{
                    ShoppingLists item = dataSnapshot.getValue(ShoppingLists.class);
                    item.setShoppingListsKey(dataSnapshot.getKey());

                    adapter.changeShoppingListsItem(dataSnapshot.getKey(), item);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved");
                Log.d("onChildRemoved", dataSnapshot.toString());
                adapter.deleteShoppingListsItem(dataSnapshot.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.d(TAG, "onChildMoved");
                Log.d("dataSnapshot", dataSnapshot.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void editShoppingListsEntry(int position){

        LayoutInflater inflater = getLayoutInflater();
        final View viewEdit = inflater.inflate(R.layout.dialog_shopping_lists, null);

        String name = adapter.getItem(position).getShoppingListsName();

        final EditText text = viewEdit.findViewById(R.id.txtShoppingListsName);
        text.setText(name);
        text.setSelection(text.getText().length());

        final String key = adapter.getItem(position).getShoppingListsKey();

        // Click action
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle(R.string.shopping_list_edit)
                .setView(viewEdit)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "Ja");

                        Log.d("Input", text.getText().toString());
                        editListItem(key, text.getText().toString());
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
                });

        final AlertDialog d = builder.show();

        if(!text.getText().toString().equals("")){
            d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        }else{
            d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        }

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("Idioten", editable.toString());
                if(!editable.toString().equals("")){
                    d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }else{
                    d.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }

            }
        });


    }

    public void openShoppingList(int position){

        Log.d("PauloDeninho", String.valueOf(adapter.getItem(position).getShoppingListsKey()));
        Log.d("PauloDeninho", String.valueOf(position));

        Intent intent = new Intent(MainActivity.context, ShoppingListActivity.class);
        String message = adapter.getItem(position).getShoppingListsKey();
        intent.putExtra("SHOPPING_LIST_ID", message);
        MainActivity.context.startActivity(intent);

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.d(TAG, "onOptionsItemSelected");
        if (id == R.id.selectItems) {
            Log.d(TAG, "selectItems");
            for (int childCount = adapter.getItemCount(), i = 0; i < childCount; ++i) {

                if(recyclerView.getChildAt(i) != null) {
                    final RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                    View cardItem = holder.itemView.findViewById(R.id.cardViewShoppingListsItem);
                    cardItem.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    adapter.setAllSelectedItems();
                }

            }
        }else if(id == R.id.trashCan){
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
            getMenuInflater().inflate(R.menu.toolbar_submenu_delete, this.menu);

            Animation anim = AnimationUtils.loadAnimation(
                    mFab.getContext(), R.anim.design_fab_out);
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(200);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    //mIsHiding = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //mIsHiding = false;
                    mFab.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mFab.startAnimation(anim);
        }else{
            this.menu.clear();

            Animation anim = AnimationUtils.loadAnimation(
                    mFab.getContext(), R.anim.design_fab_in);
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(200);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mFab.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mFab.startAnimation(anim);
        }

    }

    @Override
    public int getEditMode(){
        return mEditMode;
    }

    @Override
    public void setEditMode(int mode){
        this.mEditMode = mode;
    }

    public void deleteItems(SparseBooleanArray selectedItems){

        Log.d("ITEEEEMMMSSS", String.valueOf(selectedItems));

        for (int i = 0; i < adapter.getItemCount(); i++) {

            String key = adapter.getShoppingLists().get(i).getShoppingListsKey();
            Log.d("Reichsbürger", key);

            if(selectedItems.get(i)) {
                mConditionChildRef.child(String.valueOf(key)).removeValue();
            }
        }

        /*for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            final RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            TextView txtItemKey = holder.itemView.findViewById(R.id.txtViewTextShoppingListsKey);
            if(selectedItems.get(i) == true) {
                Log.d(TAG, String.valueOf(txtItemKey.getText()));
                Log.d("selectedItems", "deleteItems");
                mConditionChildRef.child(String.valueOf(txtItemKey.getText())).removeValue();
            }
        }*/

    }


    public void createListItem(String name){
        Log.d(TAG, "createListItem");

        String uniqueId = mConditionRef.push().getKey();

        ShoppingLists shoppingListItem = new ShoppingLists();
        shoppingListItem.setShoppingListsName(name);
        shoppingListItem.setUserId(MainActivity.mProfile.getUid());
        shoppingListItem.setShoppingListsBought(2);
        shoppingListItem.setShoppingListsKey(uniqueId);

        mConditionRef.child(MainActivity.mProfile.getUid()).child(uniqueId).setValue(shoppingListItem);

    }

    public void editListItem(String key, String name){
        Log.d(TAG, "editListItem");

        //String uniqueId = mConditionRef.push().getKey();

        ShoppingLists shoppingListItem = new ShoppingLists();
        shoppingListItem.setShoppingListsName(name);
        shoppingListItem.setUserId(MainActivity.mProfile.getUid());
        shoppingListItem.setShoppingListsBought(2);
        shoppingListItem.setShoppingListsKey(key);

        mConditionRef.child(MainActivity.mProfile.getUid()).child(key).setValue(shoppingListItem);

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

    public void setEditModeBackColor(){
        for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; ++i) {
            final RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            View test = holder.itemView.findViewById(R.id.cardViewShoppingListsItem);
            test.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }
    }
}
