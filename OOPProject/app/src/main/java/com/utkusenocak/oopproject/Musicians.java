package com.utkusenocak.oopproject;

public class Musicians {

    private String name;
    private String instrument;
    private int age;

    public Musicians(String name, String instrument, int age) {
        this.name = name;
        this.instrument = instrument;
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getInstrument() {
        return instrument;
    }

    public int getAge() {
        return age;
    }
}
