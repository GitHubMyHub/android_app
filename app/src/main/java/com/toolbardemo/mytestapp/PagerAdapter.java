package com.toolbardemo.mytestapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.toolbardemo.mytestapp.fragment.FragmentOne;
import com.toolbardemo.mytestapp.fragment.FragmentTwo;

import java.util.ArrayList;

// android.support.v4.app.FragmentStatePagerAdapter
// FragmentPagerAdapter

public class PagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

    private ArrayList<String> articleImages;

    public PagerAdapter(FragmentManager fm, ArrayList<String> articleImages) {
        super(fm);
        this.articleImages = articleImages;
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub

        //imageLoader.DisplayImage(mArticleDetailsDAO.getArticle().getArticleImage(imageNumber), imageView);

        Log.d("getItem", Integer.toString(arg0));
        Log.d("articleImagesURL", articleImages.get(arg0));


        if(articleImages.get(arg0).contains(".mp4")){
            Log.d("Fragment", "One");
            return FragmentOne.newInstance(articleImages.get(arg0));
        }else{
            Log.d("Fragment", "Two");
            return FragmentTwo.newInstance(articleImages.get(arg0));
        }


        /*switch (arg0) {
            case 0:
                return new FragmentOne();
            case 1:
                return new FragmentTwo();

            default:
                break;
        }*/
        //return null;
    }

    /*@Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Log.d("Load", "Started Science");
                return FragmentOne.newInstance(articleImages.get(position));

            case 1:
                Log.d("Load", "Back to Simple");
                return FragmentTwo.newInstance(articleImages.get(position));


            default:
                return FragmentTwo.newInstance(articleImages.get(position));

        }
    }*/

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        Log.d("articleImages", Integer.toString(articleImages.size()));
        return articleImages.size();
    }

}