package com.example.attendancemodule.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.attendancemodule.MainActivity;
import com.example.attendancemodule.R;
import com.example.attendancemodule.database.DatabaseHelper;
import com.example.attendancemodule.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private DatabaseHelper dbHelper;
    private SessionManager session;
    private TextView tvTotal, tvPresent, tvAbsent, tvRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        session = new SessionManager(this);
        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_dashboard);

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatistics();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        tvTotal = findViewById(R.id.tvDashTotal);
        tvPresent = findViewById(R.id.tvDashPresent);
        tvAbsent = findViewById(R.id.tvDashAbsent);
        tvRate = findViewById(R.id.tvDashRate);

        findViewById(R.id.btnGoToAttendance).setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, MainActivity.class));
        });
    }

    private void setupNavigation() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, 
                findViewById(R.id.toolbar), R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void updateStatistics() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Cursor cursor = dbHelper.getAttendanceSummary(today);
        
        if (cursor != null && cursor.moveToFirst()) {
            int total = cursor.getInt(cursor.getColumnIndexOrThrow("total"));
            int present = cursor.getInt(cursor.getColumnIndexOrThrow("present"));
            int absent = cursor.getInt(cursor.getColumnIndexOrThrow("absent"));

            tvTotal.setText(String.valueOf(total));
            tvPresent.setText(String.valueOf(present));
            tvAbsent.setText(String.valueOf(absent));

            if (total > 0) {
                int rate = (present * 100) / total;
                tvRate.setText(rate + "%");
            } else {
                tvRate.setText("0%");
            }
            cursor.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_dark_mode) {
            toggleDarkMode();
            return true;
        } else if (item.getItemId() == R.id.action_reports) {
            startActivity(new Intent(this, ReportActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleDarkMode() {
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        int newMode;
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            newMode = AppCompatDelegate.MODE_NIGHT_NO;
        } else {
            newMode = AppCompatDelegate.MODE_NIGHT_YES;
        }
        AppCompatDelegate.setDefaultNightMode(newMode);
        session.setNightMode(newMode);
        recreate();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            // Already here
        } else if (id == R.id.nav_students) {
            startActivity(new Intent(this, StudentListActivity.class));
        } else if (id == R.id.nav_attendance) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.nav_reports) {
            startActivity(new Intent(this, ReportActivity.class));
        } else if (id == R.id.nav_logout) {
            logout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        session.logoutUser();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
