package com.example.fratnav.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.example.fratnav.MainActivity;
import com.example.fratnav.R;
import com.example.fratnav.callbacks.getHouseByIdCallback;
import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.HouseDatabaseHelper;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.forum.Forum;
import com.example.fratnav.houses.HousesSearch;
import com.example.fratnav.models.House;
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
    public House theHouse;
    public String userId;

    public static final String NATIONAL_KEY = "nationalkey";
    public static final String SUMMARY_KEY = "summarykey";
    public static final String PRESIDENT_KEY = "presidentkey";
    public static final String VICE_KEY = "vicekey";
    public static final String TREASURER_KEY = "treasurerkey";
    public static final String RUSH_KEY = "rushkey";
    public static final String TOTAL_KEY = "totalkey";
    public static final String QUEER_KEY = "queerkey";
    public static final String POC_KEY = "pockey";

    public static final String YEAR_KEY = "year";
    public static final String SEXUALITY_KEY = "sexuality";
    public static final String GENDER_KEY = "gender";
    public static final String FRATS_KEY = "frats";
    public static final String SRATS_KEY = "srats";
    public static final String GENDER_INC_KEY = "genderInc";
    public static final String PAN_KEY = "pan";
    public static final String HOUSE_AFF_KEY = "houseAff";




    boolean ishouse;
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
        userId = currentUser.getUid();

        ishouse = getIntent().getBooleanExtra(Profile.HOUSE_BOOLEAN_KEY, false);

        //checks if the account is a house and renders the house update profile
        if (ishouse){

            setContentView(R.layout.update_houseprofile);
            setHouseProfile(savedInstanceState);
        }
        //otherwise renders user update profile
        else {
            setContentView(R.layout.update_profile);

            //create and set spinner
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



            UserDatabaseHelper.getUserById(useriD, new getUserByIdCallback() {
                @Override
                public void onCallback(User user) {
                    if (savedInstanceState != null) {
                        recoverInstanceState(savedInstanceState, user);
                    } else {
                        username = user.username;

                        RadioButton maleButton = findViewById(R.id.genderUpdateMale);
                        RadioButton femaleButton = findViewById(R.id.genderUpdateFemale);
                        RadioButton nonBinaryButton = findViewById(R.id.genderUpdateNonBinary);


                        RadioButton affiliate = findViewById(R.id.updateYesAffiliated);
                        RadioButton notAffiliate = findViewById(R.id.updateNoAffiliated);

                        //sets the default to the data in the database
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

                        //sets the default to the data in the database
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

                        //sets the default to the data in the database
                        spinner.setPrompt(user.year);
                        sexualitySpinner.setPrompt(user.sexuality);
                        CheckBox srat = findViewById(R.id.soroCheckboxUpdate);
                        CheckBox frat = findViewById(R.id.fratCheckboxUpdate);
                        CheckBox natPanHelic = findViewById(R.id.natPanHelicChackBoxUpdate);
                        CheckBox genderInclusive = findViewById(R.id.genderInclusiveCheckBoxUpdate);

                        //sets the default to the data in the database
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


                        //sets the default to the data in the database
                        if (user.gender != null) {
                            if (user.gender.equals("Male")) {
                                maleButton.setChecked(true);
                            } else if (user.gender.equals("Female")) {
                                femaleButton.setChecked(true);
                            } else {
                                nonBinaryButton.setChecked(true);
                            }
                        }

                        //sets the default to the data in the database
                        if (user.houseAffiliation) {
                            affiliate.setChecked(true);
                        } else {
                            notAffiliate.setChecked(true);
                        }

                        //sets an onclick listener in order to update the information
                        Button updateButton = findViewById(R.id.updateAccount);

                        updateButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateAccount();
                            }
                        });

                    }
                }
            });
        }

        }


    //this send the information collected from the inputs and sends it to database to update
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

    //image picks from camera
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

    //sets nav bar
    public void setNavBar(){

        bottomBar = (BottomNavigationView) findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.profile);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId()==R.id.houses) {

                    startActivity(new Intent(updateProfile.this, HousesSearch.class));
                    finish();
                    return true;
                }
                else if (item.getItemId()==R.id.home) {

                    startActivity(new Intent(updateProfile.this, MainActivity.class));
                    finish();
                    return true;
                }
                else if  (item.getItemId()==R.id.forum){

                    startActivity(new Intent(updateProfile.this, Forum.class));
                    finish();
                    return true;
                }
                return false;
            }

        });

    }


    //uploads an image
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


        // will put in house user id here
        HouseDatabaseHelper.addUrlToHouse(currentUser.getUid(), url);
    }

    //checks permisions for app
    private void checkPermissions() {
        if(Build.VERSION.SDK_INT < 23)
            return;

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 0);
        }

    }

    //set the house profile accordingly
    public void setHouseProfile(Bundle savedInstanceState){
        setNavBar();
        UserDatabaseHelper.getUserById(userId, new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {
                String houseId = user.houseId;
                HouseDatabaseHelper.getHouseById(houseId, new getHouseByIdCallback() {
                    @Override
                    public void onCallback(House house) {
                        theHouse = house;

                        if (savedInstanceState != null) {
                            recoverInstanceState(savedInstanceState, house);
                        } else {


                            //sets given information ot the textViews
                            TextView houseName = findViewById(R.id.house_name);
                            houseName.setText(house.houseName);

                            TextView subscribers = findViewById(R.id.subscribers);
                            String subs = String.valueOf(house.subscribers) + " Subscribers";
                            subscribers.setText(subs);

                            setHouseImage(house);


                            RadioButton nationalB = findViewById(R.id.national);
                            RadioButton localB = findViewById(R.id.local);

                            if (house.national) {
                                nationalB.setChecked(true);
                            } else {
                                localB.setChecked(true);
                            }

                            EditText editSum = findViewById(R.id.edit_summary);
                            editSum.setText(house.summary);

                            EditText pres = findViewById(R.id.edit_president);
                            EditText vp = findViewById(R.id.edit_vp);
                            EditText treas = findViewById(R.id.edit_treasurer);
                            EditText rush = findViewById(R.id.edit_rc);

                            pres.setText(house.president);
                            vp.setText(house.vicePresident);
                            treas.setText(house.treasurer);
                            rush.setText(house.rushChair);

                            EditText tot = findViewById(R.id.edit_total);
                            EditText poc = findViewById(R.id.edit_poc);
                            EditText queer = findViewById(R.id.edit_lgbtq);

                            tot.setText(house.totalMembers);
                            poc.setText(house.pocMembers);
                            queer.setText(house.queerMembers);
                        }
                    }
                });
            }
        });

    }

    //saves the profile information
    public void onSaveProfile(View v){
        RadioButton nationalB = findViewById(R.id.national);
        RadioButton localB = findViewById(R.id.local);

        boolean national = false;
        String summary = "";
        String president = "";
        String vice = "";
        String treasurer = "";
        String rushChair = "";
        String totalMembers = "";
        String queerMembers = "";
        String pocMembers = "";

        if (nationalB.isChecked()){
            national = true;
        }

        EditText editSum = findViewById(R.id.edit_summary);
        summary = editSum.getText().toString();


        EditText pres = findViewById(R.id.edit_president);
        EditText vp = findViewById(R.id.edit_vp);
        EditText treas = findViewById(R.id.edit_treasurer);
        EditText rush = findViewById(R.id.edit_rc);

        president = pres.getText().toString();
        vice = vp.getText().toString();
        treasurer = treas.getText().toString();
        rushChair = rush.getText().toString();


        EditText tot = findViewById(R.id.edit_total);
        EditText poc = findViewById(R.id.edit_poc);
        EditText queer = findViewById(R.id.edit_lgbtq);

        totalMembers = tot.getText().toString();
        pocMembers = poc.getText().toString();
        queerMembers = queer.getText().toString();

        House house = new House(theHouse.id, theHouse.houseName, summary, president, vice, treasurer, rushChair,
                totalMembers, pocMembers, queerMembers);

        HouseDatabaseHelper.updateHouse(house);
        finish();
    }

    @Override
    //saves input-ed information on orientation change
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (ishouse) {
            RadioButton nationalB = findViewById(R.id.national);
            RadioButton localB = findViewById(R.id.local);
            boolean national = false;
            String summary = "";
            String president = "";
            String vice = "";
            String treasurer = "";
            String rushChair = "";
            String totalMembers = "";
            String queerMembers = "";
            String pocMembers = "";

            if (nationalB.isChecked()) {
                national = true;
            }

            EditText editSum = findViewById(R.id.edit_summary);
            summary = editSum.getText().toString();


            EditText pres = findViewById(R.id.edit_president);
            EditText vp = findViewById(R.id.edit_vp);
            EditText treas = findViewById(R.id.edit_treasurer);
            EditText rush = findViewById(R.id.edit_rc);

            president = pres.getText().toString();
            vice = vp.getText().toString();
            treasurer = treas.getText().toString();
            rushChair = rush.getText().toString();


            EditText tot = findViewById(R.id.edit_total);
            EditText poc = findViewById(R.id.edit_poc);
            EditText queer = findViewById(R.id.edit_lgbtq);

            totalMembers = tot.getText().toString();
            pocMembers = poc.getText().toString();
            queerMembers = queer.getText().toString();

            outState.putBoolean(NATIONAL_KEY, national);
            outState.putString(SUMMARY_KEY, summary);
            outState.putString(PRESIDENT_KEY, president);
            outState.putString(VICE_KEY, vice);
            outState.putString(TREASURER_KEY, treasurer);
            outState.putString(RUSH_KEY, rushChair);
            outState.putString(TOTAL_KEY, totalMembers);
            outState.putString(POC_KEY, pocMembers);
            outState.putString(QUEER_KEY, queerMembers);

        }
        else{

            RadioButton maleButton = findViewById(R.id.genderUpdateMale);
            RadioButton femaleButton = findViewById(R.id.genderUpdateFemale);
            RadioButton nonBinaryButton = findViewById(R.id.genderUpdateNonBinary);
            //RadioGroup affiliatedRadioGroup = findViewById(R.id.affiliationUpdate);

            RadioButton affiliate = findViewById(R.id.updateYesAffiliated);
            RadioButton notAffiliate = findViewById(R.id.updateNoAffiliated);

            spinner = findViewById(R.id.updateYear);
            sexualitySpinner = findViewById(R.id.updateSexuality);

            String year = "";
            String sexuality = "";
            boolean frats = false;
            boolean sor = false;
            boolean gend = false;
            boolean pan = false;
            String gender = "";
            boolean house = false;

            year = spinner.getSelectedItem().toString();
            sexuality = sexualitySpinner.getSelectedItem().toString();
            CheckBox srat = findViewById(R.id.soroCheckboxUpdate);
            CheckBox frat = findViewById(R.id.fratCheckboxUpdate);
            CheckBox natPanHelic = findViewById(R.id.natPanHelicChackBoxUpdate);
            CheckBox genderInclusive = findViewById(R.id.genderInclusiveCheckBoxUpdate);

            sor = srat.isChecked();
            frats = frat.isChecked();
            gend = genderInclusive.isChecked();
            pan = natPanHelic.isChecked();

            if (maleButton.isChecked()){
                gender = "Male";
            }
            else if (femaleButton.isChecked()){
                gender = "Female";
            }
            else{
                gender = "Non-binary";
            }

            if (affiliate.isChecked()){
                house = true;
            }

            outState.putString(YEAR_KEY, year);
            outState.putString(SEXUALITY_KEY, sexuality);
            outState.putString(GENDER_KEY, gender);
            outState.putBoolean(FRATS_KEY, frats);
            outState.putBoolean(SRATS_KEY, sor);
            outState.putBoolean(GENDER_INC_KEY, gend);
            outState.putBoolean(PAN_KEY, pan);
            outState.putBoolean(HOUSE_AFF_KEY, house);
        }
    }

    //sets everyting from the saved instance state for houses
    public void recoverInstanceState(Bundle savedInstanceState, House house){
            TextView houseName = findViewById(R.id.house_name);
            houseName.setText(house.houseName);
            TextView subscribers = findViewById(R.id.subscribers);
            Log.d("housesubs", String.valueOf(house.subscribers));
            String subs = String.valueOf(house.subscribers) + " Subscribers";
            subscribers.setText(subs);
            setHouseImage(house);
            RadioButton nationalB = findViewById(R.id.national);
            RadioButton localB = findViewById(R.id.local);

            boolean national = savedInstanceState.getBoolean(NATIONAL_KEY);
            if (national){
                nationalB.setChecked(true);
            }
            else{
                localB.setChecked(true);
            }

            String summary = "";
            String president = "";
            String vice = "";
            String treasurer = "";
            String rushChair = "";
            String totalMembers = "";
            String queerMembers = "";
            String pocMembers = "";

            EditText editSum = findViewById(R.id.edit_summary);
            summary = savedInstanceState.getString(SUMMARY_KEY, "");
            editSum.setText(summary);

            EditText pres = findViewById(R.id.edit_president);
            EditText vp = findViewById(R.id.edit_vp);
            EditText treas = findViewById(R.id.edit_treasurer);
            EditText rush = findViewById(R.id.edit_rc);

            president = savedInstanceState.getString(PRESIDENT_KEY, "");
            pres.setText(president);

            vice = savedInstanceState.getString(VICE_KEY, "");
            vp.setText(vice);

            treasurer = savedInstanceState.getString(TREASURER_KEY, "");
            treas.setText(treasurer);

            rushChair = savedInstanceState.getString(RUSH_KEY, "");
            rush.setText(rushChair);

            totalMembers = savedInstanceState.getString(TOTAL_KEY, "");
            pocMembers = savedInstanceState.getString(POC_KEY, "");
            queerMembers = savedInstanceState.getString(QUEER_KEY, "");

            EditText tot = findViewById(R.id.edit_total);
            EditText poc = findViewById(R.id.edit_poc);
            EditText queer = findViewById(R.id.edit_lgbtq);

            tot.setText(totalMembers);
            poc.setText(pocMembers);
            queer.setText(queerMembers);

    }
    //sets everyting from the saved instance state for user
    public void recoverInstanceState(Bundle savedInstanceState, User user){
        String year = savedInstanceState.getString(YEAR_KEY, "");
        String sexuality = savedInstanceState.getString(SEXUALITY_KEY, "");
        String gender = savedInstanceState.getString(GENDER_KEY, "");
        boolean frats = savedInstanceState.getBoolean(FRATS_KEY, false);

        boolean srats = savedInstanceState.getBoolean(SRATS_KEY, false);
        boolean genderInc = savedInstanceState.getBoolean(GENDER_INC_KEY, false);
        boolean pan = savedInstanceState.getBoolean(PAN_KEY, false);
        boolean houseAff = savedInstanceState.getBoolean(HOUSE_AFF_KEY, false);

        username = user.username;

        RadioButton maleButton = findViewById(R.id.genderUpdateMale);
        RadioButton femaleButton = findViewById(R.id.genderUpdateFemale);
        RadioButton nonBinaryButton = findViewById(R.id.genderUpdateNonBinary);
        //RadioGroup affiliatedRadioGroup = findViewById(R.id.affiliationUpdate);


        RadioButton affiliate = findViewById(R.id.updateYesAffiliated);
        RadioButton notAffiliate = findViewById(R.id.updateNoAffiliated);

        switch (year) {
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

        switch (sexuality) {
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


        spinner.setPrompt(year);
        sexualitySpinner.setPrompt(sexuality);
        CheckBox srat = findViewById(R.id.soroCheckboxUpdate);
        CheckBox frat = findViewById(R.id.fratCheckboxUpdate);
        CheckBox natPanHelic = findViewById(R.id.natPanHelicChackBoxUpdate);
        CheckBox genderInclusive = findViewById(R.id.genderInclusiveCheckBoxUpdate);

            if (frats) {
                frat.setChecked(true);
            }
            if (srats) {
                natPanHelic.setChecked(true);
            }
            if (genderInc) {
                genderInclusive.setChecked(true);
            }
            if (pan) {
                srat.setChecked(true);

        }


        if (!gender.equals("")) {
            if (gender.equals("Male")) {
                maleButton.setChecked(true);
            } else if (gender.equals("Female")) {
                femaleButton.setChecked(true);
            } else {
                nonBinaryButton.setChecked(true);
            }
        }
        if (houseAff) {
            affiliate.setChecked(true);
        } else {
            notAffiliate.setChecked(true);
        }

    }

    public void setHouseImage(House house){
        int image = 0;
        switch (house.houseName) {
            case "Sig Nu":
                image = R.drawable.signu1;
                break;
            case "TDX":
                image = R.drawable.tdx;
                break;
            case "Zete":
                image = R.drawable.zetapsi;
                break;
            case "Sigma Delt":
                image = R.drawable.sigdelt;
                break;
            case "Tabard":
                image = R.drawable.tabard;
                break;
            case "Tri-Kap":
                image = R.drawable.trikap;
                break;
            case "Kappa":
                image = R.drawable.kkg;
                break;
            case "Hereot":
                image = R.drawable.heorot;
                break;
            case "Phi Delt":
                image = R.drawable.phidelta;
                break;
            case "KD":
                image = R.drawable.kd;
                break;
            case "KDE":
                image = R.drawable.kde;
                break;
            case "Phi Tau":
                image = R.drawable.phitau;
                break;
            case "Alpha Chi":
                image = R.drawable.ic_axa;
                break;
            case "GDX":
                image = R.drawable.gdx;
                break;
            case "Psi U":
                image = R.drawable.psiu;
                break;
            case "Chi Delt":
                image = R.drawable.chidelt;
                break;
            case "Chi Gam":
                image = R.drawable.chigam;
                break;
            case "EKT":
                image = R.drawable.ekt;
                break;
            case "Deltas":
                image = R.drawable.deltas;
                break;
            case "AXiD":
                image = R.drawable.axid;
                break;
            case "Beta":
                image = R.drawable.beta;
                break;
            case "BG":
                image = R.drawable.bg;
                break;
            case "APhi":
                image = R.drawable.aphi1;
                break;
            case "Alpha Theta":
                image = R.drawable.alphatheta;
                break;
            case "Alphas":
                image = R.drawable.alphas;
                break;
            case "AKA":
                image = R.drawable.aka;
                break;
            default:
                break;
        }
        ImageView iv = (ImageView) findViewById(R.id.housePageImage);
        iv.setImageDrawable(ResourcesCompat.getDrawable(getApplicationContext().getResources(), image, null));
    }
}
