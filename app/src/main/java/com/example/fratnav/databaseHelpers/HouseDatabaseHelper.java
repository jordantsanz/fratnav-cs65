package com.example.fratnav.databaseHelpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fratnav.callbacks.getAllHousesCallback;
import com.example.fratnav.callbacks.getHouseByIdCallback;
import com.example.fratnav.callbacks.getPostByIdCallback;
import com.example.fratnav.models.House;
import com.example.fratnav.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HouseDatabaseHelper {

    public static void getAllHouses(getAllHousesCallback myCallback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = database.getReference("/houses").child("housesnew");
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
        DatabaseReference dbRefHouses = database.getReference("/houses").child("housesnew");
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
        DatabaseReference dbRefHouses = database.getReference("/houses").child("housesnew");
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

    public static String createHouse(House house){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = db.getReference("/houses").child("housesnew");
        DatabaseReference newHouseRef = dbRefHouses.push();
        String id = newHouseRef.getKey();
        newHouseRef.setValue(house);
        return id;
    }


    public static void addUrlToHouse(String houseId, String url){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = db.getReference("/houses").child("housesnew");

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
}
