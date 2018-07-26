package com.example.raju.demoBlog.data;

import android.arch.paging.DataSource;
import android.nfc.Tag;

import com.example.raju.demoBlog.data.database.AppDatabase;
import com.example.raju.demoBlog.data.database.dao.ItemDao;
import com.example.raju.demoBlog.data.database.model.Item;

import java.util.List;

class DatabaseHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private final ItemDao mItemDao;

    DatabaseHelper(AppDatabase database) {
        mItemDao = database.itemDao();
    }

    /* Items methods */
    int fetchItemCount() {
        return mItemDao.fetchCount();
    }

    public DataSource.Factory<Integer, Item> fetchAllItems() {
        return mItemDao.fetchAllItems();
    }

    List<Long> insertAllItems(List<Item> itemList) {
        return mItemDao.insertItems(itemList);
    }
}
