package com.example.medcare;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ViewAppointmentHistoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ViewActiveBookingsAdapter adapter;
    private List<Appointment> appointmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointment_history); // You can reuse the same layout

        recyclerView = findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ViewActiveBookingsAdapter(this, appointmentList);
        recyclerView.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences("MedCarePrefs", MODE_PRIVATE);
        String medicId = prefs.getString("medicId", null);
        if (medicId == null) {
            Toast.makeText(this, "Medic ID not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseDatabase
                .getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("appointments")
                .orderByChild("medicId").equalTo(medicId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        appointmentList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Appointment appt = snap.getValue(Appointment.class);
                            if (appt != null) {
                                appointmentList.add(appt); // No status filtering
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(ViewAppointmentHistoryActivity.this, "Error fetching appointment history", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
