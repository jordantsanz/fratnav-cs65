package com.example.fratnav;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {
    BottomNavigationView bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomBar.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                Toast.makeText(Profile.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                if (item.getItemId() == R.id.home){
                    Intent intent = new Intent(Profile.this, MainActivity.class);
                    startActivity(intent);
                }
                if (item.getItemId() == R.id.forum){
                    Intent intent = new Intent(Profile.this, Forum.class);
                    startActivity(intent);
                }
                if (item.getItemId() == R.id.houses){
                    Intent intent = new Intent(Profile.this, HousesSearch.class);
                    startActivity(intent);
                }

            }

        });
    }
}
