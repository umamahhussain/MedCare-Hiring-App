package com.example.medcare;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MedicDashboard extends AppCompatActivity {

    private TextView timerView, earningsView;
    private Button timerButton, resetButton;

    private boolean isTimerRunning = false;
    private long startTime, elapsedTime;
    private Handler handler = new Handler();
    private Runnable timerRunnable;

    private double hourlyRate = 0.0;
    private String medicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medic_dashboard);

        timerView     = findViewById(R.id.liveClock);
        earningsView  = findViewById(R.id.earningsText);
        timerButton   = findViewById(R.id.timerButton);
        resetButton   = findViewById(R.id.resetButton);

        SharedPreferences prefs = getSharedPreferences("MedCarePrefs", MODE_PRIVATE);
        medicId = prefs.getString("medicId", null);


        if (medicId != null) {
            FirebaseDatabase
                    .getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Medics")
                    .child(medicId)
                    .child("fees")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Double fee = snapshot.getValue(Double.class);
                            if (fee != null) {
                                hourlyRate = fee;
                                Toast.makeText(MedicDashboard.this,
                                        "Fee loaded: " + hourlyRate, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MedicDashboard.this,
                                        "Fee not found. Default Rs. 0 used.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(MedicDashboard.this,
                                    "Failed to load fee", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        timerButton.setOnClickListener(v -> {
            if (!isTimerRunning) {
                if (hourlyRate == 0.0) {
                    Toast.makeText(this, "Hourly rate not loaded yet!", Toast.LENGTH_SHORT).show();
                    return;
                }
                startTime = System.currentTimeMillis() - elapsedTime;
                isTimerRunning = true;
                timerButton.setText("Pause");
                startTicking();
            } else {
                isTimerRunning = false;
                timerButton.setText("Resume");
                handler.removeCallbacks(timerRunnable);
            }
        });

        resetButton.setOnClickListener(v -> {
            if (elapsedTime > 0 && medicId != null) {
                long finalDuration = System.currentTimeMillis() - startTime;
                double finalEarnings = (finalDuration / 3600000.0) * hourlyRate;
                saveSession(medicId, finalDuration, finalEarnings);
            } else {
                Toast.makeText(this, "No session to save", Toast.LENGTH_SHORT).show();
            }
            resetTimer();
        });
    }

    private void startTicking() {
        timerRunnable = () -> {
            elapsedTime = System.currentTimeMillis() - startTime;

            int secs   = (int)(elapsedTime/1000) % 60;
            int mins   = (int)(elapsedTime/60000) % 60;
            int hours  = (int)(elapsedTime/3600000);

            timerView.setText(String.format("%02d:%02d:%02d", hours, mins, secs));

            double earnings = (elapsedTime/3600000.0) * hourlyRate;
            earningsView.setText(String.format("Rs. %.2f", earnings));

            handler.postDelayed(timerRunnable, 1000);
        };
        handler.post(timerRunnable);
    }

    private void resetTimer() {
        handler.removeCallbacks(timerRunnable);
        isTimerRunning = false;
        elapsedTime    = 0;
        timerView.setText("00:00:00");
        earningsView.setText("Rs. 0.00");
        timerButton.setText("Start");
    }

    private void saveSession(String medicId, long durationMillis, double amountEarned) {
        Earnings earnings = new Earnings(medicId, durationMillis, amountEarned, System.currentTimeMillis());

        FirebaseDatabase
                .getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Earnings")
                .child(medicId)
                .push()
                .setValue(earnings)
                .addOnSuccessListener(u ->
                        Toast.makeText(this, "Session saved", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Save failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timerRunnable);
    }
}
