package com.example.test;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    public static final int REQUEST_IMAGE_CAPTURE = 101;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    String currentPhotoPath;

    Bitmap bitmap = null;

    //main screen
    private Button takePhotoButton;
    private Button uploadPhotoButton;
    private Button homeGalleryButton;
    private Button homeAboutButton;

    //verify image screen
    private ImageView checkImage;
    private TextView checkImageText;
    private Button yesProcessButton;
    private Button noProcessButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //init home page
        takePhotoButton = (Button)findViewById(R.id.takePhotoButton);
        uploadPhotoButton = (Button)findViewById(R.id.uploadPhotoButton);
        homeGalleryButton = (Button)findViewById(R.id.homeGalleryButton);
        homeAboutButton = (Button)findViewById(R.id.homeAboutButton);

        //init verify page
        checkImage = (ImageView)findViewById(R.id.checkImage);
        checkImageText = (TextView)findViewById(R.id.checkImageText);
        yesProcessButton = (Button)findViewById(R.id.yesProcessButton);
        noProcessButton = (Button)findViewById(R.id.noProcessButton);

        //Home page listeners
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosephoto();
            }
        });
        homeGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(HomeActivity.this, GalleryActivity.class);
                startActivity(galleryIntent);
            }
        });
        homeAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent aboutIntent = new Intent(HomeActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
            }
        });

        //verify page listeners
        yesProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(HomeActivity.this, SendActivity.class);
                sendIntent.putExtra("bitmap", bitmap);
                startActivity(sendIntent);
                disableImageChoice();
                enableHome();
            }
        });
        noProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = null;
                disableImageChoice();
                enableHome();
            }
        });

        disableImageChoice();
        enableHome();


        //initilize first gallery_entry
        //setContentView(R.layout.gallery_entry);
    }

    //onClick method for taking a photo
    private void takePhoto(){
        if(checkAndRequestPermissions(HomeActivity.this)){
            Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, 0);
        }
    }

    //onClick method for selecting a photo
    private void choosephoto(){
        if(checkAndRequestPermissions(HomeActivity.this)){
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto , 1);
        }
    }

    // function to check permission
    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    // Handled permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(HomeActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(HomeActivity.this);
                }
                break;
        }
    }

    // function to let's the user to choose image from camera or gallery
    private void chooseImage(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    //Activity result for the photo input
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {

                        bitmap = (Bitmap) data.getExtras().get("data");
                        checkImage.setImageBitmap(bitmap);

                        //flip the page to the verification choice
                        disableHome();
                        enableImageChoice();
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);

                                bitmap = (Bitmap) BitmapFactory.decodeFile(picturePath);
                                checkImage.setImageBitmap(bitmap);

                                //flip the page to the verification choice
                                disableHome();
                                enableImageChoice();

                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void disableHome(){
        takePhotoButton.setEnabled(false);
        uploadPhotoButton.setEnabled(false);

        takePhotoButton.setVisibility(View.INVISIBLE);
        uploadPhotoButton.setVisibility(View.INVISIBLE);
    }

    private void enableHome(){
        takePhotoButton.setEnabled(true);
        uploadPhotoButton.setEnabled(true);

        takePhotoButton.setVisibility(View.VISIBLE);
        uploadPhotoButton.setVisibility(View.VISIBLE);
    }

    private void disableImageChoice(){
        checkImage.setEnabled(false);
        checkImageText.setEnabled(false);
        yesProcessButton.setEnabled(false);
        noProcessButton.setEnabled(false);

        checkImage.setVisibility(View.INVISIBLE);
        checkImageText.setVisibility(View.INVISIBLE);
        yesProcessButton.setVisibility(View.INVISIBLE);
        noProcessButton.setVisibility(View.INVISIBLE);
    }

    private void enableImageChoice(){
        checkImage.setEnabled(true);
        checkImageText.setEnabled(true);
        yesProcessButton.setEnabled(true);
        noProcessButton.setEnabled(true);

        checkImage.setVisibility(View.VISIBLE);
        checkImageText.setVisibility(View.VISIBLE);
        yesProcessButton.setVisibility(View.VISIBLE);
        noProcessButton.setVisibility(View.VISIBLE);
    }
    /*
    //function to dymanically create gallery entries
    public void addView(Bitmap image, String species){

        // get a reference to the already created main layout
        //TODO: galleryEntry is coming out null
        ConstraintLayout galleryEntry = (ConstraintLayout) findViewById(R.id.galleryEntryLayout);



        Button testB = (Button) findViewById((R.id.takePhotoButton));


        // inflate (create) another copy of our custom layout
        LayoutInflater inflater = getLayoutInflater();
        View galleryEntryLayout = inflater.inflate(R.layout., galleryEntry, false);

        // make changes to our custom layout and its subviews
        //myLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        Button speciesButton = (Button) galleryEntryLayout.findViewById(R.id.gallerySpeciesButton);
        //TODO: change new layout to the name of the species.
        speciesButton.setText(species);

        ImageView galleryImage = (ImageView) galleryEntryLayout.findViewById(R.id.galleryImage);
        galleryImage.setImageBitmap(image);

        // add our custom layout to the main layout
        galleryEntry.addView(galleryEntryLayout);

    }

     */
}