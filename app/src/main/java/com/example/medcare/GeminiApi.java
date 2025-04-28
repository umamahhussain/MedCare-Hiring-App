package com.example.medcare;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GeminiApi {
    @Headers("Content-Type: application/json")
    @POST("models/gemini-2.0-flash:generateContent")
    Call<JsonObject> getResponse(
            @Query("key") String apiKey,
            @Body JsonObject body
    );
}
