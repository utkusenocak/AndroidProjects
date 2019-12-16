package com.example.methodsandclasses;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeMusicians();
        makeSimpsons();
    }

    public void makeMusicians(){
        Musicians james = new Musicians("james", "Guitar", 50);
        System.out.println(james.instrument);
    }
    public void makeSimpsons(){
        Simpsons homer = new Simpsons("Homer", 50, "Nuclear");
        System.out.println(homer.getName());

    }
}
