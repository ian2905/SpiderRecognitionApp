package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.Database.AppDatabase;
import com.example.test.Database.Entry;
import com.example.test.Database.EntryDao;

public class DisplayActivity extends AppCompatActivity {

    private Button displayHomeButton;
    private Button displayGalleryButton;
    private Button displayAboutButton;
    private Button displayDeleteButton;
    private Button displayChangeNameButton;

    private TextView displayPhotoName;
    private TextView displaySpeciesName;
    private TextView displaySpeciesDescription;
    private TextView displayProbability;
    private ImageView displayPhoto;

    Entry entry;
    int photoID;

    AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        db = AppDatabase.getInstance(DisplayActivity.this);

        displayHomeButton = (Button)findViewById(R.id.displayHomeButton);
        displayGalleryButton = (Button)findViewById(R.id.displayGalleryButton);
        displayAboutButton = (Button)findViewById(R.id.displayAboutButton);
        displayDeleteButton = (Button)findViewById(R.id.displayDeleteButton);
        displayChangeNameButton = (Button)findViewById(R.id.displayChangeNameButton);

        displayPhotoName = (TextView)findViewById(R.id.displayPhotoName);
        displaySpeciesName = (TextView)findViewById(R.id.displaySpeciesName);
        displaySpeciesDescription = (TextView)findViewById(R.id.displaySpeciesDescription);
        displayPhoto = (ImageView)findViewById(R.id.displayPhoto);
        displayProbability = (TextView)findViewById(R.id.displayProbability);


        displayHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(DisplayActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finish();
            }
        });
        displayGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(DisplayActivity.this, GalleryActivity.class);
                startActivity(galleryIntent);
                finish();
            }
        });
        displayAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutIntent = new Intent(DisplayActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                finish();
            }
        });
        displayDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        displayChangeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeName();
            }
        });

        //get the ID from the intent data
        Intent i = this.getIntent();
        photoID = i.getIntExtra("photoID", photoID);

        //pull the entry from the database
        EntryDao entryDao = db.entryDao();
        entry = entryDao.getEntry(photoID);


        displayPhotoName.setText(entry.photoName);
        if(entry.speciesName != null){
            displaySpeciesName.setText(entry.speciesName);
        }
        if(entry.description != null){
            displaySpeciesDescription.setText(entry.description);
        }
        if(entry.probability != null){
            displayProbability.setText(String.format("%.2f", entry.probability));
        }
        displayPhoto.setImageBitmap(BitmapFactory.decodeFile(entry.path));


        //photoName = i.getStringExtra("photoName");
        //displayPhotoID.setText(photoName);
    }

    private void delete(){
        EntryDao entryDao = db.entryDao();
        entryDao.delete(entry);

        Intent galleryIntent = new Intent(DisplayActivity.this, GalleryActivity.class);
        startActivity(galleryIntent);
    }

    private void changeName(){
        Intent changeNameIntent = new Intent(DisplayActivity.this, ChangeName.class);
        changeNameIntent.putExtra("photoID", entry.photoID);
        startActivity(changeNameIntent);
        finish();
    }


}