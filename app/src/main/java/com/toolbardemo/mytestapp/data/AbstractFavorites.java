package com.toolbardemo.mytestapp.data;

/**
 * Created by V-Windows on 19.02.2018.
 * Common interface for Favorites, helps share code between RTDB and Firestore examples.
 */

public abstract class AbstractFavorites {

    public abstract int getArticleId();

    public abstract String getArticleName();

    public abstract String getUserKey();

    public abstract String getPicturePath();

    @Override
    public abstract int hashCode();

}
