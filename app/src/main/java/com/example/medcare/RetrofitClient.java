package com.example.medcare;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/v1beta/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static final GeminiApi geminiApi = retrofit.create(GeminiApi.class);
}
