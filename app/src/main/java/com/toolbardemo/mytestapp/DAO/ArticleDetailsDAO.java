package com.toolbardemo.mytestapp.DAO;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import com.toolbardemo.mytestapp.data.Article;
import com.toolbardemo.mytestapp.data.ArticleStore;
import com.toolbardemo.mytestapp.data.Certificate;
import com.toolbardemo.mytestapp.data.Ingredients;
import com.toolbardemo.mytestapp.data.Making;
import com.toolbardemo.mytestapp.data.Nutrition;
import com.toolbardemo.mytestapp.data.RatingLight;
import com.toolbardemo.mytestapp.data.Recycling;
import com.toolbardemo.mytestapp.data.Store;

/**
 * Created by V-Windows on 13.04.2017.
 */

public class ArticleDetailsDAO {

    private ArrayList<Article> web = new ArrayList<>();

    public ArticleDetailsDAO() {
    }

    public ArrayList<Article> getArticle(String sArticleCode){

        JSONParser jsonParser = new JSONParser();
        Log.d("JSONParser", "init");
        jsonParser.getJSONFromUrl("http://192.168.178.41/graph/article/index.php?id="+sArticleCode);
        Log.d("URL", "http://192.168.178.41/graph/article/index.php?id="+sArticleCode);

        try {
            JSONObject jObject = new JSONObject(jsonParser.output);

            if (jObject.getJSONObject("results").has("no_authentification")) {
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
            }


            JSONObject jRealObject = jObject.getJSONObject("results").getJSONObject("0");

                Article article = new Article();
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
                }

            // Ingredient
            if(jObject.has("ingredients")) {
                jRealObject = jObject.getJSONObject("ingredients");

                for (int i = 1; i <= jRealObject.length(); i++) {
                    Ingredients ingredient = new Ingredients();

                    JSONObject positionJsonObject = jRealObject.getJSONObject(Integer.toString(i - 1));

                    ingredient.setComponentsName(positionJsonObject.getString("components_name"));
                    ingredient.setComponentsQuantity(Integer.parseInt(positionJsonObject.getString("components_quantity")));
                    ingredient.setUnitTag(positionJsonObject.getString("unit_tag"));

                    //Log.d("setComponentsName", positionJsonObject.getString("components_name"));
                    //Log.d("setComponentsQuantity", positionJsonObject.getString("components_quantity"));
                    //Log.d("setUnitTag", positionJsonObject.getString("unit_tag"));

                    article.setIngredients(ingredient);

                }
            }

            // Rating Light
            if(jObject.has("nutrition_rating_light")){

                jRealObject = jObject.getJSONObject("nutrition_rating_light").getJSONObject("0");

                RatingLight ratingLight = new RatingLight();

                // LIPID
                String lipid_light = jRealObject.getString("lipid_light");
                String lipid_type = jRealObject.getString("lipid_type");
                String lipid_quantity = jRealObject.getString("lipid_quantity");

                if(lipid_light.equals("green")){
                    ratingLight.setLipid_light(RatingLight.LIGHT.green);
                }else if(lipid_light.equals("yellow")){
                    ratingLight.setLipid_light(RatingLight.LIGHT.yellow);
                }else if(lipid_light.equals("red")){
                    ratingLight.setLipid_light(RatingLight.LIGHT.red);
                }

                ratingLight.setLipid_type(lipid_type);
                ratingLight.setLipid_quantity(lipid_quantity);


                // TOT-LIPID
                String totLipid_light = jRealObject.getString("totLipid_light");
                String totLipid_type = jRealObject.getString("totLipid_type");
                String totLipid_quantity = jRealObject.getString("totLipid_quantity");

                if(totLipid_light.equals("green")){
                    ratingLight.setTotLipid_light(RatingLight.LIGHT.green);
                }else if(totLipid_light.equals("yellow")){
                    ratingLight.setTotLipid_light(RatingLight.LIGHT.yellow);
                }else if(totLipid_light.equals("red")){
                    ratingLight.setTotLipid_light(RatingLight.LIGHT.red);
                }

                ratingLight.setTotLipid_type(totLipid_type);
                ratingLight.setTotLipid_quantity(totLipid_quantity);

                // SUGAR
                String sugar_light = jRealObject.getString("sugar_light");
                String sugar_type = jRealObject.getString("sugar_type");
                String sugar_quantity = jRealObject.getString("sugar_quantity");

                if(sugar_light.equals("green")){
                    ratingLight.setSugar_light(RatingLight.LIGHT.green);
                }else if(sugar_light.equals("yellow")){
                    ratingLight.setSugar_light(RatingLight.LIGHT.yellow);
                }else if(sugar_light.equals("red")){
                    ratingLight.setSugar_light(RatingLight.LIGHT.red);
                }

                ratingLight.setSugar_type(sugar_type);
                ratingLight.setSugar_quantity(sugar_quantity);


                // SALT
                String salt_light = jRealObject.getString("salt_light");
                String salt_type = jRealObject.getString("salt_type");
                String salt_quantity = jRealObject.getString("salt_quantity");

                if(salt_light.equals("green")){
                    ratingLight.setSalt_light(RatingLight.LIGHT.green);
                }else if(salt_light.equals("yellow")){
                    ratingLight.setSalt_light(RatingLight.LIGHT.yellow);
                }else if(salt_light.equals("red")){
                    ratingLight.setSalt_light(RatingLight.LIGHT.red);
                }

                ratingLight.setSalt_type(salt_type);
                ratingLight.setSalt_quantity(salt_quantity);

                article.setRating(ratingLight);

            }

                // Nutrition
                if(jObject.has("nutrition")){

                    jRealObject = jObject.getJSONObject("nutrition");
                    Nutrition nutrition = new Nutrition();
                    Iterator<String> keys = jRealObject.keys();

                    while(keys.hasNext()){

                        String key = keys.next();
                        JSONObject obj = new JSONObject();
                        obj = jRealObject.getJSONObject(key);

                        //Log.d("Key", key);

                        switch(Integer.parseInt(key)){
                            case 0:
                                nutrition.setNutrition_kcal(Integer.parseInt(obj.get("nutrition_kcal").toString()));
                                try {
                                    nutrition.setNutrition_kcal_2(Integer.parseInt(obj.get("nutrition_kcal_2").toString()));
                                }catch (JSONException e){}

                                nutrition.setReferenz_quantity_kcal(Integer.parseInt(obj.get("referenz_quantity_kcal").toString()));


                                break;
                            case 1:
                                nutrition.setNutrition_kJ(Integer.parseInt(obj.get("nutrition_kJ").toString()));
                                try {
                                    nutrition.setNutrition_kJ_2(Integer.parseInt(obj.get("nutrition_kJ_2").toString()));
                                }catch (JSONException e){}

                                nutrition.setReferenz_quantity_kj(Integer.parseInt(obj.get("referenz_quantity_kj").toString()));
                                break;
                            case 2:
                                nutrition.setNutrition_Lipid(obj.get("nutrition_Lipid").toString());
                                try {
                                    nutrition.setNutrition_Lipid_2(obj.get("nutrition_Lipid_2").toString());
                                }catch (JSONException e){}

                                nutrition.setReferenz_quantity_lipid(Integer.parseInt(obj.get("referenz_quantity_lipid").toString()));
                                break;
                            case 3:
                                nutrition.setNutrition_Lipid_Sat(obj.get("nutrition_Lipid_Sat").toString());
                                try {
                                    nutrition.setNutrition_Lipid_Sat_2(obj.get("nutrition_Lipid_Sat_2").toString());
                                }catch (JSONException e){}

                                nutrition.setReferenz_quantity_sat(Integer.parseInt(obj.get("referenz_quantity_sat").toString()));
                                break;
                            case 4:
                                nutrition.setNutrition_Carbon(obj.get("nutrition_Carbon").toString());
                                try {
                                    nutrition.setNutrition_Carbon_2(obj.get("nutrition_Carbon_2").toString());
                                }catch (JSONException e){}

                                nutrition.setReferenz_quantity_carbon(Integer.parseInt(obj.get("referenz_quantity_carbon").toString()));
                                break;
                            case 5:
                                nutrition.setNutrition_Carbon_Sugar(obj.get("nutrition_Carbon_Sugar").toString());
                                try {
                                    nutrition.setNutrition_Carbon_Sugar_2(obj.get("nutrition_Carbon_Sugar_2").toString());
                                }catch (JSONException e){}

                                nutrition.setReferenz_quantity_sugar(Integer.parseInt(obj.get("referenz_quantity_sugar").toString()));
                                break;
                            case 6:
                                nutrition.setNutrition_Protein(obj.get("nutrition_Protein").toString());
                                try {
                                    nutrition.setNutrition_Protein_2(obj.get("nutrition_Protein_2").toString());
                                }catch (JSONException e){}
                                nutrition.setReferenz_quantity_protein(Integer.parseInt(obj.get("referenz_quantity_protein").toString()));
                                break;
                            case 7:
                                nutrition.setNutrition_Salt(obj.get("nutrition_Salt").toString());
                                try {
                                    nutrition.setNutrition_Salt_2(obj.get("nutrition_Salt_2").toString());
                                }catch (JSONException e){}
                                nutrition.setReferenz_quantity_salt(Integer.parseInt(obj.get("referenz_quantity_salt").toString()));
                                break;
                        }
                    }

                    article.setNutrition(nutrition);
                }


                // Brand
                if(jObject.has("brand")) {
                    jRealObject = jObject.getJSONObject("brand").getJSONObject("0");

                    String brandImagePath = jRealObject.getString("brand_image_path");
                    String brandName = jRealObject.getString("brand_name");
                    String brandDescription = jRealObject.getString("brand_description");

                    article.setBrandImagePath(brandImagePath);
                    article.setBrandName(brandName);
                    article.setBrandDescription(brandDescription);
                }




                //  Nutrition-Referenz
                if(jObject.has("nutrition_referenz")){
                    jRealObject = jObject.getJSONObject("nutrition_referenz").getJSONObject("0");

                    String nutritionQuantityMultiplikator = jRealObject.getString("nutrition_quantity_multiplikator");
                    String unitTag = jRealObject.getString("unit_tag");

                    article.getNutrition().setNutrition_quantity_multiplikator(Integer.parseInt(nutritionQuantityMultiplikator));
                    article.getNutrition().setUnit_tag(unitTag);
                }


                // Company
                if(jObject.has("company")) {
                    jRealObject = jObject.getJSONObject("company").getJSONObject("0");

                    String companyImagePath = jRealObject.getString("company_image_path");
                    String companyName = jRealObject.getString("company_name");
                    String companyDescription = jRealObject.getString("company_description");

                    article.setCompanyImagePath(companyImagePath);
                    article.setCompanyName(companyName);
                    article.setCompanyDescription(companyDescription);
                }

                // Certificate
                if(jObject.has("zertificate")) {
                    jRealObject = jObject.getJSONObject("zertificate");

                    for (int i = 1; i <= jRealObject.length(); i++) {
                        Certificate certificate = new Certificate();

                        JSONObject positionJsonObject = jRealObject.getJSONObject(Integer.toString(i - 1));

                        certificate.setZertificateImagePath(positionJsonObject.getString("zertificate_image_path"));
                        certificate.setZertificateName(positionJsonObject.getString("zertificate_name"));
                        certificate.setZertificateDescription(positionJsonObject.getString("zertificate_description"));

                        article.setZertificate(certificate);

                    }
                }

                // Making
                if(jObject.has("making")) {
                    jRealObject = jObject.getJSONObject("making");

                    for (int i = 1; i <= jRealObject.length(); i++) {
                        Making making = new Making();

                        JSONObject positionJsonObject = jRealObject.getJSONObject(Integer.toString(i - 1));

                        making.setMakingImagePath(positionJsonObject.getString("device_picture"));
                        making.setMakingImageStepPath(positionJsonObject.getString("device_step_picture"));
                        making.setDeviceName(positionJsonObject.getString("device_name"));
                        making.setDeviceStepName(positionJsonObject.getString("device_step_name"));
                        making.setMakingStep(positionJsonObject.getString("making_rel_step"));
                        making.setMakingStepTime(positionJsonObject.getString("making_time"));
                        making.setMakingDescription(positionJsonObject.getString("making_rel_description"));

                        article.setMaking(making);

                    }
                }

                // Store
                if(jObject.has("store")) {
                    jRealObject = jObject.getJSONObject("store");

                    for (int i = 1; i <= jRealObject.length(); i++) {
                        Store store = new Store();

                        JSONObject positionJsonObject = jRealObject.getJSONObject(Integer.toString(i - 1));

                        store.setStoreImagePath(positionJsonObject.getString("imgStore"));
                        store.setStorage(positionJsonObject.getString("store"));
                        store.setStoreTemp(positionJsonObject.getString("storeTemp"));
                        store.setStoreType(positionJsonObject.getString("storeType"));

                        ArticleStore articleStore = new ArticleStore();
                        articleStore.setStoreId(Integer.valueOf(positionJsonObject.getString("store_name")));
                        articleStore.setStorageLevel_3((positionJsonObject.getString("storage_rel_level_3").equals("2")) ? false : true);
                        articleStore.setStorageLevel_2((positionJsonObject.getString("storage_rel_level_2").equals("2")) ? false : true);
                        articleStore.setStorageLevel_1((positionJsonObject.getString("storage_rel_level_1").equals("2")) ? false : true);
                        articleStore.setStorageLevelVegDrawer((positionJsonObject.getString("storage_rel_level_veg_drawer").equals("2")) ? false : true);
                        articleStore.setStorageLevelDoorLevel_3((positionJsonObject.getString("storage_rel_door_level_3").equals("2")) ? false : true);
                        articleStore.setStorageLevelDoorLevel_2((positionJsonObject.getString("storage_rel_door_level_2").equals("2")) ? false : true);
                        articleStore.setStorageLevelDoorLevel_1((positionJsonObject.getString("storage_rel_door_level_1").equals("2")) ? false : true);
                        articleStore.setStorageLevelTemp(positionJsonObject.getString("storage_rel_temp"));

                        article.setStore(store);
                        article.setArticleStore(articleStore);

                    }
                }

                // Recycling
                if(jObject.has("recycling")) {
                    jRealObject = jObject.getJSONObject("recycling");

                    for (int i = 1; i <= jRealObject.length(); i++) {
                        Recycling recycling = new Recycling();

                        JSONObject positionJsonObject = jRealObject.getJSONObject(Integer.toString(i - 1));

                        recycling.setRecyclingImagePath(positionJsonObject.getString("recycling_image_path"));
                        recycling.setRecyclingNameTag(positionJsonObject.getString("recycling_name_tag"));
                        recycling.setRecyclingDescription(positionJsonObject.getString("recycling_description"));

                        recycling.setTrashImagePath(positionJsonObject.getString("trash_image"));
                        recycling.setTrashName(positionJsonObject.getString("trash_name"));

                        article.setRecycling(recycling);

                    }
                }


                //String sessionID = jRealObject.getString("sessionID");
                //news.setSessionID(sessionID);

                web.add(article);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return web;
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
