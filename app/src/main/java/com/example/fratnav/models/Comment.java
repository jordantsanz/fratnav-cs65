package com.example.fratnav.models;

public class Comment {

    public String userId;
    public String comment;
    public String commentId;

    public Comment(String userId, String comment){
        this.userId = userId;
        this.comment = comment;
    }

    public Comment(){}

    public void setCommentId(String id){
        this.commentId = id;
    }
}
