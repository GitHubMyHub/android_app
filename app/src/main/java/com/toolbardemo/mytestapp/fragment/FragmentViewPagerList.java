package com.toolbardemo.mytestapp.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;


import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.adapter.CustomRecyclerShoppingList;
import com.toolbardemo.mytestapp.adapter.SerializableAdapter;
import com.toolbardemo.mytestapp.data.ArticleItem;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by V-Windows on 10.03.2018.
 *
 */

public class FragmentViewPagerList extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    View view;
    private Paint p = new Paint();
    private CustomRecyclerShoppingList adapterShoppingList;

    private final String TAG = "FragmentViewPagerList";

    public FragmentViewPagerList newInstance(CustomRecyclerShoppingList adapter){
        Log.d(TAG, "newInstance");
        FragmentViewPagerList fragment = new FragmentViewPagerList();

        Bundle args = new Bundle();
        SerializableAdapter sAdapter = new SerializableAdapter(adapter);
        args.putSerializable(ARG_SECTION_NUMBER, sAdapter);

        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.recyclerViewShoppingList) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        view = inflater.inflate(R.layout.content_shopping_list, container, false);
        ButterKnife.bind(this, view);

        //RecyclerView recyclerView = view.findViewById(R.id.recyclerViewShoppingList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setBackgroundColor(getResources().getColor(R.color.background_list));

        Log.d("onCreateView", ARG_SECTION_NUMBER);


        SerializableAdapter args = (SerializableAdapter) getArguments().getSerializable(ARG_SECTION_NUMBER);
        //Log.d("adapter", args.getAdapter().getShoppingList().get(0).getShoppingListName());
        //Log.d("adapterItemCount", String.valueOf(args.getAdapter().getShoppingList().size()));

        this.adapterShoppingList = args.getAdapter();
        initSwipe();


        recyclerView.setAdapter(args.getAdapter());

        return recyclerView;
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d(TAG, "onSwiped");
                int position = viewHolder.getAdapterPosition();
                final ArticleItem mItem;

                if (direction == ItemTouchHelper.RIGHT) {
                    mItem = adapterShoppingList.removeItem(position);
                    adapterShoppingList.setTransferArticle(mItem, 2);
                    final View viewFragment = getActivity().findViewById(R.id.coordinatorLayout);

                    Snackbar snackbar = Snackbar
                            .make(viewFragment, R.string.shoppingListSnackbarMoved, Snackbar.LENGTH_LONG)
                            .setAction(R.string.shoppingListSnackbarUndo, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Snackbar snackbarRestore = Snackbar.make(view, R.string.shoppingListSnackbarRestore, Snackbar.LENGTH_SHORT);
                                    snackbarRestore.show();
                                    //adapterShoppingList.addItem(mItem);
                                    adapterShoppingList.setTransferArticle(mItem, 1);
                                    adapterShoppingList.addItem(mItem);
                                }
                            });

                    snackbar.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(getResources().getColor(R.color.shopping_list_carditem_2));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_add_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }

                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public CustomRecyclerShoppingList getAdapterShoppingList(){
        return adapterShoppingList;
    }

    public RecyclerView getRecyclerView(){
        return recyclerView;
    }

}
