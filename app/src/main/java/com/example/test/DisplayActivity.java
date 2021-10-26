package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DisplayActivity extends AppCompatActivity {

    private Button displayHomeButton;
    private Button displayGalleryButton;
    private Button displayAboutButton;

    private TextView displayPhotoID;
    private TextView displaySpeciesName;
    private TextView displaySpeciesDescription;

    String photoID;
    String speciesName;
    String speciesDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        displayHomeButton = (Button)findViewById(R.id.displayHomeButton);
        displayGalleryButton = (Button)findViewById(R.id.displayGalleryButton);
        displayAboutButton = (Button)findViewById(R.id.displayAboutButton);

        displayPhotoID = (TextView)findViewById(R.id.displayPhotoID);
        displaySpeciesName = (TextView)findViewById(R.id.displaySpeciesName);
        displaySpeciesDescription = (TextView)findViewById(R.id.displaySpeciesDescription);


        displayHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(DisplayActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            }
        });
        displayGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(DisplayActivity.this, GalleryActivity.class);
                startActivity(galleryIntent);
            }
        });
        displayAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutIntent = new Intent(DisplayActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
            }
        });

        Intent i = this.getIntent();

        speciesName = i.getStringExtra("SpeciesName");
        displaySpeciesName.setText(speciesName);




    }




}