package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.test.Database.AppDatabase;
import com.example.test.Database.Entry;
import com.example.test.Database.EntryDao;

import java.util.List;

public class AboutActivity extends AppCompatActivity {

    private Button aboutHomeButton;
    private Button aboutGalleryButton;
    private Button aboutEmptyDatabase;

    AppDatabase db;
    List<Entry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        db = AppDatabase.getInstance(AboutActivity.this);

        // Get instances of views
        aboutHomeButton = (Button)findViewById(R.id.aboutHomeButton);
        aboutGalleryButton = (Button)findViewById(R.id.aboutGalleryButton);
        aboutEmptyDatabase = (Button)findViewById(R.id.aboutEmptyDatabase);

        // Set onClicks for views
        aboutHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(AboutActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        });
        aboutGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(AboutActivity.this, GalleryActivity.class);
                startActivity(galleryIntent);
                finish();
            }
        });
        aboutEmptyDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyDatabase();
            }
        });
    }

    //Removes all entries from the database
    private void emptyDatabase(){
        EntryDao entryDao = db.entryDao();
        entries = entryDao.getAll();
        if(entries != null){
            for (Entry e : entries){
                entryDao.delete(e);
            }
        }
    }
}