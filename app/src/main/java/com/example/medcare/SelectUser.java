package com.example.medcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SelectUser extends AppCompatActivity {

    CardView patient;
    CardView medic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_user);

        init();

        // Slide In Animation
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        patient.startAnimation(slideIn);

        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out);
        medic.startAnimation(slideOut);

        patient.setOnClickListener(v -> {
            Intent intent = new Intent(this, Signup.class);
            intent.putExtra("EXTRA_IS_PATIENT", true);
            startActivity(intent);
        });

        medic.setOnClickListener(v -> {
            Intent intent = new Intent(this, Signup.class);
            intent.putExtra("EXTRA_IS_PATIENT", false);
            startActivity(intent);
        });
    }

    public void init(){
        patient = findViewById(R.id.patientCard);
        medic = findViewById(R.id.medicCard);
    }
}