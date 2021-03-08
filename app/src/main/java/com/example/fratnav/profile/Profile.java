package com.example.fratnav.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.example.fratnav.MainActivity;
import com.example.fratnav.R;
import com.example.fratnav.callbacks.getAllHousesCallback;
import com.example.fratnav.callbacks.getAllPostsCallback;
import com.example.fratnav.callbacks.getAllReviewsCallback;
import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.HouseDatabaseHelper;
import com.example.fratnav.databaseHelpers.PostDatabaseHelper;
import com.example.fratnav.databaseHelpers.ReviewDatabaseHelper;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.forum.Forum;
import com.example.fratnav.houses.HousesSearch;
import com.example.fratnav.models.House;
import com.example.fratnav.models.Post;
import com.example.fratnav.models.Review;
import com.example.fratnav.models.User;


import com.example.fratnav.onboarding.Authentication;

import com.example.fratnav.tools.HouseAccounts;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Text;

import java.io.File;

import java.util.ArrayList;
import java.util.UUID;

public class Profile extends AppCompatActivity {
    BottomNavigationView bottomBar;
    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private EditText userText;
    private TextView helloUser;
    //PostsAdapter adapter;
    RVPostsAdapter adapter;
    RVReviewsAdapter adapterReviews;
    public ArrayList<Review> arrayOfReviews;

    public ArrayList<Post> arrayOfPosts;
    DatabaseReference.CompletionListener completionListener;
    ListView postListView;
    public static String affiliated;
    public static String userName;
    public static String sexuality;
    public static String year;
    public static String gender;
    public static TextView profileSexuality;
    public static TextView profileUsername;
    public static TextView profileGender;
    public static TextView profileYear;
    public static TextView profileAffiliated;
    public User currentUserInfo;
    public String useriD;

    boolean ishouse;

