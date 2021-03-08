package com.example.fratnav.houses;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.fratnav.MainActivity;
import com.example.fratnav.profile.Profile;
import com.example.fratnav.R;
import com.example.fratnav.callbacks.getAllHousesCallback;
import com.example.fratnav.databaseHelpers.HouseDatabaseHelper;
import com.example.fratnav.forum.Forum;
import com.example.fratnav.models.House;
import com.example.fratnav.models.HouseCardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class HousesSearch extends AppCompatActivity {
    BottomNavigationView bottomBar;
    GridLayout gridLayout;
    FirebaseUser user;
    ImageView filter;
    PopupWindow popupWindow;
    LayoutInflater layoutInflater;
    ConstraintLayout coordinatorLayout;

    public static final String HOUSE_NAME_KEY = "housekey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.houses_search);
        Log.d("House", "house");
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        gridLayout = (GridLayout) findViewById(R.id.grid_layout);

        coordinatorLayout = (ConstraintLayout) findViewById(R.id.houseSearchConstrainLayout);
        filter = (ImageView) findViewById(R.id.searchFilter);



        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.filter_popup, null);
                popupWindow = new PopupWindow(container, 520, 240, true);
                popupWindow.showAtLocation(coordinatorLayout, Gravity.NO_GRAVITY, 250, 180);
                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });


//        initSearchWidgets();
        HouseDatabaseHelper.getAllHouses(new getAllHousesCallback() {
            @Override
            public void onCallback(ArrayList<House> houses) {
                Log.d("house", houses.toString());
                for (House house : houses){
                    HouseCardView housecard = new HouseCardView(house.houseName, getApplicationContext(), house.imageName);
                    CardView cardView = housecard.makeCardView();
                    cardView.setCardBackgroundColor(Color.parseColor("#2D2F35"));

                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                    Toast.makeText(MainActivity.this,"Clicked at index "+ finalI,
//                            Toast.LENGTH_SHORT).show();
                            ViewGroup viewGroup = (ViewGroup) view;
                            ViewGroup linearLayout = (ViewGroup) viewGroup.getChildAt(0);
                            TextView houseNameTextView = (TextView) linearLayout.getChildAt(1);

                            Intent intent = new Intent(HousesSearch.this, HousePage.class);
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

                if (item.getItemId()==R.id.home) {
                    Log.d("swtich", "home");
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                    return true;
                }
                else if (item.getItemId()==R.id.profile){
                    Log.d("swtich", "profile");
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                    finish();
                    return true;
                }
                else if (item.getItemId()==R.id.forum){
                    Log.d("swtich", "forum");
                    startActivity(new Intent(getApplicationContext(), Forum.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

    }

//    private void initSearchWidgets(){
//        SearchView searchView = (SearchView) findViewById(R.id.search_view);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                ArrayList<House> filteredHouses = new ArrayList<House>();
//                HouseDatabaseHelper.getAllHouses(new getAllHousesCallback() {
//                    @Override
//                    public void onCallback(ArrayList<House> houses) {
//                        for (House house : houses) {
//                            if (house.houseName.toLowerCase().contains(newText.toLowerCase())) {
//                                filteredHouses.add(house);
//                            }
//                        }
//                        HouseAdapter adapter
//
//                return false;
//            }
//        });
//    }
}


