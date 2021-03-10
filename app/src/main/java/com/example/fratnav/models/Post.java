package com.example.fratnav.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.navigation.NavType;

import com.example.fratnav.models.Comment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Post implements Parcelable {
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

    protected Post(Parcel in) {
        username = in.readString();
        userID = in.readString();
        text = in.readString();
        title = in.readString();
        attributes = in.createStringArrayList();
        likes = in.readInt();
        id = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(userID);
        dest.writeString(text);
        dest.writeString(title);
        dest.writeStringList(attributes);
        dest.writeInt(likes);
        dest.writeString(id);
    }
}
