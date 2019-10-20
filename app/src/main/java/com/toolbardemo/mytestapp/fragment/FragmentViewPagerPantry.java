package com.toolbardemo.mytestapp.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.adapter.CustomRecyclerShoppingList;
import com.toolbardemo.mytestapp.adapter.CustomRecyclerStock;
import com.toolbardemo.mytestapp.adapter.SerializableAdapter;
import com.toolbardemo.mytestapp.adapter.SerializableStockAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by V-Windows on 10.03.2018.
 *
 */

public class FragmentViewPagerPantry extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    View view;
    private Paint p = new Paint();
    private CustomRecyclerStock adapterFridge;

    private final String TAG = "FragmentViewPagerList";

    public FragmentViewPagerPantry newInstance(CustomRecyclerStock adapter){
        Log.d(TAG, "newInstance");
        FragmentViewPagerPantry fragment = new FragmentViewPagerPantry();

        Bundle args = new Bundle();
        SerializableStockAdapter sAdapter = new SerializableStockAdapter(adapter);
        args.putSerializable(ARG_SECTION_NUMBER, sAdapter);

        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.recyclerViewFridge) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        try {
            view = inflater.inflate(R.layout.content_stock, container, false);
        } catch (InflateException e) {

        }

        ButterKnife.bind(this, view);

        //RecyclerView recyclerView = view.findViewById(R.id.recyclerViewShoppingList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setBackgroundColor(getResources().getColor(R.color.background_list));

        Log.d("onCreateView", ARG_SECTION_NUMBER);


        SerializableStockAdapter args = (SerializableStockAdapter) getArguments().getSerializable(ARG_SECTION_NUMBER);

        this.adapterFridge = args.getAdapter();
        //initSwipe();


        recyclerView.setAdapter(args.getAdapter());

        return view;
    }

    /*private void initSwipe(){
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
                    adapterShoppingList.setTransferArticle(mItem, 1);
                    final View viewFragment = getActivity().findViewById(R.id.coordinatorLayout);

                    Snackbar snackbar = Snackbar
                            .make(viewFragment, R.string.shoppingListSnackbarMoved, Snackbar.LENGTH_LONG)
                            .setAction(R.string.shoppingListSnackbarUndo, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Snackbar snackbarRestore = Snackbar.make(view, R.string.shoppingListSnackbarRestore, Snackbar.LENGTH_SHORT);
                                    snackbarRestore.show();
                                    //adapterShoppingList.addItem(mItem);
                                    adapterShoppingList.setTransferArticle(mItem, 2);
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
                        p.setColor(Color.parseColor("#388E3C"));
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
    }*/

    public RecyclerView getRecyclerView(){
        return recyclerView;
    }

}
