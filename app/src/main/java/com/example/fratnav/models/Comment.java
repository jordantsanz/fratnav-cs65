package com.example.fratnav.models;

public class Comment {

    public String userId;
    public String comment;
    public String commentId;
    public String usernamePoster;

    public Comment(String userId, String comment, String usernamePoster){
        this.userId = userId;
        this.comment = comment;
        this.usernamePoster = usernamePoster;
    }

    public Comment(){}

    public void setCommentId(String id){
        this.commentId = id;
    }
}
