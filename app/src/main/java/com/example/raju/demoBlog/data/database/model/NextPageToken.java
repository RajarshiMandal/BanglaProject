package com.example.raju.demoBlog.data.database.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "next_page_token")
public class NextPageToken {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "page_token")
    private String nextPageToken;

    public NextPageToken(int id, String nextPageToken) {
        this.id = id;
        this.nextPageToken = nextPageToken;
    }

    @Ignore
    public NextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
}
