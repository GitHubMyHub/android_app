package com.toolbardemo.mytestapp.fragment;


import android.content.Intent;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.toolbardemo.mytestapp.NewsDetailActivity;
import com.toolbardemo.mytestapp.adapter.CustomListNews;
import com.toolbardemo.mytestapp.DAO.NewsDAO;
import com.toolbardemo.mytestapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    private static final String NEWS_ID = "com.toolbardemo.mytestapp.NEWSID";


    private NewsDAO mNewsDAO = new NewsDAO();
    private int iPage = 1;
    private int nextPage = 0;


    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @BindView(R.id.bottomBar2) FrameLayout bottomBar2;
    @BindView(R.id.bottomNavigationView2) BottomNavigationView bottomNavigationView2;
    @BindView(R.id.listNewsView) ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Eventlistener Bottom Button
        bottomNavigationView2.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(iPage < nextPage) {

                    iPage = iPage + 1;

                    Toast.makeText(getActivity(), getResources().getString(R.string.news_page) + " " + Integer.toString(iPage), Toast.LENGTH_SHORT).show();

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(NewsFragment.this).attach(NewsFragment.this).commit();

                }else{
                    bottomNavigationView2.setEnabled(false);
                }
                return false;
            }
        });

        // Bottom-Bar visibility
        listView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (listView != null) {
                    int children = listView.getChildCount();

                    Log.d("children", Integer.toString(children));

                    if (listView.getChildAt(children-1).getBottom() <= (listView.getHeight() + listView.getScrollY())) {
                        //scroll view is at bottom
                        Log.d("onScrollChanged", "bottom");

                        listView.setPadding(0,0,0,50);
                        bottomBar2.setVisibility(bottomBar2.VISIBLE);
                    } else {
                        //scroll view is not at bottom
                        Log.d("onScrollChanged", "not at bottom");
                        listView.setPadding(0,0,0,0);
                        bottomBar2.setVisibility(bottomBar2.GONE);
                    }
                }
            }
        });

        final CustomListNews adapter = new CustomListNews(getActivity(), mNewsDAO.getNews(iPage));
        nextPage = mNewsDAO.nextPage;
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tv = view.findViewById(R.id.txtViewIdNews);
                String str= tv.getText().toString();

                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra(NEWS_ID, str);
                startActivity(intent);

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("NewsFragment","onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

}