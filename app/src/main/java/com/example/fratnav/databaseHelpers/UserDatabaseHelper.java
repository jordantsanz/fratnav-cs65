package com.example.fratnav.databaseHelpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fratnav.profile.Profile;
import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.models.House;
import com.example.fratnav.models.Post;
import com.example.fratnav.models.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * UserDatabaseHelper
 * Helper functions to communicate with user entries in Firebase Realtime Database
 */

public class UserDatabaseHelper {

    public static FirebaseDatabase database;
    public static DatabaseReference dbRefUser;

    /**
     * Adds a new post to a user's post ids
     *
     * @param post - new post
     * @param user - FirebaseUser user
     * @param currentUserInfo - user's information
     */
    public static void addPostToUser(Post post, FirebaseUser user, User currentUserInfo){

        // open database
        database = FirebaseDatabase.getInstance();
        dbRefUser = database.getReference("/users");

        // get specific user
        dbRefUser.orderByKey().equalTo(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("userSnapshot", snapshot.toString());

                // add post id to user's posts
                for (DataSnapshot ds : snapshot.getChildren()){
                    Log.d("userSnapshotds", ds.toString());
                    ds.child("posts").getRef().push().setValue(post.id);
                   }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // if the current user is a house account, also add to house page
        if (currentUserInfo.house) {
            DatabaseReference dbRefHouses = database.getReference("/houses");

            // get house
            dbRefHouses.orderByKey().equalTo(currentUserInfo.houseId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("Datasnapshot", "onDataChange: ");

                    // add post to house information
                    for (DataSnapshot ds : snapshot.getChildren()){
                        ds.child("posts").getRef().push().setValue(post.id);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        };


    /**
     * Create new user object in database
     *
     * @param user - user object
     * @return String user id
     */
    public static String createUser(User user){
        // open database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefUsers = db.getReference("/users");

        // save user and return id
        DatabaseReference newUserRef = dbRefUsers.child(user.userID);
        String id = newUserRef.getKey();
        newUserRef.setValue(user);
        return id;
    }


    /**
     * Get specific user by given userId
     *
     * @param id - user id
     * @param myCallback - returns user
     */
    public static void getUserById(String id, getUserByIdCallback myCallback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = database.getReference("/users");
        dbRefHouses.orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());
                for (DataSnapshot ds : snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    assert user != null;
                    myCallback.onCallback(user);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void updateUserProfile(String userId, User user){
        HashMap<String, Object> userUpdates = user.toMap();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbUserRef = database.getReference("/users");
        dbUserRef.orderByKey().equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());
                for (DataSnapshot ds : snapshot.getChildren()){
                    Log.d("updates", ds.getRef().updateChildren(userUpdates).toString());
                }

                Profile.refresh();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void updateUserNotifSettings(String userId, boolean notifOn){
        User user = new User(userId, notifOn);
        HashMap<String, Object> map = user.toMapNotif();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbUserRef = database.getReference("/users");
        dbUserRef.orderByKey().equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().updateChildren(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void addHouseToUserSubscribed(House house, String userId){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefUsers = db.getReference("/users");

        dbRefUsers.orderByKey().equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.child("subscribedTo").getRef().child(house.id).setValue(house.houseName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void removeHouseFromUserSubscribed(House house, String userId){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefUsers = db.getReference("/users");

        dbRefUsers.orderByKey().equalTo(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.child("subscribedTo").getRef().child(house.id).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
            }
        });
    }
    }
