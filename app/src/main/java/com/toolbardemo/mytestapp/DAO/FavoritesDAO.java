package com.toolbardemo.mytestapp.DAO;

import com.toolbardemo.mytestapp.data.Favorites;
import java.util.ArrayList;

/**
 * Created by V-Windows on 19.02.2018.
 *
 */

public class FavoritesDAO {

    private ArrayList<Favorites> favorites = new ArrayList<>();

    public FavoritesDAO(){
        // leerer Kontsruktor
    }

    public ArrayList<Favorites> getFavorites(){
        return favorites;
    }


}
