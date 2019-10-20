package com.toolbardemo.mytestapp.adapter;

import java.io.Serializable;

/**
 * Created by V-Windows on 10.03.2018.
 *
 */

public class SerializableStockAdapter implements Serializable {
    private transient CustomRecyclerStock adapter;

    public SerializableStockAdapter(CustomRecyclerStock adapter){
        this.adapter = adapter;
    }

    public CustomRecyclerStock getAdapter() {
        return adapter;
    }

}

