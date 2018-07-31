package com.example.raju.demoBlog.data.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "single_item",
        primaryKeys = "id",
        foreignKeys = @ForeignKey(
                entity = Item.class,
                parentColumns = "item_id",
                childColumns = "id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
)
public class SingleItem {

    private long id;

    @SerializedName("content")
    @Expose
    private String content;

    /**
     * For Room
     */
    public SingleItem(long id, String content) {
        this.id = id;
        this.content = content;
    }

    /**
     * For inserting to Database
     */
    @Ignore
    public SingleItem(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
