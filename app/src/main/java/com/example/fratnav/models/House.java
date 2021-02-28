package com.example.fratnav.models;

import java.util.ArrayList;

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
    public ArrayList<Review> reviews;
    public String id;

    public House(String houseName, int subscribers, String summary, int date, boolean national, ArrayList<String> positions,
                 ArrayList<String> people, ArrayList<String> stats, String urlToHouseTour, ArrayList<Review> reviews){

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
    }

    public House(){}

    public void setId(String id){
        this.id = id;
    }
}
