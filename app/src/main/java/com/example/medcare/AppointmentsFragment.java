package com.example.medcare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private List<Appointment> appointmentList;

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

        // 3) Prepare dummy data
        appointmentList = new ArrayList<>();

        Appointment appt1 = new Appointment();
        appt1.setId("A001");
        appt1.setUserId("P001");
        appt1.setMedicId("D001");
        appt1.setTime("10:00 AM");
        appt1.setDate("2025-04-30");
        appt1.setUserName("Dr. Sarah");
        appt1.setProfileImageUrl("https://example.com/sarah_profile.jpg");
        appt1.setStatus("upcoming");

        Appointment appt2 = new Appointment();
        appt2.setId("A002");
        appt2.setUserId("P002");
        appt2.setMedicId("D002");
        appt2.setTime("12:30 PM");
        appt2.setDate("2025-05-02");
        appt2.setUserName("Dr. Ahmed");
        appt2.setProfileImageUrl("https://example.com/ahmed_profile.jpg");
        appt2.setStatus("upcoming");

        Appointment appt3 = new Appointment();
        appt3.setId("A003");
        appt3.setUserId("P003");
        appt3.setMedicId("D003");
        appt3.setTime("3:15 PM");
        appt3.setDate("2025-05-04");
        appt3.setUserName("Dr. Zoya");
        appt3.setProfileImageUrl(""); // No image
        appt3.setStatus("upcoming");

        appointmentList.add(appt1);
        appointmentList.add(appt2);
        appointmentList.add(appt3);


        // 4) Create & set Adapter
        adapter = new AppointmentAdapter(requireContext(), appointmentList);
        recyclerView.setAdapter(adapter);
    }
}
