package com.example.medcare;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewEarnings extends AppCompatActivity {

    private RecyclerView earningsRecyclerView;
    private TextView summaryText;
    private EarningsAdapter adapter;
    private List<Earnings> earningsList = new ArrayList<>();

    private double totalAmount = 0;
    private long totalTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_earnings);

        earningsRecyclerView = findViewById(R.id.earningsRecyclerView);
        summaryText = findViewById(R.id.summaryText);
        adapter = new EarningsAdapter(earningsList);

        earningsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        earningsRecyclerView.setAdapter(adapter);

        String medicId = getSharedPreferences("MedCarePrefs", MODE_PRIVATE).getString("medicId", null);
        if (medicId != null) {
            FirebaseDatabase.getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("Earnings")
                    .child(medicId)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snap : snapshot.getChildren()) {
                                Earnings e = snap.getValue(Earnings.class);
                                if (e != null && medicId.equals(e.getMedicId())) {
                                    earningsList.add(e);
                                    totalAmount += e.getAmountEarned();
                                    totalTime += e.getDurationMillis();
                                }
                            }

                            adapter.notifyDataSetChanged();
                            summaryText.setText("Total Time: " + formatDuration(totalTime) +
                                    "\nTotal Earnings: Rs. " + String.format("%.2f", totalAmount));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ViewEarnings.this, "Error loading data", Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private String formatDuration(long durationMillis) {
        long totalSeconds = durationMillis / 1000;
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        return hours + "h " + minutes + "m";
    }
}
