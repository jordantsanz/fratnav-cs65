package com.example.fratnav.tools;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class HouseAccounts {
    public static FirebaseAuth mAuth;
    public static Context context;


    public HouseAccounts(Context contextIn){
        mAuth = FirebaseAuth.getInstance();
        context = contextIn;
    }

    public static void createHouseAccounts(){
        HashMap<String, String> emails = new HashMap<>();
        emails.put("signu@gmail.com", "signu");

        for (String email : emails.keySet()){
            createHouseAccountAuth(email, emails.get(email));
        }
    }

    public static void createHouseAccountAuth(String email, String username){

        mAuth.createUserWithEmailAndPassword(email, "hello123") // temp password
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            assert user != null;
                            createNewUser(email, user.getUid(), username);

                        } else {

                        }

                    }
                });

    }

    public static void createNewUser(String email, String userId, String username){
        User user = new User(email, userId, username, true);
        UserDatabaseHelper.createUser(user);
        HouseCreation.createHouse(username, userId);

    }
}
