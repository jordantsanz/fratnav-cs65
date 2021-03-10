package com.example.fratnav.profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fratnav.MainActivity;
import com.example.fratnav.R;
import com.example.fratnav.tools.RVFeedAdapter;
import com.example.fratnav.callbacks.getAllHousesCallback;
import com.example.fratnav.callbacks.getAllPostsCallback;
import com.example.fratnav.callbacks.getAllReviewsCallback;
import com.example.fratnav.callbacks.getHouseByIdCallback;
import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.HouseDatabaseHelper;
import com.example.fratnav.databaseHelpers.PostDatabaseHelper;
import com.example.fratnav.databaseHelpers.ReviewDatabaseHelper;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.forum.Forum;
import com.example.fratnav.forum.PostActivity;
import com.example.fratnav.houses.HousesSearch;
import com.example.fratnav.models.House;
import com.example.fratnav.models.HouseCardView;
import com.example.fratnav.models.Post;
import com.example.fratnav.models.Review;
import com.example.fratnav.models.User;


import com.example.fratnav.onboarding.Authentication;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Profile extends AppCompatActivity implements View.OnClickListener {
    BottomNavigationView bottomBar;
    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private EditText userText;
    private TextView helloUser;
    RVFeedAdapter adapter;
    RVReviewsAdapter adapterReviews;
    GridLayout gridLayout;
    public ArrayList<Review> arrayOfReviews;
    public ArrayList<Post> arrayOfPosts;
    public ArrayList<House> arrayofSubscribedTo;
    DatabaseReference.CompletionListener completionListener;
    ListView postListView;
    public static String affiliated;
    public static String userName;
    public static String sexuality;
    public static String year;
    public static String gender;
    public RecyclerView recyclerView;
    public static TextView profileSexuality;
    public static TextView profileUsername;
    public static TextView profileGender;
    public static TextView profileYear;
    public static TextView profileAffiliated;
    public User currentUserInfo;
    public static String useriD;
    public static final String RESET_KEY = "reset";

    boolean ishouse;

    public static final String HOUSE_BOOLEAN_KEY = "houseboolkey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //conditional rendering for house v. user accounts
        if (getIntent().getStringExtra(Forum.USER_ID_KEY) == null || getIntent().getStringExtra(RESET_KEY) != null) {
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            // checks to make sure the user is currently logged in; otherwise, send to authentication
            if (currentUser == null) {
                startActivity(new Intent(this, Authentication.class));
                finish();
                return;
            }
            useriD = currentUser.getUid();
            ishouse = getIntent().getBooleanExtra(MainActivity.USER_HOUSE_BOOL, false);

            //sets appropriate XML
            if (ishouse) {
                setContentView(R.layout.house_profile);
                setHouseProfile();
            } else {
                setContentView(R.layout.profile);
                setUserProfile(false);
            }
        }
        else{
            //viewing the profile and information rendered as such
            useriD = getIntent().getStringExtra(Forum.USER_ID_KEY);
            UserDatabaseHelper.getUserById(useriD, new getUserByIdCallback() {
                @Override
                public void onCallback(User user) {
                    currentUserInfo = user;
                        setContentView(R.layout.view_profile);
                        setUserProfile(true);

                }
            });
        }
    }
    //refreshed the information as necessary
    public static void refresh() {
        // calls helper method to render user info
        UserDatabaseHelper.getUserById(useriD, new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {

                //sets the attributes input-ed during the onboarding process
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
                // Populate the data into the view using the data object
                String userNameString = "@"+ userName;
                profileUsername.setText(userNameString);
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

    //logouts the user
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
    }

    // method to open update profile when user wants to edit
    public void edit(View view) {
        Intent intent = new Intent(this, updateProfile.class);
        intent.putExtra(HOUSE_BOOLEAN_KEY, ishouse);
        startActivity(intent);

    }

    //sets the house profile
    public void setHouseProfile(){
        setNavBar();
        UserDatabaseHelper.getUserById(useriD, new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {
                TextView houseName = findViewById(R.id.house_name);
                String userNameString ="@"+user.username;
                houseName.setText(userNameString);

                HouseDatabaseHelper.getHouseById(user.houseId, new getHouseByIdCallback() {
                    @Override
                    public void onCallback(House house) {
                        TextView subscribersText = findViewById(R.id.subscribers);
                        String subs = String.valueOf(house.subscribers) + " Subscribers";
                        subscribersText.setText(subs);
                        setHouseImage(house);
                    }
                });

                setNotifications(user);
                renderPosts();
                renderReviews();
            }
        });

    }
    //sets the user profile while checking if they are viewing or not another users profile or their own
    public void setUserProfile(boolean viewing){

        UserDatabaseHelper.getUserById(useriD, new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {

                profileUsername = (TextView) findViewById(R.id.profileUsername);
                profileSexuality = (TextView) findViewById(R.id.sexualityResponse);
                profileGender = (TextView) findViewById(R.id.genderResponse);
                profileYear = (TextView) findViewById(R.id.yearResponse);
                profileAffiliated = (TextView) findViewById(R.id.affiliatedResponse);

                if (!viewing) {
                    RadioButton notifOn = findViewById(R.id.notificationOn);
                    RadioButton notifOff = findViewById(R.id.notificationOff);

                    notifOn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserDatabaseHelper.updateUserNotifSettings(useriD, true);
                        }
                    });

                    notifOff.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UserDatabaseHelper.updateUserNotifSettings(useriD, false);
                        }
                    });

                }
                arrayofSubscribedTo = new ArrayList<>();
                gridLayout = (GridLayout)findViewById(R.id.profile_grid);

                HouseDatabaseHelper.getHousesByUserSubscribed(user.subscribedTo, new getAllHousesCallback() {
                    @Override
                    public void onCallback(ArrayList<House> houses) {
                        for (int i = houses.size() -1; i > -1; i --){

                            House house = houses.get(i);
                            arrayofSubscribedTo.add(house);
                        }

                        for (House house : arrayofSubscribedTo) {
                            HouseCardView housecard = new HouseCardView(house.houseName, getApplicationContext(), house.imageName);
                            CardView cardView = housecard.makeSmallCardView();

                            cardView.setCardBackgroundColor(Color.parseColor("#2D2F35"));

                            gridLayout.addView(cardView);

                        }
                    }
                });

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
                String userNameString = "@"+ userName;
                profileUsername.setText(userNameString);
                profileSexuality.setText(sexuality);
                profileGender.setText(gender);
                profileYear.setText(year);
                profileAffiliated.setText(affiliated);

                setNavBar();
                if (!viewing) {
                    setNotifications(user);
                }
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
        recyclerView = findViewById(R.id.rv_posts);
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(
                        Profile.this,
                        LinearLayoutManager.HORIZONTAL,
                        false);
        recyclerView.setLayoutManager(horizontalLayoutManager);


        adapter = new RVFeedAdapter(getApplicationContext(), arrayOfPosts);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    //renders the reviews on the given page
    public void renderReviews(){

        arrayOfReviews = new ArrayList<>();
        ReviewDatabaseHelper.getReviewsByUserId(useriD, new getAllReviewsCallback() {
            @Override
            public void onCallback(ArrayList<Review> reviews) {
                for (int i = reviews.size() - 1; i > -1; i--){
                    Review review = reviews.get(i);
                    arrayOfReviews.add(review);
                }
                adapterReviews.notifyDataSetChanged();
            }
        });

        if (arrayOfReviews == null) {
            arrayOfReviews = new ArrayList<Review>();
        }

        //connects the array list to the recycler view
        RecyclerView recyclerViewreviews = findViewById(R.id.rv_reviews);
        LinearLayoutManager horizontalLayoutManager2 =
                new LinearLayoutManager(Profile.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewreviews.setLayoutManager(horizontalLayoutManager2);

        adapterReviews = new RVReviewsAdapter(getApplicationContext(), arrayOfReviews);
        recyclerViewreviews.setAdapter(adapterReviews);

        adapterReviews.notifyDataSetChanged();

    }

    // checks if notificaiton are on, if they are they allow notifications
    public void setNotifications(User user){

        RadioButton notifOn = findViewById(R.id.notificationOn);
        RadioButton notifOff = findViewById(R.id.notificationOff);

        if (notifOn != null) {
            notifOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserDatabaseHelper.updateUserNotifSettings(useriD, true);
                }
            });

            notifOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserDatabaseHelper.updateUserNotifSettings(useriD, false);
                }
            });
            if (user.notificationSettings) {
                notifOn.setChecked(true);
            } else {
                notifOff.setChecked(true);
            }
        }

    }

    //renders the houses the user is subscribed to
    public void renderHousesSubscribed(User user){
        if (arrayofSubscribedTo == null) {
            arrayofSubscribedTo = new ArrayList<House>();
        }
        HouseDatabaseHelper.getHousesByUserSubscribed(user.subscribedTo, new getAllHousesCallback() {
            @Override
            public void onCallback(ArrayList<House> houses) {

            }
        });
    }

    //set the nav bar for any type of account
    public void setNavBar(){
        bottomBar = (BottomNavigationView) findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.profile);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.houses) {
                    startActivity(new Intent(Profile.this, HousesSearch.class));
                    finish();
                    return true;
                }
                else if (item.getItemId()==R.id.home) {
                    startActivity(new Intent(Profile.this, MainActivity.class));
                    finish();
                    return true;
                }
                else if  (item.getItemId()==R.id.forum){
                    startActivity(new Intent(Profile.this, Forum.class));
                    finish();
                    return true;
                }
                else if (item.getItemId()==R.id.profile){
                    Intent intent = new Intent(Profile.this, Profile.class);
                    intent.putExtra(RESET_KEY, "reset");
                    UserDatabaseHelper.getUserById(currentUser.getUid(), new getUserByIdCallback() {
                        @Override
                        public void onCallback(User user) {
                            currentUserInfo = user;
                            if (currentUserInfo.house){
                                intent.putExtra(MainActivity.USER_HOUSE_BOOL, true);
                            }
                            else{
                                intent.putExtra(MainActivity.USER_HOUSE_BOOL, false);
                            }
                            startActivity(intent);
                            finish();

                        }
                    });
                    return true;

                }
                return false;
            }

        });

    }
    //checks which user is clicked and opens that PostActivity to comment
    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildAdapterPosition(v);
        if (position!=-1) {

            long viewId = v.getId();

            Post post = adapter.getItem(position);


            Intent intent = new Intent(Profile.this, PostActivity.class);
            intent.putExtra(Forum.POST_ID_KEY, post.id);
            intent.putExtra(Forum.USER_ID_KEY, currentUserInfo.username);


            startActivity(intent);
        }
    }

    //sets the appropriate image for the house account
    public void setHouseImage(House house){
            int image = 0;
            switch (house.houseName) {
                case "Sig Nu":
                    image = R.drawable.signu1;
                    break;
                case "TDX":
                    image = R.drawable.tdx;
                    break;
                case "Zete":
                    image = R.drawable.zetapsi;
                    break;
                case "Sigma Delt":
                    image = R.drawable.sigdelt;
                    break;
                case "Tabard":
                    image = R.drawable.tabard;
                    break;
                case "Tri-Kap":
                    image = R.drawable.trikap;
                    break;
                case "Kappa":
                    image = R.drawable.kkg;
                    break;
                case "Hereot":
                    image = R.drawable.heorot;
                    break;
                case "Phi Delt":
                    image = R.drawable.phidelta;
                    break;
                case "KD":
                    image = R.drawable.kd;
                    break;
                case "KDE":
                    image = R.drawable.kde;
                    break;
                case "Phi Tau":
                    image = R.drawable.phitau;
                    break;
                case "Alpha Chi":
                    image = R.drawable.ic_axa;
                    break;
                case "GDX":
                    image = R.drawable.gdx;
                    break;
                case "Psi U":
                    image = R.drawable.psiu;
                    break;
                case "Chi Delt":
                    image = R.drawable.chidelt;
                    break;
                case "Chi Gam":
                    image = R.drawable.chigam;
                    break;
                case "EKT":
                    image = R.drawable.ekt;
                    break;
                case "Deltas":
                    image = R.drawable.deltas;
                    break;
                case "AXiD":
                    image = R.drawable.axid;
                    break;
                case "Beta":
                    image = R.drawable.beta;
                    break;
                case "BG":
                    image = R.drawable.bg;
                    break;
                case "APhi":
                    image = R.drawable.aphi1;
                    break;
                case "Alpha Theta":
                    image = R.drawable.alphatheta;
                    break;
                case "Alphas":
                    image = R.drawable.alphas;
                    break;
                case "AKA":
                    image = R.drawable.aka;
                    break;
                default:
                    break;
            }
            //sets the image
            ImageView iv = (ImageView) findViewById(R.id.housePageImage);
            iv.setImageDrawable(ResourcesCompat.getDrawable(getApplicationContext().getResources(), image, null));
    }
}