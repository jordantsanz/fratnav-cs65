package com.example.fratnav.houses;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.service.autofill.UserData;
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
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fratnav.MainActivity;
import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.AuthenticationHelper;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.models.User;
import com.example.fratnav.profile.Profile;
import com.example.fratnav.R;
import com.example.fratnav.callbacks.getAllHousesCallback;
import com.example.fratnav.databaseHelpers.HouseDatabaseHelper;
import com.example.fratnav.forum.Forum;
import com.example.fratnav.models.House;
import com.example.fratnav.models.HouseCardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HousesSearch extends AppCompatActivity {
    BottomNavigationView bottomBar;
    GridLayout gridLayout;
    FirebaseUser user;
    ArrayList<HouseCardView> cards;
    ImageView filter;
    PopupWindow popupWindow;
    LayoutInflater layoutInflater;
    public static final String HOUSECARDS_KEY = "housecards";
    ConstraintLayout coordinatorLayout;
    public HashMap<String, String> houseCategories;
    public String searchText = "";

    public boolean sororityOn = false;
    public boolean fraternityOn = false;
    public boolean genderOn = false;
    public boolean panOn = false;
    boolean isHouse;


    public static final String HOUSE_NAME_KEY = "housekey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets XML
        setContentView(R.layout.houses_search);
        FirebaseUser currentUser = AuthenticationHelper.getCurrentUser();

        //instantiates the hashmap
        houseCategories = new HashMap<>();

        //checks if the user is a house
        UserDatabaseHelper.getUserById(currentUser.getUid(), new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {
                isHouse = user.house;
            }
        });

        //sets up gridLayout
        gridLayout = (GridLayout) findViewById(R.id.grid_layout);

        //
        coordinatorLayout = (ConstraintLayout) findViewById(R.id.houseSearchConstrainLayout);
        filter = (ImageView) findViewById(R.id.searchFilter);

        //implements the search functionality
        searchThings();


        //popupWindow for the filter and sets the onClickListener for each filterable tag
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.filter_popup, null);
                popupWindow = new PopupWindow(container, 520, 240, true);
                popupWindow.showAtLocation(coordinatorLayout, Gravity.NO_GRAVITY, 250, 180);

                ToggleButton sor = container.findViewById(R.id.soroTag);
                sor.setChecked(sororityOn);

                ToggleButton frat = container.findViewById(R.id.fratTag);
                frat.setChecked(fraternityOn);

                ToggleButton gen = container.findViewById(R.id.genderInclusiveTag);
                gen.setChecked(genderOn);

                ToggleButton pan = container.findViewById(R.id.nationalPanHellicTag);
                pan.setChecked(panOn);



                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
                container.findViewById(R.id.soroTag).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (houseCategories.containsKey("sorority")){
                                    houseCategories.remove("sorority");
                                    sororityOn = false;
                                }
                                else{
                                    houseCategories.put("sorority", "");
                                    sororityOn = true;
                                }
                                firebaseHouseSearch();

                            }
                        }
                );

                container.findViewById(R.id.fratTag).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (houseCategories.containsKey("fraternity")){
                            houseCategories.remove("fraternity");
                            fraternityOn = false;
                        }
                        else{
                            houseCategories.put("fraternity", "");
                            fraternityOn = true;
                        }
                        firebaseHouseSearch();
                    }
                });

                container.findViewById(R.id.genderInclusiveTag).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (houseCategories.containsKey("genderInclusive")){
                            houseCategories.remove("genderInclusive");
                            genderOn = false;
                        }
                        else{
                            houseCategories.put("genderInclusive", "");
                            genderOn = true;
                        }
                        firebaseHouseSearch();
                    }
                });

                container.findViewById(R.id.nationalPanHellicTag).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (houseCategories.containsKey("pan")){
                            houseCategories.remove("pan");
                            panOn = false;
                        }
                        else{
                            houseCategories.put("pan", "");
                            panOn = true;
                        }
                        firebaseHouseSearch();
                    }
                });
            }
        });


        //sets the card view and onclicklistener to pull up the specific house account page
        if (savedInstanceState == null) {
            HouseDatabaseHelper.getAllHouses(new getAllHousesCallback() {
                @Override
                public void onCallback(ArrayList<House> houses) {
                    cards = new ArrayList<>();
                    for (House house : houses) {
                        HouseCardView housecard = new HouseCardView(house.houseName, getApplicationContext(), house.imageName);
                        CardView cardView = housecard.makeCardView();
                        cardView.setCardBackgroundColor(Color.parseColor("#2D2F35"));

                        cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
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
        }
        //renders information from the savedInstanceState
        else{
            cards = savedInstanceState.getParcelableArrayList(HOUSECARDS_KEY);
            gridLayout.removeAllViews();
            for (HouseCardView houseCardView : cards){
                CardView cardView = houseCardView.makeCardView();
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


        //navBar
        bottomBar = (BottomNavigationView) findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.houses);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                if (item.getItemId()==R.id.home) {

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                    return true;
                }
                else if (item.getItemId()==R.id.profile){
                    Intent intent = new Intent(HousesSearch.this, Profile.class);
                    intent.putExtra(MainActivity.USER_HOUSE_BOOL, isHouse);
                    startActivity(intent);
                    return true;
                }
                else if (item.getItemId()==R.id.forum){

                    startActivity(new Intent(getApplicationContext(), Forum.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }


    //search functionality
    public void searchThings(){
        androidx.appcompat.widget.SearchView searchView = findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = searchView.getQuery().toString();
                firebaseHouseSearch();
                return true;
            }
        });

    }


    //saved information for each page
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(HOUSECARDS_KEY, cards);
    }

    //searches the database and organized the gridLayout
    public void firebaseHouseSearch(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/houses");
        Query q = databaseReference.orderByChild("query").startAt(searchText.toUpperCase()).endAt(searchText.toUpperCase() + "\uf8ff");
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                gridLayout.removeAllViews();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    House house = ds.getValue(House.class);
                    assert house != null;
                    if (houseCategories.size() == 0 || houseCategories.containsKey(house.houseType)) {

                        HouseCardView housecard = new HouseCardView(house.houseName, getApplicationContext(), house.imageName);
                        CardView cardView = housecard.makeCardView();
                        cardView.setCardBackgroundColor(Color.parseColor("#2D2F35"));

                        cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}