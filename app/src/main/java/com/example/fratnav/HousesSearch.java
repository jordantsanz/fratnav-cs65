package com.example.fratnav;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HousesSearch extends AppCompatActivity {
    BottomNavigationView bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.houses_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomBar.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                Toast.makeText(HousesSearch.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                if (item.getItemId() == R.id.home){
                    Intent intent = new Intent(HousesSearch.this, MainActivity.class);
                    startActivity(intent);
                }
                if (item.getItemId() == R.id.forum){
                    Intent intent = new Intent(HousesSearch.this, Forum.class);
                    startActivity(intent);
                }
                if (item.getItemId() == R.id.profile){
                    Intent intent = new Intent(HousesSearch.this, Profile.class);
                    startActivity(intent);
                }
            }
        });
    }
}


