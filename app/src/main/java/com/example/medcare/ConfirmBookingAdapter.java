package com.example.medcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConfirmBookingAdapter extends RecyclerView.Adapter<ConfirmBookingAdapter.ViewHolder> {
    private List<Appointments> appointments;
    private Context context;

    public ConfirmBookingAdapter(Context context, List<Appointments> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.confirmappointmentcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointments appointment = appointments.get(position);
        if (appointment == null) {
            Toast.makeText(context, "Appointment is null at position " + position, Toast.LENGTH_SHORT).show();
            return;
        }

        if (holder.dateTimeText == null) {
            Toast.makeText(context, "dateTimeText is null", Toast.LENGTH_SHORT).show();
        }

        holder.dateTimeText.setText(appointment.getDate() + " at " + appointment.getTime());
        holder.notesText.setText("Notes: " + appointment.getNotes());

        holder.approveBtn.setOnClickListener(v -> {
            Toast.makeText(context, "Approved", Toast.LENGTH_SHORT).show();
            // Firebase update logic here
        });

        holder.denyBtn.setOnClickListener(v -> {
            Toast.makeText(context, "Denied", Toast.LENGTH_SHORT).show();
            // Firebase delete logic here
        });
    }


    @Override
    public int getItemCount() {
        return appointments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTimeText, notesText;
        Button approveBtn, denyBtn;

        ViewHolder(View itemView) {
            super(itemView);
            dateTimeText = itemView.findViewById(R.id.dateTimeText);
            notesText = itemView.findViewById(R.id.notesText);
            approveBtn = itemView.findViewById(R.id.approveBtn);
            denyBtn = itemView.findViewById(R.id.denyBtn);
        }
    }
}
