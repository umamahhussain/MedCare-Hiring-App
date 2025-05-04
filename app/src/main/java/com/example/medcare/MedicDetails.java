package com.example.medcare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class MedicDetails extends AppCompatActivity {

    private TextView nameTextView, experienceTextView, specialtyTextView, feesTextView, ratingTextView, availabilityTextView, roleTextView;
    private ImageView profileImageView;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medic_details);

        // Link views
        nameTextView = findViewById(R.id.medicNameforMedicDetails);
        experienceTextView = findViewById(R.id.detailExperienceforMedicDetails);
        specialtyTextView = findViewById(R.id.detailSpecialtyforMedicDetails);
        feesTextView = findViewById(R.id.detailFeesforMedicDetails);
        ratingTextView = findViewById(R.id.detailRatingforMedicDetails);
        availabilityTextView = findViewById(R.id.detailAvailabilityforMedicDetails);
        roleTextView = findViewById(R.id.detailRoleforMedicDetails);
        profileImageView = findViewById(R.id.detailImageforMedicDetails);
        btn = findViewById(R.id.bookNowButtonforMedicDetails);


        // Get data
        String name = getIntent().getStringExtra("medicName");
        int experience = getIntent().getIntExtra("experience", 0);
        String specialty = getIntent().getStringExtra("specialty");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        double fees = getIntent().getDoubleExtra("fees", 0.0);
        double rating = getIntent().getDoubleExtra("rating", 0.0);
        boolean available = getIntent().getBooleanExtra("available", false);
        String role = getIntent().getStringExtra("role");

        // Set views
        nameTextView.setText(name);
        experienceTextView.setText(experience + " years of experience");
        specialtyTextView.setText("Specialization: " + specialty);
        feesTextView.setText("Fees: $" + fees);
        ratingTextView.setText("Rating: " + rating + " / 5");
        availabilityTextView.setText("Available: " + (available ? "Yes" : "No"));
        roleTextView.setText("Role: " + role);

        btn.setOnClickListener(v->{
            Intent intent = new Intent(this, BookMedicNow.class);
            intent.putExtra("name", name);
            intent.putExtra("role", experience);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            intent.putExtra("imageUrl", imageUrl);
            startActivity(intent);
        });

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.boy)
                .into(profileImageView);
    }
}
