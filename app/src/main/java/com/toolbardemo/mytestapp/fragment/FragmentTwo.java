package com.toolbardemo.mytestapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.image.ImageLoader;
import com.toolbardemo.mytestapp.widgets.SquareImageView;

public class FragmentTwo extends Fragment {

    private ImageLoader imageLoader = new ImageLoader(getContext());
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public FragmentTwo() {
        // Required empty public constructor
    }

    public static FragmentTwo newInstance(String imageUrl){
        FragmentTwo fragment = new FragmentTwo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.fragment_two,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*ImageView imageView = (ImageView) view.findViewById(R.id.imageViewArticle);
        Log.d("mParam1", mParam1);
        this.imageLoader.DisplayImage(mParam1, imageView);*/


        SquareImageView imageView = view.findViewById(R.id.imageViewArticle);
        Log.d("mParam1", mParam1);
        this.imageLoader.DisplayImage(mParam1, imageView);

        //imageView.setImageResource(R.drawable.drawer_background);
    }
}