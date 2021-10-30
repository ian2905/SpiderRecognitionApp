package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.test.Database.AppDatabase;
import com.example.test.Database.Entry;
import com.example.test.Database.EntryDao;

import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    private Button galleryHomeButton;
    private Button galleryAboutButton;

    private LinearLayout feed;

    AppDatabase db;

    List<Entry> entries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        db = AppDatabase.getInstance(GalleryActivity.this);

        feed = (LinearLayout) findViewById(R.id.galleryLinearFeed);

        galleryHomeButton = (Button)findViewById(R.id.galleryHomeButton);
        galleryAboutButton = (Button)findViewById(R.id.galleryAboutButton);

        galleryHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(GalleryActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                //finish();
            }
        });
        galleryAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutIntent = new Intent(GalleryActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                //finish();
            }
        });

        /*
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                EntryDao entryDao = db.entryDao();
                entries = entryDao.getAll();
            }
        });
         */

        updateFeed();

        testAdd();
    }

    public void updateFeed(){
        EntryDao entryDao = db.entryDao();
        entries = entryDao.getAll();
        if(entries != null){
            for (Entry e : entries){
                createEntry(e);
            }
        }
    }

    public void testAdd(){
        //XmlPullParser parser = getResources().getXml(R.layout.gallery_entry);
        //ConstraintLayout newAddition = new ConstraintLayout(GalleryActivity.this, );
        ConstraintLayout newAddition = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.gallery_entry, null);
        Button entry = (Button)newAddition.getViewById(R.id.galleryNameButton);
        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //displayEntry(1);
            }
        });
        entry.setText("First Photo");
        feed.addView(newAddition);

        newAddition = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.gallery_entry, null);
        entry = (Button)newAddition.getViewById(R.id.galleryNameButton);
        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //displayEntry(1);
            }
        });
        entry.setText("Second Photo");
        feed.addView(newAddition);

        newAddition = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.gallery_entry, null);
        entry = (Button)newAddition.getViewById(R.id.galleryNameButton);
        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //displayEntry(1);
            }
        });
        entry.setText("Third Photo");
        feed.addView(newAddition);
    }

    //onCLick method to open a display activity
    private void displayEntry(Entry e){
        Intent displayIntent = new Intent(GalleryActivity.this, DisplayActivity.class);
        displayIntent.putExtra("photoID", e.photoID);
        //displayIntent.putExtra("entry", (Parcelable) e); //only works if I make the entry class parcelable, which may fuck with Room requirements
        startActivity(displayIntent);
    }

    //Creates an entry in the gallery
    private void createEntry(Entry e){
        //get the template layout
        ConstraintLayout newAddition = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.gallery_entry, null);

        //create the button
        Button galleryNameButton = (Button)newAddition.getViewById(R.id.galleryNameButton);
        galleryNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEntry(e);
            }
        });
        galleryNameButton.setText(e.photoName);

        //create the image view
        ImageView galleryImage = (ImageView)newAddition.getViewById(R.id.galleryImage);
        galleryImage.setImageBitmap(BitmapFactory.decodeFile(e.path));

        TextView galleryIndex = (TextView)newAddition.getViewById(R.id.galleryIndex);
        galleryIndex.setText(Integer.toString(entries.indexOf(e)+1));

        //insert the entry
        feed.addView(newAddition);
    }
}