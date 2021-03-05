package com.example.fratnav.tools;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class HouseAccounts {
    public static FirebaseAuth mAuth;
    public static Context context;


    public HouseAccounts(Context contextIn){
        mAuth = FirebaseAuth.getInstance();
        context = contextIn;
    }

    public void createHouseAccounts(){

        // fraternities
        createHouseAccountAuth("alphachi@dartmouth.edu", "alphachi", "-MUqoqiR3O8h_iUfRGsb");
        createHouseAccountAuth("beta@dartmouth.edu", "beta", "-MUqoqialsJpCE5Et2l-");
        createHouseAccountAuth("bg@dartmouth.edu", "bonesgate", "-MUqoqifBPWZMRGHT-oR");
        createHouseAccountAuth("chigam@dartmouth.edu", "chiGam", "-MUqoqilvWd0iN0emv4O");
        createHouseAccountAuth("hereot@dartmouth.edu", "hereot", "-MUqoqiqyo1Y-xocoVNQ");
        createHouseAccountAuth("gdx@dartmouth.edu", "gdx", "-MUqoqiwnYkV3lqT6v-M");
        createHouseAccountAuth("trikap@dartmouth.edu", "trikap", "-MUqoqj1LzBG9YBhAMVd");
        createHouseAccountAuth("phidelt@dartmouth.edu", "phidelt", "-MUqoqj6_uaPwgYltCWa");
        createHouseAccountAuth("psiu@dartmouth.edu", "psiu", "-MUqoqjBYEza880-LqY6");
        createHouseAccountAuth("signu@dartmouth.edu", "signu", "-MUqoqjFrDMJVLmTD-TZ");
        createHouseAccountAuth("tdx@dartmouth.edu", "tdx", "-MUqoqjIkjbVQ_hMGaZd");
        createHouseAccountAuth("zete@dartmouth.edu", "zete", "-MUqoqjMCmPsfqf5Gcy1");

        //sororities
        createHouseAccountAuth("aphi@dartmouth.edu", "aphi", "-MUqoqjQDbAv7Lwc0tgv");
        createHouseAccountAuth("axid@dartmouth.edu", "axid", "-MUqoqjVjMV3Tzc86yzd");
        createHouseAccountAuth("chidelt@dartmouth.edu", "chidelt", "-MUqoqj_anCmImV1oagv");
        createHouseAccountAuth("ekt@dartmouth.edu", "ekt", "-MUqoqjdNHouCHq8596i");
        createHouseAccountAuth("kd@dartmouth.edu", "kd", "-MUqoqjfXMMBfDPDtu-T");
        createHouseAccountAuth("kde@dartmouth.edu", "kde", "-MUqoqjiOKA1mjMaWOcj");
        createHouseAccountAuth("kappa@dartmouth.edu", "kappa", "-MUqoqjk4n3VaO-kMuKo");
        createHouseAccountAuth("sigmadelt@dartmouth.edu", "sigmadelt", "-MUqoqjuYEIGny1WEbwt");

        // gender inclusive
        createHouseAccountAuth("alphatheta@dartmouth.edu", "alphatheta", "-MUqoqjx1JVWqs_hzNJO");
        createHouseAccountAuth("phitau@dartmouth.edu", "phitau", "-MUqoqjx1JVWqs_hzNJO");
        createHouseAccountAuth("tabard@dartmouth.edu", "tabard", "-MUqoqk4n3rKP-PNQSX5");

        // pan-helenic
        createHouseAccountAuth("alphas@dartmouth.edu", "alphas", "-MUqoqk7kpZcJmN5LVW1");
        createHouseAccountAuth("aka@dartmouth.edu" ,"aka", "-MUqoqkAv6g8HqlkxDrc");
        createHouseAccountAuth("deltas@dartmouth.edu" ,"deltas", "-MUqoqkHhtNKvWtrvpsj");
    }

    public static void createHouseAccountAuth(String email, String username, String houseId){

        mAuth.createUserWithEmailAndPassword(email, "hello123") // temp password
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            createNewUser(email, user.getUid(), username, houseId);

                        } else {

                            Log.d("error", "Unsuccessful task");
                            Log.d("error", Objects.requireNonNull(task.getException()).toString());
                        }

                    }
                });

    }

    public static void createNewUser(String email, String userId, String username, String houseId){
        User user = new User(email, userId, username, true, houseId);
        UserDatabaseHelper.createUser(user);
    }
}
