package com.example.fratnav.databaseHelpers;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fratnav.callbacks.getAllPostsCallback;
import com.example.fratnav.callbacks.getPostByIdCallback;
import com.example.fratnav.models.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PostDatabaseHelper {

    public static FirebaseDatabase database;
    public static DatabaseReference dbRefPosts;


    public static void getAllPosts(getAllPostsCallback myCallback) {
        database = FirebaseDatabase.getInstance();
        dbRefPosts = database.getReference("/posts");
        ArrayList<Post> posts = new ArrayList<>();
        dbRefPosts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    assert post != null;
                    post.setId(ds.getKey());
                    posts.add(post);
                    }
                    myCallback.onCallback(posts);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", error.getMessage());
            }
    });
}

public static void getPostById(String id, getPostByIdCallback myCallback){
        database = FirebaseDatabase.getInstance();
        dbRefPosts = database.getReference("/posts");
        dbRefPosts.orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());
                for (DataSnapshot ds : snapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    assert post != null;
                    post.setId(ds.getKey());
                    myCallback.onCallback(post);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
}

public static void createPost(){}
}


