package com.example.fratnav.forum;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ToggleButton;

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

import java.lang.reflect.Array;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //connects the xmls
        setContentView(R.layout.create_post);

        //gets the current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        //gets all of the components that will be rendered with information from the database
        userName = (TextView) findViewById(R.id.createPostUsername);
        userText = (EditText) findViewById(R.id.postUserText);
        info = (ImageView) findViewById(R.id.houseTagsInformation);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.createPostRelativeLayout);

        //opens a popupWindow for the tags color  legend
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.house_tag_popup, null);
                popupWindow = new PopupWindow(container, 340, 240, true);
                popupWindow.showAtLocation(coordinatorLayout, Gravity.NO_GRAVITY, 340, 420);
                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });



        //goes to log in page if there is no currentUser
        if (currentUser == null) {
            startActivity(new Intent(this, Authentication.class));
            finish();
            return;
        }
        //uses userdatabasehelper to get the current user information
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

        //gets firebase database
        database = FirebaseDatabase.getInstance();
        // sends post to database
        dbRefPosts = database.getReference("/posts");
    }


    //method of returning back to forum page/canceling the post
    public void cancelPost(View view) {
        Intent intent = new Intent(CreatePost.this, Forum.class);
        startActivity(intent);
        finish();

    }


    // if error
    private void notifyUser(String message) {
        Toast.makeText(CreatePost.this, message,
                Toast.LENGTH_SHORT).show();
    }



    // saves post currently
    public void savePost(View view) {
        ArrayList<ToggleButton> toggles = new ArrayList<>();
        ToggleButton axaTag = findViewById(R.id.axaTag);
        ToggleButton akaTag = findViewById(R.id.akaTag);
        ToggleButton aphiTag = findViewById(R.id.aPhiTag);
        ToggleButton alphasTag = findViewById(R.id.alphasTag);
        ToggleButton alphaThetaTag = findViewById(R.id.alphaThetaTag);
        ToggleButton axidTag = findViewById(R.id.aXiDTag);
        ToggleButton bgTag = findViewById(R.id.bGTag);
        ToggleButton betaTag = findViewById(R.id.betaTag);
        ToggleButton chiDeltTag = findViewById(R.id.chiDeltTag);
        ToggleButton chiGamTag = findViewById(R.id.chiGamTag);
        ToggleButton deltasTag = findViewById(R.id.deltasTag);
        ToggleButton ektTag = findViewById(R.id.ektTag);
        ToggleButton gdxTag = findViewById(R.id.gdxTag);
        ToggleButton hereotTag = findViewById(R.id.hereotTag);
        ToggleButton kappaTag = findViewById(R.id.kappaTag);
        ToggleButton kdTag = findViewById(R.id.kdTag);
        ToggleButton kdeTag = findViewById(R.id.kdeTag);
        ToggleButton trikapTag = findViewById(R.id.triKapTag);
        ToggleButton phiDeltTag = findViewById(R.id.phiDeltTag);
        ToggleButton phiTauTag = findViewById(R.id.phiTauTag);
        ToggleButton psiUTag = findViewById(R.id.psiUTag);
        ToggleButton sigDeltTag = findViewById(R.id.sigDeltTag);
        ToggleButton sigNuTag = findViewById(R.id.sigNuTag);
        ToggleButton tabardTag = findViewById(R.id.tabardTag);
        ToggleButton tdxTag = findViewById(R.id.tdxTag);
        ToggleButton zeteTag = findViewById(R.id.zeteTag);

        ToggleButton pocTag = findViewById(R.id.pocTag);
        ToggleButton lgbtTag = findViewById(R.id.lgbtqTag);
        ToggleButton firstGenTag = findViewById(R.id.firstGenTag);
        ToggleButton lowincTag = findViewById(R.id.lowIncome);

        //adds all of the buttons
        toggles.add(axaTag);
        toggles.add(akaTag);
        toggles.add(aphiTag);
        toggles.add(alphasTag);
        toggles.add(alphaThetaTag);
        toggles.add(axidTag);
        toggles.add(bgTag);
        toggles.add(betaTag);
        toggles.add(chiDeltTag);
        toggles.add(chiGamTag);
        toggles.add(deltasTag);
        toggles.add(ektTag);
        toggles.add(gdxTag);
        toggles.add(hereotTag);
        toggles.add(kappaTag);
        toggles.add(kdTag);
        toggles.add(kdeTag);
        toggles.add(trikapTag);
        toggles.add(phiDeltTag);
        toggles.add(phiTauTag);
        toggles.add(psiUTag);
        toggles.add(sigDeltTag);
        toggles.add(sigNuTag);
        toggles.add(tabardTag);
        toggles.add(tdxTag);
        toggles.add(zeteTag);
        toggles.add(pocTag);
        toggles.add(lgbtTag);
        toggles.add(firstGenTag);
        toggles.add(lowincTag);

        ArrayList<String> names = new ArrayList<>();
        //adds clicked tags
        for (ToggleButton toggle : toggles){
            if (toggle.isChecked()){
                names.add(toggle.getText().toString());
            }
        }
        //checks that no more than 3 tags are added
        if (names.size() <= 3) {
            //creates a post
            Post post = new Post(currentUserInfo.username, currentUser.getUid(), userText.getText().toString(),
                    names, new HashMap<>(), 0);

            // preps a push
            DatabaseReference pushRef = dbRefPosts.push();

            post.id = pushRef.getKey();

            //pushed the post to the database
            pushRef.setValue(post);

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

            Forum.arrayOfPosts.add(0, post);
            Forum.postsAdapter.notifyDataSetChanged();
            UserDatabaseHelper.addPostToUser(post, currentUser, currentUserInfo);

            //sends toast to the user
            Toast.makeText(this, "Post successfully created.", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            //lets the user know to only choose 3 tags
            Toast.makeText(this, "Please only select a maximum of three tags.", Toast.LENGTH_SHORT).show();
        }
    }

}

