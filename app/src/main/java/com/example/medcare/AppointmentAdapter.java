package com.example.medcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private Context context;
    private List <Appointment> appointmentList;
    private OnItemClickListener listener;


    public AppointmentAdapter(Context context, List<Appointment> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    public interface OnItemClickListener {
        void onItemClick(Appointment appointment);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.appointment_card, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentList.get(position);

        holder.doctorName.setText(appointment.getMedicName());
        holder.appointmentTime.setText(appointment.getTime());
        holder.appointmentLocation.setText(appointment.getLocation());

        // Load profile image
        if (appointment.getProfileImageUrl() != null && !appointment.getProfileImageUrl().isEmpty()) {
            // Load real profile image with rounded corners
            Glide.with(context)
                    .load(appointment.getProfileImageUrl())
                    .apply(RequestOptions.circleCropTransform()) // Circle crop!
                    .placeholder(R.drawable.boy)           // While loading
                    .error(R.drawable.boy)                 // If failed
                    .into(holder.profileImage);
        } else {
            // No profile picture, show default
            holder.profileImage.setImageResource(R.drawable.boy);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(appointment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView doctorName, appointmentTime, appointmentLocation;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            doctorName = itemView.findViewById(R.id.doctorName);
            appointmentTime = itemView.findViewById(R.id.appointmentTime);
            appointmentLocation = itemView.findViewById(R.id.appointmentLocation);
        }
    }
}
