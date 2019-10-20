package com.toolbardemo.mytestapp;

import android.content.res.ColorStateList;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.VectorDrawable;

import android.os.Build;
import android.os.Bundle;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.transition.Slide;

import android.view.MotionEvent;

import android.widget.ProgressBar;

import com.toolbardemo.mytestapp.DAO.NewsDetailsDAO;
import com.toolbardemo.mytestapp.adapter.CustomRecyclerNewsDetails;
import com.toolbardemo.mytestapp.image.ImageLoader;
import com.toolbardemo.mytestapp.widgets.SquareImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetailActivity extends AppCompatActivity {

    public static final String  NEWS_ID = "com.toolbardemo.mytestapp.NEWSID";
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private NewsDetailsDAO mNewsDetailsDAO = new NewsDetailsDAO();
    private ImageLoader imageLoader = new ImageLoader(this);


    // Cardview
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;

    @BindView (R.id.toolbarNews) Toolbar toolbar;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActivityTransitions();
        setContentView(R.layout.activity_detail_news);

        ButterKnife.bind(this);


        // Toolbar
        toolbar.setTitle("News");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        VectorDrawable navIcon = (VectorDrawable) toolbar.getNavigationIcon();
        navIcon.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);


        // CardView-Layout
        recyclerView = findViewById(R.id.recyclerViewShoppingLists);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        // CardView-Content
        String itemID = getIntent().getStringExtra(NEWS_ID);
        adapter = new CustomRecyclerNewsDetails(mNewsDetailsDAO.getNews(Integer.parseInt(itemID)));
        recyclerView.setAdapter(adapter);



        // ToolBar-Collapse
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        //collapsingToolbarLayout.setTitle(itemTitle); <-- auskommentiert
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        ProgressBar progressBar = findViewById(R.id.progressBarNewsDetail);

        SquareImageView imageView = findViewById(R.id.imageViewNews);
        //imageView.setImageResource(R.drawable.drawer_background);
        this.imageLoader.DisplayImage(mNewsDetailsDAO.getNews().getImage(), imageView);

        progressBar.setVisibility(ProgressBar.GONE);

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
    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        int primary = getResources().getColor(R.color.colorPrimary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        updateBackground((FloatingActionButton) findViewById(R.id.fabDetailNews), palette);
        supportStartPostponedEnterTransition();
    }

    private void updateBackground(FloatingActionButton fab, Palette palette) {
        int lightVibrantColor = palette.getLightVibrantColor(getResources().getColor(android.R.color.white));
        int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.colorPrimary));

        fab.setRippleColor(lightVibrantColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(vibrantColor));
    }
}
