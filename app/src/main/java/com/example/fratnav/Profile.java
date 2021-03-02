package com.example.fratnav;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.example.fratnav.callbacks.getAllPostsCallback;
import com.example.fratnav.callbacks.getPostByIdCallback;
import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.HouseDatabaseHelper;
import com.example.fratnav.databaseHelpers.PostDatabaseHelper;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.models.Post;
import com.example.fratnav.models.User;
import com.example.fratnav.tools.HouseCreation;
import com.example.fratnav.tools.PostsAdapter;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Calendar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

public class Profile extends AppCompatActivity {
    BottomNavigationView bottomBar;
    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private EditText userText;
    private TextView helloUser;
    PostsAdapter adapter;
    public ArrayList<Post> arrayOfPosts;
    DatabaseReference.CompletionListener completionListener;
    ListView postListView;
    String affiliated;
    String userName;
    String sexuality;
    String year;
    String gender;
    String key = "AKIAYLJMLQUVXCV5377P"; // will secure these soon
    String secret = "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";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        checkPermissions();
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

                arrayOfPosts = new ArrayList<>();
                PostDatabaseHelper.getAllPostsByUser(useriD, new getAllPostsCallback() {
                    @Override
                    public void onCallback(ArrayList<Post> posts) {
                        Log.d("posts", posts.toString());;
                        for (int i = posts.size() - 1; i > -1; i--){
                            Post post = posts.get(i);
                            arrayOfPosts.add(post);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });


                // Create the adapter to convert the array to views
                adapter = new PostsAdapter(getApplicationContext(), arrayOfPosts);
                // Attach the adapter to a ListView
                ListView list = findViewById(android.R.id.list);
                list.setAdapter(adapter); // sets adapter for list
                adapter.notifyDataSetChanged();
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


        // Construct the data source
        if (arrayOfPosts == null) {
            arrayOfPosts = new ArrayList<Post>();
        }


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


    public void edit(View view) {
//        HouseCreation hc = new HouseCreation();
//        hc.createHouses();
        Intent intent = new Intent(this, updateProfile.class);
        startActivity(intent);

    }


    public void imagePicker(View view){
        ImagePicker.Companion.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            assert data != null;
            Uri uri = data.getData();
            Log.d("uri", uri.toString());



            new Thread() {
                @Override
                public void run() {
                    upload(uri);

                }
            }.start();
        }
        else if (resultCode == ImagePicker.RESULT_ERROR) {
            assert data != null;
        } else {
            Toast.makeText(this, "Cancelled.", Toast.LENGTH_SHORT).show();
        }

    }

    public void upload(Uri filepath){
        File file = new File(filepath.getPath());
        AmazonS3Client s3Client =   new AmazonS3Client(new BasicAWSCredentials( key, secret ));

        String extension = FilenameUtils.getExtension(filepath.toString());
        String random = UUID.randomUUID().toString();
        String fileName = random +
                "." +
                extension;

        PutObjectRequest por =    new PutObjectRequest("greeknav", fileName, file);
        PutObjectResult result = s3Client.putObject( por );
        String url = "https://greeknav.s3.amazonaws.com/" +
                random +
                "." +
                extension;
        Log.d("stringbuilder", url);

        // will put in house user id here
        HouseDatabaseHelper.addUrlToHouse("-MUf40ida5oWaHqNwnix", url);
    }

    private void checkPermissions() {
        if(Build.VERSION.SDK_INT < 23)
            return;

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
        }

    }

}