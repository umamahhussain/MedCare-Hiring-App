package com.example.medcare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class MainActivity extends AppCompatActivity {

    TextView title;
    TextView subTitle;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef, medicsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        init();
        mAuth = FirebaseAuth.getInstance();

        usersRef = FirebaseDatabase.getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users");
        medicsRef = FirebaseDatabase.getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Medics");

        new Handler().postDelayed(() -> checkSession(), 4000); // wait 4s before checking
    }

    public void init() {
        title = findViewById(R.id.title);
        subTitle = findViewById(R.id.subtitle);
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        title.startAnimation(fade);
        subTitle.startAnimation(fade);
    }

    private void checkSession() {
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();

            usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String role = snapshot.child("role").getValue(String.class);
                        goToDashboard(role);
                    } else {
                        medicsRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    String role = snapshot.child("role").getValue(String.class);
                                    goToDashboard(role);
                                } else {
                                    goToLogin();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                goToLogin();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    goToLogin();
                }
            });
        } else {
            goToLogin();
        }
    }

    private void goToDashboard(String role) {
        Intent intent;
        if ("nurse".equalsIgnoreCase(role) || "Physiotherapist".equalsIgnoreCase(role)) {
            intent = new Intent(this, MedicDashboard.class);
        } else {
            intent = new Intent(this, Home.class);
        }
        startActivity(intent);
        finish();
    }

    private void goToLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}
