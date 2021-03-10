package com.example.fratnav.onboarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.fratnav.MainActivity;
import com.example.fratnav.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.UUID;

import java.util.Random;

public class CreateAccountActivity extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    String email;
    String username;
    String password;
    FirebaseUser user;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    ImageView info;
    LinearLayout linearLayout;
    PopupWindow popupWindow;
    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        mAuth = FirebaseAuth.getInstance();
        Button createAccountButton = findViewById(R.id.createAccountButton);
        info = (ImageView) findViewById(R.id.passwordInfo);
        linearLayout = (LinearLayout) findViewById(R.id.createaccountLinearLayout);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.password_popup, null);
                popupWindow = new PopupWindow(container, 320, 100, true);
                popupWindow.showAtLocation(linearLayout, Gravity.NO_GRAVITY, 300, 820);
                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(v);
            }
        });


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
        StringBuilder randomStringBuilder = new StringBuilder();
        randomStringBuilder.append("User");
        String string = String.valueOf(UUID.randomUUID());
        string = string.substring(0, 6);
        randomStringBuilder.append(string);
        return randomStringBuilder.toString();
    }

    public void createAccount(View view){

        EditText emailView = (EditText) findViewById(R.id.createEmail);
        EditText emailViewConfirm = (EditText) findViewById(R.id.createEmailConfirm);
        EditText passwordView = (EditText) findViewById(R.id.createPassword);
        EditText passwordViewConfirm = (EditText) findViewById(R.id.createPasswordConfirm);

        emailView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        emailViewConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        passwordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        passwordViewConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        email = emailView.getText().toString();
        password = passwordView.getText().toString();
        username= randomUsername();

        String emailCon = emailViewConfirm.getText().toString();
        String passwordCon = passwordViewConfirm.getText().toString();

        if (email == null || password == null){
            Toast.makeText(CreateAccountActivity.this, "Please enter in email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6){
            Toast.makeText(CreateAccountActivity.this, "Your password must be at least 6 characters long.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.equals(emailCon)){
            Toast.makeText(this, "Your emails do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(passwordCon)){
            Toast.makeText(this, "Your passwords do not match!", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(CreateAccountActivity.this, "New user created: now tell us about yourself!", Toast.LENGTH_LONG).show();
//                            User newUser = new User(email, randomUsername(), "Male", "gay", user.getUid(), 2022,
//                                    true, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
//                                    false);
//                            UserDatabaseHelper.createUser(newUser);

                            Intent intent = new Intent(getBaseContext(), CreateProfile.class);
                            intent.putExtra("email", email);
                            intent.putExtra("password", password);
                            intent.putExtra("userName", username);
                            startActivity(intent);
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            switch(Objects.requireNonNull(task.getException()).toString()){
                                case "com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted.":
                                    Toast.makeText(CreateAccountActivity.this, "The email address you're trying to use is not valid.", Toast.LENGTH_SHORT).show();
                                    break;
                                case "com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account.":
                                    Toast.makeText(CreateAccountActivity.this, "That email address is already in use by another account.", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(CreateAccountActivity.this, "Something happened. Try again!", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            Log.w("authentication", "createUserWithEmail:failure", task.getException());
                        }

                    }
                });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



}