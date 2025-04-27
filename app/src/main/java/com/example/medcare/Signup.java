package com.example.medcare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Signup extends AppCompatActivity {

    EditText fullName, email, number, password, confirmPassword, dobET;
    Button signupButton;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private boolean isPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        init();

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        isPatient = getIntent().getBooleanExtra("EXTRA_IS_PATIENT", true);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        dobET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
    }

    public void init() {
        fullName = findViewById(R.id.nameET);
        email = findViewById(R.id.emailET);
        number = findViewById(R.id.phoneET);
        password = findViewById(R.id.passwordET);
        confirmPassword = findViewById(R.id.confirmPasswordET);
        dobET = findViewById(R.id.dobET);  // This is the EditText for DOB
        signupButton = findViewById(R.id.buttonSignUp);
    }

    private void openDatePicker() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                Signup.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format the date as YYYY-MM-DD
                    selectedMonth += 1;  // Months are zero-indexed in DatePickerDialog
                    String dateOfBirth = selectedYear + "-" + selectedMonth + "-" + selectedDay;
                    dobET.setText(dateOfBirth);  // Set the selected date to the EditText
                },
                year, month, day
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void registerUser() {
        String name = fullName.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String phone = number.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();
        String dobText = dobET.getText().toString().trim();

        if (name.isEmpty() || userEmail.isEmpty() || phone.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || dobText.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirmPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Register user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(userEmail, pass).addOnCompleteListener(this, task ->
        {
            if (task.isSuccessful()) {
                // User successfully signed up
                String userId = mAuth.getCurrentUser().getUid();  // Get the user ID from FirebaseAuth

                // Store user data in Realtime Database
                User user = new User(userId, name, userEmail, phone, 0, isPatient);
                databaseReference.child(userId).setValue(user)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(Signup.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(this, Login.class);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(Signup.this, "Database error, try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(Signup.this, "Signup failed. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
