package com.example.medcare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mikepenz.iconics.view.IconicsImageView;

public class Home extends AppCompatActivity {

    Button Support;
    Button Profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        init();
        Support.setOnClickListener(v -> {
            Intent i = new Intent(this, Support.class);
            startActivity(i);
        });
        Profile.setOnClickListener(v -> {
            Intent i = new Intent(this, ManageUserProfile.class);
            startActivity(i);
        });


    }

    public void init(){

        Support = findViewById(R.id.btnSupport);
        Profile = findViewById(R.id.btnUserProfile);
    }
}