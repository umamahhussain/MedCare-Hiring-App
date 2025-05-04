package com.example.medcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity {

    private EditText usernameET, passwordET;
    private MaterialButton buttonSignIn;
    private FirebaseAuth mAuth;
    TextView signupLink;
    private DatabaseReference usersRef;
    private DatabaseReference medicsRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        usersRef = FirebaseDatabase
                .getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users");
        medicsRef = FirebaseDatabase
                .getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Medics");


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

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();

                        // First check in "Users"
                        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    // User found in Users node
                                    // Save userId to SharedPreferences
                                    SharedPreferences prefs = getSharedPreferences("MedCarePrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("userId", userId);
                                    editor.apply();
                                    String role = snapshot.child("role").getValue(String.class);
                                    goToDashboard(role);
                                } else {
                                    // Not found in Users, check in Medics
                                    medicsRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                String role = snapshot.child("role").getValue(String.class);
                                                goToDashboard(role);
                                            } else {
                                                Toast.makeText(Login.this, "User not found in database.", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                            Toast.makeText(Login.this, "Failed to get medic data.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                Toast.makeText(Login.this, "Failed to get user data.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(Login.this, "Authentication failed. Try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToDashboard(String role) {
        if ("nurse".equalsIgnoreCase(role) || "Physiotherapist".equalsIgnoreCase(role)) {
            startActivity(new Intent(Login.this, MedicDashboard.class));
        } else {
            startActivity(new Intent(Login.this, Home.class));
        }
        finish();
    }

}
