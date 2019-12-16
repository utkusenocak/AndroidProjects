package com.utkusenocak.travelbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> locationAddress;
    ArrayList<Double> latitude;
    ArrayList<Double> longitude;
    ArrayAdapter arrayAdapter;
    SQLiteDatabase database;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_location, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addLocation){
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra("info", "newLocation");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.savedLocationsListView);
        locationAddress = new ArrayList<>();
        latitude = new ArrayList<>();
        longitude = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, locationAddress);
        listView.setAdapter(arrayAdapter);
        try {
            database = this.openOrCreateDatabase("Locations", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS locations (address VARCHAR, latitude DOUBLE, longitude DOUBLE)");
        }catch (Exception e){
            e.printStackTrace();
        }

        getData();
        listView.setLongClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("info", "oldLocation");
                intent.putExtra("address", locationAddress.get(position));
                intent.putExtra("lat", latitude.get(position));
                intent.putExtra("long", longitude.get(position));
                startActivity(intent);


            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete Location");
                builder.setMessage("Are you Sure ?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.execSQL("DELETE FROM locations WHERE address = '" + locationAddress.get(position) + "'");
                        getData();
                        Toast.makeText(getApplicationContext(), "İtem deleted", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.show();
                return true;
            }
        });


    }
    public void getData(){
        try {


            Cursor cursor = database.rawQuery("SELECT * FROM locations", null);
            int addressIx = cursor.getColumnIndex("address");
            int latIx = cursor.getColumnIndex("latitude");
            int longIx = cursor.getColumnIndex("longitude");
            locationAddress.clear();
            latitude.clear();
            longitude.clear();
            while (cursor.moveToNext()){
                locationAddress.add(cursor.getString(addressIx));
                latitude.add(cursor.getDouble(latIx));
                longitude.add(cursor.getDouble(longIx));
                arrayAdapter.notifyDataSetChanged();
            }
            cursor.close();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        arrayAdapter.notifyDataSetChanged();
    }
}
