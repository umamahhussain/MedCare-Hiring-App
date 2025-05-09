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
    IconicsImageView navHome, navBookings,navChat,navProfile;
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

        navHome.setOnClickListener(v -> {
            startActivity(new Intent(this, Home.class));
        });

        navBookings.setOnClickListener(v -> {
            startActivity(new Intent(this, BookMedicNow.class));
        });

        navChat.setOnClickListener(v -> {
            startActivity(new Intent(this, user_and_medic_chat.class));
        });

        navProfile.setOnClickListener(v -> {
//            startActivity(new Intent(this, ProfileActivity.class));
        });

    }

    public void init(){
        Support = findViewById(R.id.btnSupport);
        navHome = findViewById(R.id.nav_home);
        navBookings = findViewById(R.id.nav_bookings);
        navChat = findViewById(R.id.nav_chat);
        navProfile = findViewById(R.id.nav_profile);
    }
}