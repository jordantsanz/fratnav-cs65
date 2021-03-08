package com.example.fratnav.models;

import android.graphics.drawable.Drawable;

import java.lang.reflect.Array;
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
    public String president = "";
    public String vicePresident = "";
    public String treasurer = "";
    public String rushChair = "";
    public String totalMembers = "";
    public String queerMembers = "";
    public String pocMembers = "";
    public HashMap<String, String> posts;

    /**
     * Use this constructor to make an initial house!
     *
     * @param houseName - house name
     * @param subscribers - number of subscribers on the house
     * @param summary - summary of the house
     * @param date - date the house was created
     * @param national - if the house is national or not
     * @param positions - positions held in the house
     * @param people - people holding those positions
     * @param stats - statistics about the house
     * @param urlToHouseTour - s3 url to the house tour
     * @param reviews - reviews of the house
     * @param imageName - binary of the cardview cover image
     * @param houseId - id of the house
     */
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
        this.totalMembers = "";
        this.pocMembers = "";
        this.queerMembers = "";
        this.urlToHouseTour = urlToHouseTour;
        this.reviews = reviews;
        this.imageName = imageName;
        this.id = id;
        this.president = "";
        this.vicePresident = "";
        this.treasurer = "";
        this.rushChair = "";
        this.posts = new HashMap<>();

    }

    // default
    public House(){}


    /**
     * use this constructor to make updates to the house!
     *
     * @param president - name of the president of the house
     * @param vicePresident - name of the vp of the house
     * @param treasurer - name of the treasurer of the house
     * @param rushChair - name of the rush chair in the house
     */
    public House(String id, String houseName, String summary, String president, String vicePresident, String treasurer, String rushChair, String totalMembers, String pocMembers, String queerMembers){
        this.president = president;
        this.vicePresident = vicePresident;
        this.treasurer = treasurer;
        this.rushChair = rushChair;
        this.id = id;
        this.houseName = houseName;
        this.totalMembers = totalMembers;
        this.pocMembers = pocMembers;
        this.queerMembers = queerMembers;
        this.summary = summary;
    }

    public void setId(String id){
        this.id = id;
    }


    /**
     * Method to help with updating houses in firebase
     * @return returns a map of updates for the house
     */
    public HashMap<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("summary" ,summary);
        map.put("president", president);
        map.put("vicePresident", vicePresident);
        map.put("treasurer", treasurer);
        map.put("rushChair", rushChair);
        map.put("totalMembers", totalMembers);
        map.put("pocMembers", pocMembers);
        map.put("queerMembers", queerMembers);

        return map;
    }
}
