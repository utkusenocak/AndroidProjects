package com.utkusenocak.landmarkbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.landMarkListView);

        final ArrayList<String> landmarkList = new ArrayList<>();
        landmarkList.add("Pisa");
        landmarkList.add("Eiffel");
        landmarkList.add("Colleseo");
        landmarkList.add("London Bridge");


        final ArrayList<String> countryNames = new ArrayList<>();
        countryNames.add("Italy");
        countryNames.add("France");
        countryNames.add("Italy");
        countryNames.add("United Kingdom");

        Bitmap pisa = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.pisa);
        Bitmap eiffel = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.eiffel);
        Bitmap collesium = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.collesium);
        Bitmap londonBridge = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.londonbridge);

        final ArrayList<Bitmap> images = new ArrayList<>();
        images.add(pisa);
        images.add(eiffel);
        images.add(collesium);
        images.add(londonBridge);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, landmarkList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("landmarkName", landmarkList.get(position));
                intent.putExtra("landmarkCountry", countryNames.get(position));
                Singleton singleton = Singleton.getInstance();
                singleton.setChosenImage(images.get(position));
                startActivity(intent);
            }
        });
    }

}
