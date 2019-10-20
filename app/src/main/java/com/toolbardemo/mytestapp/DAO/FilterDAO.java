package com.toolbardemo.mytestapp.DAO;

import android.util.Log;

import com.toolbardemo.mytestapp.data.Article;

import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by V-Windows on 13.04.2017.
 */

public class FilterDAO {

    private ArrayList<Article> web = new ArrayList<>();

    public FilterDAO() {
    }

    public void setFilter(String userKey, String sFilter, boolean bChecked) {

        Log.d("userKey", String.valueOf(userKey));
        Log.d("sFilter", String.valueOf(sFilter));


        try{
            URI uri = new URI(sFilter.replace(" ", "%20"));
            sFilter = URLEncoder.encode(uri.toString(), "utf-8");
            Log.d("sFilter", String.valueOf(uri));
        }catch(Exception e){

        }


        Log.d("bChecked", String.valueOf(bChecked));

        JSONParser jsonParser = new JSONParser();
        Log.d("JSONParser", "init");


        try {
            jsonParser.getJSONFromUrl("http://192.168.178.41/ajax/add-filter.php?user_id="+userKey+"&sFilter="+sFilter+"&bChecked="+bChecked);
            Log.d("URL", "http://192.168.178.41/ajax/add-filter.php?user_id="+userKey+"&sFilter="+sFilter+"&bChecked="+bChecked);

            //JSONObject jObject = new JSONObject(jsonParser.output);

            //Log.d("jObject", jObject.toString());

            /*if (jObject.getJSONObject("results").has("no_authentification")) {
                Log.d("results", "no_authentification");
                Article article = new Article();
                article.setNoAthentification(jObject.getJSONObject("results").getString("no_authentification"));
                web.add(article);
                return web;
            }

            if (jObject.getJSONObject("results").has("no_entry")) {
                Log.d("results", "no_entry");
                Article article = new Article();
                article.setNoEntry(jObject.getJSONObject("results").getString("no_entry"));
                web.add(article);
                return web;
            }*/


            //JSONObject jRealObject = jObject.getJSONObject("results").getJSONObject("0");


                //web.add(article);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * Returns the first Item of an ArrayList<Article></Article>
    * */
    public Article getArticle(){
        return web.get(0);
    }

    /**
     * Returns an ArrayList<Article></Article>
     * */
    public ArrayList<Article> getArticleArrayList(){
        return web;
    }


}
