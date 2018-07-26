package com.example.raju.demoBlog.data.database.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "tag",
        indices = @Index(value = "tag_name", unique = true)
)
public class Tag {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "tag_name")
    private String tagName;

    public Tag(int id, @NonNull String tagName) {
        this.id = id;
        this.tagName = tagName;
    }

    @Ignore
    public Tag(@NonNull String tagName) {
        this.tagName = tagName;
    }

    /* Getters and Setters */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getTagName() {
        return tagName;
    }

    public void setTagName(@NonNull String tagName) {
        this.tagName = tagName;
    }
}