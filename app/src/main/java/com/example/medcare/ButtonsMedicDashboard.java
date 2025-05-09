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
    AppCompatButton ViewActiveBookings;
    AppCompatButton ViewEarnings;
    AppCompatButton ViewHistoryButton;

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

        ViewActiveBookings = view.findViewById(R.id.viewActiveBookingsBtn);
        ViewActiveBookings.setOnClickListener(v->{
            Intent i = new Intent(getActivity(), ViewActiveBookingsMedic.class);
            startActivity(i);
        });

        ViewEarnings = view.findViewById(R.id.viewEarningsBtn);
        ViewEarnings.setOnClickListener(v->{
            Intent i = new Intent(getActivity(), ViewEarnings.class);
            startActivity(i);
        });

        ViewHistoryButton = view.findViewById(R.id.viewHistoryBtn);
        ViewHistoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ViewAppointmentHistoryActivity.class);
            startActivity(intent);
        });


        return view;
    }
}
