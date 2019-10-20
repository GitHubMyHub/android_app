package com.toolbardemo.mytestapp.DAO;


import android.util.Log;

import com.toolbardemo.mytestapp.MainActivity;
import com.toolbardemo.mytestapp.profile.oAuth;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by V-Windows on 11.04.2017.
 *
 */

public class JSONParser {

    public String output = "";

    // constructor
    public JSONParser() {

    }

    public String getJSONFromUrl(String getUrl) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(getUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            /*connection.setRequestProperty("username","Dreck");
            connection.setRequestProperty("password","pass123");
            connection.setRequestProperty("apikey","kqRjhKm46hkqdwshJhCGtsyNSdVK72iB");*/
            connection.setDoInput(true);
            connection.setDoOutput(true);

            // POST Userdata
            //oAuth oAuthApi = new oAuth();
            //String query = oAuthApi.getAuthProfile("admin", "admin", "kqRjhKm46hkqdwshJhCGtsyNSdVK72iB");
            //Profile profile = new Profile();
            //profile.setProfile("admin", "admin");
            oAuth oAuthApi = new oAuth(MainActivity.mProfile, "2Romu0JYixbYLUohobIvDhFV8I6ZpAF7");
            String query = oAuthApi.getAuth();

            Log.d("URL", url.toString());



            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuilder builder = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }
            this.output = builder.toString();

            return builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

}
