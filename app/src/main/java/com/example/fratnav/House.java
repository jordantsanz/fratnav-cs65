package com.example.fratnav;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class House {

    public String houseName;
    public int subscribers;
    public String summary;
    public String date;
    public boolean national;
    public ArrayList<String> positions;
    public ArrayList<String> people;
    public ArrayList<String> stats;
    public String urlToHouseTour;
    public ArrayList<Reviews> reviews;

    public House(String houseName, int subscribers, String summary, String date, boolean national, ArrayList<String> positions,
                 ArrayList<String> people, ArrayList<String> stats, String urlToHouseTour, ArrayList<Reviews> reviews){

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
}
