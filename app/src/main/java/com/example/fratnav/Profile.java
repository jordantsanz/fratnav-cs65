package com.example.fratnav;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Profile extends AppCompatActivity {
    BottomNavigationView bottomBar;
    ListView postListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        postListView = (ListView) findViewById(R.id.profileListView);

        //need to change to post
        ArrayList<String>arrayList= new ArrayList();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);

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
                    return true;
                }
                else if (item.getItemId()==R.id.home) {
                    Log.d("swtich", "home");
                    startActivity(new Intent(Profile.this, MainActivity.class));
                    return true;
                }
                else if  (item.getItemId()==R.id.forum){
                    Log.d("swtich", "forum");
                    startActivity(new Intent(Profile.this, Forum.class));
                    return true;
                }
                return false;
            }

        });

    }
}