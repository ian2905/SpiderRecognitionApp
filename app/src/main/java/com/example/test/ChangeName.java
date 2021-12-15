package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.test.Database.AppDatabase;
import com.example.test.Database.Entry;
import com.example.test.Database.EntryDao;

public class ChangeName extends AppCompatActivity {

    AppDatabase db;

    int photoID;

    Button changeButton;
    Button cancelButton;
    EditText changeNameEditText;
    ConstraintLayout changeNameLayout;

    Entry entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        db = AppDatabase.getInstance(ChangeName.this);

        changeButton = (Button)findViewById(R.id.changeButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);
        changeNameEditText = (EditText)findViewById(R.id.changeNameEditText);
        changeNameLayout = (ConstraintLayout)findViewById(R.id.changeNameLayout);

        Intent i = this.getIntent();
        photoID = i.getIntExtra("photoID", photoID);

        EntryDao entryDao = db.entryDao();
        entry = entryDao.getEntry(photoID);


        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entryDao.updateName(changeNameEditText.getText().toString(), entry.photoID);

                Intent displayIntent = new Intent(ChangeName.this, DisplayActivity.class);
                displayIntent.putExtra("photoID", entry.photoID);
                startActivity(displayIntent);
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent displayIntent = new Intent(ChangeName.this, DisplayActivity.class);
                displayIntent.putExtra("photoID", entry.photoID);
                startActivity(displayIntent);
                finish();
            }
        });
        changeNameEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNameEditText.setText("");
            }
        });
        changeNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // From Reto Meier via https://stackoverflow.com/questions/1109022/how-do-you-close-hide-the-android-soft-keyboard-programmatically
                View view = ChangeName.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }
}