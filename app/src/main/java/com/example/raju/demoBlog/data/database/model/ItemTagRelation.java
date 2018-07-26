package com.example.raju.demoBlog.data.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

@Entity(tableName = "item_tag",
        primaryKeys = {"item_id", "tag_id"},
        foreignKeys = {
                @ForeignKey(entity = Item.class,
                        parentColumns = "item_id",
                        childColumns = "item_id",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Tag.class,
                        parentColumns = "id",
                        childColumns = "tag_id",
                        onDelete = ForeignKey.CASCADE,
                        onUpdate = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = "tag_id"),
                @Index(value = "item_id")
        })
public class ItemTagRelation {

    private long item_id;

    private int tag_id;

    public ItemTagRelation(long item_id, int tag_id) {
        this.item_id = item_id;
        this.tag_id = tag_id;
    }

    public long getItem_id() {
        return item_id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }
}