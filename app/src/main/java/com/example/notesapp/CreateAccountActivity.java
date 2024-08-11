package com.example.notesapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {
    EditText emailEdittxt, passedittxt, confirmpass;
    ProgressBar progressBar;
    TextView loginbtn;
    Button createaccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acount);
        emailEdittxt = findViewById(R.id.signupemail);
        passedittxt = findViewById(R.id.signuppassword);
        confirmpass = findViewById(R.id.signupConfirmPassword);
        progressBar = findViewById(R.id.progrees_bar);
        loginbtn = findViewById(R.id.signup_login_btn);
        createaccount = findViewById(R.id.Create_account_btn);

        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccountSignUp();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void createAccountSignUp() {
        String email = emailEdittxt.getText().toString();
        String pass = passedittxt.getText().toString();
        String confpass = confirmpass.getText().toString();
        boolean isvalidated = validation(email, pass, confpass);
        if (!isvalidated) {
            return;
        }
        createAccountInFirebase(email, pass);
    }

    private void createAccountInFirebase(String email, String pass) {
        changeInProgress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if (task.isSuccessful()) {
                    Toast.makeText(CreateAccountActivity.this, "Check Email For Verification", Toast.LENGTH_LONG).show();
                    if (firebaseAuth.getCurrentUser() != null) {
                        firebaseAuth.getCurrentUser().sendEmailVerification();
                    }
                    firebaseAuth.signOut();
                    startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(CreateAccountActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void changeInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            createaccount.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            createaccount.setVisibility(View.VISIBLE);
        }
    }

    Boolean validation(String email, String pass, String confpass) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdittxt.setError("Invalid Email");
            return false;
        }
        if (pass.length() < 6) {
            passedittxt.setError("Password length is invalid");
            return false;
        }
        if (!pass.equals(confpass)) {
            confirmpass.setError("Passwords do not match");
            return false;
        }
        return true;
    }
}
