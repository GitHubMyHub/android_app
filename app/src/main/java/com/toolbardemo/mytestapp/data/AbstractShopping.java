package com.toolbardemo.mytestapp.data;

/**
 * Created by V-Windows on 19.02.2018.
 * Common interface for Favorites, helps share code between RTDB and Firestore examples.
 */

public abstract class AbstractShopping {

    public abstract String getShoppingListsName();

    public abstract String getUserId();

    @Override
    public abstract int hashCode();

}
