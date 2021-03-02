package com.example.fratnav;

import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.models.Post;
import com.example.fratnav.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Profile extends AppCompatActivity {
    BottomNavigationView bottomBar;
    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private EditText userText;
    private TextView helloUser;
    DatabaseReference.CompletionListener completionListener;
    ListView postListView;
    String affiliated;
    String userName;
    String sexuality;
    String year;
    String gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // checks to make sure the user is currently logged in; otherwise, send to authentication
        if (currentUser == null) {
            startActivity(new Intent(this, Authentication.class));
            finish();
            return;
        }


        TextView profileUsername = (TextView) findViewById(R.id.profileUsername);
        TextView profileSexuality = (TextView) findViewById(R.id.sexualityResponse);
        TextView profileGender = (TextView) findViewById(R.id.genderResponse);
        TextView profileYear = (TextView) findViewById(R.id.yearResponse);
        TextView profileAffiliated = (TextView) findViewById(R.id.affiliatedResponse);
        String useriD = currentUser.getUid();
        Log.d("firebaseuser", currentUser.getUid());

        UserDatabaseHelper.getUserById(useriD, new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {
                Log.d("username", user.username);
                if (user.username != null){
                    userName= user.username;
                }
                else{
                    userName="N/A";
                }
                if(user.sexuality!=null) {
                    sexuality = user.sexuality;
                }
                else{
                    sexuality="N/A";
                }
                if(user.year != null) {
                    year = user.year;
                }
                else{
                    year = "N/A";
                }
                if(user.gender!=null) {
                    gender = user.gender;
                }
                else{
                    gender = "N/A";
                }
                if (user.houseAffiliation){
                    affiliated= "Yes";
                }
                else{
                    affiliated = "No";
                }
                profileUsername.setText(userName);
                profileSexuality.setText(sexuality);
                profileGender.setText(gender);
                profileYear.setText(year);
                profileAffiliated.setText(affiliated);
            }
        });



        // checks to see if data has been updated
        ValueEventListener changeListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String change = dataSnapshot.child(
                        currentUser.getUid()).child("message")
                        .getValue(String.class);

//                helloUser.setText(change);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                notifyUser("Database error: " + databaseError.toException());
            }
        };

        // checks to see if data has been entered
        completionListener =
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {

                        if (databaseError != null) {
                            notifyUser(databaseError.getMessage());
                        }
                    }
                };



        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("/data");
        dbRef.addValueEventListener(changeListener);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//         setSupportActionBar(toolbar);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        postListView = (ListView) findViewById(R.id.profileListView);

        //need to change to post // changed by will
        ArrayList<Post>arrayList= new ArrayList();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);

        bottomBar = (BottomNavigationView) findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.profile);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("sad", "made it item clicked " + item.getTitle());
                Toast.makeText(Profile.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                if (item.getItemId()==R.id.houses) {
                    Log.d("swtich", "houses");
                    startActivity(new Intent(Profile.this, HousesSearch.class));
                    finish();
                    return true;
                }
                else if (item.getItemId()==R.id.home) {
                    Log.d("swtich", "home");
                    startActivity(new Intent(Profile.this, MainActivity.class));
                    finish();
                    return true;
                }
                else if  (item.getItemId()==R.id.forum){
                    Log.d("swtich", "forum");
                    startActivity(new Intent(Profile.this, Forum.class));
                    finish();
                    return true;
                }
                return false;
            }

        });
    }

    private void notifyUser(String message) {
        Toast.makeText(Profile.this, message,
                Toast.LENGTH_SHORT).show();
    }

    public void saveData(View view) {
        dbRef.child(currentUser.getUid()).child("message")
                .setValue(userText.getText().toString(), completionListener);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
    }

    public void edit(View view){
        Intent intent = new Intent(this, updateProfile.class);
        startActivity(intent);



    }

}