package com.example.fratnav.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fratnav.MainActivity;
import com.example.fratnav.R;
import com.example.fratnav.databaseHelpers.AuthenticationHelper;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateProfile extends AppCompatActivity  {
    private static FirebaseAuth mAuth;
    String gender;
    String sexuality;
    String year;
    Integer affiliatedCheck;
    Integer genderCheck;
    boolean affiliated;
    String email;
    String username;
    String password;
    FirebaseUser user;
    CheckBox srat, frat, genderInclusive, natPanHelic;
    ArrayList<String> preferences;
    Spinner spinner;
    Spinner sexualitySpinner;



    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //sets XML
        setContentView(R.layout.create_profile_houses);
        //checks authentication
        mAuth = FirebaseAuth.getInstance();
        //gets all of the information from the passed in bundle
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        username = getIntent().getStringExtra("userName");

        //instantiates the spinners
        spinner = findViewById(R.id.createYear);
        sexualitySpinner =findViewById(R.id.createSexuality);


        //adds to an array list for the year spinner
        ArrayList<String> yearOptions = new ArrayList<>();
        yearOptions.add("2021");
        yearOptions.add("2022");
        yearOptions.add("2023");
        yearOptions.add("2024");
        yearOptions.add("2025");

        //creates the spinner using an array adapter and the arraylist
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearOptions);

        //adds to an array list for the sexuality spinner
        ArrayList<String> sexualityOptions = new ArrayList<>();
        sexualityOptions.add("Heterosexual");
        sexualityOptions.add("Lesbian");
        sexualityOptions.add("Gay");
        sexualityOptions.add("Bisexual");
        sexualityOptions.add("Transgender");
        sexualityOptions.add("Queer");
        sexualityOptions.add("N/A");

        //creates the spinner using an array adapter and the arraylist
        ArrayAdapter<String> sexualityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexualityOptions);


        //sets the adapter
        spinner.setAdapter(yearAdapter);
        sexualitySpinner.setAdapter(sexualityAdapter);




    }
    //creates the account in the Firebase Realtime Database
    public void createAccount2(View view) {
       // EditText genderView = (EditText) findViewById(R.id.createGender);

        Spinner yearView =findViewById(R.id.createYear);
        RadioGroup affiliatedView = (RadioGroup) findViewById(R.id.createAffiliated);
        RadioGroup genderRadioGroup = findViewById(R.id.genderRadioGroup);
       // gender = genderView.getText().toString();
        sexuality = sexualitySpinner.getSelectedItem().toString();

        genderCheck= genderRadioGroup.getCheckedRadioButtonId();

        if(genderCheck==R.id.genderMale){
            gender ="Male";
        }
        else if (genderCheck == R.id.genderFemale){
            gender = "Female";
        }
        else if (genderCheck == R.id.genderNonBinary){
            gender = "Non-Binary";
        }
        else{
            gender = "N/A";
        }

        year = yearView.getSelectedItem().toString();
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

        year = spinner.getSelectedItem().toString();

        user = AuthenticationHelper.getCurrentUser();

        if(gender == null){
            gender ="N/A";
        }

        if(sexuality == null){
            sexuality ="N/A";
        }
        if (year == null){
            year = "0";
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

        User newUser = new User( email, username, gender, sexuality, user.getUid(), year, affiliated, preferences, new HashMap<>(), new HashMap<>(), false);

        UserDatabaseHelper.createUser(newUser);

        Intent intent = new Intent(getBaseContext(), MainActivity.class);
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