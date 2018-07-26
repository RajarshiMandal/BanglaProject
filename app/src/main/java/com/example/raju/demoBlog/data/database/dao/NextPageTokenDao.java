package com.example.raju.demoBlog.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.raju.demoBlog.data.database.model.NextPageToken;

@Dao
public interface NextPageTokenDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNextPageToken(NextPageToken nextPageToken);

    @Query("SELECT page_token FROM next_page_token ORDER BY id DESC LIMIT 1")
    String fetchDbPageToken();
}
