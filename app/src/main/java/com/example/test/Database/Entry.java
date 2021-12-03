package com.example.test.Database;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.DateTimeException;
import java.util.Date;

@Entity(tableName = "Entry")
public class Entry {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "photoID")
    public int photoID;

    @ColumnInfo(name = "path")
    public String path;

    @ColumnInfo(name = "photo_name")
    public String photoName;

    @ColumnInfo(name = "species_name")
    public String speciesName;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "probability")
    public Double probability;

    //@ColumnInfo(name = "created_on")
    //public Date createdOn;

    public Entry(String path, String photoName){
        this.path = path;
        this.photoName = photoName;
    }


}
