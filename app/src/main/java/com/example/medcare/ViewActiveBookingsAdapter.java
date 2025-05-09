package com.example.medcare;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ViewActiveBookingsAdapter extends RecyclerView.Adapter<ViewActiveBookingsAdapter.ViewHolder> {
    private List<Appointment> appointments;
    private Context context;

    public ViewActiveBookingsAdapter(Context context, List<Appointment> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.active_appointment_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);

        holder.dateTimeText.setText(appointment.getDate() + " at " + appointment.getTime());
        holder.notesText.setText("Notes: " + appointment.getNotes());
        holder.locationText.setText(appointment.getLocation());
        holder.name.setText(appointment.getUserName());

        String userId = appointment.getUserId();
        if (userId != null) {
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(userId).child("name")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String userName = snapshot.getValue(String.class);
                            holder.name.setText(userName != null ? userName : "Unknown User");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            holder.name.setText("Failed to load");
                        }
                    });
        }

        holder.chatBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, user_and_medic_chat.class);
            intent.putExtra("userId", appointment.getUserId()); // this is the user
            intent.putExtra("medicId", appointment.getMedicId());      // this is the medic
            intent.putExtra("isMedic", true);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTimeText, notesText, locationText, name;
        Button chatBtn;

        ViewHolder(View itemView) {
            super(itemView);
            dateTimeText = itemView.findViewById(R.id.dateTimeText);
            notesText = itemView.findViewById(R.id.notesText);
            locationText = itemView.findViewById(R.id.confirmAppointmentLocation);
            name = itemView.findViewById(R.id.confirmAppointmentName);
            chatBtn = itemView.findViewById(R.id.chatBtn);
        }
    }
}
