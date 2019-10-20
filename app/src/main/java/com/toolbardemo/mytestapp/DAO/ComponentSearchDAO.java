package com.toolbardemo.mytestapp.DAO;

import android.util.Log;

import com.toolbardemo.mytestapp.data.SearchComponent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by V-Windows on 13.04.2017.
 *
 */

public class ComponentSearchDAO {

    private ArrayList<SearchComponent> web = new ArrayList<>();

    public ComponentSearchDAO() {
    }

    public ArrayList<SearchComponent> getComponent(String sComponentSearch){

        JSONParser jsonParser = new JSONParser();
        Log.d("JSONParser", "init");

        //jsonParser.getJSONFromUrl("http://192.168.178.41/graph/searchcomponent/index.php?id="+sComponentSearch);
        jsonParser.getJSONFromUrl("http://192.168.178.41/graph/searchcomponent/index.php?search="+sComponentSearch);
        Log.d("URL", "http://192.168.178.41/graph/searchcomponent/index.php?search="+sComponentSearch);

        try {
            JSONObject jObject = new JSONObject(jsonParser.output);

            /*if (jObject.getJSONObject("results").has("no_authentification")) {
                Log.d("results", "no_authentification");
                SearchArticle article = new SearchArticle();
                article.setNoAuthentification(jObject.getJSONObject("results").getString("no_authentification"));
                web.add(article);
                return web;
            }

            if (jObject.getJSONObject("results").has("no_entry")) {
                Log.d("results", "no_entry");
                SearchArticle article = new SearchArticle();
                article.setNoEntry(jObject.getJSONObject("results").getString("no_entry"));
                web.add(article);
                return web;
            }*/


            JSONObject jRealObject = jObject.getJSONObject("results");


            for(int i= 0; i<jRealObject.length(); i++){
            Log.d("TAG", jRealObject.getJSONObject(Integer.toString(i)).toString());

                JSONObject subObject = jRealObject.getJSONObject(Integer.toString(i));

                SearchComponent component = new SearchComponent();

                String components_id = subObject.getString("components_id");
                String components_name = subObject.getString("components_name");
                String components_description = subObject.getString("components_description");

                component.setComponentId(Integer.parseInt(components_id));
                component.setComponentName(components_name);
                component.setComponentDescription(components_description);


                web.add(component);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return web;
    }

    /**
     * getComponent
    * Returns the first Item of an ArrayList
     * @param position Position of ArrayList<SearchComponent> web
    * */
    public SearchComponent getComponent(int position){
        return web.get(position);
    }

    /**
     * getArticleArrayList
     * Returns an ArrayList
     * */
    public ArrayList<SearchComponent> getArticleArrayList(){
        return web;
    }

}
