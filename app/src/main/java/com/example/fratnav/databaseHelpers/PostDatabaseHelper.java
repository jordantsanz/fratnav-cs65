package com.example.fratnav.databaseHelpers;


import android.renderscript.Sampler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fratnav.callbacks.getAllPostsCallback;
import com.example.fratnav.callbacks.getCommentsByPostIdCallback;
import com.example.fratnav.callbacks.getPostByIdCallback;
import com.example.fratnav.callbacks.likePostCallback;
import com.example.fratnav.models.Comment;
import com.example.fratnav.models.House;
import com.example.fratnav.models.Post;
import com.example.fratnav.models.User;
import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class PostDatabaseHelper {

    public static FirebaseDatabase database;
    public static DatabaseReference dbRefPosts;


    public static void getAllPosts(getAllPostsCallback myCallback) {
        database = FirebaseDatabase.getInstance();
        dbRefPosts = database.getReference("/posts");
        ArrayList<Post> posts = new ArrayList<>();
        dbRefPosts.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Log.d("ds", ds.toString());

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


public static void getAllPostsByUser(String userId, getAllPostsCallback myCallback){
        database = FirebaseDatabase.getInstance();
        dbRefPosts = database.getReference("/posts");
        dbRefPosts.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Post> posts = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    assert post != null;
                    if (post.userID.equals(userId)){
                        posts.add(post);
                    }
                }

                myCallback.onCallback(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());
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

    public static String createPost(Post post){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = db.getReference("/posts");
        DatabaseReference newPostRef = dbRefHouses.push();
        String id = newPostRef.getKey();
        newPostRef.setValue(post);
        return id;
    }

    public static void addPostComment(Post post, Comment comment){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefPosts = db.getReference("/posts");
        dbRefPosts.orderByKey().equalTo(post.id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("post", snapshot.toString());
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.child("comments").getRef().push().setValue(comment);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static void getAllCommentsByPostId(Post post, getCommentsByPostIdCallback myCallback){
        database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefPosts = database.getReference("/posts");
        dbRefPosts.orderByKey().equalTo(post.id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("postSnapshot", snapshot.toString());
                for (DataSnapshot ds : snapshot.getChildren()){
                    Object obj = ds.child("comments").getValue();
                    if (obj == null){
                        myCallback.onCallback(new ArrayList<Comment>());
                    }
                    else {
                        ds.child("comments").getRef().orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Comment> comments = new ArrayList<Comment>();
                                for (DataSnapshot ds : snapshot.getChildren()){
                                    comments.add(ds.getValue(Comment.class));
                                }

                                myCallback.onCallback(comments);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Log.d("obj", obj.toString());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static void addLiketoPost(String userId, String postId, likePostCallback myCallback){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefPosts = db.getReference("/posts");
        dbRefPosts.orderByKey().equalTo(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());
                for (DataSnapshot ds : snapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    assert post != null;

                    ds.child("usersLiked").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d("postLikesBeforeUpdate", String.valueOf(post.likes));
                            if (!snapshot.hasChild(userId)){
                                ds.child("likes").getRef().setValue(post.likes + 1);
                                ds.child("usersLiked").getRef().child(userId).setValue(userId);
                                myCallback.onCallback(post.likes + 1);
                            }
                            else{
                                myCallback.onCallback(post.likes);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void removeLikefromPost(String userId, String postId, likePostCallback myCallback){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefPosts = db.getReference("/posts");
        dbRefPosts.orderByKey().equalTo(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot", snapshot.toString());
                for (DataSnapshot ds : snapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    assert post != null;

                    ds.child("usersLiked").getRef().getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d("snapshot", snapshot.toString());
                            if(snapshot.hasChild(userId)){
                                ds.child("usersLiked").getRef().child(userId).removeValue();
                                ds.child("likes").getRef().setValue(Math.max(post.likes - 1, 0));
                                myCallback.onCallback(post.likes -  1);
                            }
                            else{
                                myCallback.onCallback(post.likes);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void getAllSubscribedPosts(User user, getAllPostsCallback myCallback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = database.getReference("/houses");
        dbRefHouses.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> postKeys = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Log.d("snapshot", ds.toString());
                    if (user.subscribedTo != null) {
                        for (String houseId : user.subscribedTo.keySet()) {
                            if (Objects.equals(ds.getKey(), houseId)) {
                                for (DataSnapshot postKeyDs : ds.child("posts").getChildren()) {
                                    Log.d("postkeyds", postKeyDs.toString());
                                    postKeys.add(postKeyDs.getValue(String.class));
                                }
                            }
                        }
                    }
                    else{
                        myCallback.onCallback(new ArrayList<>());
                    }
                }

                DatabaseReference dbRefPosts = database.getReference("/posts");
                dbRefPosts.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Post> posts = new ArrayList<>();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            for (String postKey : postKeys){
                                if (Objects.equals(ds.getKey(), postKey)){
                                    posts.add(ds.getValue(Post.class));
                                }
                            }
                        }
                        Log.d("posts", posts.toString());
                        myCallback.onCallback(posts);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


