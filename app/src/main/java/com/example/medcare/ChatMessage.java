package com.example.medcare;

public class ChatMessage {
    private String message;
    private boolean isUserMessage; // true for user, false for bot

    public ChatMessage() {
    }

    public ChatMessage(String message, boolean isUserMessage) {
        this.message = message;
        this.isUserMessage = isUserMessage;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUserMessage() {
        return isUserMessage;
    }
}
