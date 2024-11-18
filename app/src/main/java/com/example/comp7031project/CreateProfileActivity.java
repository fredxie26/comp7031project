package com.example.comp7031project;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreateProfileActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText editFirstName, editLastName, editAddress;
    private ImageView imageProfilePreview;
    private String photoPath = null;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);


        databaseHelper = new DatabaseHelper(this);

        editFirstName = findViewById(R.id.edit_first_name);
        editLastName = findViewById(R.id.edit_last_name);
        editAddress = findViewById(R.id.edit_address);
        imageProfilePreview = findViewById(R.id.image_profile_preview);
        Button buttonTakePhoto = findViewById(R.id.button_take_photo);
        Button buttonSave = findViewById(R.id.button_save);
        Button buttonCancel = findViewById(R.id.button_cancel);

        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);  // Call super

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Get the image data and display it in the ImageView
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageProfilePreview.setImageBitmap(imageBitmap);

            // Save the image to internal storage
            try {
                File file = new File(getFilesDir(), "profile_" + System.currentTimeMillis() + ".jpg");
                FileOutputStream fos = new FileOutputStream(file);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
                photoPath = file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void saveProfile() {
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String address = editAddress.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseHelper.addUser(firstName, lastName, address, photoPath);
        Toast.makeText(this, "Profile saved", Toast.LENGTH_SHORT).show();
        finish(); // Return to MainActivity
    }
}

