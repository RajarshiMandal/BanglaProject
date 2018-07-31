package com.example.raju.demoBlog.data.database.dao;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.raju.demoBlog.data.database.model.Item;

import java.util.List;

@Dao
public interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertItems(List<Item> items);

    @Query("SELECT id, item_id, title FROM item ORDER BY published_at DESC")
    DataSource.Factory<Integer, Item> fetchAllItemsOrdered();

    @Query("SELECT * FROM item")
    DataSource.Factory<Integer, Item> fetchAllItems();

    @Query("SELECT COUNT(item_id) FROM item")
    int fetchCount();

}
