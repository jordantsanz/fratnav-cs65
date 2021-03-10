package com.example.fratnav.databaseHelpers;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Database helper to communicate with Firebase Authentication
 * Returns Firebase User with userId
 *
 */
public class AuthenticationHelper {

    public static FirebaseUser getCurrentUser(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        return user;

    }
}
