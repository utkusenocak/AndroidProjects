package com.utkusenocak.simpsonbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    ImageView imageView;
    TextView nameTextView;
    TextView jobTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        imageView = findViewById(R.id.imageView);
        nameTextView = findViewById(R.id.nameText);
        jobTextView = findViewById(R.id.jobText);

        Intent intent = getIntent();
        Simpson selectedSimpson = (Simpson) intent.getSerializableExtra("selectedSimpson");

        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), selectedSimpson.getImageId());
        imageView.setImageBitmap(bitmap);

        nameTextView.setText(selectedSimpson.getName());
        jobTextView.setText(selectedSimpson.getJob());

    }
}
