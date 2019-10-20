package com.toolbardemo.mytestapp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.toolbardemo.mytestapp.MainActivity;
import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.image.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.toolbardemo.mytestapp.MainActivity.mProfile;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    ImageLoader imageLoader = new ImageLoader(getContext());

    @BindView(R.id.profile_image) ImageView userPicture;
    @BindView(R.id.username) TextView username;
    @BindView(R.id.email) TextView email;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if(!mProfile.getPhotoUrl().equals("")) {
            imageLoader.DisplayImage(mProfile.getPhotoUrl(), userPicture);
        }
        username.setText(MainActivity.mProfile.getName());
        email.setText(MainActivity.mProfile.getEmail());

    }
}
