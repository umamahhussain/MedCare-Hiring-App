package com.example.medcare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    private static final String TAG = "SignupDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        init();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase
                .getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users");

        signupButton.setOnClickListener(v -> {
            registerUser();
        });
        dobET.setOnClickListener(v -> {
            openDatePicker();
        });
    }

    public void init() {
        fullName = findViewById(R.id.nameET);
        email = findViewById(R.id.emailET);
        number = findViewById(R.id.phoneET);
        password = findViewById(R.id.passwordET);
        confirmPassword = findViewById(R.id.confirmPasswordET);
        dobET = findViewById(R.id.dobET);
        signupButton = findViewById(R.id.buttonSignUp);
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH),
                day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedMonth += 1;
                    String dateOfBirth = selectedYear + "-" + selectedMonth + "-" + selectedDay;
                    dobET.setText(dateOfBirth);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void registerUser() {
        String name        = fullName.getText().toString().trim();
        String userEmail   = email.getText().toString().trim();
        String phone       = number.getText().toString().trim();
        String pass        = password.getText().toString().trim();
        String confirmPass = confirmPassword.getText().toString().trim();
        String dobText     = dobET.getText().toString().trim();

        if (name.isEmpty() || userEmail.isEmpty() || phone.isEmpty() || pass.isEmpty() || confirmPass.isEmpty() || dobText.isEmpty()) {
            Toast.makeText(this, "Validation failed: empty fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pass.equals(confirmPass)) {
            Toast.makeText(this, "Validation failed: passwords don't match", Toast.LENGTH_SHORT).show();
            return;
        }

       mAuth.createUserWithEmailAndPassword(userEmail, pass).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                String userId = mAuth.getCurrentUser().getUid();
                User user = new User(userId, name, userEmail, phone, 0, "user");

                databaseReference.child(userId).setValue(user).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(Signup.this, "DB write success", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Signup.this, Login.class);
                        startActivity(i);
                        finish();
                    } else {
                        String msg = task1.getException() != null
                                ? task1.getException().getMessage()
                                : "unknown";
                        Toast.makeText(Signup.this, "DB error: " + msg, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                String msg = task.getException() != null
                        ? task.getException().getMessage()
                        : "unknown";
                Toast.makeText(Signup.this, "Auth error: " + msg, Toast.LENGTH_LONG).show();
                Log.e(TAG, "Auth failed", task.getException());
            }
        });
    }
}
