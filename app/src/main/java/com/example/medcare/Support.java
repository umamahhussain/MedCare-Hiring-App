package com.example.medcare;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mikepenz.iconics.view.IconicsImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Support extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<String> messages;
    private AppCompatEditText editTextMessage;
    private ImageView buttonSend;

    private static final String API_KEY = "AIzaSyALapbW3VkHQT9CXTatjM_40HCHyaYcHG4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        recyclerView = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(messages);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        buttonSend.setOnClickListener(v -> {
            String userMessage = editTextMessage.getText().toString().trim();
            if (userMessage.isEmpty()) {
                Toast.makeText(this, "Please type something", Toast.LENGTH_SHORT).show();
                return;
            }

            addMessage(userMessage, true); // user message
            editTextMessage.setText("");
            fetchGeminiResponse(userMessage);
        });
    }

    private void addMessage(String text, boolean isUser) {
        if (isUser) {
            messages.add(text);
        } else {
            messages.add("AI: " + text);
        }
        chatAdapter.notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(messages.size() - 1);
    }

    private void fetchGeminiResponse(String userInput) {
        String prompt = "You are an AI medical assistant providing precise first aid instructions.\n" +
                "- Always give step-by-step emergency instructions first.\n" +
                "- Mention when professional medical attention is needed precisely.\n" +
                "- Keep responses clear, concise, and in simple language and return plain text, no bold or anything.\n" +
                "- If the question is unclear, ask for more details.\n\n" +
                "User: \"" + userInput + "\"\n" +
                "AI:";

        JsonObject part = new JsonObject();
        part.addProperty("text", prompt);

        JsonArray partsArray = new JsonArray();
        partsArray.add(part);

        JsonObject content = new JsonObject();
        content.add("parts", partsArray);

        JsonArray contentsArray = new JsonArray();
        contentsArray.add(content);

        JsonObject requestBody = new JsonObject();
        requestBody.add("contents", contentsArray);

        Call<JsonObject> call = RetrofitClient.geminiApi.getResponse(API_KEY, requestBody);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JsonObject body = response.body();
                        JsonArray candidates = body.getAsJsonArray("candidates");
                        JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
                        JsonObject content = firstCandidate.getAsJsonObject("content");
                        JsonArray parts = content.getAsJsonArray("parts");
                        String aiResponse = parts.get(0).getAsJsonObject().get("text").getAsString();

                        addMessage(aiResponse, false);
                    } catch (Exception e) {
                        Toast.makeText(Support.this, "Parsing error!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Support.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(Support.this, "Failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
