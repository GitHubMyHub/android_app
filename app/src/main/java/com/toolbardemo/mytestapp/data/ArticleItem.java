package com.toolbardemo.mytestapp.data;

/**
 * Created by V-Windows on 27.02.2018.
 *
 */

public class ArticleItem {
    private String shoppingListsKey;    // eindeutig Liste-Key
    private String shoppingListKey;     // eindeutig Arikel-Key
    private String userId;              // eindeutig User-Key
    private int articleId;              // die eindeutige Artikel-ID
    private String shoppingListPicture; // eigenes Artikelbild
    private String shoppingListName;    // Artikelname
    private double shoppingListQuantity;// Artikelanzahl
    private int unitId;                 // Artikel-Einheit (Bsp: g,kg, Stk) etc
    private String shoppingListDurability;// Halbarkeitsdatum
    private int shoppingListBought;     // gekauft oder nicht gekauft
    private int placeId;                // 1->Shopping-List | 2->Shopping-Cart | 3->Stock | 4->History
    private ArticleStore store;         // Lagerung des Artikels

    public ArticleItem(){
        // leerer Konstruktor
    }

    public String getShoppingListsKey() {
        return shoppingListsKey;
    }

    public void setShoppingListsKey(String shoppingListsKey) {
        this.shoppingListsKey = shoppingListsKey;
    }

    public String getShoppingListKey() {
        return shoppingListKey;
    }

    public void setShoppingListKey(String shoppingListKey) {
        this.shoppingListKey = shoppingListKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public void setShoppingListPicture(String shoppingListPicture) {
        this.shoppingListPicture = shoppingListPicture;
    }

    public String getShoppingListPicture() {
        return shoppingListPicture;
    }

    public String getShoppingListName() {
        return shoppingListName;
    }

    public void setShoppingListName(String shoppingListName) {
        this.shoppingListName = shoppingListName;
    }

    public double getShoppingListQuantity() {
        return shoppingListQuantity;
    }

    public void setShoppingListQuantity(double shoppingListQuantity) {
        this.shoppingListQuantity = shoppingListQuantity;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getShoppingListDurability() {
        return shoppingListDurability;
    }

    public void setShoppingListDurability(String shoppingListDurability) {
        this.shoppingListDurability = shoppingListDurability;
    }

    public int getShoppingListBought() {
        return shoppingListBought;
    }

    public void setShoppingListBought(int shoppingListBought) {
        this.shoppingListBought = shoppingListBought;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public ArticleStore getStore() {
        return store;
    }

    public int getSelectedStorePlace(){

        if(store.isStorageLevel_1()){
            return 2;
        }else if(store.isStorageLevel_2()) {
            return 1;
        }else if(store.isStorageLevel_3()) {
            return 0;
        }else if(store.isStorageLevelVegDrawer()) {
            return 3;
        }else if(store.isStorageLevelDoorLevel_1()) {
            return 6;
        }else if(store.isStorageLevelDoorLevel_2()) {
            return 5;
        }else{
            return 4;
        }

    }

    public void setStore(ArticleStore store) {
        this.store = store;
    }

    public String toString(){
        return shoppingListPicture;
    }

}
