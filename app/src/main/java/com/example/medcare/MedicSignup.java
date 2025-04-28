package com.example.medcare;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MedicSignup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medic_signup);


        AutoCompleteTextView roleDropdown = findViewById(R.id.medicRoleDropdown);

        String[] roles = {"Nurse", "Physiotherapist"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.dropdown_item, // this improved layout
                R.id.dropdownItem,           // 👈 important: connect to the TextView inside the file
                roles
        );

        roleDropdown.setAdapter(adapter);


        roleDropdown.setAdapter(adapter);




    }
}