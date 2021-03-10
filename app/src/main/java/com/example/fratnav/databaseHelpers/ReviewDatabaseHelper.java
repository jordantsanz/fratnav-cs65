package com.example.fratnav.databaseHelpers;

import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fratnav.callbacks.createCallback;
import com.example.fratnav.callbacks.getAllHousesCallback;
import com.example.fratnav.callbacks.getAllReviewsCallback;
import com.example.fratnav.callbacks.getHouseByIdCallback;
import com.example.fratnav.houses.HousePage;
import com.example.fratnav.models.House;
import com.example.fratnav.models.Review;
import com.example.fratnav.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.fratnav.houses.HousePage.arrayOfReviews;

/**
 * ReviewDatabaseHelper
 * Class of helper functions to interact with reviews in Firebase Realtime Database
 *
 */
public class ReviewDatabaseHelper {

    /**
     * Get all reviews by a specific houseId
     *
     * @param houseId - house that has reviwes left on them
     * @param myCallback - returns reviews
     */
    public static void getReviewsByHouseId(String houseId, getAllReviewsCallback myCallback){
        // open database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = database.getReference("/houses");

        // get specific house
        dbRefHouses.orderByKey().equalTo(houseId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshotReviewHouse", snapshot.toString());
                ArrayList<Review> reviews = new ArrayList<>();

                // for each house
                for (DataSnapshot ds : snapshot.getChildren()){
                    House house = ds.getValue(House.class);
                    assert house != null;

                    // if there are reviews
                    if (house.reviews != null) {

                        // for each review, add to array of reviews
                        for (DataSnapshot dss : ds.child("reviews").getChildren()) {
                            Review review = dss.getValue(Review.class);
                            assert review != null;
                            reviews.add(review);
                        }
                    }

                }
                // send back reviews
                myCallback.onCallback(reviews);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Get all reviews made by a user
     * @param userId - id of user
     * @param myCallback - sends back reviews
     */
    public static void getReviewsByUserId(String userId, getAllReviewsCallback myCallback){
        // open database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = database.getReference("/users");

        // find user
        dbRefHouses.orderByKey().equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());
                ArrayList<Review> reviews = new ArrayList<>();

                // for user
                for (DataSnapshot ds : snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    assert user != null;

                    // for each review, add to array
                    for (DataSnapshot dss : ds.child("reviews").getChildren()){
                        Review review = dss.getValue(Review.class);
                        reviews.add(review);
                    }

                }

                // send back reviews
                myCallback.onCallback(reviews);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Creates a new review node and puts the id into user and house storage
     * @param review - new review
     * @param myCallback -
     */
    public static void createReview(Review review, createCallback myCallback){

        // add to reviews database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // store in house
        addReviewToObject(database.getReference("/houses"), review, review.houseId, myCallback, "house");

        // store in user
        addReviewToObject(database.getReference("/users"), review, review.userID, myCallback, "user");


    }


    /**
     * Helper function that adds a review to a reference node
     *
     * @param ref - reference node
     * @param review - new review
     * @param id - id to add to
     * @param myCallback - says done message
     * @param type = type of thing added to
     */
    public static void addReviewToObject(DatabaseReference ref, Review review, String id, createCallback myCallback, String type){

        // get specific object
        ref.orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());

                // add to reviews
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.child("reviews").getRef().push().setValue(review);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
