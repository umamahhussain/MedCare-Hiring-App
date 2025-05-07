package com.example.medcare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.Locale;
import java.util.Map;

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
    private ActivityResultLauncher <Intent> imagePickerLauncher;
    private Uri selectedImageUri;

    private final String[] nurseSpecializations = {
            "Cardiac Nursing", "Critical Care Nursing", "Dermatology Nursing",
            "Emergency Nursing", "Family Nurse Practitioner", "Forensic Nursing",
            "General Nursing", "Geriatric Nursing", "Home Health Nursing",
            "Infectious Disease Nursing","Labor and Delivery Nursing",
            "Mental Health/Psychiatric Nursing", "Neonatal Nursing",
            "Nephrology Nursing", "Neurology Nursing", "Nurse Anesthetist",
            "Pediatric Nursing", "Rehabilitation Nursing", "Trauma Nursing", "Women's Health Nursing"
    };

    private final String[] physiotherapistSpecializations = {
            "Orthopedic Physiotherapy", "Neurological Physiotherapy",
            "Cardiopulmonary Physiotherapy", "Sports Physiotherapy",
            "Geriatric Physiotherapy", "Pediatric Physiotherapy"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_medic_profile);

        CloudinaryUtils.init(this);

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
        experienceEditText = findViewById(R.id.medicExperienceProfileEdit);
        feesEditText = findViewById(R.id.medicFeesProfileEdit);
        availabilitySwitch = findViewById(R.id.availabilitySwitchManageProfile);
        saveButton = findViewById(R.id.saveProfileMedicButton);
        resetButton = findViewById(R.id.resetProfileMedicButton);
        roleTextView = findViewById(R.id.roleTextView);
        ratingTextView = findViewById(R.id.ratingTextView);


        loadProfile();

        resetButton.setOnClickListener(v -> loadProfile());
        saveButton.setOnClickListener(v -> saveProfile());



        // Launch picker
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        profileImage.setImageURI(selectedImageUri);
                        uploadToCloudinary(selectedImageUri);
                    }
                }
        );

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });
    }

    private void uploadToCloudinary(Uri imageUri) {
        Toast.makeText(this, "Uploading image...", Toast.LENGTH_SHORT).show();

        MediaManager.get().upload(imageUri)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {}

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {}

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String imageUrl = (String) resultData.get("secure_url");
                        medicRef.child("pictureURL").setValue(imageUrl);
                        Toast.makeText(ManageMedicProfile.this, "Image uploaded!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(ManageMedicProfile.this, "Upload error: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {}
                }).dispatch();
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
                    experienceEditText.setText(String.valueOf(currentMedic.getExperience()));
                    feesEditText.setText(String.valueOf(currentMedic.getFees()));
                    availabilitySwitch.setChecked(currentMedic.isAvailable());
                    ratingTextView.setText(String.format(Locale.getDefault(), "%.1f", currentMedic.getRating()));
                    roleTextView.setText(currentMedic.getRole());

                    String role = currentMedic.getRole();
                    roleTextView.setText(role); // Set role in UI

                    String[] specializationOptions;

                    if ("Nurse".equalsIgnoreCase(role)) {
                        specializationOptions = nurseSpecializations;
                    } else if ("Physiotherapist".equalsIgnoreCase(role)) {
                        specializationOptions = physiotherapistSpecializations;
                    } else {
                        specializationOptions = new String[]{"General"}; // Default option
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ManageMedicProfile.this,
                            android.R.layout.simple_spinner_dropdown_item, specializationOptions);
                    specializationSpinner.setAdapter(adapter);

                    // Set spinner to match the medic's specialization
                    for (int i = 0; i < specializationOptions.length; i++) {
                        if (specializationOptions[i].equalsIgnoreCase(currentMedic.getSpecialization())) {
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