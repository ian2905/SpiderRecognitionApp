package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity {

    private Button aboutHomeButton;
    private Button aboutGalleryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        aboutHomeButton = (Button)findViewById(R.id.aboutHomeButton);
        aboutGalleryButton = (Button)findViewById(R.id.aboutGalleryButton);

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
    }
}