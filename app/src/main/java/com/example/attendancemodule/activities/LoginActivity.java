package com.example.attendancemodule.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attendancemodule.R;
import com.example.attendancemodule.database.DatabaseHelper;
import com.example.attendancemodule.utils.SessionManager;
import com.example.attendancemodule.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUser, etPass;
    private DatabaseHelper db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        session = new SessionManager(this);

        etUser = findViewById(R.id.etUsername);
        etPass = findViewById(R.id.etPassword);
        MaterialButton btn = findViewById(R.id.btnLogin);

        btn.setOnClickListener(v -> {
            String u = etUser.getText().toString().trim();
            String p = etPass.getText().toString().trim();

            if (ValidationUtils.isEmpty(u) || ValidationUtils.isEmpty(p)) {
                Toast.makeText(this, "Empty fields", Toast.LENGTH_SHORT).show();
            } else if (db.login(u, p)) {
                session.createLoginSession(u);
                Toast.makeText(this, "Welcome " + u, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, DashboardActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Auth Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
