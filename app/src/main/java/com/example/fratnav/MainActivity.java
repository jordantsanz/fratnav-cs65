package com.example.fratnav;

import android.content.Intent;
import android.os.Bundle;


import com.baoyachi.stepview.VerticalStepView;

import com.example.fratnav.callbacks.getAllPostsCallback;
import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.PostDatabaseHelper;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;

import com.example.fratnav.forum.Forum;
import com.example.fratnav.houses.HousesSearch;
import com.example.fratnav.models.Post;
import com.example.fratnav.models.User;
import com.example.fratnav.onboarding.Authentication;
import com.example.fratnav.profile.Profile;
import com.example.fratnav.tools.RVFeedAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    BottomNavigationView bottomBar;
    private static FirebaseUser currentUser;
    public static final String USER_HOUSE_BOOL = "userbool";
    boolean isHouse;
    ArrayList<Post> arrayOfPosts;
    public User currentUserInfo;
    RVFeedAdapter feedAdapter;
    RecyclerView recyclerViewfeed;
    public String randomKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets the XML
        setContentView(R.layout.activity_main);

        //gets the current user
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
                //gets the user from the database
                currentUserInfo = user;

                //get the post to display on the feed
                PostDatabaseHelper.getAllSubscribedPosts(user, new getAllPostsCallback() {
                    @Override
                    public void onCallback(ArrayList<Post> posts) {

                        for (int i = posts.size() - 1; i > -1; i--){

                            Post post = posts.get(i);
                            arrayOfPosts.add(post);
                        }
                        feedAdapter.notifyDataSetChanged();


                    }
                });


            }
        });
        // Construct the data source
        if (arrayOfPosts == null) {
            arrayOfPosts = new ArrayList<Post>();
        }
        //connects recycler view to the adapter and updates
        recyclerViewfeed = (RecyclerView) findViewById(R.id.rv_feedposts);
        LinearLayoutManager verticalLayoutManager =
                new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewfeed.setLayoutManager(verticalLayoutManager);

        feedAdapter = new RVFeedAdapter(getApplicationContext(), arrayOfPosts);
        recyclerViewfeed.setAdapter(feedAdapter);

        feedAdapter.notifyDataSetChanged();





        //checks if user is logged in
        if (currentUser == null){
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);
            finish();
        }

        //checks if user is a house
        UserDatabaseHelper.getUserById(currentUser.getUid(), new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {
                isHouse = user.house;
            }
        });

        try {

        }
        catch(Exception e){
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);
            finish();
        }

        // checks to make sure the user is currently logged in; otherwise, send to authentication
        if (currentUser == null) {
            startActivity(new Intent(this, Authentication.class));
            return;
        }

        //navBar
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.home);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId()==R.id.houses) {

                    startActivity(new Intent(MainActivity.this, HousesSearch.class));
                    finish();
                    return true;
                } else if (item.getItemId()==R.id.profile) {

                    Intent intent = new Intent(MainActivity.this, Profile.class);
                    intent.putExtra(MainActivity.USER_HOUSE_BOOL, isHouse);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (item.getItemId()==R.id.forum) {

                    startActivity(new Intent(MainActivity.this, Forum.class));
                    finish();
                    return true;
                }
                return false;
            }

        });

        //follow the github implementation
        VerticalStepView setpview5 = (VerticalStepView) findViewById(R.id.step_view0);
        List<String> list0 = new ArrayList<>();
        list0.add(getResources().getString(R.string.TwentyOneSpring));
        list0.add(getResources().getString(R.string.TwentyOneSummer));
        list0.add(getResources().getString(R.string.TwentyOneFall));
        list0.add(getResources().getString(R.string.TwentyTwoWinter));


        setpview5//设置完成的步数
                .reverseDraw(false)//default is true
                .setStepViewTexts(list0)//总步骤
//                .setLinePaddingProportion(0.85f)//设置indicator线与线间距的比例系数
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(this, android.R.color.white))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(this, R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(this, android.R.color.white))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(this, R.color.uncompleted_text_color))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.default_icon));//设置StepsViewIndicator DefaultIcon


    }



    //inflates the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //log out
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
    }

}
