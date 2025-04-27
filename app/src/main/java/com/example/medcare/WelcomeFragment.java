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

public class WelcomeFragment extends Fragment {

    private TextView welcomeTV;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        welcomeTV = view.findViewById(R.id.welcomeTV);

        // Get current Firebase user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            // No user signed in
            welcomeTV.setText("Welcome, Guest");
            return view;
        }

        // Initialize the database reference with the custom URL
        databaseReference = FirebaseDatabase
                .getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users");

        // Fetch user data by user UID
        String uid = currentUser.getUid();

        // Reference to the specific user's data
        databaseReference.child(uid).child("name")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = snapshot.getValue(String.class);
                        if (name != null && !name.isEmpty()) {
                            welcomeTV.setText("Welcome, " + name + "!");
                        } else {
                            welcomeTV.setText("Welcome!");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("WelcomeFragment", "Failed to read user name", error.toException());
                        welcomeTV.setText("Welcome!");
                    }
                });

        return view;
    }
}
