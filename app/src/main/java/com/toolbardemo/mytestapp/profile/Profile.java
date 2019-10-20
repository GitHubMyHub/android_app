package com.toolbardemo.mytestapp.profile;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.toolbardemo.mytestapp.DAO.LoginDAO;

import static com.twitter.sdk.android.core.Twitter.TAG;

/**
 * Created by V-Windows on 04.06.2017.
 */

public class Profile {


    private SharedPreferences sharedPref;
    private SharedPreferences.Editor edit;
    private Context context;

    public static final String PROFILE_SESSION = "PROFILE_SESSION";
    private String txtProfileSession;

    private FirebaseUser user;
    private String name;
    private static String email;
    private String photoUrl = "";
    private boolean emailVerified;
    private String uid;
    //private Task<GetTokenResult> token;
    private static String token = "";

    public Profile(){
    }

    public void setUser(FirebaseUser user){
        this.user = user;
        // Name, email address, and profile photo Url
        this.name = user.getDisplayName();
        this.email = user.getEmail();

        if(user.getPhotoUrl() != null){
            this.photoUrl = user.getPhotoUrl().toString();
        }else{
            this.photoUrl = "";
        }

        // Check if user's email is verified
        this.emailVerified = user.isEmailVerified();

        // The user's ID, unique to the Firebase project. Do NOT use this value to
        // authenticate with your backend server, if you have one. Use
        // FirebaseUser.getToken() instead.
        this.uid = user.getUid();
        //this.token = user.getIdToken(true);

        this.user.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult result) {
                String idToken = result.getToken();
                //Do whatever
                Log.d(TAG, "GetTokenResult result = " + idToken);
                token = idToken;
                LoginDAO loginDAO = new LoginDAO();
                loginDAO.getLogin(email);
            }
        });


    }

    public FirebaseUser getUser(){
        return this.user;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public String getUid() {
        return uid;
    }

    public String getToken() {
        return token;
    }

    public void loadPrefs(Context context){

        this.context = context;

        sharedPref = context.getSharedPreferences("prefsData", Context.MODE_PRIVATE);
        txtProfileSession = sharedPref.getString(PROFILE_SESSION, "default");

        Log.d("LoadPrefs", txtProfileSession);
    }

    public String getSessionId(){
        return txtProfileSession;
    }

    public void savePrefs(String key, String value){
        sharedPref = context.getSharedPreferences("prefsData", Context.MODE_PRIVATE);
        edit = sharedPref.edit();
        //Log.d(CHECKBOX_TORCH, Boolean.toString(value));
        edit.putString(key, value);
        //edit.commit();
        edit.apply();
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
    }


}
