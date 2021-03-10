package com.example.fratnav.onboarding;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fratnav.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //sets XML
        setContentView(R.layout.forgot_password);
    }

    //sends the email to Firbase to sent the email and recover the password
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
