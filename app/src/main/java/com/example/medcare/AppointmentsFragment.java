package com.example.medcare;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;




public class AppointmentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private List<Appointment> appointmentList;
    private DatabaseReference appointmentsRef;
    private String currentUserId;



    public AppointmentsFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_appointments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1) Find RecyclerView
        recyclerView = view.findViewById(R.id.appointmentsRecyclerView);

        // 2) Set LayoutManager to horizontal
        LinearLayoutManager lm = new LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        recyclerView.setLayoutManager(lm);

        appointmentList = new ArrayList<>();
        adapter = new AppointmentAdapter(requireContext(), appointmentList);
        recyclerView.setAdapter(adapter);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        appointmentsRef = FirebaseDatabase
                .getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("appointments");

        appointmentsRef.orderByChild("userId").equalTo(currentUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        appointmentList.clear();
                        for (DataSnapshot apptSnap : snapshot.getChildren()) {
                            Appointment appt = apptSnap.getValue(Appointment.class);
                            if (appt != null && "confirmed".equalsIgnoreCase(appt.getStatus())) {
                                appointmentList.add(appt);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(requireContext(), "Failed to load appointments", Toast.LENGTH_SHORT).show();
                    }
                });



        // 4) Create & set Adapter
        adapter = new AppointmentAdapter(requireContext(), appointmentList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(appointment -> {
            showAppointmentDialog(appointment);
        });

    }

    private void showAppointmentDialog(Appointment appt) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_appointment_details, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomDialog);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        ((TextView) dialogView.findViewById(R.id.tvDoctorName)).setText("Dr. " + appt.getMedicName());
        ((TextView) dialogView.findViewById(R.id.tvDateTime)).setText("Date: " + appt.getDate() + " | Time: " + appt.getTime());
        ((TextView) dialogView.findViewById(R.id.tvLocation)).setText("Location: " + appt.getLocation());
        ((TextView) dialogView.findViewById(R.id.tvFees)).setText("Fees: $" + appt.getFees());
        ((TextView) dialogView.findViewById(R.id.tvNotes)).setText("Notes: " + (appt.getNotes().isEmpty() ? "N/A" : appt.getNotes()));

        dialog.show();
        Button contactBtn = dialogView.findViewById(R.id.btnContact);
        contactBtn.setOnClickListener(v -> {
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String medicId = appt.getMedicId(); // assuming you store medicId in the Appointment object

            Intent chatIntent = new Intent(requireContext(), user_and_medic_chat.class);
            chatIntent.putExtra("userId", currentUserId);
            chatIntent.putExtra("medicId", medicId);
            chatIntent.putExtra("isMedic", false);
            startActivity(chatIntent);

            dialog.dismiss(); // optional: close the dialog after clicking Contact
        });

    }

}
