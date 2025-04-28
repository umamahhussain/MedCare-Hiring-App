package com.example.medcare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_AI = 1;

    private final List<String> messages;

    public ChatAdapter(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        String message = messages.get(position);
        if (message.startsWith("AI: ")) {
            return VIEW_TYPE_AI;
        } else {
            return VIEW_TYPE_USER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_message_item, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ai_message_item, parent, false);
            return new AIViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String message = messages.get(position);

        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).textMessageUser.setText(message);
        } else if (holder instanceof AIViewHolder) {
            ((AIViewHolder) holder).textMessageAI.setText(message.replaceFirst("AI: ", ""));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textMessageUser;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessageUser = itemView.findViewById(R.id.textMessageUser);
        }
    }

    static class AIViewHolder extends RecyclerView.ViewHolder {
        TextView textMessageAI;

        AIViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessageAI = itemView.findViewById(R.id.textMessageAI);
        }
    }
}
