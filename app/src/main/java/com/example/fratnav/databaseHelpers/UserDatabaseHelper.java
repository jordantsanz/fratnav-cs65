package com.example.fratnav.databaseHelpers;

import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fratnav.Profile;
import com.example.fratnav.callbacks.getHouseByIdCallback;
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
import java.util.Objects;

public class UserDatabaseHelper {

    public static FirebaseDatabase database;
    public static DatabaseReference dbRefUser;

    public static void addPostToUser(Post post, FirebaseUser user){
        database = FirebaseDatabase.getInstance();
        dbRefUser = database.getReference("/users");
        dbRefUser.orderByKey().equalTo(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("userSnapshot", snapshot.toString());
                for (DataSnapshot ds : snapshot.getChildren()){
                    Log.d("userSnapshotds", ds.toString());
                    ds.child("posts").getRef().push().setValue(post.id);
                   }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        };


    public static String createUser(User user){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefUsers = db.getReference("/users");
        DatabaseReference newUserRef = dbRefUsers.child(user.userID);
        Log.d("ref", newUserRef.toString());
        String id = newUserRef.getKey();
        newUserRef.setValue(user);
        return id;
    }

    public static void editUser(User user){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefUsers = db.getReference("/users");
        dbRefUsers.orderByKey().equalTo(user.userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().setValue(user);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


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
                    Log.d("updatesmap", userUpdates.toString());
                    Log.d("updates", ds.getRef().updateChildren(userUpdates).toString());
                }

                Profile.refresh();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    }
