package com.example.fratnav.models;

import com.example.fratnav.models.House;
import com.example.fratnav.models.Post;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {

    public String email;
    public String username;
    public String gender;
    public String sexuality;
    public String userID;
    public String year;
    public boolean houseAffiliation;
    public List<String> interestedIn;
    public HashMap<String, String> subscribedTo;
    public HashMap<String, String> posts;
    public HashMap<String, Review> reviews;
    public boolean notificationSettings;
    public boolean house;

    public User(String email, String username, String gender, String sexuality, String userID, String year, boolean houseAffiliation,
                ArrayList<String> interestedIn, HashMap<String, String> subscribedTo, HashMap<String, String> posts, boolean notificationSettings){
        this.email = email;
        this.username = username;
        this.gender = gender;
        this.sexuality = sexuality;
        this.userID = userID;
        this.year = year;
        this.houseAffiliation = houseAffiliation;
        this.interestedIn = interestedIn;
        this.subscribedTo = subscribedTo;
        this.posts = new HashMap<>();
        this.notificationSettings = notificationSettings;
        this.house = false;
    }

    public User(){}

    public User(String userId, boolean notificationSettings){
        this.userID = userId;
        this.notificationSettings = notificationSettings;
    }


    // To create a house user account
    public User(String email, String userId, String username, boolean house){
        this.email = email;
        this.userID = userId;
        this.house = house;
        this.username = username;

        this.gender = "";
        this.sexuality = "";
        this.year = "";
        this.houseAffiliation = true;
        this.interestedIn = new ArrayList<>();
        this.subscribedTo = new HashMap<>();
        this.posts = new HashMap<>();
        this.reviews = new HashMap<>();
        this.notificationSettings = false;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> map = new HashMap<>();

        map.put("gender", this.gender);
        map.put("sexuality", this.sexuality);
        map.put("year", this.year);
        map.put("houseAffiliation", this.houseAffiliation);
        map.put("interestedIn", this.interestedIn);

        return map;
    }


    public HashMap<String, Object> toMapNotif(){
        HashMap<String, Object> map = new HashMap<>();

        map.put("notificationSettings", this.notificationSettings);
        return map;
    }
}
