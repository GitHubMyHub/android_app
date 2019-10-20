package com.toolbardemo.mytestapp.profile;

import android.net.Uri;
import android.util.Log;

/**
 * Created by V-Windows on 04.06.2017.
 */

public class oAuth {

    Profile profile;
    String apiKey;

    public oAuth(){
        // leerer Konstruktor
    }

    public oAuth(Profile profile){
        this.profile = profile;
    }

    public oAuth(String apiKey){
        this.apiKey = apiKey;
    }

    public oAuth(Profile profile, String apiKey){
        this.profile = profile;
        this.apiKey = apiKey;
    }

    public String getAuth(){

        String query;

        Log.d("oAuth", "getAuth");

        if(!this.profile.getToken().equals("")){
            Log.d("SASUKE", "SASUKE");
            Log.d("TOKEN", this.profile.getToken().toString());

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("token", this.profile.getToken().toString())
                    .appendQueryParameter("apikey", this.apiKey);
            query = builder.build().getEncodedQuery();

        }else{
            // POST Userdata
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("apikey", apiKey);
            query = builder.build().getEncodedQuery();
        }

        Log.d("query", query);
        return query;
    }

    /*public String getAuthApi(String apiKey){
        // POST Userdata
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("apikey", apiKey);
        String query = builder.build().getEncodedQuery();

        return query;
    }

    public String getAuthProfile(String username, String password, String apiKey) {
        // POST Userdata
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("username", username)
                .appendQueryParameter("password", password)
                .appendQueryParameter("apikey", apiKey);
        String query = builder.build().getEncodedQuery();

        return query;
    }*/
}
