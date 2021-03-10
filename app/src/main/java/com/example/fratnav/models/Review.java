package com.example.fratnav.models;

/**
 * Review class
 *
 */
public class Review {

    public String reviewID;
    public String description;
    public String username;
    public String userID;
    public float safetyRating;
    public float inclusivityRating;
    public float basementRating;
    public float overallRating;
    public String houseId;
    public String houseName;

    public Review(){}

    public Review(String description, String username, String userID, float safetyRating, float inclusivityRating, float basementRating,
                  float overallRating, String houseId, String houseName){
        this.description = description;
        this.username = username;
        this.userID = userID;
        this.safetyRating = safetyRating;
        this.inclusivityRating = inclusivityRating;
        this.basementRating = basementRating;
        this.overallRating = overallRating;
        this.houseId = houseId;
        this.houseName = houseName;
    }


    public void setReviewID(String reviewID){
        this.reviewID = reviewID;
    }
}
