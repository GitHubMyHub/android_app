package com.toolbardemo.mytestapp.data;

import java.util.ArrayList;

/**
 * Created by V-Windows on 12.04.2017.
 *
 */

public class Article {

    // Base Infos
    private int id;
    private String name;
    private String barcode;
    private ArrayList<String> articleImages = new ArrayList<>();
    private String description;
    private String qrCode;
    private String preparation;
    private String traces;
    private String noEntry;
    private String noAuthentification;

    // Nutrition
    private Nutrition nutrition = new Nutrition();

    // Brand
    private String brandImagePath;
    private String brandName;
    private String brandDescription;

    // Company
    private String company_image_path;
    private String company_name;
    private String company_description;


    private ArrayList<Certificate> zertifikate = new ArrayList<>();
    private RatingLight rating = new RatingLight();
    private ArrayList<Making> making = new ArrayList<>();
    private ArrayList<Store> store = new ArrayList<>();
    private ArticleStore articleStore;   // Lagerung des Artikels
    private ArrayList<Recycling> recycling = new ArrayList<>();
    private ArrayList<Ingredients> ingredients = new ArrayList<>();


    public Article(){

        // leerer Konstruktor
    }

    // Base Infos
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getArticleImage(int position) {
        return String.valueOf(articleImages.get(position));
    }

    public ArrayList<String> getArticleImages() {
        return articleImages;
    }

    public void setArticleImage(String image) {
        this.articleImages.add(image);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public String getTraces() {
        return traces;
    }

    public void setTraces(String traces) {
        this.traces = traces;
    }





    public String getNoEntry() {
        return noEntry;
    }

    public void setNoEntry(String noEntry) {
        this.noEntry = noEntry;
    }

    public String getNoAuthentification() {
        return noAuthentification;
    }

    public void setNoAthentification(String noAuthentification) {
        this.noAuthentification = noAuthentification;
    }


    // Ingrediants
    public ArrayList<Ingredients> getIngredients(){
        return this.ingredients;
    }

    public void setIngredients(Ingredients ingredients){
        this.ingredients.add(ingredients);
    }

    // Nutrition
    public Nutrition getNutrition(){
        return this.nutrition;
    }

    public void setNutrition(Nutrition nutrition){
        this.nutrition = nutrition;
    }


    // Brand
    public String getBrandImagePath() {
        return brandImagePath;
    }

    public void setBrandImagePath(String brandImagePath) {
        this.brandImagePath = brandImagePath;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandDescription() {
        return brandDescription;
    }

    public void setBrandDescription(String brandDescription) {
        this.brandDescription = brandDescription;
    }


    // Company
    public String getCompanyImagePath(){
        return this.company_image_path;
    }

    public void setCompanyImagePath(String company_image_path){
        this.company_image_path = company_image_path;
    }

    public String getCompanyName(){
        return this.company_name;
    }

    public void setCompanyName(String company_name){
        this.company_name = company_name;
    }

    public String getCompanyDescription(){
        return this.company_description;
    }

    public void setCompanyDescription(String company_description){
        this.company_description = company_description;
    }


    public ArrayList<Certificate> getZertifikate(){
        return this.zertifikate;
    }

    public void setZertificate(Certificate certificate){
        this.zertifikate.add(certificate);
    }

    public ArrayList<Making> getMaking(){
        return this.making;
    }

    public void setMaking(Making making){
        this.making.add(making);
    }

    public ArrayList<Store> getStore(){
        return this.store;
    }

    public void setStore(Store store){
        this.store.add(store);
    }

    public ArticleStore getArticleStore() {
        return articleStore;
    }

    public void setArticleStore(ArticleStore articleStore) {
        this.articleStore = articleStore;
    }

    public ArrayList<Recycling> getRecycling(){
        return this.recycling;
    }

    public void setRecycling(Recycling recycling){
        this.recycling.add(recycling);
    }

    public RatingLight getRating() {
        return this.rating;
    }

    public void setRating(RatingLight rating) {
        this.rating = rating;
    }



}
