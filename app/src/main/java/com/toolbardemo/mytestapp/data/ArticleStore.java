package com.toolbardemo.mytestapp.data;

/**
 *  Class to save the Information
 *  where is the Storage of the Article
 */
public class ArticleStore {

    public enum Store {FRIDGE, COOLER, PANTRY}

    private int storeId;
    private boolean storageLevel_3 = false;
    private boolean storageLevel_2 = false;
    private boolean storageLevel_1 = false;
    private boolean storageLevelVegDrawer = false;

    private boolean storageLevelDoorLevel_3 = false;
    private boolean storageLevelDoorLevel_2 = false;
    private boolean storageLevelDoorLevel_1 = false;

    private String storageLevelTemp = "0";

    public ArticleStore(){
        // leerer Konstruktor
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public boolean isStorageLevel_3() {
        return storageLevel_3;
    }

    public void setStorageLevel_3(boolean storageLevel_3) {
        this.storageLevel_3 = storageLevel_3;
    }

    public boolean isStorageLevel_2() {
        return storageLevel_2;
    }

    public void setStorageLevel_2(boolean storageLevel_2) {
        this.storageLevel_2 = storageLevel_2;
    }

    public boolean isStorageLevel_1() {
        return storageLevel_1;
    }

    public void setStorageLevel_1(boolean storageLevel_1) {
        this.storageLevel_1 = storageLevel_1;
    }

    public boolean isStorageLevelVegDrawer() {
        return storageLevelVegDrawer;
    }

    public void setStorageLevelVegDrawer(boolean storageLevelVegDrawer) {
        this.storageLevelVegDrawer = storageLevelVegDrawer;
    }

    public boolean isStorageLevelDoorLevel_3() {
        return storageLevelDoorLevel_3;
    }

    public void setStorageLevelDoorLevel_3(boolean storageLevelDoorLevel_3) {
        this.storageLevelDoorLevel_3 = storageLevelDoorLevel_3;
    }

    public boolean isStorageLevelDoorLevel_2() {
        return storageLevelDoorLevel_2;
    }

    public void setStorageLevelDoorLevel_2(boolean storageLevelDoorLevel_2) {
        this.storageLevelDoorLevel_2 = storageLevelDoorLevel_2;
    }

    public boolean isStorageLevelDoorLevel_1() {
        return storageLevelDoorLevel_1;
    }

    public void setStorageLevelDoorLevel_1(boolean storageLevelDoorLevel_1) {
        this.storageLevelDoorLevel_1 = storageLevelDoorLevel_1;
    }

    public String isStorageLevelTemp() {
        return storageLevelTemp;
    }

    public void setStorageLevelTemp(String storageLevelTemp) {
        this.storageLevelTemp = storageLevelTemp;
    }

    public void setAllToFalse(){
        this.storageLevel_3 = false;
        this.storageLevel_2 = false;
        this.storageLevel_1 = false;
        this.storageLevelVegDrawer = false;
        this.storageLevelDoorLevel_3 = false;
        this.storageLevelDoorLevel_2 = false;
        this.storageLevelDoorLevel_1 = false;
    }

}
