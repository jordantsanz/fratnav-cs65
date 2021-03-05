package com.example.fratnav.models;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;

public class House {

    public String houseName;
    public int subscribers;
    public String summary;
    public int date;
    public boolean national;
    public ArrayList<String> positions;
    public ArrayList<String> people;
    public ArrayList<String> stats;
    public String urlToHouseTour;
    public HashMap<String, Review> reviews;
    public String id;
    public int imageName;
    public String president;
    public String vicePresident;
    public String treasurer;
    public String rushChair;

    public House(String houseName, int subscribers, String summary, int date, boolean national, ArrayList<String> positions,
                 ArrayList<String> people, ArrayList<String> stats, String urlToHouseTour, HashMap<String, Review> reviews, int imageName, String houseId){

        this.houseName = houseName;
        this.subscribers = subscribers;
        this.summary = summary;
        this.date = date;
        this.national = national;
        this.positions = positions;
        this.people = people;
        this.stats = stats;
        this.urlToHouseTour = urlToHouseTour;
        this.reviews = reviews;
        this.imageName = imageName;
        this.id = id;

    }

    public House(){}

    public House(String president, String vicePresident, String treasurer, String rushChair){
        this.president = president;
        this.vicePresident = vicePresident;
        this.treasurer = treasurer;
        this.rushChair = rushChair;
    }

    public void setId(String id){
        this.id = id;
    }


    public HashMap<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("postitions", positions);
        map.put("people", people);
        map.put("stats", stats);

        return map;
    }
}
