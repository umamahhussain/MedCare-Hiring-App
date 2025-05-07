package com.example.medcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ConfirmBookingAdapter extends RecyclerView.Adapter<ConfirmBookingAdapter.ViewHolder> {
    private List<Appointment> appointments;
    private Context context;

    public ConfirmBookingAdapter(Context context, List<Appointment> appointments) {
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

        Appointment appointment = appointments.get(position);
        if (appointment == null) {
            Toast.makeText(holder.itemView.getContext(), "Appointment is null", Toast.LENGTH_SHORT).show();
            return;
        }

        if (appointment.getId() == null) {
            Toast.makeText(holder.itemView.getContext(), "Appointment ID is null", Toast.LENGTH_SHORT).show();
            return;
        }

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
        } else {
            holder.name.setText("No User ID");
        }


        holder.approveBtn.setOnClickListener(v -> {
            FirebaseDatabase
                    .getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("appointments")
                    .child(appointment.getId())
                    .child("status")
                    .setValue("confirmed")
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(holder.itemView.getContext(), "Appointment confirmed", Toast.LENGTH_SHORT).show();
                        holder.itemView.animate()
                                .translationX(holder.itemView.getWidth())
                                .alpha(0)
                                .setDuration(300)
                                .withEndAction(() -> {
                                    appointments.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());
                                });
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Failed to confirm", Toast.LENGTH_SHORT).show()
                    );
        });


        holder.denyBtn.setOnClickListener(v -> {
            FirebaseDatabase
                    .getInstance("https://medcare-cd8cc-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("appointments")
                    .child(appointment.getId())
                    .removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(holder.itemView.getContext(), "Appointment denied and removed", Toast.LENGTH_SHORT).show();
                        holder.itemView.animate()
                                .translationX(-holder.itemView.getWidth())
                                .alpha(0)
                                .setDuration(300)
                                .withEndAction(() -> {
                                    appointments.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());
                                });
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Failed to deny", Toast.LENGTH_SHORT).show()
                    );
        });


    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTimeText, notesText, name, locationText;
        AppCompatButton approveBtn, denyBtn;

        ViewHolder(View itemView) {
            super(itemView);
            dateTimeText = itemView.findViewById(R.id.dateTimeText);
            notesText = itemView.findViewById(R.id.notesText);
            locationText = itemView.findViewById(R.id.confirmAppointmentLocation);
            approveBtn = itemView.findViewById(R.id.approveBtn);
            denyBtn = itemView.findViewById(R.id.denyBtn);
            name = itemView.findViewById(R.id.confirmAppointmentName);
        }
    }


    @Override
    public int getItemCount() {
        return appointments.size();
    }


}
