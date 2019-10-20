package com.toolbardemo.mytestapp.DAO;

import android.util.Log;

/**
 * Created by V-Windows on 08.06.2017.
 */

public class LoginDAO {

    public LoginDAO() {
        // leerer Konstruktor
    }

    public void getLogin(String email){
        JSONParser jsonParser = new JSONParser();
        Log.d("JSONParser", "init");

        try{
            jsonParser.getJSONFromUrl("http://192.168.178.41/graph/auth/index.php?user_name="+email);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
