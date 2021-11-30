package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.test.Database.AppDatabase;
import com.example.test.Database.EntryDao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.json.*;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//With help from Analytics Vidha via https://medium.com/analytics-vidhya/how-to-make-client-android-application-with-flask-for-server-side-8b1d5c55446e


public class SendActivity extends AppCompatActivity {

    private ImageView processingImage;
    private Bitmap image;
    private int photoID;
    private String url;

    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        db = AppDatabase.getInstance(SendActivity.this);

        processingImage = (ImageView)findViewById(R.id.processingImage);

        //url = "http://localhost:5005/predict";
        url = "http://192.168.0.4:5005/predict";

        Intent i = this.getIntent();
        image = (Bitmap)i.getParcelableExtra("bitmap");
        photoID = i.getIntExtra("photoID", -1);

        processingImage.setImageBitmap(image);
        postRequest();
    }

    private RequestBody buildRequestBody(Bitmap bitmap) {
        // Convert bitmap to a jpeg and then into a byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] jpegByteArray = stream.toByteArray();

        //convert array of bytes into file
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("RequestJpegFile.jpg");
            fileOutputStream.write(jpegByteArray);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Load file and create request
        File file = new File("RequestJpegFile.jpg");

        MediaType mediaTypeJpeg = MediaType.parse("image/*jpeg");
        //MediaType mediaTypeJpeg = MediaType.parse("image/jpeg");
        MultipartBody.Builder multiBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multiBuilder.addFormDataPart("image", "request.jpeg", RequestBody.create(mediaTypeJpeg, jpegByteArray));
        //multiBuilder.addFormDataPart("image", "request.jpeg", RequestBody.create(mediaTypeJpeg, file));
        RequestBody request = multiBuilder.build();
        return request;
    }

    private void postRequest(){
        RequestBody requestBody = buildRequestBody(image);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).post(requestBody).build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SendActivity.this, "Something went wrong:" + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        call.cancel();
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            String jsonResponse = response.body().string();

                            // {"predictions":
                            //	[
                            //		{"probability":0.003925135359168053,"species":"Eratigena_duellica"},
                            //		{"probability":0.027516232803463936,"species":"Loxosceles_reclusa"},
                            //		{"probability":0.7798723578453064,"species":"Latrodectus_geometricus"},
                            //		{"probability":0.07223397493362427,"species":"Latrodectus_mactans"},
                            //		{"probability":0.11645231395959854,"species":"Parasteatoda_tepidariorum"}
                            //	],
                            //"success":true}

                            // JSON assistance from https://stackoverflow.com/questions/2591098/how-to-parse-json-in-java
                            JSONObject results = new JSONObject(jsonResponse);
                            Boolean spider = results.getBoolean(    "success");
                            String speciesName = "Species unrecognizable/No spider in photo";
                            Double probability = 0.0;
                            JSONObject classInstance;
                            if (spider) {
                                JSONArray predictions = results.getJSONArray("predictions");
                                for(int i = 0; i < predictions.length(); i++){
                                    classInstance = predictions.getJSONObject(i);
                                    if(classInstance.getDouble("probability") > probability){
                                        probability = classInstance.getDouble("probability");
                                        speciesName = classInstance.getString("species");
                                    }
                                }
                            }
                            //Update the database entry for this photo
                            EntryDao entryDao = db.entryDao();
                            entryDao.updateSpeciesName(speciesName, photoID);
                            entryDao.updateName("photo" + Integer.toString(photoID), photoID);

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                        Intent displayIntent = new Intent(SendActivity.this, DisplayActivity.class);
                        displayIntent.putExtra("photoID", photoID);
                        startActivity(displayIntent);
                    }
                });
            }
        });
    }
}