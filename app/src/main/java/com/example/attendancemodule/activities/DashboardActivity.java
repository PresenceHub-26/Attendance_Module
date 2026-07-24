package com.example.attendancemodule.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.attendancemodule.R;
import com.example.attendancemodule.database.DatabaseHelper;
import com.example.attendancemodule.utils.DateTimeUtils;
import com.example.attendancemodule.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private DatabaseHelper db;
    private SessionManager session;
    private TextView tvTotal, tvPresent, tvAbsent, tvRate, tvWelcome;

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
        db = new DatabaseHelper(this);

        initViews();
        setupNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStats();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        tvTotal = findViewById(R.id.tvDashTotal);
        tvPresent = findViewById(R.id.tvDashPresent);
        tvAbsent = findViewById(R.id.tvDashAbsent);
        tvRate = findViewById(R.id.tvDashRate);
        tvWelcome = findViewById(R.id.tvWelcomeHeader);

        tvWelcome.setText("Welcome, " + session.getUsername());

        findViewById(R.id.btnMarkAttendance).setOnClickListener(v -> 
                startActivity(new Intent(this, AttendanceActivity.class)));

        findViewById(R.id.btnViewAnalytics).setOnClickListener(v -> 
                startActivity(new Intent(this, ReportActivity.class)));
    }

    private void setupNavigation() {
        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, 
                findViewById(R.id.toolbar), R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void updateStats() {
        String today = DateTimeUtils.getCurrentDate();
        Cursor c = db.getDashboardStats(today);
        
        if (c != null && c.moveToFirst()) {
            int tot = c.getInt(c.getColumnIndexOrThrow("total"));
            int pre = c.getInt(c.getColumnIndexOrThrow("present"));
            int abs = c.getInt(c.getColumnIndexOrThrow("absent"));

            tvTotal.setText(String.valueOf(tot));
            tvPresent.setText(String.valueOf(pre));
            tvAbsent.setText(String.valueOf(abs));

            if (tot > 0) {
                tvRate.setText(((pre * 100) / tot) + "%");
            } else {
                tvRate.setText("0%");
            }
            c.close();
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
            int mode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) 
                    ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES;
            AppCompatDelegate.setDefaultNightMode(mode);
            session.setThemeMode(mode);
            recreate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_students) {
            startActivity(new Intent(this, StudentListActivity.class));
        } else if (id == R.id.nav_attendance) {
            startActivity(new Intent(this, AttendanceActivity.class));
        } else if (id == R.id.nav_reports) {
            startActivity(new Intent(this, ReportActivity.class));
        } else if (id == R.id.nav_logout) {
            session.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
