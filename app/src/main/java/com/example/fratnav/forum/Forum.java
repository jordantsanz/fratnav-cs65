package com.example.fratnav.forum;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.fratnav.callbacks.likePostCallback;
import com.example.fratnav.onboarding.Authentication;
import com.example.fratnav.houses.HousesSearch;
import com.example.fratnav.MainActivity;
import com.example.fratnav.profile.Profile;
import com.example.fratnav.R;
import com.example.fratnav.callbacks.getAllPostsCallback;
import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.PostDatabaseHelper;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.models.Post;
import com.example.fratnav.models.User;
import com.example.fratnav.tools.PostsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Forum extends ListActivity {
    BottomNavigationView bottomBar;
    private static FirebaseUser currentUser;
    private static User currentUserInfo;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRefPosts;
    private DatabaseReference dbRefUsers;
    private DatabaseReference dbRefHouses;
    private DatabaseReference dbRefReviews;
    private EditText userText;
    private TextView helloUser;
    DatabaseReference.CompletionListener completionListener;
    public static ArrayList<Post> arrayOfPosts;
    public String randomKey;
    Dialog filterDialog;

    public static final String POST_ID_KEY = "postid_key";
    public static final String USER_ID_KEY = "userid_key";
    public static PostsAdapter adapter;
    ImageView filter;
    PopupWindow popupWindow;
    LayoutInflater layoutInflater;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // checks to make sure the user is currently logged in; otherwise, send to authentication
        if (currentUser == null) {
            startActivity(new Intent(this, Authentication.class));
            finish();
            return;
        }
        UserDatabaseHelper.getUserById(currentUser.getUid(), new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {
                currentUserInfo = user;
            }
        });

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.forumCoordinatorLayout);
        filter = (ImageView) findViewById(R.id.filter);



        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.tag_filter_popup, null);
                popupWindow = new PopupWindow(container, 520, 240, true);
                Log.d("popupWindo", popupWindow.toString());
                popupWindow.showAtLocation(coordinatorLayout, Gravity.NO_GRAVITY, 250, 240);
                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });



        bottomBar = (BottomNavigationView) findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.forum);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("sad", "made it item clicked " + item.getTitle());
                if (item.getItemId()==R.id.houses) {
                    Log.d("swtich", "houses");
                    startActivity(new Intent(Forum.this, HousesSearch.class));
                    finish();
                    return true;
                } else if (item.getItemId()==R.id.profile) {
                    Log.d("swtich", "profile");
                    startActivity(new Intent(Forum.this, Profile.class));
                    finish();
                    return true;
                } else if (item.getItemId()==R.id.home) {
                    Log.d("swtich", "home");
                    startActivity(new Intent(Forum.this, MainActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });


        // checks to see if data has been entered
        completionListener =
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {

                        if (databaseError != null) {
                            notifyUser(databaseError.getMessage());
                        }
                    }
                };




        database = FirebaseDatabase.getInstance();
        dbRefPosts = database.getReference("/posts");

        PostDatabaseHelper.getAllPosts(new getAllPostsCallback() {
            @Override
            public void onCallback(ArrayList<Post> posts) {
                Log.d("Posts", posts.toString());
                for (int i = posts.size() - 1; i > -1; i--){
                    adapter.add(posts.get(i));
                }
                adapter.notifyDataSetChanged();
            }
        });

        // Construct the data source
        arrayOfPosts = new ArrayList<Post>();
        // Create the adapter to convert the array to views
        adapter = new PostsAdapter(this, arrayOfPosts);
        // Attach the adapter to a ListView
        ListView list = findViewById(android.R.id.list);
        list.setAdapter(adapter); // sets adapter for list



        // on click listener for posts
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position != -1) {
                    Log.d("position", String.valueOf(position));
                    long viewId = view.getId();
                    Log.d("click", String.valueOf(viewId));
                    Post post = adapter.getItem(position);


                    Intent intent = new Intent(Forum.this, PostActivity.class);
                    intent.putExtra(POST_ID_KEY, post.id);
                    intent.putExtra(USER_ID_KEY, currentUserInfo.username);


                    startActivity(intent);
                }}});

        }



    // if error
    private void notifyUser(String message) {
        Toast.makeText(Forum.this, message,
                Toast.LENGTH_SHORT).show();
    }



    // saves post currently
    public void savePost(View view) {
        Post post = new Post(currentUserInfo.username, currentUser.getUid(), userText.getText().toString(),
                new ArrayList<>(), new HashMap<>(), 0);

        DatabaseReference pushRef = dbRefPosts.push();

        post.id = pushRef.getKey();

        pushRef.setValue(post);

        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        arrayOfPosts.add(0, post);

        adapter.notifyDataSetChanged();

        UserDatabaseHelper.addPostToUser(post, currentUser);

        Toast.makeText(this, "Post successfully created.", Toast.LENGTH_SHORT).show();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void openPostCreate(View view) {
        Intent intent = new Intent (Forum.this, CreatePost.class);
        startActivity(intent);
    }


    public static void refresh(){
        PostDatabaseHelper.getAllPosts(new getAllPostsCallback() {
            @Override
            public void onCallback(ArrayList<Post> posts) {
                arrayOfPosts = posts;
                adapter.notifyDataSetChanged();
            }
        });

    }

    public void onHeartClick(View v){
        View parentRow = (View) v.getParent().getParent();
        ListView lv = (ListView) parentRow.getParent();
        int position = lv.getPositionForView(parentRow);
        if (position == -1){
            return;
        }
        Post post = adapter.getItem(position);
        assert post != null;
        Log.d("heartClick", post.id);

        if (randomKey == null){
            randomKey = UUID.randomUUID().toString();
        }

        boolean userDidLike = false;

        ImageView heart = findViewById(R.id.heart);
        heart.getTag();


        if (post.usersLiked != null){
            Log.d("userDidLike", post.usersLiked.toString());
            for (String userId : post.usersLiked.values()){
                Log.d("userDidLike", userId + ", " + currentUserInfo.userID);
                if (userId.equals(currentUserInfo.userID)){
                    userDidLike = true;
                    break;
                }
            }
        }


                if (userDidLike){
                    PostDatabaseHelper.removeLikefromPost(currentUserInfo.userID, post.id, new likePostCallback() {
                        @Override
                        public void onCallback(int likes) {
                            Forum.refresh();
                            Log.d("like", "removeLike");
                            post.usersLiked.remove(currentUserInfo.userID, currentUserInfo.userID);
                            post.likes -= 1;
                            Log.d("postprof", post.usersLiked.toString());


                            /// need to change heart image drawable here
                            heart.setBackgroundResource(R.drawable.like);

                        }
                    });


                }
                else{
                    PostDatabaseHelper.addLiketoPost(currentUserInfo.userID, post.id, new likePostCallback() {
                        @Override
                        public void onCallback(int likes) {
                            Log.d("like", "addLike");
                            Forum.refresh();
                            if (post.usersLiked == null){
                                post.usersLiked = new HashMap<>();
                            }
                            post.usersLiked.put(currentUserInfo.userID, currentUserInfo.userID);
                            post.likes += 1;




                        }
                    });

                }
            }
}