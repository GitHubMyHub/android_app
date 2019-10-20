package com.toolbardemo.mytestapp.widgets;

import android.support.design.widget.NavigationView;
import com.toolbardemo.mytestapp.MainActivity;

/**
 * Created by V-Windows on 08.06.2017.
 *
 */

public class NewLeftNav {

    private NavigationView navigationView = null;
    private LeftNavDrawer leftNav;
    private MainActivity mActivity;

    public NewLeftNav(MainActivity activity){
        this.mActivity = activity;
    }


    public NavigationView getNewLeftNav(boolean isLoggedIn){

        leftNav = new LeftNavDrawer(mActivity);
        navigationView = leftNav.setLeftNavDrawer(isLoggedIn);

        return navigationView;
    }

    public NavigationView getNavigationView(){
        return navigationView;
    }

    public LeftNavDrawer getLeftNav(){
        return leftNav;
    }

}