    public static final String HOUSE_BOOLEAN_KEY = "houseboolkey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getStringExtra(Forum.USER_ID_KEY) == null) {
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            // checks to make sure the user is currently logged in; otherwise, send to authentication
            if (currentUser == null) {
                startActivity(new Intent(this, Authentication.class));
                finish();
                return;
            }
            useriD = currentUser.getUid();
            ishouse = getIntent().getBooleanExtra(MainActivity.USER_HOUSE_BOOL, false);
            Log.d("ishouse", String.valueOf(ishouse));
            if (ishouse) {
                setContentView(R.layout.house_profile);
                setHouseProfile();
            } else {
                setContentView(R.layout.profile);
                setUserProfile();
            }
        }
        else{
            useriD = getIntent().getStringExtra(Forum.USER_ID_KEY);
            UserDatabaseHelper.getUserById(useriD, new getUserByIdCallback() {
                @Override
                public void onCallback(User user) {
                    currentUserInfo = user;
                    if (currentUserInfo.house){
                        setContentView(R.layout.house_profile);
                        setHouseProfile();
                    }
                    else{
                        setContentView(R.layout.profile);
                        setUserProfile();
                    }
                }
            });
        }
    }

    public static void refresh() {
        UserDatabaseHelper.getUserById(currentUser.getUid(), new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {
                Log.d("username", user.username);
                if (user.username != null) {
                    userName = user.username;
                } else {
                    userName = "N/A";
                }
                if (user.sexuality != null) {
                    sexuality = user.sexuality;
                } else {
                    sexuality = "N/A";
                }
                if (user.year != null) {
                    year = user.year;
                } else {
                    year = "N/A";
                }
                if (user.gender != null) {
                    gender = user.gender;
                } else {
                    gender = "N/A";
                }
                if (user.houseAffiliation) {
                    affiliated = "Yes";
                } else {
                    affiliated = "No";
                }
                profileUsername.setText(userName);
                profileSexuality.setText(sexuality);
                profileGender.setText(gender);
                profileYear.setText(year);
                profileAffiliated.setText(affiliated);
            }
        });
    }

    private void notifyUser(String message) {
        Toast.makeText(Profile.this, message,
                Toast.LENGTH_SHORT).show();
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
    }


    public void edit(View view) {

        Intent intent = new Intent(this, updateProfile.class);
        intent.putExtra(HOUSE_BOOLEAN_KEY, ishouse);
        startActivity(intent);

    }


    public void setHouseProfile(){
        Log.d("houseProf", "setHouseProfile: ");;
        setNavBar();
        UserDatabaseHelper.getUserById(currentUser.getUid(), new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {
                TextView houseName = findViewById(R.id.house_name);
                houseName.setText(user.username);

                setNotifications(user);
                renderPosts();
                renderReviews();
            }
        });

    }

    public void setUserProfile(){

        UserDatabaseHelper.getUserById(currentUser.getUid(), new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {

                profileUsername = (TextView) findViewById(R.id.profileUsername);
                profileSexuality = (TextView) findViewById(R.id.sexualityResponse);
                profileGender = (TextView) findViewById(R.id.genderResponse);
                profileYear = (TextView) findViewById(R.id.yearResponse);
                profileAffiliated = (TextView) findViewById(R.id.affiliatedResponse);


                if (user.username != null){
                    userName= user.username;
                }
                else{
                    userName="N/A";
                }
                if(user.sexuality!=null) {
                    sexuality = user.sexuality;
                }
                else{
                    sexuality="N/A";
                }
                if(user.year != null) {
                    year = user.year;
                }
                else{
                    year = "N/A";
                }
                if(user.gender!=null) {
                    gender = user.gender;
                }
                else{
                    gender = "N/A";
                }
                if (user.houseAffiliation){
                    affiliated= "Yes";
                }
                else{
                    affiliated = "No";
                }
                profileUsername.setText(userName);
                profileSexuality.setText(sexuality);
                profileGender.setText(gender);
                profileYear.setText(year);
                profileAffiliated.setText(affiliated);

                setNavBar();
                setNotifications(user);
                renderPosts();
                renderReviews();
                renderHousesSubscribed(user);


            }


        });

    }


    public void renderPosts(){

        // get all posts
        arrayOfPosts = new ArrayList<>();
        PostDatabaseHelper.getAllPostsByUser(useriD, new getAllPostsCallback() {
            @Override
            public void onCallback(ArrayList<Post> posts) {
                Log.d("posts", posts.toString());;
                for (int i = posts.size() - 1; i > -1; i--){
                    Post post = posts.get(i);
                    arrayOfPosts.add(post);
                }
                adapter.notifyDataSetChanged();
            }
        });

        // Construct the data source
        if (arrayOfPosts == null) {
            arrayOfPosts = new ArrayList<Post>();
        }


        // create recylcerview
        RecyclerView recyclerView = findViewById(R.id.rv_posts);
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(
                        Profile.this,
                        LinearLayoutManager.HORIZONTAL,
                        false);

        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.HORIZONTAL));

        adapter = new RVPostsAdapter(getApplicationContext(), arrayOfPosts);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }


    public void renderReviews(){

        arrayOfReviews = new ArrayList<>();
        ReviewDatabaseHelper.getReviewsByUserId(useriD, new getAllReviewsCallback() {
            @Override
            public void onCallback(ArrayList<Review> reviews) {
                Log.d("reviews", reviews.toString());
                for (int i = reviews.size() - 1; i > -1; i--){
                    Review review = reviews.get(i);
                    arrayOfReviews.add(review);
                }
                adapterReviews.notifyDataSetChanged();
            }
        });


        RecyclerView recyclerViewreviews = findViewById(R.id.rv_reviews);
        LinearLayoutManager horizontalLayoutManager2 =
                new LinearLayoutManager(Profile.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewreviews.setLayoutManager(horizontalLayoutManager2);
        recyclerViewreviews.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.HORIZONTAL));
        adapterReviews = new RVReviewsAdapter(getApplicationContext(), arrayOfReviews);
        recyclerViewreviews.setAdapter(adapterReviews);

        adapterReviews.notifyDataSetChanged();

    }


    public void setNotifications(User user){

        RadioButton notifOn = findViewById(R.id.notificationOn);
        RadioButton notifOff = findViewById(R.id.notificationOff);

        notifOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDatabaseHelper.updateUserNotifSettings(currentUser.getUid(), true);
            }
        });

        notifOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDatabaseHelper.updateUserNotifSettings(currentUser.getUid(), false);
            }
        });
        if (user.notificationSettings){
            notifOn.setChecked(true);
        }
        else{
            notifOff.setChecked(true);
        }

    }


    public void renderHousesSubscribed(User user){
        HouseDatabaseHelper.getHousesByUserSubscribed(user.subscribedTo, new getAllHousesCallback() {
            @Override
            public void onCallback(ArrayList<House> houses) {
                Log.d("housesFound", houses.toString());
            }
        });
    }


    public void setNavBar(){
        Log.d("navbar", "setNavBar: ");;
        bottomBar = (BottomNavigationView) findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.profile);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("sad", "made it item clicked " + item.getTitle());
                Toast.makeText(Profile.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                if (item.getItemId()==R.id.houses) {
                    Log.d("swtich", "houses");
                    startActivity(new Intent(Profile.this, HousesSearch.class));
                    finish();
                    return true;
                }
                else if (item.getItemId()==R.id.home) {
                    Log.d("swtich", "home");
                    startActivity(new Intent(Profile.this, MainActivity.class));
                    finish();
                    return true;
                }
                else if  (item.getItemId()==R.id.forum){
                    Log.d("swtich", "forum");
                    startActivity(new Intent(Profile.this, Forum.class));
                    finish();
                    return true;
                }
                return false;
            }

        });

    }
}