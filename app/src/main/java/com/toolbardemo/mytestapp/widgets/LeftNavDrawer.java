package com.toolbardemo.mytestapp.widgets;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.toolbardemo.mytestapp.ArticleDetailActivity;
import com.toolbardemo.mytestapp.FavoritesActivity;
import com.toolbardemo.mytestapp.IngredientFiltersActivity;
import com.toolbardemo.mytestapp.MainActivity;
import com.toolbardemo.mytestapp.StockActivity;
import com.toolbardemo.mytestapp.ShoppingListsActivity;
import com.toolbardemo.mytestapp.TwitterLoginActivity;
import com.toolbardemo.mytestapp.fragment.CameraFragment;
import com.toolbardemo.mytestapp.fragment.FilterFragment;
import com.toolbardemo.mytestapp.fragment.ImpressumFragment;
import com.toolbardemo.mytestapp.fragment.NewsFragment;
import com.toolbardemo.mytestapp.fragment.ProfileFragment;
import com.toolbardemo.mytestapp.fragment.SettingsFragment;
import com.toolbardemo.mytestapp.LoginActivity;
import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.ToolbarCaptureActivity;
import com.toolbardemo.mytestapp.image.ImageLoader;
import com.twitter.sdk.android.core.TwitterCore;

import static com.toolbardemo.mytestapp.MainActivity.mAuth;
import static com.toolbardemo.mytestapp.MainActivity.mProfile;

/**
 * Created by V-Windows on 04.06.2017.
 *
 */

public class LeftNavDrawer {

    private AppCompatActivity mActivity; // Referenz auf die MainActivity
    NavigationView navigationView = null;
    Toolbar toolbar = null;


    public LeftNavDrawer(AppCompatActivity activity){
        this.mActivity = activity;
    }

    /**
     * Set the Navigation Drawer
     */
    public NavigationView setLeftNavDrawer(boolean isLoggedIn) {

        toolbar = mActivity.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        mActivity.setSupportActionBar(toolbar);

        Log.d("setLeftNavDrawer", Boolean.toString(isLoggedIn));

        DrawerLayout drawer = mActivity.findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this.mActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = mActivity.findViewById(R.id.nav_view);

        navigationView.getMenu().clear();

        // Wenn eingeloggt soll das Login Menü inflated werden
        if(isLoggedIn) {
            navigationView.inflateMenu(R.menu.left_navigation_content_login);
        }else{
            navigationView.inflateMenu(R.menu.left_navigation_content);
        }

        //How to change elements in the header programatically
        View headerView = navigationView.getHeaderView(0);
        ImageLoader imageLoader = new ImageLoader(this.mActivity);
        ImageView userPicture = headerView.findViewById(R.id.profile_image);
        TextView usernameText = headerView.findViewById(R.id.username);
        TextView emailText = headerView.findViewById(R.id.email);

        if(mAuth.getCurrentUser() != null){

            // Set Content

            if(mProfile != null){
                if(!mProfile.getPhotoUrl().equals("")) {
                    imageLoader.DisplayImage(mProfile.getPhotoUrl(), userPicture);
                }else{
                    userPicture.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.profile));
                }
                usernameText.setText(mProfile.getName());
                emailText.setText(mProfile.getEmail());

                // Visability
                userPicture.setVisibility(View.VISIBLE);
                usernameText.setVisibility(View.VISIBLE);
                emailText.setVisibility(View.VISIBLE);
            }

        }else{
            // Visability
            userPicture.setVisibility(View.INVISIBLE);
            usernameText.setVisibility(View.INVISIBLE);
            emailText.setVisibility(View.INVISIBLE);
        }

        return navigationView;
    }

    /**
     * Set the chosen Page
     */
    public void setPage(int id){

        if (id == R.id.nav_news && isOnline()) {
            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, NewsFragment.newInstance()).commit();

        } else if (id == R.id.nav_scan_article && isOnline()) {
            scanToolbar();
            //Intent intent = new Intent(this, ArticleDetailActivity.class);
            //String message = "kaka";
            //intent.putExtra(EXTRA_MESSAGE, message);
            //startActivity(intent);

        } else if (id == R.id.nav_settings) {

            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, SettingsFragment.newInstance()).commit();


        } else if (id == R.id.nav_scan_article_test && isOnline()) {

            Intent intent = new Intent(mActivity, ArticleDetailActivity.class);
            String message = "0000000000000";
            intent.putExtra("BARCODE_ID", message);
            mActivity.startActivity(intent);

            /*getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, ArticleFragment.newInstance("0000000000000")).commit();*/

        } else if (id == R.id.nav_scan_camera && isOnline()) {

            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, CameraFragment.newInstance("a", "b")).commit();

        } else if (id == R.id.nav_login && isOnline()) {

            Intent intent = new Intent(mActivity, LoginActivity.class);
            mActivity.startActivity(intent);

        } else if (id == R.id.nav_profile) {

            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, ProfileFragment.newInstance()).commit();

        } else if (id == R.id.nav_shopping_list) {

            Intent intent = new Intent(mActivity, ShoppingListsActivity.class);
            mActivity.startActivity(intent);

        } else if (id == R.id.nav_pantry_list) {

            Intent intent = new Intent(mActivity, StockActivity.class);
            mActivity.startActivity(intent);

        } else if (id == R.id.nav_ingredients_list) {

            Intent intent = new Intent(mActivity, IngredientFiltersActivity.class);
            mActivity.startActivity(intent);

        } else if (id == R.id.nav_filter_list) {

            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, FilterFragment.newInstance()).commit();

        } else if (id == R.id.nav_favorites && isOnline()) {

            Intent intent = new Intent(mActivity, FavoritesActivity.class);
            mActivity.startActivity(intent);

        } else if (id == R.id.nav_logout) {

            MainActivity.mProfile.savePrefs(MainActivity.mProfile.PROFILE_SESSION, "");

            try {
                MainActivity.mProfile.finalize();
            } catch(Throwable e) {
                Log.d("Destroyed", e.getMessage());
            }

            // Firebase sign out
            MainActivity.mAuth.signOut();

            // Google sign out
            if(MainActivity.mGoogleSignInClient != null){
                MainActivity.mGoogleSignInClient.signOut();
            }

            // Google sign out

            try{
                mAuth.signOut();
                TwitterCore.getInstance().getSessionManager().clearActiveSession();
            } catch (Throwable e){
                Log.d("Destroyed", e.getMessage());
            }

            try{
                MainActivity.mProfile.finalize();
            } catch(Throwable e){
                Log.d("Profile", "Destroyed");
            }

            setLeftNavDrawer(false);

        } else if (id == R.id.nav_share) {

            //Intent intent = new Intent(mActivity, MainTabActivity.class);
            //mActivity.startActivity(intent);


        } else if (id == R.id.nav_impressum) {

            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, ImpressumFragment.newInstance("a", "b")).commit();

        }

        DrawerLayout drawer = mActivity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



    /**
     * Start the Scan produkt function
     */
    public void scanToolbar() {
        new IntentIntegrator(mActivity).setCaptureActivity(ToolbarCaptureActivity.class).initiateScan();
    }
}
