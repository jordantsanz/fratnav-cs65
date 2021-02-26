package com.example.fratnav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import java.util.ArrayList;
import java.util.Random;

public class CreateAccountActivity extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    String email;
    String password;
    FirebaseUser user;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        mAuth = FirebaseAuth.getInstance();



    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
// adapted from stack overflow
    public static String randomUsername() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        randomStringBuilder.append("User");
        String string = String.valueOf(UUID.randomUUID());
        randomStringBuilder.append(string);
//        int randomLength = generator.nextInt(20);
//        char tempChar;
//        for (int i = 0; i < randomLength; i++){
//            tempChar = (char) (generator.nextInt(96) + 32);
//            randomStringBuilder.append(tempChar);
//        }
        return randomStringBuilder.toString();
    }

    public void createAccount(View view){

        EditText emailView = (EditText) findViewById(R.id.createEmail);
        EditText passwordView = (EditText) findViewById(R.id.createPassword);

        email = emailView.getText().toString();
        password = passwordView.getText().toString();

        Log.d("email", email);
        Log.d("password", password);

        if (email == null || password == null){
            Toast.makeText(CreateAccountActivity.this, "Please enter in email and password", Toast.LENGTH_SHORT).show();
            return;
        }



        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            assert user != null;
                            Toast.makeText(CreateAccountActivity.this, user.toString(), Toast.LENGTH_SHORT).show();
                            User newUser = new User(email, randomUsername(), "Male", "gay", user.getUid(), 2022,
                                    true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                                    false);

                            // checks to see if data has been entered
                            DatabaseReference.CompletionListener completionListener =
                                    new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError,
                                                               DatabaseReference databaseReference) {

                                            if (databaseError != null) {
                                                Log.d("completion", "onComplete: ");
                                            }
                                        }
                                    };

                            database = FirebaseDatabase.getInstance();
                            dbRef = database.getReference("/users");
                            dbRef.push().setValue(newUser);

                            Intent intent = new Intent(getBaseContext(), CreateProfile.class);
                            startActivity(intent);
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("authentication", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed loser.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }



}