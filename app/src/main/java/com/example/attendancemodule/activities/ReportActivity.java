package com.example.attendancemodule.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemodule.R;
import com.example.attendancemodule.adapters.ReportAdapter;
import com.example.attendancemodule.database.DatabaseHelper;
import com.example.attendancemodule.models.AttendanceRecord;
import com.example.attendancemodule.utils.PdfGenerator;
import com.example.attendancemodule.utils.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity implements ReportAdapter.OnDeleteClickListener {

    private DatabaseHelper dbHelper;
    private ReportAdapter adapter;
    private RecyclerView rvHistory;
    private TextView tvTotal, tvPresent, tvAbsent, tvRate;
    private TextInputEditText etSearch;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        dbHelper = new DatabaseHelper(this);
        session = new SessionManager(this);

        initViews();
        loadSummary();
        loadHistory();
        setupSearch();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvTotal = findViewById(R.id.tvTotalStudents);
        tvPresent = findViewById(R.id.tvPresentToday);
        tvAbsent = findViewById(R.id.tvAbsentToday);
        tvRate = findViewById(R.id.tvAttendanceRate);
        rvHistory = findViewById(R.id.rvHistory);
        etSearch = findViewById(R.id.etSearchHistory);

        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnGeneratePdf).setOnClickListener(v -> generatePdf());
    }

    private void loadSummary() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Cursor cursor = dbHelper.getAttendanceSummary(today);
        if (cursor.moveToFirst()) {
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
        }
        cursor.close();
    }

    private void loadHistory() {
        Cursor cursor = dbHelper.getAttendanceHistory(null);
        List<AttendanceRecord> records = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                records.add(new AttendanceRecord(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("attendance_date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("status"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter = new ReportAdapter(records, this);
        rvHistory.setAdapter(adapter);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onDeleteClick(AttendanceRecord record) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Student")
                .setMessage("Delete " + record.getStudentName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteStudent(record.getStudentId());
                    loadHistory();
                    loadSummary();
                    Snackbar.make(rvHistory, "Student deleted", Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void generatePdf() {
        List<AttendanceRecord> records = adapter.getAllRecords();
        if (records.isEmpty()) return;
        String summary = "Total: " + tvTotal.getText() + ", Present: " + tvPresent.getText();
        PdfGenerator.generateAttendancePdf(this, records, summary);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);
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
