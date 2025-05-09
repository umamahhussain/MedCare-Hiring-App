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

public class ManageUserProfile extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, ageEditText, emailEditText, addressEditText;
    private TextView roleTextView;
    private Spinner genderSpinner, bloodGroupSpinner;
    private ImageView profileImage;
    private Button saveButton, resetButton;

    private DatabaseReference userRef;
    private FirebaseUser currentUser;
    private User currentUserData;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;

    private final String[] genders = {"Male", "Female", "Other"};
    private final String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user_profile);

        CloudinaryUtils.init(this);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users").child(currentUser.getUid());

        profileImage = findViewById(R.id.profileImageManageUser);
        nameEditText = findViewById(R.id.userNameEdit);
        emailEditText = findViewById(R.id.userEmailEdit);
        phoneEditText = findViewById(R.id.userPhoneEdit);
        ageEditText = findViewById(R.id.userAgeEdit);
        genderSpinner = findViewById(R.id.genderSpinner);
        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);
        addressEditText = findViewById(R.id.userAddressEdit);
        roleTextView = findViewById(R.id.userRoleTextView);
        saveButton = findViewById(R.id.saveUserProfileButton);
        resetButton = findViewById(R.id.resetUserProfileButton);

        genderSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders));
        bloodGroupSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bloodGroups));

        loadProfile();

        resetButton.setOnClickListener(v -> loadProfile());
        saveButton.setOnClickListener(v -> saveProfile());

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        profileImage.setImageURI(selectedImageUri);
                        uploadToCloudinary(selectedImageUri);
                    }
                });

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });
    }

    private void uploadToCloudinary(Uri imageUri) {
        Toast.makeText(this, "Uploading image...", Toast.LENGTH_SHORT).show();

        MediaManager.get().upload(imageUri)
                .callback(new UploadCallback() {
                    @Override public void onStart(String requestId) {}
                    @Override public void onProgress(String requestId, long bytes, long totalBytes) {}

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String imageUrl = (String) resultData.get("secure_url");
                        userRef.child("profileImageUrl").setValue(imageUrl);
                        Toast.makeText(ManageUserProfile.this, "Image uploaded!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(ManageUserProfile.this, "Upload error: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {}
                }).dispatch();
    }

    private void loadProfile() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserData = snapshot.getValue(User.class);
                if (currentUserData != null) {
                    nameEditText.setText(currentUserData.getName());
                    emailEditText.setText(currentUserData.getEmail());
                    phoneEditText.setText(currentUserData.getPhone());
                    ageEditText.setText(currentUserData.getAge() != null ? String.valueOf(currentUserData.getAge()) : "");
                    addressEditText.setText(currentUserData.getAddress());
                    roleTextView.setText(currentUserData.getRole());

                    String gender = currentUserData.getGender();
                    for (int i = 0; i < genders.length; i++) {
                        if (genders[i].equalsIgnoreCase(gender)) {
                            genderSpinner.setSelection(i);
                            break;
                        }
                    }

                    String blood = currentUserData.getBloodGroup();
                    for (int i = 0; i < bloodGroups.length; i++) {
                        if (bloodGroups[i].equalsIgnoreCase(blood)) {
                            bloodGroupSpinner.setSelection(i);
                            break;
                        }
                    }

                    if (currentUserData.getProfileImageUrl() != null && !currentUserData.getProfileImageUrl().isEmpty()) {
                        Glide.with(ManageUserProfile.this)
                                .load(currentUserData.getProfileImageUrl())
                                .placeholder(R.drawable.boy)
                                .into(profileImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageUserProfile.this, "Failed to load profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfile() {
        String newName = nameEditText.getText().toString().trim();
        String newPhone = phoneEditText.getText().toString().trim();
        String newAddress = addressEditText.getText().toString().trim();
        String newGender = genderSpinner.getSelectedItem().toString();
        String newBloodGroup = bloodGroupSpinner.getSelectedItem().toString();

        int newAge;
        try {
            newAge = Integer.parseInt(ageEditText.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid age input.", Toast.LENGTH_SHORT).show();
            return;
        }

        userRef.child("name").setValue(newName);
        userRef.child("phone").setValue(newPhone);
        userRef.child("address").setValue(newAddress);
        userRef.child("age").setValue(newAge);
        userRef.child("gender").setValue(newGender);
        userRef.child("bloodGroup").setValue(newBloodGroup)
                .addOnSuccessListener(unused -> Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show());
    }
}
