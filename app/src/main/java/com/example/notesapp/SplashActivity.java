package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
                if (firebaseUser==null ){
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }

                else {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }

            }
        },1500);

    }
}