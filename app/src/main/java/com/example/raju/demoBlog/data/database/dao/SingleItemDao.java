package com.example.raju.demoBlog.data.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.raju.demoBlog.data.database.model.SingleItem;

@Dao
public interface SingleItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSingleItem(SingleItem singleItem);

    @Query("select * from single_item where id = :item_id")
    LiveData<SingleItem> fetchSingleItem(long item_id);

    @Query("select id from single_item where id = :item_id")
    long fetchSingleItemId(long item_id);

}
