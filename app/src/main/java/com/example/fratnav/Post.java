package com.example.fratnav;

import java.util.ArrayList;

public class Post {
    public String username;
    public String userID;
    public String text;
    public ArrayList<String> attributes;
    public ArrayList<Comment> comments;
    public int likes;
    public String id;

    public Post(String username, String userID, String text, ArrayList<String> attributes, ArrayList<Comment> comments, int likes){
        this.username = username;
        this.userID = userID;
        this.text = text;
        this.attributes = attributes;
        this.comments = comments;
        this.likes = likes;
    }

    public Post(){}

    public void setId(String id){
        this.id = id;
    }
}
