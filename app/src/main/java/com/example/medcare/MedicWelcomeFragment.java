package com.example.medcare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class MedicWelcomeFragment extends Fragment {

    private TextView welcomeTV;
    private TextView profTV;
    private TextView specTV;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medic_welcome, container, false);

        // Bind UI elements
        welcomeTV = view.findViewById(R.id.medicNamewelcomeFrag);
        profTV = view.findViewById(R.id.medicProfwelcomeFrag);
        specTV = view.findViewById(R.id.medicSpecialwelcomeFrag);

        // Get current Firebase user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            // No user signed in
            welcomeTV.setText("Welcome, Guest");
            profTV.setText("Profession: -");
            specTV.setText("Specialization: -");
            return view;
        }

        // Initialize the database reference
        databaseReference = FirebaseDatabase
                .getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Medics");

        // Fetch user UID
        String uid = currentUser.getUid();

        // Fetch name, profession, and specialization
        databaseReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String profession = snapshot.child("role").getValue(String.class);
                String specialization = snapshot.child("specialization").getValue(String.class);

                welcomeTV.setText(name != null ? "Welcome, " + name + "!" : "Welcome!");
                profTV.setText("Profession: " + (profession != null ? profession : "-"));
                specTV.setText("Specialization: " + (specialization != null ? specialization : "-"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MedicWelcomeFragment", "Failed to read user data", error.toException());
                welcomeTV.setText("Welcome!");
                profTV.setText("Profession: -");
                specTV.setText("Specialization: -");
            }
        });

        return view;
    }
}
