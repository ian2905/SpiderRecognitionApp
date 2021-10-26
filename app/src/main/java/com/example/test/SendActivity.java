package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

public class SendActivity extends AppCompatActivity {

    ImageView processingImage;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        processingImage = (ImageView)findViewById(R.id.processingImage);

        Intent i = this.getIntent();
        image = (Bitmap) i.getParcelableExtra("bitmap");

        processingImage.setImageBitmap(image);

    }
}