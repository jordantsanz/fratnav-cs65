package com.example.fratnav.models;

import com.example.fratnav.models.House;
import com.example.fratnav.models.Post;

import java.util.ArrayList;
import java.util.List;

public class User {

    public String email;
    public String username;
    public String gender;
    public String sexuality;
    public String userID;
    public int year;
    public boolean houseAffiliation;
    public List<String> interestedIn;
    public List<House> subscribedTo;
    public List<Post> posts;
    public boolean notificationSettings;
    public boolean house;

    public User(String email, String username, String gender, String sexuality, String userID, int year, boolean houseAffiliation,
                ArrayList<String> interestedIn, ArrayList<House> subscribedTo, ArrayList<Post> posts, boolean notificationSettings){
        this.email = email;
        this.username = username;
        this.gender = gender;
        this.sexuality = sexuality;
        this.userID = userID;
        this.year = year;
        this.houseAffiliation = houseAffiliation;
        this.interestedIn = interestedIn;
        this.subscribedTo = subscribedTo;
        this.posts = new ArrayList<>();
        this.notificationSettings = notificationSettings;
        this.house = false;
    }

    public User(){}
}
