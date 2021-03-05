package com.example.fratnav.houses;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Rating;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.fratnav.MainActivity;
import com.example.fratnav.databaseHelpers.ReviewDatabaseHelper;
import com.example.fratnav.models.Review;
import com.example.fratnav.profile.Profile;
import com.example.fratnav.R;
import com.example.fratnav.callbacks.getHouseByIdCallback;
import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.AuthenticationHelper;
import com.example.fratnav.databaseHelpers.HouseDatabaseHelper;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.forum.Forum;
import com.example.fratnav.models.House;
import com.example.fratnav.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

public class HousePage extends AppCompatActivity {
    BottomNavigationView bottomBar;
    public House theHouse;
    private VrPanoramaView mVRPanoramaView;
    public VrPanoramaView.Options options;
    public Bitmap bp;
    public String url;
    public static final String URL_KEY = "Url_key";
    FirebaseUser currentUser;
    public boolean subbed = false;
    Button subscribeButton;
    public ImageView iv;
    AlertDialog.Builder dialog;
    User currentUserInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.houses_page);

        currentUser = AuthenticationHelper.getCurrentUser();

        // get current user's info
        UserDatabaseHelper.getUserById(currentUser.getUid(), new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {
                currentUserInfo = user;
            }
        });

        subscribeButton = findViewById(R.id.subscribe_button);

        dialog = new AlertDialog.Builder(this);

        bottomBar = (BottomNavigationView) findViewById(R.id.bottomBar);
        if (bottomBar != null) {
            bottomBar.setSelectedItemId(R.id.houses);
            bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Log.d("sad", "made it item clicked " + item.getTitle());

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
                iv = findViewById(R.id.housePageImage);
                setImageView(house.houseName);


                url = house.urlToHouseTour;

                UserDatabaseHelper.getUserById(currentUser.getUid(), new getUserByIdCallback() {
                    @Override
                    public void onCallback(User user) {
                        if (user.subscribedTo != null) {
                            for (String house : user.subscribedTo.values()) {
                                if (house.equals(theHouse.houseName)) {
                                    String unsubscribe = "Unsubscribe";
                                    subscribeButton.setText(unsubscribe);
                                    subbed = true;
                                }
                            }
                        }
                    }
                });
            }
        });
        houseNameTextView.setText(name);


        Button makeReviewButton = findViewById(R.id.review_button);
        makeReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReviewDialog dialog = new ReviewDialog(HousePage.this, theHouse, currentUserInfo, currentUser);
                dialog.show();
            }
        });

    }

    public void onBackClick(View view) {
        startActivity(new Intent(HousePage.this,HousesSearch.class));
        finish();
    }

    public void onHouseTour(View view) {
        if (!url.equals("")) {
            Intent i = new Intent(this, HouseTourActivity.class);
            i.putExtra(URL_KEY, url);
            startActivity(i);
        }
        else{
            Toast.makeText(this, "Sorry, this house has not uploaded a virtual tour yet.", Toast.LENGTH_LONG).show();
        }

    }

    public void subscribe(View view){
        if (subbed){
            UserDatabaseHelper.removeHouseFromUserSubscribed(theHouse, currentUser.getUid());
            Toast.makeText(getApplicationContext(), "You unsubscribed from " + theHouse.houseName + ".", Toast.LENGTH_SHORT).show();
        }
        else {
            UserDatabaseHelper.addHouseToUserSubscribed(theHouse, currentUser.getUid());
            Toast.makeText(getApplicationContext(), "You subscribed to " + theHouse.houseName + "!", Toast.LENGTH_SHORT).show();
        }
        subbed = !subbed;

        if (subbed){
            String t = "Unsubscribe";
            subscribeButton.setText(t);
        }
        else{
            String t = "Subscribe";
            subscribeButton.setText(t);
        }
    }


    public void setImageView(String name){
        int image = 0;
        switch(name){
            case "Sig Nu":
                image = R.drawable.signu1;
                break;
            case "TDX":
                image = R.drawable.tdx;
                break;
            case "Zete":
                image = R.drawable.zetapsi;
                break;
            case "Sigma Delt":
                image = R.drawable.sigdelt;
                break;
            case "Tabard":
                image = R.drawable.tabard;
                break;
            case "Tri-Kap":
                image = R.drawable.trikap;
                break;
            case "Kappa":
                image = R.drawable.kkg;
                break;
            case "Hereot":
                image = R.drawable.heorot;
                break;
            case "Phi Delt":
                image = R.drawable.phidelta;
                break;
            case "KD":
                image = R.drawable.kd;
                break;
            case "KDE":
                image = R.drawable.kde;
                break;
            case "Phi Tau":
                image = R.drawable.phitau;
                break;
            case "Alpha Chi":
                image = R.drawable.ic_axa;
                break;
            case "GDX":
                image = R.drawable.gdx;
                break;
            case "Psi U":
                image = R.drawable.psiu;
                break;
            case "Chi Delt":
                image = R.drawable.chidelt;
                break;
            case "Chi Gam":
                image = R.drawable.chigam;
                break;
            case "EKT":
                image = R.drawable.ekt;
                break;
            case "Deltas":
                image = R.drawable.deltas;
                break;
            case "AXiD":
                image = R.drawable.axid;
                break;
            case "Beta":
                image = R.drawable.beta;
                break;
            case "BG":
                image = R.drawable.bg;
                break;
            case "APhi":
                image = R.drawable.aphi1;
                break;
            case "Alpha Theta":
                image = R.drawable.alphatheta;
                break;
            case "Alphas":
                image = R.drawable.alphas;
                break;
            case "AKA":
                image = R.drawable.aka;
                break;
            default:
                break;
        }
        iv.setImageDrawable(ResourcesCompat.getDrawable(getApplicationContext().getResources(), image, null));
    }


}

