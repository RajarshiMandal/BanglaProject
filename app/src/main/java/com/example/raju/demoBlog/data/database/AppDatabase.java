package com.example.raju.demoBlog.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.nfc.Tag;

import com.example.raju.demoBlog.data.database.dao.ItemDao;
import com.example.raju.demoBlog.data.database.model.Item;

@Database(entities = {Item.class},
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
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        // TODO Delete this
//                        .allowMainThreadQueries()
                        .build();
            }
        }
        return sInstance;
    }


    public abstract ItemDao itemDao();

}