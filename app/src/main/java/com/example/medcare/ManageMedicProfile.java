package com.example.medcare;

import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.Locale;

public class ManageMedicProfile extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, experienceEditText, feesEditText, emailTextView, specialTV;
    private TextView roleTextView, ratingTextView;
    private Spinner specializationSpinner;
    private Switch availabilitySwitch;
    private ImageView profileImage;
    private Button saveButton, resetButton;

    private DatabaseReference medicRef;
    private FirebaseUser currentUser;
    private Medic currentMedic;

    private final String[] specializations = {
            "Cardiac Nursing",
            "Critical Care Nursing",
            "Dermatology Nursing",
            "Emergency Nursing",
            "Family Nurse Practitioner",
            "Forensic Nursing",
            "Gastroenterology Nursing",
            "General Nursing",
            "Geriatric Nursing",
            "Home Health Nursing",
            "Hospice and Palliative Care Nursing",
            "Infectious Disease Nursing",
            "Labor and Delivery Nursing",
            "Mental Health/Psychiatric Nursing",
            "Neonatal Nursing",
            "Nephrology Nursing",
            "Neurology Nursing",
            "Nurse Anesthetist",
            "Nurse Educator",
            "Nurse Midwife",
            "Occupational Health Nursing",
            "Oncology Nursing",
            "Orthopedic Nursing",
            "Pediatric Nursing",
            "Perioperative (Surgical) Nursing",
            "Public Health Nursing",
            "Rehabilitation Nursing",
            "School Nursing",
            "Telemetry Nursing",
            "Transplant Nursing",
            "Trauma Nursing",
            "Women's Health Nursing"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_medic_profile);

        // Initialize Firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        medicRef = FirebaseDatabase.getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Medics").child(currentUser.getUid());

        // Initialize views
        profileImage = findViewById(R.id.profileImageManageMedic);
        nameEditText = findViewById(R.id.medicNameProfileEdit);
        emailTextView = findViewById(R.id.medicEmailProfileEdit); // still EditText (disabled)
        phoneEditText = findViewById(R.id.medicPhoneProfileEdit);
        specializationSpinner = findViewById(R.id.medicSpecializationSpinner);
        specialTV = findViewById(R.id.medicSpecialProfileEdit);
        experienceEditText = findViewById(R.id.medicExperienceProfileEdit);
        feesEditText = findViewById(R.id.medicFeesProfileEdit);
        availabilitySwitch = findViewById(R.id.availabilitySwitchManageProfile);
        saveButton = findViewById(R.id.saveProfileMedicButton);
        resetButton = findViewById(R.id.resetProfileMedicButton);
        roleTextView = findViewById(R.id.roleTextView);
        ratingTextView = findViewById(R.id.ratingTextView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, specializations);
        specializationSpinner.setAdapter(adapter);

        loadProfile();

        resetButton.setOnClickListener(v -> loadProfile());
        saveButton.setOnClickListener(v -> saveProfile());
    }

    private void loadProfile() {
        medicRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentMedic = snapshot.getValue(Medic.class);
                if (currentMedic != null) {
                    nameEditText.setText(currentMedic.getName());
                    emailTextView.setText(currentMedic.getEmail());
                    phoneEditText.setText(currentMedic.getPhone());
                    specialTV.setText(currentMedic.getSpecialization());
                    experienceEditText.setText(String.valueOf(currentMedic.getExperience()));
                    feesEditText.setText(String.valueOf(currentMedic.getFees()));
                    availabilitySwitch.setChecked(currentMedic.isAvailable());
                    ratingTextView.setText(String.format(Locale.getDefault(), "%.1f", currentMedic.getRating()));
                    roleTextView.setText(currentMedic.getRole());

                    for (int i = 0; i < specializations.length; i++) {
                        if (specializations[i].equalsIgnoreCase(currentMedic.getSpecialization())) {
                            specializationSpinner.setSelection(i);
                            break;
                        }
                    }

                    if (currentMedic.getPictureURL() != null && !currentMedic.getPictureURL().isEmpty()) {
                        Glide.with(ManageMedicProfile.this)
                                .load(currentMedic.getPictureURL())
                                .placeholder(R.drawable.boy)
                                .into(profileImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageMedicProfile.this, "Failed to load profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfile() {
        String newName = nameEditText.getText().toString().trim();
        String newPhone = phoneEditText.getText().toString().trim();
        String newSpec = specializationSpinner.getSelectedItem().toString();
        boolean isAvailable = availabilitySwitch.isChecked();

        int newExp = 0;
        double newFees = 0.0;

        try {
            newExp = Integer.parseInt(experienceEditText.getText().toString().trim());
            newFees = Double.parseDouble(feesEditText.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input in experience or fees.", Toast.LENGTH_SHORT).show();
            return;
        }

        medicRef.child("name").setValue(newName);
        medicRef.child("phone").setValue(newPhone);
        medicRef.child("specialization").setValue(newSpec);
        medicRef.child("experience").setValue(newExp);
        medicRef.child("fees").setValue(newFees);
        medicRef.child("available").setValue(isAvailable)
                .addOnSuccessListener(unused -> Toast.makeText(ManageMedicProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ManageMedicProfile.this, "Failed to update.", Toast.LENGTH_SHORT).show());
    }
}