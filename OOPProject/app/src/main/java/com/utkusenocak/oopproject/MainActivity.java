package com.utkusenocak.oopproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Musicians james = new Musicians("james", "guitar", 50);
        System.out.println(james.getName());
        SuperMusician lars = new SuperMusician("lars", "Drums", 55);
        System.out.println(lars.sing());



        //Polymorphism

        //Static Polymorphism

        Matematik matematik = new Matematik();
        System.out.println(matematik.sum());
        System.out.println(matematik.sum(5, 3));
        System.out.println(matematik.sum(5, 3, 4));

        //Dynamic Polymorphism
        Animal animal = new Animal();
        animal.sing();

        Dog barley = new Dog();
        barley.test();
        barley.sing();

        //Abstract & Interface
        User myuser = new User("utku", "engineer");
        System.out.println(myuser.information());
        Piano myPiano = new Piano();
        myPiano.brand = "Yamaha";
        myPiano.digital = true;
        myPiano.info();
    }
}
