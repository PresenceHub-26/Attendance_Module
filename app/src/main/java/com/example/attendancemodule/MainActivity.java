package com.example.attendancemodule;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemodule.activities.LoginActivity;
import com.example.attendancemodule.adapters.AttendanceAdapter;
import com.example.attendancemodule.database.DatabaseHelper;
import com.example.attendancemodule.models.Student;
import com.example.attendancemodule.utils.SessionManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private AttendanceAdapter adapter;
    private RecyclerView rvAttendance;
    private EditText etSearch;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);
        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        initViews();
        loadStudents();
        setupSearch();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rvAttendance = findViewById(R.id.rvAttendance);
        etSearch = findViewById(R.id.etSearch);
        Button btnSave = findViewById(R.id.btnSave);

        rvAttendance.setLayoutManager(new LinearLayoutManager(this));

        btnSave.setOnClickListener(v -> saveAttendance());
    }

    private void loadStudents() {
        List<Student> students = dbHelper.getAllStudents();
        adapter = new AttendanceAdapter(students);
        rvAttendance.setAdapter(adapter);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) {
                    adapter.filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void saveAttendance() {
        List<Student> students = adapter.getAllStudents();
        boolean allMarked = true;
        int count = 0;

        for (Student s : students) {
            if (s.getStatus() == null || s.getStatus().isEmpty()) {
                allMarked = false;
                break;
            }
        }

        if (!allMarked) {
            Toast.makeText(this, "Please mark attendance for all students", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Student s : students) {
            if (dbHelper.addAttendance(s.getId(), s.getStatus())) {
                count++;
            }
        }

        if (count == students.size()) {
            Toast.makeText(this, "Attendance saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error saving some records", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_dark_mode) {
            toggleDarkMode();
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
}
