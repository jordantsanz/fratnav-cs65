package com.example.fratnav;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateProfile extends AppCompatActivity {
    private static FirebaseAuth mAuth;
    String gender;
    String sexuality;
    Integer year;
    Integer affiliatedCheck;
    String affiliated;
    FirebaseUser user;

    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile_houses);
        mAuth = FirebaseAuth.getInstance();
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
            affiliated = "yes";
        }
        else{
            affiliated="no";
        }
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