package com.utkusenocak.landmarkbook;

import android.graphics.Bitmap;

class Singleton {
    private static final Singleton ourInstance = new Singleton();

    static Singleton getInstance() {
        return ourInstance;
    }

    private Bitmap chosenImage;



    private Singleton() {

    }

    public Bitmap getChosenImage() {
        return chosenImage;
    }

    public void setChosenImage(Bitmap chosenImage) {
        this.chosenImage = chosenImage;
    }

}
