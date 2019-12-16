package com.utkusenocak.artbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Main2Activity extends AppCompatActivity {
    ImageView imageView;
    EditText nameEditText;
    EditText artistEditText;
    Button saveButton;
    SQLiteDatabase myDatabase;
    Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        imageView = findViewById(R.id.selectedImage);
        nameEditText = findViewById(R.id.artNameEditText);
        artistEditText = findViewById(R.id.artistEditText);
        saveButton = findViewById(R.id.saveButton);
        Intent intent = getIntent();
        String info = intent.getStringExtra("info");
        if (info.matches("newArt")){
            nameEditText.setText("");
            artistEditText.setText("");
            saveButton.setVisibility(View.VISIBLE);
            imageView.setClickable(true);


        }else {
            saveButton.setVisibility(View.INVISIBLE);
            imageView.setClickable(false);
            nameEditText.setEnabled(false);
            artistEditText.setEnabled(false);
            String name = intent.getStringExtra("name");
            String artist = intent.getStringExtra("artist");
            nameEditText.setText(name);
            artistEditText.setText(artist);
            Singleton singleton = Singleton.getInstance();
            imageView.setImageBitmap(singleton.getImage());
        }
    }

    public void selectImage(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        }else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        }

    }

    public void save(View view){
        String name = nameEditText.getText().toString();
        String artist = artistEditText.getText().toString();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG, 25, outputStream);
        byte[] byteArrays = outputStream.toByteArray();
        try {
            myDatabase = this.openOrCreateDatabase("Arts", MODE_PRIVATE, null);
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS arts (name VARCHAR, artist VARCHAR, image BLOB)");
            String sqlString = "INSERT INTO arts (name, artist, image) VALUES (?, ?, ?)";
            SQLiteStatement sqLiteStatement = myDatabase.compileStatement(sqlString);
            sqLiteStatement.bindString(1, name);
            sqLiteStatement.bindString(2, artist);
            sqLiteStatement.bindBlob(3, byteArrays);
            sqLiteStatement.execute();
        }catch (Exception e){
            e.printStackTrace();
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            Uri imageData = data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    imageView.setImageBitmap(selectedImage);


                }else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
                    imageView.setImageBitmap(selectedImage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2 ){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        }
    }
}
