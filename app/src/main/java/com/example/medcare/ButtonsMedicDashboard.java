package com.example.medcare;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ButtonsMedicDashboard extends Fragment {
    AppCompatButton ManageProf;
    AppCompatButton ViewBookings;

    public ButtonsMedicDashboard() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buttons_medic_dashboard, container, false);

        // Initialize and set click listener
        ManageProf = view.findViewById(R.id.manageProfileBtn);
        ManageProf.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), ManageMedicProfile.class);
            startActivity(i);
        });

        ViewBookings = view.findViewById(R.id.viewBookingsBtn);
        ViewBookings.setOnClickListener(v->{
            Intent i = new Intent(getActivity(), ViewBookingsMedic.class);
            startActivity(i);
        });

        return view;
    }
}
