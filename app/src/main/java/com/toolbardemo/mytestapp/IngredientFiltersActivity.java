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
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.Spinner;

import com.toolbardemo.mytestapp.DAO.IngredientFiltersDAO;
import com.toolbardemo.mytestapp.DAO.StatusDAO;
import com.toolbardemo.mytestapp.adapter.CustomListIngredientFilters;
import com.toolbardemo.mytestapp.adapter.CustomStatus;
import com.toolbardemo.mytestapp.data.IngredientFilters;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientFiltersActivity extends AppCompatActivity implements CustomListIngredientFilters.AdapterCallback{

    private static final String TAG = "IngredientFilters";
    private IngredientFiltersDAO ingredientFiltersDAO = new IngredientFiltersDAO();
    private CustomListIngredientFilters adapterIngredientFilters;
    private RecyclerView.LayoutManager layoutManager;

    public static Context mContext;

    private Menu menu;
    private static int mEditMode = 0;

    private StatusDAO mStatusDAO = new StatusDAO();
    private CustomStatus dataUnitAdapter;
    private Spinner spinner;

    @BindView(R.id.toolbarIngredientFilters) Toolbar toolbar;
    @BindView(R.id.recyclerViewIngredientFilters) RecyclerView recyclerView;
    @BindView(R.id.fab) FloatingActionButton mFabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_filters);
        ButterKnife.bind(this);

        mContext = this;

        // Toolbar
        toolbar.setTitle(R.string.ingredient_filters);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ingredientFiltersDAO.getIngredientFilters(MainActivity.mProfile.getUid());

        //Log.d("Alice", ingredientFiltersDAO.toString());

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapterIngredientFilters = new CustomListIngredientFilters(this, this, ingredientFiltersDAO.getIngredientFilters());
        recyclerView.setAdapter(adapterIngredientFilters);

        setListeners();
    }



    private void setListeners() {
        Log.d(TAG, "setListeners");

        final LayoutInflater inflater = getLayoutInflater();

        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View viewEdit = inflater.inflate(R.layout.dialog_ingredient_lists, null);
                addItemsOnSpinner(viewEdit);

                final EditText text = viewEdit.findViewById(R.id.txtIngredientFiltersName);

                final Spinner spinner = viewEdit.findViewById(R.id.spinnerStatus);

                final IngredientFilters ingredientFilters = new IngredientFilters();

                // Click action
                new AlertDialog.Builder(mContext)
                        .setTitle(R.string.shopping_list_edit)
                        .setView(viewEdit)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "Ja");
                                Log.d("Input", text.getText().toString());

                                ingredientFilters.setFiltersName(text.getText().toString());
                                ingredientFilters.setStatusId(spinner.getSelectedItemPosition()+1);
                                ingredientFilters.setUserId(MainActivity.mProfile.getUid());

                                createListItem(ingredientFilters);

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
        });

    }

    /**
     * addItemsOnSpinner
     * add items into spinner dynamically
     * @param view
     */
    public void addItemsOnSpinner(View view) {

        spinner = view.findViewById(R.id.spinnerStatus);

        dataUnitAdapter = new CustomStatus(this, mStatusDAO.getStati());
        dataUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setOnItemSelectedListener(dataUnitAdapter);
        spinner.setAdapter(dataUnitAdapter);
    }

    @Override
    public void changeEditMode() {
        Log.d(TAG, "changeEditMode");

        if(mEditMode == 1){
            Vibrator vibe = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(100);
            getMenuInflater().inflate(R.menu.toolbar_submenu_delete, this.menu);

            Animation anim = AnimationUtils.loadAnimation(
                    mFabAdd.getContext(), R.anim.design_fab_out);
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
                    mFabAdd.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mFabAdd.startAnimation(anim);
        }else{
            this.menu.clear();

            Animation anim = AnimationUtils.loadAnimation(
                    mFabAdd.getContext(), R.anim.design_fab_in);
            anim.setInterpolator(new LinearInterpolator());
            anim.setDuration(200);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mFabAdd.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mFabAdd.startAnimation(anim);
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

    @Override
    public void editIngredientsEntry(final int position) {

        LayoutInflater inflater = getLayoutInflater();
        final View viewEdit = inflater.inflate(R.layout.dialog_ingredient_lists, null);
        addItemsOnSpinner(viewEdit);

        final EditText text = viewEdit.findViewById(R.id.txtIngredientFiltersName);
        text.setText(adapterIngredientFilters.getItem(position).getFiltersName());
        text.setSelection(text.getText().length());

        final Spinner spinner = viewEdit.findViewById(R.id.spinnerStatus);
        spinner.setSelection(adapterIngredientFilters.getItem(position).getStatusId()-1);



        final IngredientFilters ingredientFilters = adapterIngredientFilters.getItem(position);

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

                        ingredientFilters.setFiltersName(text.getText().toString());
                        ingredientFilters.setStatusId(spinner.getSelectedItemPosition()+1);

                        editListItem(position, ingredientFilters);

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                                Log.d(TAG, "Nein");
                    }
                }).setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                            //finish();
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

    public void createListItem(IngredientFilters ingredientFilters){

        if(ingredientFiltersDAO.createIngredientFilters(ingredientFilters)){
            //adapterIngredientFilters.setIngredientLists(ingredientFiltersDAO.getIngredientFiltersItem(ingredientFiltersDAO.getIngredientFiltersCount()-1));
            adapterIngredientFilters.notifyDataSetChanged();
        }

    }

    public void editListItem(int position, IngredientFilters ingredientFilters){

        if(ingredientFiltersDAO.updateIngredientFilters(position)){
            adapterIngredientFilters.changeIngredientFiltersItem(ingredientFilters.getFiltersId(), ingredientFilters);
            adapterIngredientFilters.notifyDataSetChanged();
        }

    }

    public void openIngredientList(int position){


        Log.d("PauloDeninho", String.valueOf(adapterIngredientFilters.getItem(position).getFiltersId()));
        Log.d("PauloDeninho", String.valueOf(position));

        Intent intent = new Intent(MainActivity.context, IngredientFilterActivity.class);
        String message = String.valueOf(adapterIngredientFilters.getItem(position).getFiltersId());
        intent.putExtra("INGREDIENT_FILTER_ID", message);
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
            for (int childCount = adapterIngredientFilters.getItemCount(), i = 0; i < childCount; ++i) {

                if(recyclerView.getChildAt(i) != null) {
                    final RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                    View cardItem = holder.itemView.findViewById(R.id.cardViewIngredientFiltersItem);
                    cardItem.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    adapterIngredientFilters.setAllSelectedItems();
                }

                /*final RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                View cardItem = holder.itemView.findViewById(R.id.cardViewIngredientFiltersItem);
                cardItem.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                cardItem.callOnClick();*/
            }
        }else if(id == R.id.trashCan){
            Log.d(TAG, "trashCan");
            Log.d("trashCan", adapterIngredientFilters.getSelectedItems().toString());
            deleteItems(adapterIngredientFilters.getSelectedItems());
            adapterIngredientFilters.deleteAllSelectedItems();
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

    public void deleteItems(SparseBooleanArray selectedItems){

        Log.d(TAG, "deleteItems");
        Log.d("getChildCount", String.valueOf(recyclerView.getAdapter().getItemCount()));


        if(ingredientFiltersDAO.deleteIngredientFilters(selectedItems)){

            for (int i = recyclerView.getAdapter().getItemCount() - 1; i >= 0; i--) {
                Log.d("Doktor1", String.valueOf(i));
                //Log.d("Counter", String.valueOf(recyclerView.getAdapter().getItemCount()));

                if(selectedItems.get(i)) {
                    Log.d("Doktor2", String.valueOf(i));
                    adapterIngredientFilters.deleteIngredientFiltersItem(i);
                }
            }
        }
        adapterIngredientFilters.notifyDataSetChanged();
        adapterIngredientFilters.deleteSelectedItems();

    }

    @Override
    public void onBackPressed() {
        // your code.
        Log.d(TAG, "onBackPressed");
        if(mEditMode == 1){
            mEditMode = 0;
            adapterIngredientFilters.deleteAllSelectedItems();
            this.changeEditMode();
            this.setEditModeBackColor();
        }else{
            finish();
        }
    }

    public void setEditModeBackColor(){
        for (int childCount = recyclerView.getChildCount(), i = 0; i < childCount; ++i) {
            final RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            View test = holder.itemView.findViewById(R.id.cardViewIngredientFiltersItem);
            test.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }
    }
}
