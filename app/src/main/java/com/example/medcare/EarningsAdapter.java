package com.example.medcare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EarningsAdapter extends RecyclerView.Adapter<EarningsAdapter.EarningsViewHolder> {

    private final List<Earnings> earningsList;

    public EarningsAdapter(List<Earnings> earningsList) {
        this.earningsList = earningsList;
    }

    @NonNull
    @Override
    public EarningsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_earning, parent, false);
        return new EarningsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EarningsViewHolder holder, int position) {
        Earnings e = earningsList.get(position);
        holder.timeText.setText("Duration: " + formatDuration(e.getDurationMillis()));
        holder.amountText.setText("Earned: Rs." + String.format("%.2f", e.getAmountEarned()));

        Date date = new Date(e.getTimestamp());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        holder.dateText.setText(sdf.format(date));
    }

    private String formatDuration(long durationMillis) {
        long seconds = durationMillis / 1000;
        long minutes = (seconds / 60) % 60;
        long hours = seconds / 3600;
        return hours + "h " + minutes + "m";
    }

    @Override
    public int getItemCount() {
        return earningsList.size();
    }

    static class EarningsViewHolder extends RecyclerView.ViewHolder {
        TextView timeText, amountText, dateText;

        EarningsViewHolder(@NonNull View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.timeText);
            amountText = itemView.findViewById(R.id.amountText);
            dateText = itemView.findViewById(R.id.dateText);
        }
    }
}
