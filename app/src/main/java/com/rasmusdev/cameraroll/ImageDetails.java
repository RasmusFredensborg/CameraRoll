package com.rasmusdev.cameraroll;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);
        String path = getIntent().getStringExtra("IMAGE_CLICKED");
        ImageView imageView = findViewById(R.id.details_image);
        Glide.with(this)
                .load(path)
                .asBitmap()
                .into(imageView);
    }

}
