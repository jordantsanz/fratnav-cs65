package com.example.fratnav;

import android.content.Intent;
import android.os.Bundle;

import com.anychart.chart.common.dataentry.PertDataEntry;
import com.baoyachi.stepview.VerticalStepView;
import com.example.fratnav.forum.Forum;
import com.example.fratnav.houses.HousesSearch;
import com.example.fratnav.onboarding.Authentication;
import com.example.fratnav.profile.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomBar;
    private static FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("home", "home");
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();



        if (currentUser == null){
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);
            finish();
        }

        try {
            Log.d("cu", currentUser.toString());
        }
        catch(Exception e){
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);
            finish();
        }

        // checks to make sure the user is currently logged in; otherwise, send to authentication
        if (currentUser == null) {
            Log.d("null", "sup");
            startActivity(new Intent(this, Authentication.class));
            return;
        }
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.home);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId()==R.id.houses) {
                    Log.d("swtich", "houses");
                    startActivity(new Intent(MainActivity.this, HousesSearch.class));
                    finish();
                    return true;
                } else if (item.getItemId()==R.id.profile) {
                    Log.d("swtich", "profile");
                    startActivity(new Intent(MainActivity.this, Profile.class));
                    finish();
                    return true;
                } else if (item.getItemId()==R.id.forum) {
                    Log.d("swtich", "forum");
                    startActivity(new Intent(MainActivity.this, Forum.class));
                    finish();
                    return true;
                }
                return false;
            }

        });


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
//                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.default_icon));//设置StepsViewIndicator DefaultIcon
               // .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable.attention));//设置StepsViewIndicator AttentionIcon


    }
    //from GitHubRep
    private static class CustomPertDataEntry extends PertDataEntry {
        CustomPertDataEntry(String id, String description, String name, String fullName) {
            super(id, name, fullName);
            setValue("description", description);
        }

        CustomPertDataEntry(String id, String description, String name, String[] dependsOn, String fullName) {
            super(id, name, fullName, dependsOn);
            setValue("description", description);
        }
    }



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



    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
    }
//    if using menu on toolbar
//    public void logout(MenuItem item) {
//        FirebaseAuth.getInstance().signOut();
//        finishAffinity();
//    }
}
