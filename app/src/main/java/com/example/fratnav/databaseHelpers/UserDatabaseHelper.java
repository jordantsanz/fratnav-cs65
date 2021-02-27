package com.example.fratnav.databaseHelpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fratnav.models.Post;
import com.example.fratnav.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UserDatabaseHelper {

    public static FirebaseDatabase database;
    public static DatabaseReference dbRefUser;

    public static void addPostToUser(Post post, User user){
        database = FirebaseDatabase.getInstance();
        dbRefUser = database.getReference("/users");
        DataSnapshot userPost = Objects.requireNonNull(dbRefUser.orderByKey().equalTo(user.userID).get().getResult()).child("posts");
           userPost.getRef().push();

        };
    }
