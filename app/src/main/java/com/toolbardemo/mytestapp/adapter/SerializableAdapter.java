package com.toolbardemo.mytestapp.adapter;

import java.io.Serializable;

/**
 * Created by V-Windows on 10.03.2018.
 *
 */

public class SerializableAdapter implements Serializable {
    private transient CustomRecyclerShoppingList adapter;

    public SerializableAdapter(CustomRecyclerShoppingList adapter){
        this.adapter = adapter;
    }

    public CustomRecyclerShoppingList getAdapter() {
        return adapter;
    }

}

