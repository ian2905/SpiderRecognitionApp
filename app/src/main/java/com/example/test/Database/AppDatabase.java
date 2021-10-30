package com.example.test.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

//With help from Android documentation and https://medium.com/mindorks/using-room-database-android-jetpack-675a89a0e942
@Database(entities = {Entry.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "spider_recognition";
    private static AppDatabase db;

    public static synchronized AppDatabase getInstance(Context context){
        if(db == null){
            db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
            //db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return db;
    }

    public abstract EntryDao entryDao();
}
