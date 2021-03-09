package com.example.fratnav.houses;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.fratnav.R;
import com.example.fratnav.callbacks.createCallback;
import com.example.fratnav.callbacks.getAllReviewsCallback;
import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.AuthenticationHelper;
import com.example.fratnav.databaseHelpers.HouseDatabaseHelper;
import com.example.fratnav.databaseHelpers.ReviewDatabaseHelper;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.models.House;
import com.example.fratnav.models.Review;
import com.example.fratnav.models.User;
import com.example.fratnav.onboarding.Authentication;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import static com.example.fratnav.houses.HousePage.arrayOfReviews;

public class ReviewDialog extends Dialog {

    public Activity c;
    public Dialog dialog;
    public Button reviewSave;
    public FirebaseUser currentUser;
    public User currentUserInfo;
    public House theHouse;

    public ReviewDialog(Activity a, House theHouse, User user, FirebaseUser fbuser){
        super(a);
        this.c = a;
        this.theHouse = theHouse;
        this.currentUser = fbuser;
        this.currentUserInfo = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        reviewSave = findViewById(R.id.reviewSave);
        reviewSave.setOnClickListener(this::onClick);

    }

    public void onClick(View v){
        RatingBar safetyRatingbar = findViewById(R.id.safetyRating);
        RatingBar inclusivityRatingbar = findViewById(R.id.inclusivityRating);
        RatingBar basementRatingbar = findViewById(R.id.basementRating);
        RatingBar overallRatingbar = findViewById(R.id.overallRating);
        EditText reviewEditText = findViewById(R.id.reviewUserEditText);

        float safety = safetyRatingbar.getRating();
        float incl = inclusivityRatingbar.getRating();
        float basement = basementRatingbar.getRating();
        float overall = overallRatingbar.getRating();
        String reviewText = reviewEditText.getText().toString();


        Review review = new Review(reviewText, currentUserInfo.username, currentUser.getUid(),
        safety, incl, basement, overall, theHouse.id, theHouse.houseName);
        Log.d("reviewSent", review.toString());

        Log.d("reviewUserid", review.userID);
        Log.d("houseId", review.houseId);
        Log.d("housename", theHouse.houseName);


        HousePage.refresh(review);
        dismiss();
        ReviewDatabaseHelper.createReview(review, new createCallback() {
            @Override
            public void onCallback(boolean didFinish) {

                }
        });


    }
}
