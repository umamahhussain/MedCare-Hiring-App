package com.example.medcare;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class Booking extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MedicBookingAdapter adapter;
    private List <Medic> medicList;
    private DatabaseReference databaseReference;
    private String type; // "nurse" or "physio"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        type = getIntent().getStringExtra("type");
        databaseReference = FirebaseDatabase.getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Medics");

        recyclerView = findViewById(R.id.recyclerViewBooking);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        medicList = new ArrayList<>();
        adapter = new MedicBookingAdapter(this, medicList);
        recyclerView.setAdapter(adapter);

        fetchData();
    }

    private void fetchData() {
        databaseReference.orderByChild("role").equalTo(type).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medicList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Medic medic = data.getValue(Medic.class);
                    medicList.add(medic);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
