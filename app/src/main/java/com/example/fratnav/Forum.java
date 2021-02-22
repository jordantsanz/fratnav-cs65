package com.example.fratnav;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Forum extends AppCompatActivity {
    BottomNavigationView bottomBar;
    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRefPosts;
    private DatabaseReference dbRefUsers;
    private EditText userText;
    private TextView helloUser;
    DatabaseReference.CompletionListener completionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum);
        Log.d("Forum", "forum");

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userText = (EditText) findViewById(R.id.postText);

        // checks to make sure the user is currently logged in; otherwise, send to authentication
        if (currentUser == null) {
            startActivity(new Intent(this, Authentication.class));
            finish();
            return;
        }

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        bottomBar = (BottomNavigationView) findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.forum);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("sad", "made it item clicked " + item.getTitle());
                Toast.makeText(Forum.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                if (item.getItemId()==R.id.houses) {
                    Log.d("swtich", "houses");
                    startActivity(new Intent(Forum.this, HousesSearch.class));
                    return true;
                } else if (item.getItemId()==R.id.profile) {
                    Log.d("swtich", "profile");
                    startActivity(new Intent(Forum.this, Profile.class));
                    return true;
                } else if (item.getItemId()==R.id.home) {
                    Log.d("swtich", "home");
                    startActivity(new Intent(Forum.this, MainActivity.class));
                    return true;
                }
                return false;
            }
        });


        // checks to see if data has been updated
        ValueEventListener changeListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String change = dataSnapshot.child(
                        currentUser.getUid()).child("message")
                        .getValue(String.class);
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
        dbRefPosts = database.getReference("/posts");
        dbRefPosts.addValueEventListener(changeListener);

        dbRefUsers = database.getReference("/users");
        dbRefUsers.addValueEventListener(changeListener);

    }
    private void notifyUser(String message) {
        Toast.makeText(Forum.this, message,
                Toast.LENGTH_SHORT).show();
    }

    public void savePost(View view) {
        Post post = new Post(currentUser.getDisplayName(), currentUser.getUid(), userText.getText().toString(),
                new ArrayList<>(), new ArrayList<>(), 0);
        dbRefPosts.child("Post")
                .setValue(post, completionListener);
        dbRefUsers.orderByChild("userID").equalTo(currentUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                boolean yo = false;
                for (DataSnapshot n : snapshot.getChildren()) {
                    if(Objects.equals(n.getKey(), "Posts")){
                        List<Post> posts = (List<Post>) n.getValue();
                        assert posts != null;
                        posts.add(post);
                        yo = true;
                    }

                }
                if (!yo){
                    Log.d("ohoh", "onChildAdded: ");
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}


