package com.example.fratnav;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.internal.Constants;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onecode.s3.model.S3Credentials;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.net.URI;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomBar;
    private static FirebaseUser currentUser;
    String key = "AKIAYLJMLQUVXCV5377P";
    String secret = "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";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        Log.d("home", "home");
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null){
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);
            finish();
        }

        try {
            Log.d("cu", currentUser.toString());
        }
        catch(Exception e){
            Intent intent = new Intent(this, Authentication.class);
            startActivity(intent);
            finish();
        }

        // checks to make sure the user is currently logged in; otherwise, send to authentication
        if (currentUser == null) {
            Log.d("null", "sup");
            startActivity(new Intent(this, Authentication.class));
            return;
        }
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.home);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("sad", "made it item clicked " + item.getTitle());
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();

                if (item.getItemId()==R.id.houses) {
                    Log.d("swtich", "houses");
                    startActivity(new Intent(MainActivity.this, HousesSearch.class));
                    finish();
                    return true;
                } else if (item.getItemId()==R.id.profile) {
                    Log.d("swtich", "profile");
                    startActivity(new Intent(MainActivity.this, Profile.class));
                    finish();
                    return true;
                } else if (item.getItemId()==R.id.forum) {
                    Log.d("swtich", "forum");
                    startActivity(new Intent(MainActivity.this, Forum.class));
                    finish();
                    return true;
                }
                return false;
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
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
            Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }

}

    public void upload(Uri filepath){
        File file = new File(filepath.getPath());
        AmazonS3Client s3Client =   new AmazonS3Client(new BasicAWSCredentials( key, secret ));
        PutObjectRequest por =    new PutObjectRequest("greeknav", "testfile.png", file);
        PutObjectResult result = s3Client.putObject( por );
        StringBuilder builder = new StringBuilder();
        builder.append("https://greeknav.s3.amazonaws.com/");
        builder.append("testfile");
        String extension = FilenameUtils.getExtension(filepath.toString());
        builder.append(extension);
    }

    private void checkPermissions() {
        if(Build.VERSION.SDK_INT < 23)
            return;

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
        }
    }

//    if using menu on toolbar
//    public void logout(MenuItem item) {
//        FirebaseAuth.getInstance().signOut();
//        finishAffinity();
//    }
}
