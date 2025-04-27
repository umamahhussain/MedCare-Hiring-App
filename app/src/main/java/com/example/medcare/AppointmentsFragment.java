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
        appointmentList.add(new Appointment(
                "A001", "P001", "D001",
                "10:00 AM", "2025-04-30",
                 "Dr. Sarah",
                "https://example.com/sarah_profile.jpg",
                "upcoming"
        ));
        appointmentList.add(new Appointment(
                "A002", "P002", "D002",
                "12:30 PM", "2025-05-02",
                "Dr. Ahmed",
                "https://example.com/ahmed_profile.jpg",
                "upcoming"
        ));
        appointmentList.add(new Appointment(
                "A003", "P003", "D003",
                "3:15 PM", "2025-05-04",
                 "Dr. Zoya",
                "",  // no doctor image
                "upcoming"
        ));

        // 4) Create & set Adapter
        adapter = new AppointmentAdapter(requireContext(), appointmentList);
        recyclerView.setAdapter(adapter);
    }
}
