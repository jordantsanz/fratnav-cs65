package com.example.fratnav;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.fratnav.callbacks.getAllHousesCallback;
import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.AuthenticationHelper;
import com.example.fratnav.databaseHelpers.HouseDatabaseHelper;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.models.House;
import com.example.fratnav.models.HouseCardView;
import com.example.fratnav.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HousesSearch extends AppCompatActivity {
    BottomNavigationView bottomBar;
    GridLayout gridLayout;
    FirebaseUser user;

    public static final String HOUSE_NAME_KEY = "housekey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.houses_search);
        Log.d("House", "house");
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        gridLayout = (GridLayout) findViewById(R.id.grid_layout);

        HouseDatabaseHelper.getAllHouses(new getAllHousesCallback() {
            @Override
            public void onCallback(ArrayList<House> houses) {
                for (House house : houses){
                    HouseCardView housecard = new HouseCardView(house.houseName, getApplicationContext());
                    CardView cardView = housecard.makeCardView();

                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                    Toast.makeText(MainActivity.this,"Clicked at index "+ finalI,
//                            Toast.LENGTH_SHORT).show();
                            ViewGroup viewGroup = (ViewGroup) view;
                            ViewGroup linearLayout = (ViewGroup) viewGroup.getChildAt(0);
                            TextView houseNameTextView = (TextView) linearLayout.getChildAt(0);

                            Intent intent = new Intent(HousesSearch.this,HousePage.class);
                            intent.putExtra(HOUSE_NAME_KEY, houseNameTextView.getText().toString());
                            startActivity(intent);
                        }
                    });
                    gridLayout.addView(cardView);


                }
            }
        });


        bottomBar = (BottomNavigationView) findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.houses);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("sad", "made it item clicked " + item.getTitle());
                Toast.makeText(HousesSearch.this, item.getTitle(), Toast.LENGTH_SHORT).show();

                if (item.getItemId()==R.id.home) {
                    Log.d("swtich", "home");
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                }
                else if (item.getItemId()==R.id.profile){
                    Log.d("swtich", "profile");
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                    return true;
                }
                else if (item.getItemId()==R.id.forum){
                    Log.d("swtich", "forum");
                    startActivity(new Intent(getApplicationContext(), Forum.class));
                    return true;
                }
                return false;
            }
        });

    }
}


