package com.example.fratnav.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.example.fratnav.R;
import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.HouseDatabaseHelper;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.models.User;
import com.example.fratnav.onboarding.Authentication;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class updateProfile extends AppCompatActivity {
    BottomNavigationView bottomBar;
    private static FirebaseUser currentUser;
    private static final String TAG = "RealtimeDB";
    String key = "AKIAYLJMLQUVXCV5377P"; // will secure these soon
    String secret = "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";
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private EditText userText;
    private TextView helloUser;
    private String username;
    Integer position;
    Spinner spinner;
    Spinner sexualitySpinner;
    DatabaseReference.CompletionListener completionListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // checks to make sure the user is currently logged in; otherwise, send to authentication
        if (currentUser == null) {
            startActivity(new Intent(this, Authentication.class));
            finish();
            return;
        }
        String userId = currentUser.getUid();

        boolean ishouse = getIntent().getBooleanExtra(Profile.HOUSE_BOOLEAN_KEY, false);

        if (ishouse){
            setContentView(R.layout.update_houseprofile);
        }
        else {
            setContentView(R.layout.update_profile);


            spinner = findViewById(R.id.updateYear);
            sexualitySpinner = findViewById(R.id.updateSexuality);

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

                    username = user.username;

                    RadioButton maleButton = findViewById(R.id.genderUpdateMale);
                    RadioButton femaleButton = findViewById(R.id.genderUpdateFemale);
                    RadioButton nonBinaryButton = findViewById(R.id.genderUpdateNonBinary);
                    //RadioGroup affiliatedRadioGroup = findViewById(R.id.affiliationUpdate);


                    RadioButton affiliate = findViewById(R.id.updateYesAffiliated);
                    RadioButton notAffiliate = findViewById(R.id.updateNoAffiliated);

                    switch (user.year) {
                        case "2021":
                            spinner.setSelection(0);
                            break;
                        case "2022":
                            spinner.setSelection(1);
                            break;
                        case "2023":
                            spinner.setSelection(2);
                            break;
                        case "2024":
                            spinner.setSelection(3);
                            break;
                        case "2025":
                            spinner.setSelection(4);
                            break;
                    }
                    Log.d("user sexuality", user.sexuality);
                    switch (user.sexuality) {
                        case "Heterosexual":
                            sexualitySpinner.setSelection(0);
                            break;
                        case "Lesbian":
                            sexualitySpinner.setSelection(1);
                            break;
                        case "Gay":
                            sexualitySpinner.setSelection(2);
                            break;
                        case "Bisexual":
                            sexualitySpinner.setSelection(3);
                            break;
                        case "Transgender":
                            sexualitySpinner.setSelection(4);
                            break;
                        case "Queer":
                            sexualitySpinner.setSelection(5);
                            break;
                        case "N/A":
                            sexualitySpinner.setSelection(6);
                            break;

                    }


                    spinner.setPrompt(user.year);
                    sexualitySpinner.setPrompt(user.sexuality);
                    CheckBox srat = findViewById(R.id.soroCheckboxUpdate);
                    CheckBox frat = findViewById(R.id.fratCheckboxUpdate);
                    CheckBox natPanHelic = findViewById(R.id.natPanHelicChackBoxUpdate);
                    CheckBox genderInclusive = findViewById(R.id.genderInclusiveCheckBoxUpdate);


                    if (user.interestedIn != null) {
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
                            if (user.interestedIn.get(i).equals("Sororities")) {
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
                    if (user.houseAffiliation) {
                        affiliate.setChecked(true);
                    } else {
                        notAffiliate.setChecked(true);
                    }

                }
            });


            Button updateButton = findViewById(R.id.updateAccount);

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateAccount();
                }
            });

        }



    }



    public void updateAccount(){

        String gender = "";
        String sexuality = "";
        String year = "";
        boolean houseAffiliation = false;
        ArrayList<String> interestedIn = new ArrayList<>();


        // find gender
        RadioGroup genderGroup = findViewById(R.id.genderUpdateRadioGroup);
        int id = genderGroup.getCheckedRadioButtonId();
        RadioButton rb = findViewById(id);

        gender = rb.getText().toString();


        // find sexuality

        Spinner sexualitySpinner = findViewById(R.id.updateSexuality);
        sexuality = sexualitySpinner.getSelectedItem().toString();

        // find year
        Spinner yearSpinner = findViewById(R.id.updateYear);
        year = yearSpinner.getSelectedItem().toString();

        // find affiliation
        RadioGroup affiliatedGroup = findViewById(R.id.createAffiliated);
        int idaff = affiliatedGroup.getCheckedRadioButtonId();
        RadioButton afButton = findViewById(idaff);

        String houseAffiliationString = afButton.getText().toString();

        Log.d("affil", houseAffiliationString);

        if (houseAffiliationString.equals("yes")){
            houseAffiliation = true;
        }

        // find interested in

        CheckBox soroCheckbox = findViewById(R.id.soroCheckboxUpdate);
        CheckBox fratCheckbox = findViewById(R.id.fratCheckboxUpdate);
        CheckBox panCheckbox = findViewById(R.id.natPanHelicChackBoxUpdate);
        CheckBox genderIncCheckbox = findViewById(R.id.genderInclusiveCheckBoxUpdate);

        if (soroCheckbox.isChecked()){
            interestedIn.add("Sororities");
        }
        if (fratCheckbox.isChecked()){
            interestedIn.add("Fraternities");
        }
        if (panCheckbox.isChecked()){
            interestedIn.add("National Pan-Hellenic");
        }
        if (genderIncCheckbox.isChecked()){
            interestedIn.add("Gender Inclusive Houses");
        }

        User user = new User(currentUser.getEmail(), username, gender, sexuality, currentUser.getUid(),
        year, houseAffiliation, interestedIn, new HashMap<>(), new HashMap<>(), false);

        UserDatabaseHelper.updateUserProfile(currentUser.getUid(), user);

        finish();
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
        HouseDatabaseHelper.addUrlToHouse(currentUser.getUid(), url);
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
