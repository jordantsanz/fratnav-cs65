package com.example.fratnav.databaseHelpers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.fratnav.onboarding.Authentication;
import com.example.fratnav.profile.Profile;
import com.example.fratnav.R;
import com.google.firebase.auth.FirebaseAuth;

public class RealtimeFirebaseDB extends AppCompatActivity {

    FirebaseAuth auth;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_firebase_d_b);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, Profile.class));
            finish();
        } else {
            startActivity(new Intent(this, Authentication.class));
            finish();
        }

    }


}