package com.example.fratnav;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomBar;
    private static FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("home", "home");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                    return true;
                } else if (item.getItemId()==R.id.profile) {
                    Log.d("swtich", "profile");
                    startActivity(new Intent(MainActivity.this, Profile.class));
                    return true;
                } else if (item.getItemId()==R.id.forum) {
                    Log.d("swtich", "forum");
                    startActivity(new Intent(MainActivity.this, Forum.class));
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


    public void logout(MenuItem item) {
        FirebaseAuth.getInstance().signOut();
        finish();
    }
}
