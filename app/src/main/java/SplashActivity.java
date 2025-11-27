package com.example.testesmartstock;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper; // <--- NOVO IMPORT
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000; // 3 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Animação na logo
        ImageView logo = findViewById(R.id.logoImage);
        Animation animacao = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(animacao);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIME_OUT);
    }
}