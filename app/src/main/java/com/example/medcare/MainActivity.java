package com.example.medcare;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView title;
    TextView subTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        init();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish(); // Close the SplashActivity
        }, 1000);
    }

    public void init(){
        title = findViewById(R.id.title);
        subTitle = findViewById(R.id.subtitle);
        Animation fade = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        title.startAnimation(fade);
        subTitle.startAnimation(fade);
    }
}