package com.toolbardemo.mytestapp.fragment;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.image.ImageLoader;

public class FragmentOne extends Fragment {

    private ImageLoader imageLoader = new ImageLoader(getContext());
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public FragmentOne() {
        // Required empty public constructor
    }

    public static FragmentOne newInstance(String imageUrl){
        FragmentOne fragment = new FragmentOne();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.fragment_one,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        VideoView videoView = getActivity().findViewById(R.id.videoViewArticle);

        //mageLoader.DisplayImage(mParam1, videoView);

        Uri uri=Uri.parse(mParam1);
        Log.d("ViedeoUri", uri.toString());

        videoView.setVideoURI(uri);


        //String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.no_name_panorama_3;
        //videoView.setVideoURI(Uri.parse(path));

        //if you want the controls to appear
        //imageView.setMediaController(new MediaController(getActivity()));

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.setLooping(true);

            }
        });

        videoView.start();
    }
}
