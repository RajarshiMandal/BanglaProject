package com.example.raju.demoBlog.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.raju.demoBlog.data.database.dao.ItemDao;
import com.example.raju.demoBlog.data.database.dao.ItemTagsDao;
import com.example.raju.demoBlog.data.database.dao.NextPageTokenDao;
import com.example.raju.demoBlog.data.database.dao.SingleItemDao;
import com.example.raju.demoBlog.data.database.dao.TagDao;
import com.example.raju.demoBlog.data.database.model.Item;
import com.example.raju.demoBlog.data.database.model.ItemTagRelation;
import com.example.raju.demoBlog.data.database.model.NextPageToken;
import com.example.raju.demoBlog.data.database.model.SingleItem;
import com.example.raju.demoBlog.data.database.model.Tag;

@Database(entities = {
        Item.class,
        NextPageToken.class,
        Tag.class,
        ItemTagRelation.class,
        SingleItem.class
},
        version = 1,
        exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "blogger_api.db";
    private static final Object LOCK = new Object();
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null)
                    sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
            }
        }
        return sInstance;
    }

    public abstract ItemDao itemDao();

    public abstract NextPageTokenDao nextPageTokenDao();

    public abstract TagDao tagDao();

    public abstract ItemTagsDao itemTagsDao();

    public abstract SingleItemDao singleItemDao();
}