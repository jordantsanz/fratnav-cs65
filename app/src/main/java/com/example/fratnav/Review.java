package com.example.fratnav;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Review extends AppCompatActivity {

    private static FirebaseUser currentUser;
    private static User currentUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, Authentication.class));
            finish();
            return;
        }
        UserDatabaseHelper.getUserById(currentUser.getUid(), new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {
                currentUserInfo = user;
            }
        });

    }
    public void cancelPost(View view) {
        Intent intent = new Intent(Review.this, HousePage.class);
        startActivity(intent);
        finish();

    }
}
