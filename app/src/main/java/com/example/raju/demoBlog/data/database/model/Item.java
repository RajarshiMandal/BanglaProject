package com.example.raju.demoBlog.data.database.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

@Entity(tableName = "item",
        indices = {
                @Index(value = "id", unique = true),
                @Index(value = "item_id")
        })
public class Item {

    /**
     * A variable that let RecyclerView know whether Items changed or not so that it can update the
     * list accordingly.
     */
    @Ignore
    public static DiffUtil.ItemCallback<Item> DIFF_CALLBACK = new DiffUtil.ItemCallback<Item>() {
        @Override
        public boolean areItemsTheSame(Item oldItem, Item newItem) {
            return oldItem.item_id == newItem.item_id;
        }

        @Override
        public boolean areContentsTheSame(Item oldItem, Item newItem) {
            return oldItem.updatedAt.equals(newItem.updatedAt);
        }
    };

    @PrimaryKey(autoGenerate = true)
    private long item_id;

    @SerializedName("id")
    @Expose
    private String id;

    @ColumnInfo(name = "published_at")
    @SerializedName("published")
    @Expose
    private Date publishedAt;

    @ColumnInfo(name = "updated_at")
    @SerializedName("updated")
    @Expose
    private Date updatedAt;

    @ColumnInfo(name = "self_link")
    @SerializedName("selfLink")
    @Expose
    private String selfLink;

    //    @SerializedName("content")
//    @Expose
//    private String content;
    @NonNull
    @SerializedName("title")
    @Expose
    private String title = "";

    @Ignore
    @SerializedName("labels")
    @Expose
    private List<String> tags;

    public long getItem_id() {
        return item_id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title.trim();
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Item{" +
                "item_id=" + item_id +
                ", title='" + title;
    }
}
