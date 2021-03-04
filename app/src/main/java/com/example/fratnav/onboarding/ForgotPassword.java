package com.example.fratnav.onboarding;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fratnav.R;
import com.example.fratnav.tools.Utilities;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class ForgotPassword extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
    }


    public void onSubmitEmail(View view){
        TextView emailText = findViewById(R.id.email_recovery);
        String email = emailText.getText().toString();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (!email.equals("")) {
            mAuth.sendPasswordResetEmail(email);
            Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
