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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fratnav.MainActivity;
import com.example.fratnav.callbacks.getAllReviewsCallback;
import com.example.fratnav.callbacks.likePostCallback;
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
import com.example.fratnav.profile.RVReviewsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.util.ArrayList;

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
    public int subscribers;
    public boolean isHouse;
    public static ArrayList<Review> arrayOfReviews;
    public static RVReviewsAdapter adapterReviews;
    public String houseId;

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
                isHouse = user.house;
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
                    }
              else if (item.getItemId()==R.id.profile){
                            Intent intent = new Intent(HousePage.this, Profile.class);
                            intent.putExtra(MainActivity.USER_HOUSE_BOOL, isHouse);
                            startActivity(intent);
                            return true;
                    } else if (item.getItemId() == R.id.forum) {
                        Log.d("swtich", "forum");
                        startActivity(new Intent(getApplicationContext(), Forum.class));
                        finish();
                        return true;
                    } else if (item.getItemId()==R.id.houses) {
                        Log.d("swtich", "houses");
                        startActivity(new Intent(getApplicationContext(), HousesSearch.class));
                        finish();
                        return true;
                    }
                    return false;
                }
            });
        }

        TextView houseNameTextView = findViewById(R.id.house_name);
        TextView pointsofContact = findViewById(R.id.contactsResponse);
        TextView selfReportedStats = findViewById(R.id.statsResponse);
        TextView summaryResponse = findViewById(R.id.summaryResponse);
        String name = getIntent().getStringExtra(HousesSearch.HOUSE_NAME_KEY);
        HouseDatabaseHelper.getHouseByName(name, new getHouseByIdCallback() {
            @Override
            public void onCallback(House house) {
                theHouse = house;
                subscribers = theHouse.subscribers;
                Log.d("house", theHouse.toString());
                TextView houseDateView = findViewById(R.id.house_date);
                houseDateView.setText(String.valueOf(theHouse.date));
                String president = "President: ";
                String vicePresident = "Vice President: ";
                String tres = "Treasurer: ";
                String rush = "Rush Chair: ";
                String poc ="POC Members: ";
                String queer ="LGBTQ+ Members: ";
                String total = "Total Members: ";
                String summary = "House Summary: ";

                ReviewDatabaseHelper.getReviewsByHouseId(house.id, new getAllReviewsCallback() {
                    @Override
                    public void onCallback(ArrayList<Review> reviews) {
                        Log.d("reviews", reviews.toString());
                    }
                });

                TextView houseNationalView = findViewById(R.id.house_national);
                String n = "";
                if (theHouse.national) {
                    n = "National";
                } else {
                    n = "Local";
                }
                if (house.president != null && house.president.length() > 1 ) {
                    president += house.president;
                }
                else{
                    president += "N/A";
                }
                if (house.vicePresident != null && house.vicePresident.length() > 1 ){
                    vicePresident += house.vicePresident;
                }
                else{
                    vicePresident += "N/A";
                }
                if (house.treasurer != null && house.treasurer.length() > 1 ){
                    tres += house.treasurer;
                }
                else{
                    tres += "N/A";
                }
                if(house.rushChair!=null && house.rushChair.length() > 1 ){
                    rush += house.rushChair;
                }
                else{
                    rush += "N/A";
                }
                String contacts = president + '\n' + vicePresident +'\n'+ tres +'\n' + rush;
                pointsofContact.setText(contacts);

                if(house.pocMembers != null && house.pocMembers.length() >= 1 ){
                    poc += house.pocMembers;
                }
                else{
                    poc += "N/A";
                }

                if (house.queerMembers !=null && house.queerMembers.length() >=1){
                    queer += house.queerMembers;
                }
                else{
                    queer += "N/A";
                }

                if(house.totalMembers != null && house.totalMembers.length() >=1){
                    total += house.totalMembers;
                }
                else{
                    total += "N/A";
                }
                String stats = total + '\n' + queer + '\n'+ poc;
                selfReportedStats.setText(stats);

                if (house.summary != null && house.summary.length() >1){
                    summary += house.summary;
                }
                else{
                    summary +="N/A";
                }
                summaryResponse.setText(summary);


                TextView subscribersText = findViewById(R.id.subscribers);
                String subs = String.valueOf(subscribers) + " Subscribers";
                subscribersText.setText(subs);

                houseNationalView.setText(n);
                iv = findViewById(R.id.housePageImage);
                setImageView(house.houseName);
                renderReviews();


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
        startActivity(new Intent(HousePage.this, HousesSearch.class));
        finish();
    }

    public void onHouseTour(View view) {
        if (!url.equals("")) {
            Intent i = new Intent(this, HouseTourActivity.class);
            i.putExtra(URL_KEY, url);
            startActivity(i);
        } else {
            Toast.makeText(this, "Sorry, this house has not uploaded a virtual tour yet.", Toast.LENGTH_LONG).show();
        }

    }

    public void subscribe(View view) {
        if (subbed) {
            UserDatabaseHelper.removeHouseFromUserSubscribed(theHouse, currentUser.getUid());
            HouseDatabaseHelper.removeSubscriberFromCount(theHouse.id, new likePostCallback() {
                @Override
                public void onCallback(int likes) {
                    TextView subscribersView = findViewById(R.id.subscribers);
                    subscribers -= 1;
                    if (subscribers <= 0){
                        subscribers = 0;
                    }
                    String subs = String.valueOf(subscribers) + " Subscribers";
                    subscribersView.setText(subs);
                }
            });
            FirebaseMessaging.getInstance().unsubscribeFromTopic(theHouse.houseName);
            Toast.makeText(getApplicationContext(), "You unsubscribed from " + theHouse.houseName + ".", Toast.LENGTH_SHORT).show();
        } else {
            UserDatabaseHelper.addHouseToUserSubscribed(theHouse, currentUser.getUid());
            HouseDatabaseHelper.addSubscriberToCount(theHouse.id, new likePostCallback() {
                @Override
                public void onCallback(int likes) {
                    TextView subscribersView = findViewById(R.id.subscribers);
                    subscribers += 1;
                    if (subscribers <= 0){
                        subscribers = 0;
                    }
                    String subs = String.valueOf(subscribers) + " Subscribers";
                    subscribersView.setText(subs);
                }
            });
            FirebaseMessaging.getInstance().subscribeToTopic(theHouse.houseName);
            Toast.makeText(getApplicationContext(), "You subscribed to " + theHouse.houseName + "!", Toast.LENGTH_SHORT).show();
        }
        subbed = !subbed;

        if (subbed) {
            String t = "Unsubscribe";
            subscribeButton.setText(t);
        } else {
            String t = "Subscribe";
            subscribeButton.setText(t);
        }
    }


    public void setImageView(String name) {
        int image = 0;
        switch (name) {
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

    public void renderReviews(){
        arrayOfReviews = new ArrayList<>();
        Log.d("houseidd",theHouse.id);
        ReviewDatabaseHelper.getReviewsByHouseId(theHouse.id, new getAllReviewsCallback() {
            @Override
            public void onCallback(ArrayList<Review> reviews) {
                Log.d("reviews", reviews.toString());
                for (int i = reviews.size() - 1; i > -1; i--){
                    Review review = reviews.get(i);
                    arrayOfReviews.add(review);
                }
                adapterReviews.notifyDataSetChanged();
            }
        });

        if (arrayOfReviews == null) {
            arrayOfReviews = new ArrayList<Review>();
        }


        RecyclerView recyclerViewreviews = findViewById(R.id.rv_reviews);
        LinearLayoutManager horizontalLayoutManager2 =
                new LinearLayoutManager(HousePage.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewreviews.setLayoutManager(horizontalLayoutManager2);

        adapterReviews = new RVReviewsAdapter(getApplicationContext(), arrayOfReviews);
        recyclerViewreviews.setAdapter(adapterReviews);

        adapterReviews.notifyDataSetChanged();

    }

    public static void refresh(Review review){
        arrayOfReviews.add(review);
        adapterReviews.notifyDataSetChanged();

    }


}

