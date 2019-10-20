package com.toolbardemo.mytestapp.DAO;

import android.util.Log;

import com.toolbardemo.mytestapp.data.ArticleStore;
import com.toolbardemo.mytestapp.data.SearchArticle;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by V-Windows on 13.04.2017.
 *
 */

public class ArticleSearchDAO {

    private ArrayList<SearchArticle> web = new ArrayList<>();

    public ArticleSearchDAO() {
    }

    public ArrayList<SearchArticle> getArticle(String sArticleSearch){

        JSONParser jsonParser = new JSONParser();
        Log.d("JSONParser", "init");
        //jsonParser.getJSONFromUrl("http://192.168.178.41/graph/article/index.php?id="+sArticleCode);
        jsonParser.getJSONFromUrl("http://192.168.178.41/graph/searcharticle/index.php?search="+sArticleSearch);
        Log.d("URL", "http://192.168.178.41/graph/searcharticle/index.php?search="+sArticleSearch);

        try {
            JSONObject jObject = new JSONObject(jsonParser.output);

            if (jObject.getJSONObject("results").has("no_authentification")) {
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
            }


            JSONObject jRealObject = jObject.getJSONObject("results");


            for(int i= 0; i<jRealObject.length(); i++){
            Log.d("TAG", jRealObject.getJSONObject(Integer.toString(i)).toString());

                JSONObject subObject = jRealObject.getJSONObject(Integer.toString(i));

                SearchArticle article = new SearchArticle();
                String id = subObject.getString("article_id");
                String barcode = subObject.getString("article_barcode");
                String name = subObject.getString("article_name");
                String description = subObject.getString("article_description");
                String qrCode = subObject.getString("article_qrcode");
                String preparation = subObject.getString("article_preparation");
                String traces = subObject.getString("article_traces");
                String imagePath = subObject.getJSONObject("images").getJSONObject("0").getString("thumbnail");
                //String imagePath = subObject.getJSONObject("store").getJSONObject("0");

                Integer storeId = 0;
                if(!subObject.getJSONObject("store").getJSONObject("0").getString("store_name").equals("null")){
                    storeId = Integer.valueOf(subObject.getJSONObject("store").getJSONObject("0").getString("store_name"));
                    Log.d("COLIN", String.valueOf(storeId));
                }
                Boolean storageLevel_1 = subObject.getJSONObject("store").getJSONObject("0").getString("storage_rel_level_1").equals("1") ? true : false;
                Boolean storageLevel_2 = subObject.getJSONObject("store").getJSONObject("0").getString("storage_rel_level_2").equals("1") ? true : false;
                Boolean storageLevel_3 = subObject.getJSONObject("store").getJSONObject("0").getString("storage_rel_level_3").equals("1") ? true : false;
                Boolean storageLevelVegDrawer = subObject.getJSONObject("store").getJSONObject("0").getString("storage_rel_level_veg_drawer").equals("1") ? true : false;
                Boolean storageDoorLevel_1 = subObject.getJSONObject("store").getJSONObject("0").getString("storage_rel_door_level_1").equals("1") ? true : false;
                Boolean storageDoorLevel_2 = subObject.getJSONObject("store").getJSONObject("0").getString("storage_rel_door_level_2").equals("1") ? true : false;
                Boolean storageDoorLevel_3 = subObject.getJSONObject("store").getJSONObject("0").getString("storage_rel_door_level_3").equals("1") ? true : false;
                String storageTemp = subObject.getJSONObject("store").getJSONObject("0").getString("storage_rel_temp");



                Log.d("imagePath", imagePath);

                article.setArticleId(Integer.parseInt(id));
                article.setArticleBarcode(barcode);
                article.setArticleName(name);
                article.setArticleDescription(description);
                article.setArticleQrCode(qrCode);
                article.setArticlePreparation(preparation);
                article.setArticleTraces(traces);
                article.setArticleImagePath(imagePath);

                ArticleStore articleStore = new ArticleStore();
                articleStore.setStoreId(storeId);
                articleStore.setStorageLevel_3(storageLevel_3);
                articleStore.setStorageLevel_2(storageLevel_2);
                articleStore.setStorageLevel_1(storageLevel_1);
                articleStore.setStorageLevelVegDrawer(storageLevelVegDrawer);
                articleStore.setStorageLevelDoorLevel_3(storageDoorLevel_3);
                articleStore.setStorageLevelDoorLevel_2(storageDoorLevel_2);
                articleStore.setStorageLevelDoorLevel_1(storageDoorLevel_1);
                articleStore.setStorageLevelTemp(storageTemp);

                article.setStore(articleStore);

                web.add(article);

            }

                /*Article article = new Article();
                String id = jRealObject.getString("article_id");
                String barcode = jRealObject.getString("article_barcode");
                String name = jRealObject.getString("article_name");
                String description = jRealObject.getString("article_description");
                String qrCode = jRealObject.getString("article_qrcode");
                String preparation = jRealObject.getString("article_preparation");
                String traces = jRealObject.getString("article_traces");

                article.setId(Integer.parseInt(id));
                article.setBarcode(barcode);
                article.setName(name);
                article.setDescription(description);
                article.setQrCode(qrCode);
                article.setPreparation(preparation);
                article.setTraces(traces);


                jRealObject = jObject.getJSONObject("image");


                // IMAGES
                for(int i= 0; i<jRealObject.length(); i++){
                    //Log.d("test", Integer.toString(jObject3.length()));
                    JSONObject jRealObject2 = jRealObject.getJSONObject(Integer.toString(i));
                    String image = jRealObject2.getString("image_path");
                    //Log.d("test2", image);

                    article.setArticleImage(image);
                }*/

                //String sessionID = jRealObject.getString("sessionID");
                //news.setSessionID(sessionID);

                //web.add(article);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return web;
    }

    /**
    * Returns the first Item of an ArrayList<Article></Article>
    * */
    public SearchArticle getArticle(int position){
        return web.get(position);
    }

    /**
     * Returns an ArrayList<Article></Article>
     * */
    public ArrayList<SearchArticle> getArticleArrayList(){
        return web;
    }

}
