package com.example.fratnav;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fratnav.databaseHelpers.AuthenticationHelper;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateProfile extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    String gender;
    String sexuality;
    Integer year;
    Integer affiliatedCheck;
    boolean affiliated;
    String email;
    String username;
    String password;
    FirebaseUser user;
    CheckBox srat, frat, genderInclusive, natPanHelic;
    Button createProfile;
    ArrayList<String> preferences;



    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile_houses);
        mAuth = FirebaseAuth.getInstance();

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        username = getIntent().getStringExtra("userName");


    }

    public void createAccount2(View view) {
        EditText genderView = (EditText) findViewById(R.id.createGender);
        EditText sexualityView = (EditText) findViewById(R.id.createSexuality);
        EditText yearView = (EditText) findViewById(R.id.createYear);
        RadioGroup affiliatedView = (RadioGroup) findViewById(R.id.createAffiliated);

        gender = genderView.getText().toString();
        sexuality = sexualityView.getText().toString();
        year = yearView.getInputType();
        affiliatedCheck = affiliatedView.getCheckedRadioButtonId();
        if (affiliatedCheck== R.id.createYesAffiliated){
            affiliated = true;
        }
        else{
            affiliated=false;
        }
        srat = findViewById(R.id.soroCheckbox);
        frat = findViewById(R.id.fratCheckbox);
        natPanHelic = findViewById(R.id.natPanHelicChackBox);
        genderInclusive = findViewById(R.id.genderInclusiveCheckBox);


        user = AuthenticationHelper.getCurrentUser();

        if(gender == null){
            gender ="N/A";
        }

        if(sexuality == null){
            sexuality ="N/A";
        }
        if (year == null){
            year = 0;
        }

        preferences = new ArrayList<>();
        if (frat.isChecked()){
            preferences.add("Fraternities");
        }
        if (srat.isChecked()){
            preferences.add("Sororities");
        }
        if (genderInclusive.isChecked()){
            preferences.add("Gender Inclusive Houses");
        }
        if(natPanHelic.isChecked()){
            preferences.add("National Pan-Hellenic");
        }

        User newUser = new User( email, username, gender, sexuality, user.getUid(), year, affiliated, preferences, new ArrayList<>(), new ArrayList<>(), false);

        UserDatabaseHelper.createUser(newUser);

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        Log.d("intentintent", "onComplete: ");
        startActivity(intent);
        finish();

        // should this be optional ?
//        if (gender == null || sexuality == null || year == null ||affiliated == null ){
//            Toast.makeText(CreateProfile.this, "Please enter in gender and sexuality ", Toast.LENGTH_SHORT).show();
//            return;
//        }
        //more shit should happen



    }




}