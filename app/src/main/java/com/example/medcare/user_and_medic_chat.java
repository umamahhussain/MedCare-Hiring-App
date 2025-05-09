package com.example.medcare;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class user_and_medic_chat extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Message> messages = new ArrayList<>();
    private InhouseChatAdapter chatAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_and_medic_chat);

        recyclerView = findViewById(R.id.medicuserRV);
        EditText messageInput = findViewById(R.id.messageInput);
        ImageButton sendButton = findViewById(R.id.sendButton);

        // Get intent data
        String userId = getIntent().getStringExtra("userId");
        String medicId = getIntent().getStringExtra("medicId");
        boolean isMedic = getIntent().getBooleanExtra("isMedic", false);

        String currentUserId = isMedic ? medicId : userId;
        String otherUserId = isMedic ? userId : medicId;

        String chatId = generateChatId(userId, medicId);

        // Setup adapter
        chatAdapter = new InhouseChatAdapter(messages, currentUserId);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Listen to messages
        db.collection("chats")
                .document(chatId)
                .collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null || snapshots == null) return;

                    messages.clear();
                    for (DocumentSnapshot doc : snapshots) {
                        Message message = doc.toObject(Message.class);
                        if (message != null) messages.add(message);
                    }
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(messages.size() - 1);
                });

        // Send message
        ChatHelper chatHelper = new ChatHelper();
        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim();
            if (!messageText.isEmpty()) {
                chatHelper.sendMessage(currentUserId, otherUserId, messageText);
                messageInput.setText("");
            }
        });
    }

    private String generateChatId(String user1, String user2) {
        return user1.compareTo(user2) < 0 ? user1 + "_" + user2 : user2 + "_" + user1;
    }
}
