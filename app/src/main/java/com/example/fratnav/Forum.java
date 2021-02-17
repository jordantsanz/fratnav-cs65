package com.example.fratnav;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Forum extends AppCompatActivity {
    BottomNavigationView bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum);
        Log.d("Forum", "forum");
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        bottomBar = (BottomNavigationView) findViewById(R.id.bottomBar);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("sad", "made it item clicked " + item.getTitle());
                Toast.makeText(Forum.this, item.getTitle(), Toast.LENGTH_SHORT).show();


                Log.d("rad", "didn;t make it");
                switch (item.getItemId()) {
                    case R.id.houses:
                        Log.d("swtich", "houses");
                        startActivity(new Intent(getApplicationContext(), HousesSearch.class));
                    case R.id.profile:
                        Log.d("swtich", "profile");
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        Log.d("swtich", "forum");
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                return true;
            }
        });
    }
}


