package com.example.comp7031project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailViewActivity extends AppCompatActivity {

    private ImageView imageProfileDetail;
    private TextView textFirstNameDetail, textLastNameDetail, textAddressDetail, textStatusDetail;
    private Button buttonBack;
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_view);

        imageProfileDetail = findViewById(R.id.image_profile_detail);
        textFirstNameDetail = findViewById(R.id.text_first_name_detail);
        textLastNameDetail = findViewById(R.id.text_last_name_detail);
        textAddressDetail = findViewById(R.id.text_address_detail);
        textStatusDetail = findViewById(R.id.text_status);
        buttonBack = findViewById(R.id.button_back);

        Intent intent = getIntent();
        String firstName = intent.getStringExtra("firstName");
        String lastName = intent.getStringExtra("lastName");
        String address = intent.getStringExtra("address");
        String status = intent.getStringExtra("status");
        String photoPath = intent.getStringExtra("photoPath");

        textFirstNameDetail.setText("First name: " + firstName);
        textLastNameDetail.setText("Last name: " + lastName);
        textAddressDetail.setText("Address: " + address);
        textStatusDetail.setText("Status: " + status);

        if (photoPath != null) {
            imageProfileDetail.setImageURI(Uri.parse(photoPath));
        } else {
            imageProfileDetail.setImageResource(R.drawable.cat);
        }

        buttonBack.setOnClickListener(v -> finish());

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 3.0f));
            imageProfileDetail.setScaleX(scaleFactor);
            imageProfileDetail.setScaleY(scaleFactor);
            return true;
        }
    }
}
