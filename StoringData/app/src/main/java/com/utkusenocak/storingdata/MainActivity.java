package com.utkusenocak.storingdata;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private EditText editText;
    private SharedPreferences sharedPreferences;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);

        sharedPreferences = this.getSharedPreferences("com.utkusenocak.storingdata", Context.MODE_PRIVATE);
        int age = sharedPreferences.getInt("age", 0);
        if (age != 0){
            textView.setText("Your Age :" + age);
        }

    }
    @SuppressLint("SetTextI18n")
    public void save(View view){
        if (!editText.getText().toString().matches("")) {
            int age= Integer.parseInt(editText.getText().toString());
            textView.setText("Your Age :" + age);
            sharedPreferences.edit().putInt("age", age).apply();

        }

    }
    @SuppressLint("SetTextI18n")
    public void delete(View view){
        int storedData = sharedPreferences.getInt("age", 0);
        if (storedData != 0){
            sharedPreferences.edit().remove("age").apply();
            textView.setText("Your Age");
        }
    }
}
