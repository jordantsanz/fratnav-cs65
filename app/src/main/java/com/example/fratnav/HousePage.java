package com.example.fratnav;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fratnav.callbacks.getHouseByIdCallback;
import com.example.fratnav.databaseHelpers.HouseDatabaseHelper;
import com.example.fratnav.models.House;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

public class HousePage extends AppCompatActivity {
    BottomNavigationView bottomBar;
    public House theHouse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.houses_page);
        Log.d("House", "house");
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        bottomBar = (BottomNavigationView) findViewById(R.id.bottomBar);
        if (bottomBar != null) {
            bottomBar.setSelectedItemId(R.id.houses);
            bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Log.d("sad", "made it item clicked " + item.getTitle());
                    Toast.makeText(HousePage.this, item.getTitle(), Toast.LENGTH_SHORT).show();

                    if (item.getItemId() == R.id.home) {
                        Log.d("swtich", "home");
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                        return true;
                    } else if (item.getItemId() == R.id.profile) {
                        Log.d("swtich", "profile");
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        finish();
                        return true;
                    } else if (item.getItemId() == R.id.forum) {
                        Log.d("swtich", "forum");
                        startActivity(new Intent(getApplicationContext(), Forum.class));
                        finish();
                        return true;
                    }
                    return false;
                }
            });
        }

        TextView houseNameTextView = findViewById(R.id.house_name);
        String name = getIntent().getStringExtra(HousesSearch.HOUSE_NAME_KEY);
        HouseDatabaseHelper.getHouseByName(name, new getHouseByIdCallback() {
            @Override
            public void onCallback(House house) {
                theHouse = house;
                Log.d("house", theHouse.toString());
                TextView houseDateView = findViewById(R.id.house_date);
                houseDateView.setText(String.valueOf(theHouse.date));

                TextView houseNationalView = findViewById(R.id.house_national);
                String n = "";
                if (theHouse.national){
                    n = "National";
                }
                else{
                    n = "Local";
                }

                houseNationalView.setText(n);
            }
        });
        houseNameTextView.setText(name);

    }


    public void onBackClick(View view) {
        startActivity(new Intent(HousePage.this,HousesSearch.class));
        finish();
    }
}

