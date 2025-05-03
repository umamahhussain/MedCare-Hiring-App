package com.example.medcare;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BookingFragment extends Fragment {

    public BookingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        CardView nurseCard = view.findViewById(R.id.patientCard);
        CardView physioCard = view.findViewById(R.id.medicCard);

        nurseCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Booking.class);
            intent.putExtra("type", "Nurse");
            startActivity(intent);
        });

        physioCard.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Booking.class);
            intent.putExtra("type", "Physiotherapist");
            startActivity(intent);
        });

        return view;
    }
}