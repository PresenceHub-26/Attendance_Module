package com.example.attendancemodule.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.attendancemodule.R;
import com.example.attendancemodule.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager session = new SessionManager(this);
        int savedMode = session.getNightMode();
        if (savedMode != -1) {
            AppCompatDelegate.setDefaultNightMode(savedMode);
        }

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            if (session.isLoggedIn()) {
                startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, 3000); 
    }
}
