package com.example.medcare;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

public class ViewBookingsMedic extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ConfirmBookingAdapter adapter;
    private List<Appointment> appointmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings_medic);

        recyclerView = findViewById(R.id.appointmentsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConfirmBookingAdapter(this, appointmentList);
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
                            if (appt != null && "pending".equals(appt.getStatus())) {
                                appointmentList.add(appt);
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(ViewBookingsMedic.this, "Error fetching appointments", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
