package com.example.fratnav.forum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fratnav.onboarding.Authentication;
import com.example.fratnav.R;
import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.models.Post;
import com.example.fratnav.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class CreatePost extends AppCompatActivity {
    private static FirebaseUser currentUser;
    private static User currentUserInfo;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRefPosts;
    EditText userText;
    TextView userName;
    DatabaseReference.CompletionListener completionListener;

    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userName = (TextView) findViewById(R.id.createPostUsername);
        userText = (EditText) findViewById(R.id.postUserText);

//        userText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    hideKeyboard(v);
//                }
//            }
//        });

        //Tags
        //linearLayout = findViewById(R.id.linearLayoutInHorizontalScroll);
        //buildHorizontalScroll();


        if (currentUser == null) {
            startActivity(new Intent(this, Authentication.class));
            finish();
            return;
        }
        UserDatabaseHelper.getUserById(currentUser.getUid(), new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {
                currentUserInfo = user;
                userName.setText(user.username);
            }
        });

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
    }

//    public void buildHorizontalScroll(){
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(0, 10, 30, 10);
//
//        HouseDatabaseHelper.getAllHouses(new getAllHousesCallback() {
//            @Override
//            public void onCallback(ArrayList<House> houses) {
//                for (int i = 0; i < houses.size(); i++) {
//                    final Button btCategory = new Button(CreatePost.this);
//                    btCategory.setTextSize(16);
//                    btCategory.setBackground(ContextCompat.getDrawable(CreatePost.this, R.drawable.frat_tag_background));
//                    btCategory.setText(houses.get(i).houseName);
//                    btCategory.setLayoutParams(layoutParams);
//                    //btCategory.setOnClickListener(new onClickListenter);
//
//                }
//
//            }
//        });
//
//
//
//    }

    public void cancelPost(View view) {
        Intent intent = new Intent(CreatePost.this, Forum.class);
        startActivity(intent);

    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // if error
    private void notifyUser(String message) {
        Toast.makeText(CreatePost.this, message,
                Toast.LENGTH_SHORT).show();
    }

    // saves post currently
    public void savePost(View view) {
        Post post = new Post(currentUserInfo.username, currentUser.getUid(), userText.getText().toString(),
                new ArrayList<>(), new HashMap<>(), 0);

        DatabaseReference pushRef = dbRefPosts.push();

        post.id = pushRef.getKey();

        pushRef.setValue(post);

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    // issues here
        Forum.arrayOfPosts.add(0, post);
        Forum.adapter.notifyDataSetChanged();
        UserDatabaseHelper.addPostToUser(post, currentUser);

        Toast.makeText(this, "Post successfully created.", Toast.LENGTH_SHORT).show();
        finish();
    }

}

