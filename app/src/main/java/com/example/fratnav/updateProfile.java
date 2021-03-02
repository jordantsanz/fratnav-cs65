package com.example.fratnav;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class updateProfile extends AppCompatActivity {
    BottomNavigationView bottomBar;
    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private EditText userText;
    private TextView helloUser;
    Spinner spinner;
    Spinner sexualitySpinner;
    DatabaseReference.CompletionListener completionListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // checks to make sure the user is currently logged in; otherwise, send to authentication
        if (currentUser == null) {
            startActivity(new Intent(this, Authentication.class));
            finish();
            return;
        }
        spinner = findViewById(R.id.updateYear);
        sexualitySpinner =findViewById(R.id.updateSexuality);

        ArrayList<String> yearOptions = new ArrayList<>();
        yearOptions.add("2021");
        yearOptions.add("2022");
        yearOptions.add("2023");
        yearOptions.add("2024");
        yearOptions.add("2025");

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearOptions);


        ArrayList<String> sexualityOptions = new ArrayList<>();
        sexualityOptions.add("Heterosexual");
        sexualityOptions.add("Lesbian");
        sexualityOptions.add("Gay");
        sexualityOptions.add("Bisexual");
        sexualityOptions.add("Transgender");
        sexualityOptions.add("Queer");
        sexualityOptions.add("N/A");
        ArrayAdapter<String> sexualityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sexualityOptions);



        spinner.setAdapter(yearAdapter);
        sexualitySpinner.setAdapter(sexualityAdapter);

        String useriD = currentUser.getUid();
        Log.d("firebaseuser", currentUser.getUid());



        UserDatabaseHelper.getUserById(useriD, new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {

                RadioButton maleButton = findViewById(R.id.genderUpdateMale);
                RadioButton femaleButton = findViewById(R.id.genderUpdateFemale);
                RadioButton nonBinaryButton = findViewById(R.id.genderUpdateNonBinary);
                RadioGroup affiliatedRadioGroup = findViewById(R.id.affiliationUpdate);

                RadioButton affiliate = findViewById(R.id.updateYesAffiliated);
                RadioButton notAffilated = findViewById(R.id.updateNoAffiliated);
                CheckBox srat = findViewById(R.id.soroCheckboxUpdate);
                CheckBox frat = findViewById(R.id.fratCheckboxUpdate);
                CheckBox natPanHelic = findViewById(R.id.natPanHelicChackBoxUpdate);
                CheckBox genderInclusive = findViewById(R.id.genderInclusiveCheckBoxUpdate);
                if (user.interestedIn != null){
                    for (int i = 0; i < user.interestedIn.size(); i++) {
                        //Log.d("user",user.interestedIn.toString());
                            if (user.interestedIn.get(i).equals("Fraternities")) {
                                frat.setChecked(true);
                            }
                            if (user.interestedIn.get(i).equals("National Pan-Hellenic")) {
                                natPanHelic.setChecked(true);
                            }
                            if (user.interestedIn.get(i).equals("Gender Inclusive Houses")) {
                                genderInclusive.setChecked(true);
                            }
                            if (user.interestedIn.get(i).equals("Sororities")){
                                srat.setChecked(true);
                            }
                        }
                    }


                if (user.gender != null) {
                    if (user.gender.equals("Male")) {
                        maleButton.setChecked(true);
                    } else if (user.gender.equals("Female")) {
                        femaleButton.setChecked(true);
                    } else {
                        nonBinaryButton.setChecked(true);
                    }
                }
                if (user.houseAffiliation){
                    affiliate.setChecked(true);
                }
                else{
                    affiliate.setChecked(true);
                }

            }
        });



    }



    public void update(){
        finish();
    }
}
