package com.example.notesapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText emailedittxt,passedittxt;
    Button loginbtn;
    ProgressBar progressBar;
    TextView createaccount;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailedittxt=findViewById(R.id.loginemail);
        passedittxt=findViewById(R.id.loginpassword);
        progressBar=findViewById(R.id.progrees_bar);
        loginbtn=findViewById(R.id.login_account_btn);
        createaccount=findViewById(R.id.login_signup_btn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signintheaccount();
            }
        });
        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class));
                finish();
            }
        });

    }

    private void signintheaccount() {
        String email = emailedittxt.getText().toString();
        String pass = passedittxt.getText().toString();
        boolean isvalidated = validation(email, pass);
        if (!isvalidated) {
            return;
        }
        loginoperation(email, pass);
    }

    private void loginoperation(String email, String pass) {
        changeinprogress(true);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeinprogress(false);
                if (task.isSuccessful()) {
                    if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        utility.showtoast(LoginActivity.this, "Email not verified, Please verify your email");
                        utility.showtoast(LoginActivity.this, "Check Your Email");
                    }
                } else {
                    utility.showtoast(LoginActivity.this, task.getException().getLocalizedMessage());
                }
            }
        });
    }




    void changeinprogress(boolean inprogress){
        if(inprogress){
            progressBar.setVisibility(View.VISIBLE);
            loginbtn.setVisibility(View.GONE);
        }
        else {
            progressBar.setVisibility(View.GONE);
            loginbtn.setVisibility(View.VISIBLE);
        }
    }

    Boolean validation(String email,String pass){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailedittxt.setError("Invalid Email");
            return false;
        }
        if (pass.length()<6){
            passedittxt.setError("Password Lenght is invalid");
            return  false;
        }

        return  true;
    }


}