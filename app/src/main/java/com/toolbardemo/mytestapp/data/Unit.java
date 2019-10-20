package com.toolbardemo.mytestapp.data;

/**
 * Created by V-Windows on 05.03.2018.
 *
 */

public class Unit {
    private int unitId;
    private String unitName;

    public Unit(){
        // leerer Konstruktor
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
