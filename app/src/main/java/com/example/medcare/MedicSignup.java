package com.example.medcare;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MedicSignup extends AppCompatActivity {
    private static final String TAG = "MedicSignup";

    private TextInputEditText medicNameET,
            medicEmailET,
            medicPhoneET,
            medicDobET,
            medicPasswordET,
            medicConfirmPasswordET;
    private AutoCompleteTextView medicRoleDropdown;
    private MaterialButton medicButtonSignUp;

    private FirebaseAuth mAuth;
    private DatabaseReference medicsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medic_signup);

        // 1) Hook up all views
        medicNameET             = findViewById(R.id.medicNameET);
        medicEmailET            = findViewById(R.id.medicEmailET);
        medicPhoneET            = findViewById(R.id.medicPhoneET);
        medicDobET              = findViewById(R.id.medicDobET);
        medicPasswordET         = findViewById(R.id.medicPasswordET);
        medicConfirmPasswordET  = findViewById(R.id.medicConfirmPasswordET);
        medicRoleDropdown       = findViewById(R.id.medicRoleDropdown);
        medicButtonSignUp       = findViewById(R.id.medicButtonSignUp);

        // 2) Date picker for DOB field
        medicDobET.setOnClickListener(v -> showDatePicker());

        // 3) Populate role dropdown
        String[] roles = {"Nurse", "Physiotherapist"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.dropdown_item,
                R.id.dropdownItem,
                roles
        );
        medicRoleDropdown.setAdapter(adapter);

        // 4) Firebase init
        mAuth = FirebaseAuth.getInstance();
        medicsRef = FirebaseDatabase
                .getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Medics");

        // 5) Sign-up button listener
        medicButtonSignUp.setOnClickListener(v -> registerMedic());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    month++; // zero-based
                    medicDobET.setText(year + "-" + month + "-" + dayOfMonth);
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void registerMedic() {
        String name       = medicNameET.getText().toString().trim();
        String email      = medicEmailET.getText().toString().trim();
        String phone      = medicPhoneET.getText().toString().trim();
        String dob        = medicDobET.getText().toString().trim();
        String role       = medicRoleDropdown.getText().toString().trim();
        String pass       = medicPasswordET.getText().toString();
        String confirm    = medicConfirmPasswordET.getText().toString();

        // 6) Simple validation
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || dob.isEmpty() || role.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pass.equals(confirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // 7) Create user with FirebaseAuth
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, "Signup failed: " +
                                task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "Auth error", task.getException());
                        return;
                    }
                    // 8) On success, push Medic object to Realtime DB
                    String uid = mAuth.getCurrentUser().getUid();
                    Medic medic = new Medic(
                            uid, name, email, phone, role, 0, 0.0, 0.0,true, "", ""
                    );


                    medicsRef.child(uid)
                            .setValue(medic)
                            .addOnCompleteListener(dbTask -> {
                                if (dbTask.isSuccessful()) {
                                    Toast.makeText(this,
                                            "Signup successful!", Toast.LENGTH_SHORT).show();
                                    // navigate to login or home
                                    startActivity(new Intent(this, Login.class));
                                    finish();
                                } else {
                                    Toast.makeText(this,
                                            "Database error: " +
                                                    dbTask.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                    Log.e(TAG, "DB error", dbTask.getException());
                                }
                            });
                });
    }
}
