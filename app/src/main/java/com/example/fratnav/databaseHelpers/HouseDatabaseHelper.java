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

public class HouseDatabaseHelper {

    public static void getAllHouses(getAllHousesCallback myCallback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = database.getReference("/houses");
        ArrayList<House> houses = new ArrayList<>();
        dbRefHouses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("datasnapshot", dataSnapshot.toString());
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    House house = ds.getValue(House.class);
                    assert house != null;
                    Log.d("house", house.houseName);
                    house.setId(ds.getKey());
                    houses.add(house);
                }
                myCallback.onCallback(houses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", error.getMessage());
            }
        });
    }


    public static void getHouseByName(String name, getHouseByIdCallback myCallback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = database.getReference("/houses");
        dbRefHouses.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());
                for (DataSnapshot ds : snapshot.getChildren()){
                    House house = ds.getValue(House.class);
                    assert house != null;
                    Log.d("house", house.toString());

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


    public static void getHouseById(String id, getHouseByIdCallback myCallback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = database.getReference("/houses");
        dbRefHouses.orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());
                for (DataSnapshot ds : snapshot.getChildren()){
                    House house = ds.getValue(House.class);
                    assert house != null;
                    house.setId(ds.getKey());
                    myCallback.onCallback(house);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }

    public static void createHouse(House house){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = db.getReference("/houses");
        DatabaseReference newHouseRef = dbRefHouses.child(house.id);
        newHouseRef.setValue(house);
    }


    public static void addUrlToHouse(String houseId, String url){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = db.getReference("/houses");

        dbRefHouses.orderByKey().equalTo(houseId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());
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

    public static void getHousesByUserSubscribed(HashMap<String, String> subscribedTo, getAllHousesCallback myCallback){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbHousesRef = db.getReference("/houses");
        ArrayList<House> houses = new ArrayList<>();
            dbHousesRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("snapshot", snapshot.toString());
                    for (DataSnapshot ds : snapshot.getChildren()){
                        House house = ds.getValue(House.class);
                        assert house != null;
                        house.setId(ds.getKey());
                        if (subscribedTo != null) {
                            for (String string : subscribedTo.keySet()) {
                                Log.d("theString", string);
                                Log.d("houseid", house.id);
                                if (string.equals(house.id)){
                                    houses.add(house);
                                }
                            }
                        }
                    }
                    myCallback.onCallback(houses);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        Log.d("houses", houses.toString());
        myCallback.onCallback(houses);
    }


    public static void updateHouse(House house){
        HashMap<String, Object> updates = house.toMap();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouse = db.getReference("/houses");
        dbRefHouse.orderByKey().equalTo(house.id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().updateChildren(updates);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void removeSubscriberFromCount(String houseId, likePostCallback myCallback){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefPosts = db.getReference("/houses");
        dbRefPosts.orderByKey().equalTo(houseId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());
                for (DataSnapshot ds : snapshot.getChildren()){
                    House house = ds.getValue(House.class);
                    assert house != null;
                    ds.child("subscribers").getRef().setValue(house.subscribers - 1);
                    myCallback.onCallback(house.subscribers - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void addSubscriberToCount(String houseId, likePostCallback myCallback){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefPosts = db.getReference("/houses");
        dbRefPosts.orderByKey().equalTo(houseId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());
                for (DataSnapshot ds : snapshot.getChildren()){
                    House house = ds.getValue(House.class);
                    assert house != null;
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
