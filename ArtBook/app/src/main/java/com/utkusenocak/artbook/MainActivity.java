package com.utkusenocak.artbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> artNameArray;
    ArrayList<String> artistArray;
    ArrayList<Bitmap> imageArray;
    ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_art, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addArt){
            Intent intent = new Intent(MainActivity.this, Main2Activity.class);
            intent.putExtra("info", "newArt");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.artBookListView);
        artNameArray = new ArrayList<>();
        artistArray = new ArrayList<>();
        imageArray = new ArrayList<>();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, artNameArray);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("info", "oldArt");
                intent.putExtra("name", artNameArray.get(position));
                intent.putExtra("artist", artistArray.get(position));
                Singleton singleton = Singleton.getInstance();
                singleton.setImage(imageArray.get(position));
                startActivity(intent);
            }
        });

        getData();
    }
    public void getData(){
        SQLiteDatabase database = this.openOrCreateDatabase("Arts", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS arts (name VARCHAR, artist VARCHAR, image BLOB)");
        Cursor cursor = database.rawQuery("SELECT * FROM arts", null);

        int nameIx = cursor.getColumnIndex("name");
        int artistIx = cursor.getColumnIndex("artist");
        int imageIx = cursor.getColumnIndex("image");

        while (cursor.moveToNext()){
            artNameArray.add(cursor.getString(nameIx));
            artistArray.add(cursor.getString(artistIx));
            byte[] byteArray = cursor.getBlob(imageIx);
            Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            imageArray.add(image);

            arrayAdapter.notifyDataSetChanged();


        }

        cursor.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        arrayAdapter.notifyDataSetChanged();


    }
}
