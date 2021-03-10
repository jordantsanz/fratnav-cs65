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

/**
 * PostDatabaseHelper
 * Class of helper functions to interact with posts in Firebase Realtime Database
 */
public class PostDatabaseHelper {

    public static FirebaseDatabase database;
    public static DatabaseReference dbRefPosts;


    /**
     * Gets all posts in database
     *
     * @param myCallback - sends back posts
     */
    public static void getAllPosts(getAllPostsCallback myCallback) {
        // open database
        database = FirebaseDatabase.getInstance();
        dbRefPosts = database.getReference("/posts");
        ArrayList<Post> posts = new ArrayList<>();

        // finds all posts
        dbRefPosts.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // for post
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Post post = ds.getValue(Post.class);
                    assert post != null;
                    post.setId(ds.getKey());
                    posts.add(post);
                    }

                // sends back posts
                    myCallback.onCallback(posts);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error", error.getMessage());
            }
    });
}

    /**
     * Gets all posts by a specific user
     *
     * @param userId - id of given user
     * @param myCallback - sends posts back
     */
    public static void getAllPostsByUser(String userId, getAllPostsCallback myCallback){
        // open database
        database = FirebaseDatabase.getInstance();
        dbRefPosts = database.getReference("/posts");

        // finds all posts
        dbRefPosts.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Post> posts = new ArrayList<>();

                // for each post
                for (DataSnapshot ds : snapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    assert post != null;

                    // if userid is correct, add to array of posts
                    if (post.userID.equals(userId)){
                        posts.add(post);
                    }
                }

                // send back posts
                myCallback.onCallback(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error", error.toString());

            }
        });
}

    /**
     * Gets a specific post by post.id
     *
     * @param id - id of wanted post
     * @param myCallback - sends back post
     */
    public static void getPostById(String id, getPostByIdCallback myCallback){
        // open database
        database = FirebaseDatabase.getInstance();
        dbRefPosts = database.getReference("/posts");

        // finds specific post
        dbRefPosts.orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // for post
                for (DataSnapshot ds : snapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    assert post != null;
                    post.setId(ds.getKey());

                    // send back post
                    myCallback.onCallback(post);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
}

    /**
     * Add a comment to a specific post
     * @param post - post to add comment to
     * @param comment - comment to add to post
     */
    public static void addPostComment(Post post, Comment comment){
        // open database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefPosts = db.getReference("/posts");

        // finds specific post
        dbRefPosts.orderByKey().equalTo(post.id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // adds comment to post's comments
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.child("comments").getRef().push().setValue(comment);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     * Gets all comments on a specific post
     *
     * @param post - post in question
     * @param myCallback - sends back comments
     */
    public static void getAllCommentsByPostId(Post post, getCommentsByPostIdCallback myCallback){
        // open database
        database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefPosts = database.getReference("/posts");

        // gets specific post
        dbRefPosts.orderByKey().equalTo(post.id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // for post
                for (DataSnapshot ds : snapshot.getChildren()){
                    Object obj = ds.child("comments").getValue();

                    // if no comments
                    if (obj == null){
                        myCallback.onCallback(new ArrayList<Comment>());
                    }

                    // there are comments
                    else {

                        // find all comments
                        ds.child("comments").getRef().orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<Comment> comments = new ArrayList<Comment>();

                                // for each comment, add to comments arraylist
                                for (DataSnapshot ds : snapshot.getChildren()){
                                    comments.add(ds.getValue(Comment.class));
                                }

                                // send back comments
                                myCallback.onCallback(comments);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    /**
     * Adds a like to a specific post. Also adds that user to usersLiked field on post.
     *
     * @param userId - userID that liked the post
     * @param postId - specific post ID
     * @param myCallback - sends correct likes number
     */
    public static void addLiketoPost(String userId, String postId, likePostCallback myCallback){
        // open database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefPosts = db.getReference("/posts");

        // get specific post
        dbRefPosts.orderByKey().equalTo(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // for post
                for (DataSnapshot ds : snapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    assert post != null;

                    // get users liked list
                    ds.child("usersLiked").getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            // if user is not currently liking the post already
                            if (!snapshot.hasChild(userId)){

                                // add to likes and add user to usersLiked
                                ds.child("likes").getRef().setValue(post.likes + 1);
                                ds.child("usersLiked").getRef().child(userId).setValue(userId);
                                myCallback.onCallback(post.likes + 1);
                            }

                            // user already liked, don't change
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

    /**
     * Removes a like from a specific post. Also removes user from usersLiked field on post.
     *
     * @param userId - id of user that liked
     * @param postId - id of post
     * @param myCallback - sends back likes #
     */
    public static void removeLikefromPost(String userId, String postId, likePostCallback myCallback){
        // open database
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRefPosts = db.getReference("/posts");

        // finds specific post
        dbRefPosts.orderByKey().equalTo(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // get post
                for (DataSnapshot ds : snapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    assert post != null;


                    // get list of users liked
                    ds.child("usersLiked").getRef().getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            // if user has liked already
                            if(snapshot.hasChild(userId)){

                                // remove user and remove like
                                ds.child("usersLiked").getRef().child(userId).removeValue();
                                ds.child("likes").getRef().setValue(Math.max(post.likes - 1, 0));
                                myCallback.onCallback(post.likes -  1);
                            }

                            // error, user had not already liked
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

    /**
     * Returns all posts of subscribed houses by user's subscribed list.
     *
     * @param user - user in question
     * @param myCallback - returns posts
     */
    public static void getAllSubscribedPosts(User user, getAllPostsCallback myCallback){
        // open database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRefHouses = database.getReference("/houses");

        // get all houses
        dbRefHouses.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> postKeys = new ArrayList<>();

                // for house in houses
                for (DataSnapshot ds : snapshot.getChildren()){

                    // if user has subscribed to things
                    if (user.subscribedTo != null) {

                        // for each house they have subcscirbed to
                        for (String houseId : user.subscribedTo.keySet()) {

                            // if the house id of subscribe is the houseId of current house
                            if (Objects.equals(ds.getKey(), houseId)) {

                                // get all post ids from the house
                                for (DataSnapshot postKeyDs : ds.child("posts").getChildren()) {
                                    postKeys.add(postKeyDs.getValue(String.class));
                                }
                            }
                        }
                    }

                    // no subscriptions
                    else{
                        myCallback.onCallback(new ArrayList<>());
                    }
                }

                // open post node
                DatabaseReference dbRefPosts = database.getReference("/posts");
                dbRefPosts.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Post> posts = new ArrayList<>();

                        // for each post
                        for (DataSnapshot ds : snapshot.getChildren()){

                            // for each post key
                            for (String postKey : postKeys){

                                // if match, add post
                                if (Objects.equals(ds.getKey(), postKey)){
                                    posts.add(ds.getValue(Post.class));
                                }
                            }
                        }

                        // send back posts
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


