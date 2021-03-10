package com.example.fratnav.databaseHelpers;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fratnav.callbacks.getAllHousesCallback;
import com.example.fratnav.callbacks.getHouseByIdCallback;
import com.example.fratnav.callbacks.getPostByIdCallback;
import com.example.fratnav.callbacks.likePostCallback;
import com.example.fratnav.models.House;
import com.example.fratnav.models.Post;
import com.example.fratnav.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * House Database Helper
 * Helper functions to communicate with houses in Firebase Realtime Database
 *
 */
public class HouseDatabaseHelper {

    /**
     * Gets all houses from database and sends back to ui with callback
     *
     * @param myCallback - sends houses
     */
    public static void getAllHouses(getAllHousesCallback myCallback) {
        // open database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = database.getReference("/houses");
        ArrayList<House> houses = new ArrayList<>();

        // get all houses
        dbRefHouses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // for each house
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    House house = ds.getValue(House.class);
                    assert house != null;
                    house.setId(ds.getKey());
                    houses.add(house);
                }

                // send houses back in callback
                myCallback.onCallback(houses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", error.getMessage());
            }
        });
    }


    /**
     * Gets a house by its name
     *
     *
     * @param name
     * @param myCallback
     */
    public static void getHouseByName(String name, getHouseByIdCallback myCallback){
        // open database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = database.getReference("/houses");

        // order houses by key
        dbRefHouses.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // for each house found
                for (DataSnapshot ds : snapshot.getChildren()){
                    House house = ds.getValue(House.class);
                    assert house != null;

                    // if house has right name, send back
                    if (house.houseName.equals(name)){
                        house.setId(ds.getKey());
                        myCallback.onCallback(house);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });

    }


    /**
     * Gets a house by houseId
     * @param id
     * @param myCallback
     */
    public static void getHouseById(String id, getHouseByIdCallback myCallback){
        // open database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = database.getReference("/houses");

        // looks for house equal to houseId
        dbRefHouses.orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                // for houses found (will just be one)
                for (DataSnapshot ds : snapshot.getChildren()){
                    House house = ds.getValue(House.class);
                    assert house != null;
                    house.setId(ds.getKey());

                    // send house back
                    myCallback.onCallback(house);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

    /**
     * Creates a new house in the database
     *
     * @param house - from house class
     */
    public static void createHouse(House house){
        // open database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = db.getReference("/houses");

        // save new reference node at house.id
        DatabaseReference newHouseRef = dbRefHouses.child(house.id);
        newHouseRef.setValue(house);
    }


    /**
     * Adds S3 url to a house object in database
     *
     * @param houseId - id of house
     * @param url - S3 url to house tour image
     */
    public static void addUrlToHouse(String houseId, String url){

        // opens database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = db.getReference("/houses");

        // find house with houseId
        dbRefHouses.orderByKey().equalTo(houseId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                // for house, save url to urlToHouseTour child node
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.child("urlToHouseTour").getRef().setValue(url);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

    /**
     * Gets all houses that a user is subscribed to
     *
     * @param subscribedTo - hashmap of house ids that a user subscribes to
     * @param myCallback - callback to send array of houses
     */
    public static void getHousesByUserSubscribed(HashMap<String, String> subscribedTo, getAllHousesCallback myCallback){
        // open database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbHousesRef = db.getReference("/houses");
        ArrayList<House> houses = new ArrayList<>();

        // get all houses
            dbHousesRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    // for each house
                    for (DataSnapshot ds : snapshot.getChildren()){
                        House house = ds.getValue(House.class);
                        assert house != null;
                        house.setId(ds.getKey());

                        // if there are subscriptions
                        if (subscribedTo != null) {

                            // for all subscribed
                            for (String string : subscribedTo.keySet()) {

                                // if subscribed, add to houses arraylist
                                if (string.equals(house.id)){
                                    houses.add(house);
                                }
                            }
                        }
                    }

                    // send back houses
                    myCallback.onCallback(houses);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        myCallback.onCallback(houses);
    }


    /**
     * Updates a house object
     *
     *
     * @param house - house object with wanted update fields
     */
    public static void updateHouse(House house){
        // make updates map of house
        HashMap<String, Object> updates = house.toMap();

        // open database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouse = db.getReference("/houses");

        // find specific house
        dbRefHouse.orderByKey().equalTo(house.id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // update house
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().updateChildren(updates);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Removes a subscriber from a house
     *
     * @param houseId - house's id
     * @param myCallback - sends correct amount of subscribers back
     */
    public static void removeSubscriberFromCount(String houseId, likePostCallback myCallback){
        // open database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefPosts = db.getReference("/houses");

        // finds specific house by id
        dbRefPosts.orderByKey().equalTo(houseId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // for house
                for (DataSnapshot ds : snapshot.getChildren()){
                    House house = ds.getValue(House.class);
                    assert house != null;

                    // subtract one from subscribers count
                    ds.child("subscribers").getRef().setValue(house.subscribers - 1);
                    myCallback.onCallback(house.subscribers - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Add one subscriber to house's number of subscribers
     *
     * @param houseId - id of house
     * @param myCallback - sends correct amount back to ui
     */
    public static void addSubscriberToCount(String houseId, likePostCallback myCallback){
        // open database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefPosts = db.getReference("/houses");

        // find specific house
        dbRefPosts.orderByKey().equalTo(houseId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // for house
                for (DataSnapshot ds : snapshot.getChildren()){
                    House house = ds.getValue(House.class);
                    assert house != null;

                    // increment subscribers
                    ds.child("subscribers").getRef().setValue(house.subscribers + 1);
                    myCallback.onCallback(house.subscribers + 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
