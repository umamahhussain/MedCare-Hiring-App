package com.example.medcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText usernameET, passwordET;
    private MaterialButton buttonSignIn;
    private FirebaseAuth mAuth;
    TextView signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        init();
        signupLink.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectUser.class);
            startActivity(intent);
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // On sign-in button click
        buttonSignIn.setOnClickListener(v -> signInUser());

        // On Sign-Up link click
        signupLink.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectUser.class);
            startActivity(intent);
        });
    }

    // Initialize views
    public void init() {
        signupLink = findViewById(R.id.signUpLink);
        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        signupLink = findViewById(R.id.signUpLink);
    }

    // Handle Sign-in functionality
    private void signInUser() {
        String email = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        // Validate input
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sign in with Firebase Auth
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in successful, redirect to another activity
                        Intent intent = new Intent(Login.this, Home.class);  // Replace with your main activity
                        startActivity(intent);
                        finish();  // Close the login activity
                    } else {
                        // Sign-in failed, show error
                        Toast.makeText(Login.this, "Authentication failed. Try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
