package com.example.fratnav.models;

import com.example.fratnav.models.Comment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Post {
    public String username = "";
    public String userID = "";
    public String text = "";
    public String title = "";
    public ArrayList<String> attributes;
    public HashMap<String, Comment> comments;
    public int likes;
    public HashMap<String, String> usersLiked;
    public String id;

    public Post(String username, String userID, String text, ArrayList<String> attributes, HashMap<String, Comment> comments, int likes){
        this.username = username;
        this.userID = userID;
        this.text = text;
        this.attributes = attributes;
        this.comments = comments;
        this.likes = likes;
        this.usersLiked = new HashMap<>();
    }

    public Post(){}

    public void setId(String id){
        this.id = id;
    }

    public String stringify() {
        StringBuilder sb = new StringBuilder();
        sb.append("username: ");
        if (this.username != null) {
            sb.append(this.username);
        } else {
            sb.append(" ");
        }

        sb.append("userID: ");

        if (this.userID != null){
            sb.append(this.userID);
        }
        else{
            sb.append(" ");
        }

        return sb.toString();
    }
}
