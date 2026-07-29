package com.example.attendancemodule.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemodule.R;
import com.example.attendancemodule.adapters.ReportAdapter;
import com.example.attendancemodule.database.DatabaseHelper;
import com.example.attendancemodule.models.AttendanceRecord;
import com.example.attendancemodule.utils.DateTimeUtils;
import com.example.attendancemodule.utils.PdfGenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReportFragment extends Fragment {

    private DatabaseHelper db;
    private ReportAdapter adapter;
    private RecyclerView rv;
    private TextView tvTotal, tvPresent, tvAbsent, tvRate;
    private EditText etSearch, etDate;
    private View emptyState;
    private String selectedDate = "";

    // Modern Activity Result Launchers
    private final ActivityResultLauncher<Intent> exportLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                    saveJsonToFile(result.getData().getData());
                }
            }
    );

    private final ActivityResultLauncher<Intent> importLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == android.app.Activity.RESULT_OK && result.getData() != null) {
                    readJsonFromFile(result.getData().getData());
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        db = new DatabaseHelper(getContext());
        
        tvTotal = view.findViewById(R.id.tvTotal);
        tvPresent = view.findViewById(R.id.tvPresent);
        tvAbsent = view.findViewById(R.id.tvAbsent);
        tvRate = view.findViewById(R.id.tvRate);
        rv = view.findViewById(R.id.rvHistory);
        emptyState = view.findViewById(R.id.layoutEmpty);
        etSearch = view.findViewById(R.id.etSearch);
        etDate = view.findViewById(R.id.etDate);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        
        view.findViewById(R.id.btnExport).setOnClickListener(v -> exportPdf());
        view.findViewById(R.id.btnReset).setOnClickListener(v -> resetFilters());
        
        // System Data Sharing Buttons
        view.findViewById(R.id.btnExportData).setOnClickListener(v -> triggerExportJson());
        view.findViewById(R.id.btnImportData).setOnClickListener(v -> triggerImportJson());
        view.findViewById(R.id.btnShareData).setOnClickListener(v -> shareStudentList());

        etDate.setOnClickListener(v -> showDatePicker());

        loadHistory();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadHistory();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(getContext(), (view, year, month, day) -> {
            selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day);
            etDate.setText(selectedDate);
            loadHistory();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void resetFilters() {
        etSearch.setText("");
        etDate.setText("");
        selectedDate = "";
        loadHistory();
    }

    private void loadHistory() {
        String query = etSearch.getText().toString().trim();
        List<AttendanceRecord> list = db.getHistory(query, selectedDate);
        
        emptyState.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        rv.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
        
        adapter = new ReportAdapter(list);
        rv.setAdapter(adapter);
        
        updateStats(list);
    }

    private void updateStats(List<AttendanceRecord> list) {
        int total = list.size();
        int present = 0;
        int absent = 0;
        
        for (AttendanceRecord r : list) {
            if ("Present".equals(r.getStatus())) present++;
            else if ("Absent".equals(r.getStatus())) absent++;
        }
        
        tvTotal.setText(String.valueOf(total));
        tvPresent.setText(String.valueOf(present));
        tvAbsent.setText(String.valueOf(absent));
        tvRate.setText(total > 0 ? ((present * 100) / total) + "%" : "0%");
    }

    private void exportPdf() {
        if (adapter == null) return;
        List<AttendanceRecord> list = adapter.getFilteredRecords();
        if (list.isEmpty()) {
            Toast.makeText(getContext(), "Nothing to export", Toast.LENGTH_SHORT).show();
            return;
        }
        String summary = "Total: " + tvTotal.getText() + ", Present: " + tvPresent.getText();
        PdfGenerator.generateAttendancePdf(getContext(), list, summary);
    }

    // --- SYSTEM DATA SHARING LOGIC ---

    private void triggerExportJson() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, "students_backup.json");
        exportLauncher.launch(intent);
    }

    private void triggerImportJson() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        importLauncher.launch(intent);
    }

    private void shareStudentList() {
        String json = db.exportStudentsToJson();
        if (json == null || json.isEmpty()) {
            Toast.makeText(getContext(), "No student data to share", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            File cacheDir = new File(getContext().getCacheDir(), "exports");
            if (!cacheDir.exists()) cacheDir.mkdirs();
            
            File file = new File(cacheDir, "students_list.json");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(json.getBytes(StandardCharsets.UTF_8));
            fos.close();

            Uri uri = FileProvider.getUriForFile(getContext(), 
                    getContext().getPackageName() + ".provider", file);
            
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/json");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Share Student List via..."));
            
        } catch (Exception e) {
            Toast.makeText(getContext(), "Sharing failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveJsonToFile(Uri uri) {
        try {
            String json = db.exportStudentsToJson();
            OutputStream os = getContext().getContentResolver().openOutputStream(uri);
            if (os != null) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
                os.close();
                Toast.makeText(getContext(), "Backup saved successfully", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Export failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void readJsonFromFile(Uri uri) {
        try {
            InputStream is = getContext().getContentResolver().openInputStream(uri);
            if (is != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                is.close();

                int count = db.importStudentsFromJson(sb.toString());
                if (count >= 0) {
                    Toast.makeText(getContext(), "Successfully imported " + count + " students", Toast.LENGTH_SHORT).show();
                    loadHistory();
                } else {
                    Toast.makeText(getContext(), "Invalid data format", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Import failed", Toast.LENGTH_SHORT).show();
        }
    }
}
