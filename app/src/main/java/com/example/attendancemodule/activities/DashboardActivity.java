package com.example.attendancemodule.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.attendancemodule.R;
import com.example.attendancemodule.fragments.AccountFragment;
import com.example.attendancemodule.fragments.AttendanceFragment;
import com.example.attendancemodule.fragments.DashboardFragment;
import com.example.attendancemodule.fragments.ReportFragment;
import com.example.attendancemodule.fragments.StudentListFragment;
import com.example.attendancemodule.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private SessionManager session;
    private BottomNavigationView bottomNav;

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

        initViews();
        setupNavigation();
        
        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment(), "Dashboard");
        }
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        bottomNav = findViewById(R.id.bottomNavigation);

        findViewById(R.id.fabAddMain).setOnClickListener(v -> 
                startActivity(new Intent(this, AddStudentActivity.class)));
    }

    private void setupNavigation() {
        NavigationView nav = findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(this);

        // Update header role text
        View headerView = nav.getHeaderView(0);
        if (headerView != null) {
            TextView tvRole = headerView.findViewById(R.id.tvUserRole);
            if (tvRole != null) {
                String role = session.getUserRole();
                if (role.equalsIgnoreCase("super")) {
                    tvRole.setText("Super Administrator");
                } else {
                    tvRole.setText("Staff Administrator");
                }
            }
        }

        // Color Sign Out item red
        MenuItem logoutItem = nav.getMenu().findItem(R.id.nav_logout);
        if (logoutItem != null) {
            SpannableString s = new SpannableString(logoutItem.getTitle());
            s.setSpan(new ForegroundColorSpan(Color.parseColor("#EF4444")), 0, s.length(), 0);
            logoutItem.setTitle(s);
            // Also tint the icon red
            if (logoutItem.getIcon() != null) {
                logoutItem.getIcon().setTint(Color.parseColor("#EF4444"));
            }
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, 
                findViewById(R.id.toolbar), R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            
            // Re-enable checkable state when user taps bottom nav
            int bSize = bottomNav.getMenu().size();
            for (int i = 0; i < bSize; i++) {
                bottomNav.getMenu().getItem(i).setCheckable(true);
            }

            if (id == R.id.nav_dashboard) {
                loadFragment(new DashboardFragment(), "Dashboard");
            } else if (id == R.id.nav_students) {
                loadFragment(new StudentListFragment(), "Students");
            } else if (id == R.id.nav_attendance) {
                loadFragment(new AttendanceFragment(), "Attendance");
            } else if (id == R.id.nav_reports) {
                loadFragment(new ReportFragment(), "Reports");
            }
            return true;
        });
    }

    private void loadFragment(Fragment fragment, String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    public void switchTab(int id) {
        bottomNav.setSelectedItemId(id);
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
        } else if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        // Re-enable checkable for bottom nav items just in case
        int size = bottomNav.getMenu().size();
        for (int i = 0; i < size; i++) {
            bottomNav.getMenu().getItem(i).setCheckable(true);
        }

        if (id == R.id.nav_dashboard) {
            loadFragment(new DashboardFragment(), "Dashboard");
            bottomNav.setSelectedItemId(R.id.nav_dashboard);
        } else if (id == R.id.nav_account) {
            loadFragment(new AccountFragment(), "Manage Account");
            // Uncheck bottom nav items to show we are in a side-menu page
            for (int i = 0; i < size; i++) {
                bottomNav.getMenu().getItem(i).setCheckable(false);
            }
        } else if (id == R.id.nav_logout) {
            logout();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        session.logout();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (bottomNav.getSelectedItemId() != R.id.nav_dashboard) {
            bottomNav.setSelectedItemId(R.id.nav_dashboard);
        } else {
            super.onBackPressed();
        }
    }
}
