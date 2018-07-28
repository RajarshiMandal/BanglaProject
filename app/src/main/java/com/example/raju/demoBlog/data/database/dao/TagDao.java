package com.example.raju.demoBlog.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.raju.demoBlog.data.database.model.Tag;

@Dao
public interface TagDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insetTagName(Tag tagName);

    @Query("SELECT id FROM tag WHERE tag_name = :labelName")
    int fetchIdByTagName(String labelName);

}