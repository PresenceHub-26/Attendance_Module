package com.example.attendancemodule.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.attendancemodule.R;
import com.example.attendancemodule.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager session = new SessionManager(this);
        int themeMode = session.getThemeMode();
        if (themeMode != -1) {
            AppCompatDelegate.setDefaultNightMode(themeMode);
        }

        setContentView(R.layout.activity_splash);

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1500);
        findViewById(R.id.ivLogo).startAnimation(fadeIn);
        findViewById(R.id.tvAppName).startAnimation(fadeIn);

        new Handler().postDelayed(() -> {
            if (session.isLoggedIn()) {
                startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, 2500);
    }
}
