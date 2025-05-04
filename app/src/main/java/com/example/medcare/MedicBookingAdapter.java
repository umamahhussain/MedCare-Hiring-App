package com.example.medcare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.medcare.MedicDetails;
import com.example.medcare.R;
import com.example.medcare.Medic;

import java.util.List;

public class MedicBookingAdapter extends RecyclerView.Adapter<MedicBookingAdapter.MedicViewHolder> {

    private Context context;
    private List <Medic> medicList;

    public MedicBookingAdapter(Context context, List<Medic> medicList) {
        this.context = context;
        this.medicList = medicList;
    }

    @NonNull
    @Override
    public MedicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.booking_cards, parent, false);
        return new MedicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicViewHolder holder, int position) {
        Medic medic = medicList.get(position);
        holder.nameText.setText(medic.getName());
        holder.specialtyText.setText(medic.getSpecialization());
        holder.experienceText.setText(medic.getExperience() + " years");


        Glide.with(context)
                .load(medic.getPictureURL())
                .placeholder(R.drawable.boy)
                .into(holder.imageProfile);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MedicDetails.class);
            intent.putExtra("medicName", medic.getName());
            intent.putExtra("experience", medic.getExperience());
            intent.putExtra("specialty", medic.getSpecialization());
            intent.putExtra("imageUrl", medic.getPictureURL());
            intent.putExtra("email", medic.getEmail());
            intent.putExtra("phone", medic.getPhone());
            intent.putExtra("fees", medic.getFees());
            intent.putExtra("rating", medic.getRating());
            intent.putExtra("available", medic.isAvailable());
            intent.putExtra("role", medic.getRole());
            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return medicList.size();
    }

    public static class MedicViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProfile;
        TextView nameText, experienceText, specialtyText;

        public MedicViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.MedicBookingProfileImage);
            nameText = itemView.findViewById(R.id.MedicBookingName);
            experienceText = itemView.findViewById(R.id.MedicBookingExperience);
            specialtyText = itemView.findViewById(R.id.MedicBookingSpecialization);
        }
    }
}
