package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.xmlpull.v1.XmlPullParser;

public class GalleryActivity extends AppCompatActivity {

    private Button galleryHomeButton;
    private Button galleryAboutButton;

    private LinearLayout feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        feed = (LinearLayout) findViewById(R.id.galleryLinearFeed);

        galleryHomeButton = (Button)findViewById(R.id.galleryHomeButton);
        galleryAboutButton = (Button)findViewById(R.id.galleryAboutButton);

        galleryHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(GalleryActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            }
        });
        galleryAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutIntent = new Intent(GalleryActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
            }
        });

        testAdd();
        int f = 12;
    }

    public void updateGallery(){
        //recreate each entry based on the database

    }

    public void testAdd(){
        //XmlPullParser parser = getResources().getXml(R.layout.gallery_entry);
        //ConstraintLayout newAddition = new ConstraintLayout(GalleryActivity.this, );
        ConstraintLayout newAddition = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.gallery_entry, null);
        Button entry = (Button)newAddition.getViewById(R.id.gallerySpeciesButton);
        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEntry(v);
            }
        });
        entry.setText("First Species");
        feed.addView(newAddition);

        newAddition = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.gallery_entry, null);
        entry = (Button)newAddition.getViewById(R.id.gallerySpeciesButton);
        entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayEntry(v);
            }
        });
        entry.setText("Second Species");
        feed.addView(newAddition);
    }

    private void displayEntry(View v){
        Button b = (Button)v;
        Intent displayIntent = new Intent(GalleryActivity.this, DisplayActivity.class);
        displayIntent.putExtra("SpeciesName", b.getText());
        startActivity(displayIntent);
    }
}