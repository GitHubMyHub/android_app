package com.toolbardemo.mytestapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import com.toolbardemo.mytestapp.fragment.MessageFragment;
import com.toolbardemo.mytestapp.fragment.NewsFragment;
import com.toolbardemo.mytestapp.fragment.SettingsFragment;
import com.toolbardemo.mytestapp.profile.Profile;
import com.toolbardemo.mytestapp.widgets.NewLeftNav;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private MainActivity mActivity; // Referenz auf die MainActivity
    private static final int REQUEST_PERMISSIONS = 1; // gewünschte Permission Response

    // String Array mit den gewünschten Berechtigungen
    private static final String PERMISSIONS_REQUIRED[] = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.VIBRATE
    };

    public static Profile mProfile = new Profile();
    public static GoogleSignInClient mGoogleSignInClient;
    public static FirebaseAuth mAuth;
    public static FirebaseUser user;
    public static NewLeftNav mNav;
    private NavigationView navigationView = null;
    private boolean isLoggedIn = false;

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mActivity = this;
        context = this;

        setLogin();

        Log.d("getUid", ""+ mProfile.getUid());

        /*if(mProfile.getUid() != null){
            isLoggedIn = true;
        }*/

        // this listener will be called when there is change in firebase user session
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    mProfile.setUser(user);
                }
            }
        };

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            // User is logged in
            user = mAuth.getCurrentUser();
            mProfile.setUser(user);
            isLoggedIn = true;
        }

        // Navigation
        mNav = new NewLeftNav(this);
        navigationView = mNav.getNewLeftNav(isLoggedIn);
        navigationView.setNavigationItemSelectedListener(this);


        // Assume thisActivity is the current activity
        int permissionCheckRead = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        // Assume thisActivity is the current activity
        int permissionCheckWrite = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //Log.d("permissionCheck- Write", Integer.toString(permissionCheckWrite));
        //Log.d("permissionCheck- Read", Integer.toString(permissionCheckRead));


        checkPermissions();



        //Log.d("permissionCheck- Write", Integer.toString(permissionCheckWrite));
        //Log.d("permissionCheck- Read", Integer.toString(permissionCheckRead));

        if(isOnline()){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, NewsFragment.newInstance()).commit();
        }else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, MessageFragment.newInstance("a", "b")).commit();
        }



        //MenuItem item = (MenuItem) findViewById(R.id.nav_scan_login);


        //MenuItem item = (MenuItem) navigationView.findViewById(R.id.nav_scan_login);
        //item.setVisible(false);

    }

    public static void setLogin(){
        mProfile.loadPrefs(context);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {

        Log.d("onBackPressed", "ausgeführt");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_submenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, SettingsFragment.newInstance()).commit();
        }else if(id == R.id.action_shutdown) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        mNav.getLeftNav().setPage(id);

        return true;
    }

    private boolean checkPermission(String permissions[]) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void checkPermissions() {
        boolean permissionsGranted = checkPermission(PERMISSIONS_REQUIRED);
        if (permissionsGranted) {
            Toast.makeText(this, getResources().getString(R.string.permission_title_4), Toast.LENGTH_SHORT).show();
        } else {
            boolean showRationale = true;
            for (String permission: PERMISSIONS_REQUIRED) {
                showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                if (!showRationale) {
                    break;
                }
            }

            String dialogMsg = showRationale ? getResources().getString(R.string.permission_title_1) : getResources().getString(R.string.permission_title_3);

            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.permission_title_5))
                    .setMessage(dialogMsg)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(mActivity, PERMISSIONS_REQUIRED, REQUEST_PERMISSIONS);
                        }
                    }).create().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("MainActivity", "requestCode: " + requestCode);
        Log.d("MainActivity", "Permissions:" + Arrays.toString(permissions));
        Log.d("MainActivity", "grantResults: " + Arrays.toString(grantResults));


        if (requestCode == REQUEST_PERMISSIONS) {
            boolean hasGrantedPermissions = true;
            /*for (int i=0; i<grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    hasGrantedPermissions = false;
                    break;
                }
            }*/

            for (Integer grantResult: grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    hasGrantedPermissions = false;
                    break;
                }
            }

            if (!hasGrantedPermissions) {
                finish();
            }

        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Log.d("MainActivity", "Scanned");
                Intent intent = new Intent(this, ArticleDetailActivity.class);
                String message = result.getContents();
                intent.putExtra("BARCODE_ID", message);
                startActivity(intent);
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
