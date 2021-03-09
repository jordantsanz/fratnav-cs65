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

public class ReviewDatabaseHelper {

    public static void getReviewsByHouseId(String houseId, getAllReviewsCallback myCallback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = database.getReference("/houses");
        dbRefHouses.orderByKey().equalTo(houseId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshotReviewHouse", snapshot.toString());
                ArrayList<Review> reviews = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()){
                    Log.d("snapsnap", ds.toString());
                    House house = ds.getValue(House.class);
                    assert house != null;
                    Log.d("house", house.toString());
                    if (house.reviews != null) {
                        for (DataSnapshot dss : ds.child("reviews").getChildren()) {
                            Log.d("dss", dss.toString());
                            Review review = dss.getValue(Review.class);
                            assert review != null;
                            Log.d("review", review.toString());

                            reviews.add(review);
                        }
                    }

                }

                Log.d("reviewsFoundInMethod", reviews.toString());
                myCallback.onCallback(reviews);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static void getReviewsByUserId(String userId, getAllReviewsCallback myCallback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = database.getReference("/users");
        dbRefHouses.orderByKey().equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());
                ArrayList<Review> reviews = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    assert user != null;
                    for (DataSnapshot dss : ds.child("reviews").getChildren()){
                        Review review =  dss.getValue(Review.class);
                        reviews.add(review);
                    }

                }

                myCallback.onCallback(reviews);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static void createReview(Review review, createCallback myCallback){

        // add to reviews database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // store in house
        addReviewToObject(database.getReference("/houses"), review, review.houseId, myCallback, "house");

        // store in user
        addReviewToObject(database.getReference("/users"), review, review.userID, myCallback, "user");


    }


    public static void addReviewToObject(DatabaseReference ref, Review review, String id, createCallback myCallback, String type){
        ref.orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());

                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.child("reviews").getRef().push().setValue(review);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void addReviewtoHouse(DatabaseReference ref, Review review, String houseId){}
}
