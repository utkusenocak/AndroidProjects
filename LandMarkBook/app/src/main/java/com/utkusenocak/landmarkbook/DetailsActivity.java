package com.utkusenocak.landmarkbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    ImageView landmarkImage;
    TextView landmarkName;
    TextView landmarkCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        landmarkImage = findViewById(R.id.landmarkImage);
        landmarkName = findViewById(R.id.landmarkNameText);
        landmarkCountry = findViewById(R.id.landmarkCityText);

        Intent intent = getIntent();
        landmarkName.setText(intent.getStringExtra("landmarkName"));
        landmarkCountry.setText(intent.getStringExtra("landmarkCountry"));
        Singleton singleton = Singleton.getInstance();
        landmarkImage.setImageBitmap(singleton.getChosenImage());
    }
}
