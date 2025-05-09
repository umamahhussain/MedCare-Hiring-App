package com.example.medcare;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.*;
import java.util.*;

public class ChatHelper {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Get or create a chat between two users
    public void sendMessage(String currentUserId, String otherUserId, String messageText) {
        String chatId = generateChatId(currentUserId, otherUserId);

        // Step 1: Ensure chat document exists
        DocumentReference chatRef = db.collection("chats").document(chatId);
        chatRef.get().addOnSuccessListener(snapshot -> {
            if (!snapshot.exists()) {
                Map<String, Object> chatData = new HashMap<>();
                chatData.put("user1Id", currentUserId);
                chatData.put("user2Id", otherUserId);
                chatData.put("lastUpdated", FieldValue.serverTimestamp());
                chatRef.set(chatData);
            } else {
                chatRef.update("lastUpdated", FieldValue.serverTimestamp());
            }

            // Step 2: Add message to messages subcollection
            CollectionReference messagesRef = chatRef.collection("messages");
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("senderId", currentUserId);
            messageData.put("messageText", messageText);
            messageData.put("timestamp", Timestamp.now());

            messagesRef.add(messageData)
                    .addOnSuccessListener(msgDoc -> {
                        // Message sent successfully
                        Log.d("ChatHelper", "Message sent!");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("ChatHelper", "Failed to send message", e);
                    });

        }).addOnFailureListener(e -> Log.e("ChatHelper", "Chat fetch failed", e));
    }

    // Ensures consistent chat ID format (alphabetically sorted)
    private String generateChatId(String user1, String user2) {
        return user1.compareTo(user2) < 0 ? user1 + "_" + user2 : user2 + "_" + user1;
    }
}
