package com.toolbardemo.mytestapp.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import com.toolbardemo.mytestapp.DAO.AllergenicDAO;
import com.toolbardemo.mytestapp.MainActivity;
import com.toolbardemo.mytestapp.data.Article;
import com.toolbardemo.mytestapp.R;
import com.toolbardemo.mytestapp.data.RatingLight;
import com.toolbardemo.mytestapp.image.ImageLoader;


/**
 * Created by V-Windows on 11.04.2017.
 *
 */

public class CustomListArticleDetails {

    private final Activity context;
    private final ArrayList<Article> web;
    public final int ARTICLE_POSITION = 0;
    private ImageLoader imageLoader;
    private ImageLoader imageLoaderBrand;
    private ImageLoader imageLoaderCompany;

    public CustomListArticleDetails(Activity context, ArrayList<Article> web) {
        this.context = context;
        this.web = web;
        this.imageLoader = new ImageLoader(context);
        this.imageLoaderBrand = new ImageLoader(context);
        this.imageLoaderCompany = new ImageLoader(context);
    }

    public void setContent(int position) {

        // Set Allergenic
        AllergenicDAO allergenicDAO = new AllergenicDAO();
        allergenicDAO.setAlergene(MainActivity.mProfile.getUid(), String.valueOf(web.get(position).getId()));
        if(allergenicDAO.getArticleArrayList().size() > 0){
            TextView txtAllergenic = this.context.findViewById(R.id.txtViewArticleAllergenicMessage);
            String message = this.context.getString(R.string.allergenic_message).toString() + allergenicDAO.getArticleArrayList().get(0).toString() + " " + allergenicDAO.getArticleArrayList().get(1).toString();
            txtAllergenic.setText(message);
        }else{
            CardView cardView = this.context.findViewById(R.id.cardViewAllergenic);
            cardView.setVisibility(View.GONE);
        }

        // ID
        TextView txtId = this.context.findViewById(R.id.txtIdViewArticle);
        txtId.setText(String.valueOf(web.get(position).getId()));

        // Barcode
        TextView txtBarcode = this.context.findViewById(R.id.txtBarcodeViewArticle);
        txtBarcode.setText(web.get(position).getBarcode());

        // Name
        TextView txtName = this.context.findViewById(R.id.txtNameViewArticle);
        txtName.setText(web.get(position).getName());

        // Description
        TextView txtDescription = this.context.findViewById(R.id.txtDescriptionViewArticle);
        txtDescription.setText(web.get(position).getDescription());

        // QR-Code
        TextView txtQrCode = this.context.findViewById(R.id.txtSiteViewArticle);
        txtQrCode.setText(web.get(position).getQrCode());

        // Preparation
        TextView txtPreparation = this.context.findViewById(R.id.txtPreparationViewArticle);
        txtPreparation.setText(web.get(position).getPreparation());

        // Traces
        TextView txtTraces = this.context.findViewById(R.id.txtTracesViewArticle);
        txtTraces.setText(web.get(position).getTraces());

        final CustomListArticleIngredients adapterIngredients = new CustomListArticleIngredients(this.context, web.get(position).getIngredients());
        ListView ingredients = this.context.findViewById(R.id.listViewIngredients);
        ingredients.setAdapter(adapterIngredients);


        View item = this.context.findViewById(R.id.ratingTabLipid);
        TextView itemTextView = this.context.findViewById(R.id.txtViewLipidTitle);
        TextView itemTextViewCount = this.context.findViewById(R.id.txtViewLipidCount);

        if(web.get(position).getRating().getLipid_light().equals(RatingLight.LIGHT.green)){
            item.setBackground(this.context.getResources().getDrawable(R.drawable.drawer_rating_green));
            itemTextView.setShadowLayer(2, 1, 1, this.context.getResources().getColor(R.color.rating_green_shadow));
            itemTextView.setTextColor(this.context.getResources().getColor(R.color.rating_green));
            itemTextViewCount.setText(web.get(position).getRating().getLipid_quantity());
        }else if(web.get(position).getRating().getLipid_light().equals(RatingLight.LIGHT.yellow)){
            item.setBackground(this.context.getResources().getDrawable(R.drawable.drawer_rating_yellow));
            itemTextView.setShadowLayer(2, 1, 1, this.context.getResources().getColor(R.color.rating_yellow_shadow));
            itemTextView.setTextColor(this.context.getResources().getColor(R.color.rating_yellow));
            itemTextViewCount.setText(web.get(position).getRating().getLipid_quantity());
        }else if(web.get(position).getRating().getLipid_light().equals(RatingLight.LIGHT.red)){
            item.setBackground(this.context.getResources().getDrawable(R.drawable.drawer_rating_red));
            itemTextView.setShadowLayer(2, 1, 1, this.context.getResources().getColor(R.color.rating_red_shadow));
            itemTextView.setTextColor(this.context.getResources().getColor(R.color.rating_red));
            itemTextViewCount.setText(web.get(position).getRating().getLipid_quantity());
        }

        item = this.context.findViewById(R.id.ratingTabLipidSat);
        itemTextView = this.context.findViewById(R.id.txtViewLipidTotTitle);
        itemTextViewCount = this.context.findViewById(R.id.txtViewLipidTotCount);

        if(web.get(position).getRating().getTotLipid_light().equals(RatingLight.LIGHT.green)){
            item.setBackground(this.context.getResources().getDrawable(R.drawable.drawer_rating_green));
            itemTextView.setShadowLayer(2, 1, 1, this.context.getResources().getColor(R.color.rating_green_shadow));
            itemTextView.setTextColor(this.context.getResources().getColor(R.color.rating_green));
            itemTextViewCount.setText(web.get(position).getRating().getTotLipid_quantity());
        }else if(web.get(position).getRating().getTotLipid_light().equals(RatingLight.LIGHT.yellow)){
            item.setBackground(this.context.getResources().getDrawable(R.drawable.drawer_rating_yellow));
            itemTextView.setShadowLayer(2, 1, 1, this.context.getResources().getColor(R.color.rating_yellow_shadow));
            itemTextView.setTextColor(this.context.getResources().getColor(R.color.rating_yellow));
            itemTextViewCount.setText(web.get(position).getRating().getTotLipid_quantity());
        }else if(web.get(position).getRating().getTotLipid_light().equals(RatingLight.LIGHT.red)){
            item.setBackground(this.context.getResources().getDrawable(R.drawable.drawer_rating_red));
            itemTextView.setShadowLayer(2, 1, 1, this.context.getResources().getColor(R.color.rating_red_shadow));
            itemTextView.setTextColor(this.context.getResources().getColor(R.color.rating_red));
            itemTextViewCount.setText(web.get(position).getRating().getTotLipid_quantity());
        }

        item = this.context.findViewById(R.id.ratingTabLipidSugar);
        itemTextView = this.context.findViewById(R.id.txtViewSugarTitle);
        itemTextViewCount = this.context.findViewById(R.id.txtViewSugarCount);

        if(web.get(position).getRating().getSugar_light().equals(RatingLight.LIGHT.green)){
            item.setBackground(this.context.getResources().getDrawable(R.drawable.drawer_rating_green));
            itemTextView.setShadowLayer(2, 1, 1, this.context.getResources().getColor(R.color.rating_green_shadow));
            itemTextView.setTextColor(this.context.getResources().getColor(R.color.rating_green));
            itemTextViewCount.setText(web.get(position).getRating().getSugar_quantity());
        }else if(web.get(position).getRating().getSugar_light().equals(RatingLight.LIGHT.yellow)){
            item.setBackground(this.context.getResources().getDrawable(R.drawable.drawer_rating_yellow));
            itemTextView.setShadowLayer(2, 1, 1, this.context.getResources().getColor(R.color.rating_yellow_shadow));
            itemTextView.setTextColor(this.context.getResources().getColor(R.color.rating_yellow));
            itemTextViewCount.setText(web.get(position).getRating().getSugar_quantity());
        }else if(web.get(position).getRating().getSugar_light().equals(RatingLight.LIGHT.red)){
            item.setBackground(this.context.getResources().getDrawable(R.drawable.drawer_rating_red));
            itemTextView.setShadowLayer(2, 1, 1, this.context.getResources().getColor(R.color.rating_red_shadow));
            itemTextView.setTextColor(this.context.getResources().getColor(R.color.rating_red));
            itemTextViewCount.setText(web.get(position).getRating().getSugar_quantity());
        }

        item = this.context.findViewById(R.id.ratingTabLipidSalt);
        itemTextView = this.context.findViewById(R.id.txtViewSaltTitle);
        itemTextViewCount = this.context.findViewById(R.id.txtViewSaltCount);

        if(web.get(position).getRating().getSalt_light().equals(RatingLight.LIGHT.green)){
            item.setBackground(this.context.getResources().getDrawable(R.drawable.drawer_rating_green));
            itemTextView.setShadowLayer(2, 1, 1, this.context.getResources().getColor(R.color.rating_green_shadow));
            itemTextView.setTextColor(this.context.getResources().getColor(R.color.rating_green));
            itemTextViewCount.setText(web.get(position).getRating().getSalt_quantity());
        }else if(web.get(position).getRating().getSalt_light().equals(RatingLight.LIGHT.yellow)){
            item.setBackground(this.context.getResources().getDrawable(R.drawable.drawer_rating_yellow));
            itemTextView.setShadowLayer(2, 1, 1, this.context.getResources().getColor(R.color.rating_yellow_shadow));
            itemTextView.setTextColor(this.context.getResources().getColor(R.color.rating_yellow));
            itemTextViewCount.setText(web.get(position).getRating().getSalt_quantity());
        }else if(web.get(position).getRating().getSalt_light().equals(RatingLight.LIGHT.red)){
            item.setBackground(this.context.getResources().getDrawable(R.drawable.drawer_rating_red));
            itemTextView.setShadowLayer(2, 1, 1, this.context.getResources().getColor(R.color.rating_red_shadow));
            itemTextView.setTextColor(this.context.getResources().getColor(R.color.rating_red));
            itemTextViewCount.setText(web.get(position).getRating().getSalt_quantity());
        }

        // Nutrition
        if(String.valueOf(web.get(position).getNutrition().getNutrition_kcal_2()).equals("")) {
            View gridLayout = this.context.findViewById(R.id.gridViewArticle);
            gridLayout.findViewById(R.id.textView3).setVisibility(View.GONE);
            gridLayout.findViewById(R.id.textView7).setVisibility(View.GONE);
            gridLayout.findViewById(R.id.textView11).setVisibility(View.GONE);
            gridLayout.findViewById(R.id.textView15).setVisibility(View.GONE);
            gridLayout.findViewById(R.id.textView19).setVisibility(View.GONE);
            gridLayout.findViewById(R.id.textView23).setVisibility(View.GONE);
            gridLayout.findViewById(R.id.textView27).setVisibility(View.GONE);
            gridLayout.findViewById(R.id.textView31).setVisibility(View.GONE);
            gridLayout.findViewById(R.id.textView35).setVisibility(View.GONE);
        }else{
            TextView txtView3 = this.context.findViewById(R.id.textView3);
            String textMultiplier = String.valueOf(web.get(position).getNutrition().getNutrition_quantity_multiplikator()) + " " + web.get(position).getNutrition().getUnit_tag();
            txtView3.setText(textMultiplier);
        }

        TextView txtView4 = this.context.findViewById(R.id.textView4);
        String referenceQuantity = this.context.getString(R.string.menge_4) + "(" + String.valueOf(web.get(position).getNutrition().getNutrition_quantity_multiplikator())
                + " " + web.get(position).getNutrition().getUnit_tag() + ")";
        txtView4.setText(referenceQuantity);




        TextView txtView6 = this.context.findViewById(R.id.textView6);
        txtView6.setText(String.valueOf(web.get(position).getNutrition().getNutrition_kcal()));

        TextView txtView7 = this.context.findViewById(R.id.textView7);
        txtView7.setText(String.valueOf(web.get(position).getNutrition().getNutrition_kcal_2()));

        TextView txtView8 = this.context.findViewById(R.id.textView8);
        txtView8.setText(String.valueOf(web.get(position).getNutrition().getReferenz_quantity_kcal()));




        TextView txtView10 = this.context.findViewById(R.id.textView10);
        txtView10.setText(String.valueOf(web.get(position).getNutrition().getNutrition_kJ()));

        TextView txtView11 = this.context.findViewById(R.id.textView11);
        txtView11.setText(String.valueOf(web.get(position).getNutrition().getNutrition_kJ_2()));

        TextView txtView12 = this.context.findViewById(R.id.textView12);
        txtView12.setText(String.valueOf(web.get(position).getNutrition().getReferenz_quantity_kj()));




        TextView txtView14 = this.context.findViewById(R.id.textView14);
        txtView14.setText(web.get(position).getNutrition().getNutrition_Lipid());

        TextView txtView15 = this.context.findViewById(R.id.textView15);
        txtView15.setText(web.get(position).getNutrition().getNutrition_Lipid_2());

        TextView txtView16 = this.context.findViewById(R.id.textView16);
        txtView16.setText(String.valueOf(web.get(position).getNutrition().getReferenz_quantity_lipid()));



        TextView txtView18 = this.context.findViewById(R.id.textView18);
        txtView18.setText(web.get(position).getNutrition().getNutrition_Lipid_Sat());

        TextView txtView19 = this.context.findViewById(R.id.textView19);
        txtView19.setText(web.get(position).getNutrition().getNutrition_Lipid_Sat_2());

        TextView txtView20 = this.context.findViewById(R.id.textView20);
        txtView20.setText(String.valueOf(web.get(position).getNutrition().getReferenz_quantity_sat()));



        TextView txtView22 = this.context.findViewById(R.id.textView22);
        txtView22.setText(web.get(position).getNutrition().getNutrition_Carbon());

        TextView txtView23 = this.context.findViewById(R.id.textView23);
        txtView23.setText(web.get(position).getNutrition().getNutrition_Carbon_2());

        TextView txtView24 = this.context.findViewById(R.id.textView24);
        txtView24.setText(String.valueOf(web.get(position).getNutrition().getReferenz_quantity_carbon()));



        TextView txtView26 = this.context.findViewById(R.id.textView26);
        txtView26.setText(web.get(position).getNutrition().getNutrition_Carbon_Sugar());

        TextView txtView27 = this.context.findViewById(R.id.textView27);
        txtView27.setText(web.get(position).getNutrition().getNutrition_Carbon_Sugar_2());

        TextView txtView28 = this.context.findViewById(R.id.textView28);
        txtView28.setText(String.valueOf(web.get(position).getNutrition().getReferenz_quantity_sugar()));




        TextView txtView30 = this.context.findViewById(R.id.textView30);
        txtView30.setText(web.get(position).getNutrition().getNutrition_Protein());

        TextView txtView31 = this.context.findViewById(R.id.textView31);
        txtView31.setText(web.get(position).getNutrition().getNutrition_Protein_2());

        TextView txtView32 = this.context.findViewById(R.id.textView32);
        txtView32.setText(String.valueOf(web.get(position).getNutrition().getReferenz_quantity_protein()));




        TextView txtView34 = this.context.findViewById(R.id.textView34);
        txtView34.setText(web.get(position).getNutrition().getNutrition_Salt());

        TextView txtView35 = this.context.findViewById(R.id.textView35);
        txtView35.setText(web.get(position).getNutrition().getNutrition_Salt_2());

        TextView txtView36 = this.context.findViewById(R.id.textView36);
        txtView36.setText(String.valueOf(web.get(position).getNutrition().getReferenz_quantity_salt()));



        // Image-Brand
        ImageView imageViewBrand = this.context.findViewById(R.id.imgViewBrand);
        this.imageLoaderBrand.DisplayImage(web.get(position).getBrandImagePath(), imageViewBrand);

        TextView txtBrandViewName = this.context.findViewById(R.id.txtBrandViewName);
        txtBrandViewName.setText(web.get(position).getBrandName());

        TextView txtBrandViewDescription = this.context.findViewById(R.id.txtBrandViewDescription);
        txtBrandViewDescription.setText(web.get(position).getBrandDescription());



        // Image-Company
        ImageView imageViewCompany = this.context.findViewById(R.id.imgViewCompany);
        this.imageLoaderCompany.DisplayImage(web.get(position).getCompanyImagePath(), imageViewCompany);

        TextView txtCompanyViewName = this.context.findViewById(R.id.txtCompanyViewName);
        txtCompanyViewName.setText(web.get(position).getCompanyName());

        TextView txtCompanyViewDescription = this.context.findViewById(R.id.txtCompanyViewDescription);
        txtCompanyViewDescription.setText(web.get(position).getCompanyDescription());








        /*final CustomListArticleSubCompany adapterSubCompany = new CustomListArticleSubCompany(this.context, web.get(position).getSubcompany());
        ListView subCompany = (ListView) this.context.findViewById(R.id.listViewSubCompany);
        subCompany.setAdapter(adapterSubCompany);*/


        final CustomListArticleCertificate adapterZertificate = new CustomListArticleCertificate(this.context, web.get(position).getZertifikate());
        ListView zertificate = this.context.findViewById(R.id.listViewZertificate);
        zertificate.setAdapter(adapterZertificate);

        final CustomListArticleMake adapterMaking = new CustomListArticleMake(this.context, web.get(position).getMaking());
        ListView making = this.context.findViewById(R.id.listViewMaking);
        making.setAdapter(adapterMaking);

        final CustomListArticleStore adapterStore = new CustomListArticleStore(this.context, web.get(position).getStore());
        ListView store = this.context.findViewById(R.id.listViewStore);
        store.setAdapter(adapterStore);

        final CustomListArticleRecycling adapterRecycling = new CustomListArticleRecycling(this.context, web.get(position).getRecycling());
        ListView recycling = this.context.findViewById(R.id.listViewRecycling);
        recycling.setAdapter(adapterRecycling);


        // Ingredient
        /*TextView txtIngredient = (TextView) rowView.findViewById(R.id.txtDescriptionViewArticle);
        txtIngredient.setText(web.get(position).getComponentsName());

        TextView txtIngredient = (TextView) rowView.findViewById(R.id.txtDescriptionViewArticle);
        txtIngredient.setText(web.get(position).getComponentsName());*/




        // Assume thisActivity is the current activity
        /*int permissionCheckRead = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);*/

        // Assume thisActivity is the current activity
        /*int permissionCheckWrite = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        Log.d("permissionCheck- Write", Integer.toString(permissionCheckRead));
        Log.d("permissionCheck- Read", Integer.toString(permissionCheckRead));*/


        //ImageView imageView = (ImageView) rowView.findViewById(R.id.imgView);
        //txtTitle.setText(web.get(position));

        //imageView.setImageResource(imageId[position]);

    }
}