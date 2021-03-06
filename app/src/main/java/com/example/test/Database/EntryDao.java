package com.example.test.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EntryDao {

    @Query("SELECT * FROM entry")
        //LiveData<Entry> getAll();
    List<Entry> getAll();

    @Query("SELECT *" +
            "   FROM Entry E" +
            "   WHERE E.photoID = (:photoID)")
    Entry getEntry(int photoID);

    @Query("UPDATE entry SET photo_name = (:name) WHERE photoID = (:photoID)")
    void updateName(String name, int photoID);

    @Query("UPDATE entry SET species_name = (:speciesName) WHERE photoID = (:photoID)")
    void updateSpeciesName(String speciesName, int photoID);

    @Query("UPDATE entry SET description = (:description) WHERE photoID = (:photoID)")
    void updateDescription(String description, int photoID);

    @Query("UPDATE entry SET probability = (:probability) WHERE photoID = (:photoID)")
    void updateProbability(Double probability, int photoID);

    @Insert
    void insert(Entry entry);

    @Delete
    void delete(Entry entry);
}
