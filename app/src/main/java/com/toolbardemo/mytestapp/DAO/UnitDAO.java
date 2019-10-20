package com.toolbardemo.mytestapp.DAO;

import com.toolbardemo.mytestapp.data.Unit;
import java.util.ArrayList;

/**
 * Created by V-Windows on 19.02.2018.
 *
 */

public class UnitDAO {

    private ArrayList<Unit> units = new ArrayList<>();

    public UnitDAO(){
        // leerer Kontsruktor
        Unit unit1 = new Unit();
        unit1.setUnitId(1);
        unit1.setUnitName("g");
        units.add(unit1);

        Unit unit2 = new Unit();
        unit2.setUnitId(2);
        unit2.setUnitName("kg");
        units.add(unit2);
    }

    public ArrayList<Unit> getUnits(){
        return units;
    }

    public String getUnitIndexToName(int position){
        return units.get(position).getUnitName();
    }


}
