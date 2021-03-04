package com.example.fratnav.forum;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;


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
    ImageView info;
    PopupWindow popupWindow;
    LayoutInflater layoutInflater;
    CoordinatorLayout coordinatorLayout;

    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userName = (TextView) findViewById(R.id.createPostUsername);
        userText = (EditText) findViewById(R.id.postUserText);
        info = (ImageView) findViewById(R.id.houseTagsInformation);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.createPostRelativeLayout);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.house_tag_popup, null);
                popupWindow = new PopupWindow(container, 360, 240, true);
                popupWindow.showAtLocation(coordinatorLayout, Gravity.NO_GRAVITY, 300, 340);
                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });



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



    public void cancelPost(View view) {
        Intent intent = new Intent(CreatePost.this, Forum.class);
        startActivity(intent);
        finish();

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

