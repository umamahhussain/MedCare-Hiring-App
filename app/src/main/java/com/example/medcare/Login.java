package com.example.medcare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

    TextView signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        init();

        signupLink.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectUser.class);
            startActivity(intent);
        });

    }

    public void init()
    {
        signupLink = findViewById(R.id.signUpLink);
    }
}