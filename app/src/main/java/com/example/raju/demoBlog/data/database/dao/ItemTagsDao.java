package com.example.raju.demoBlog.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.raju.demoBlog.data.database.model.ItemTagRelation;

import java.util.List;

@Dao
public interface ItemTagsDao {

    @Query("SELECT tag_name FROM item_tag INNER JOIN tag ON tag_id = id AND item_id = :itemId")
    List<String> fetchTagNameById(long itemId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insetItemTagRelation(ItemTagRelation itemTagRelation);

}
