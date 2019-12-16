package com.example.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Main {
    public static void main(String[] args){
        ArrayList<String> Musicians = new ArrayList<>();
        Musicians.add("JAMES");
        Musicians.add("Lars");
        Musicians.add(1, "Kirk");
        System.out.println(Musicians.get(0));
        System.out.println(Musicians.get(1));
        System.out.println(Musicians.get(2));

        //Set


        HashSet<String> mySet = new HashSet<>();
        mySet.add("james");
        mySet.add("james");
        System.out.println(mySet.size());


        //HashMap

        HashMap<String, String> myHashmap = new HashMap<>();
        myHashmap.put("name", "james");
        System.out.println(myHashmap.get("name"));

    }
}
