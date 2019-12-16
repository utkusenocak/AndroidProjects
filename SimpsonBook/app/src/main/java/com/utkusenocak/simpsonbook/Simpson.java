package com.utkusenocak.simpsonbook;

import java.io.Serializable;

public class Simpson implements Serializable {

    private String name;
    private String job;
    private int imageId;

    public Simpson(String name, String job, int imageId) {
        this.name = name;
        this.job = job;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public int getImageId() {
        return imageId;
    }
}
