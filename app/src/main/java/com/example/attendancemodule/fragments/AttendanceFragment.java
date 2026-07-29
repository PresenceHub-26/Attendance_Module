package com.example.attendancemodule.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemodule.R;
import com.example.attendancemodule.adapters.AttendanceAdapter;
import com.example.attendancemodule.database.DatabaseHelper;
import com.example.attendancemodule.models.Student;
import com.example.attendancemodule.utils.DateTimeUtils;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AttendanceFragment extends Fragment {

    private DatabaseHelper db;
    private AttendanceAdapter adapter;
    private RecyclerView rv;
    private ChipGroup chipGroup;
    private String currentDeptFilter = "";
    private String currentLevelFilter = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        db = new DatabaseHelper(getContext());
        rv = view.findViewById(R.id.rvAttendance);
        EditText etSearch = view.findViewById(R.id.etSearch);
        chipGroup = view.findViewById(R.id.chipGroupFilters);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        
        view.findViewById(R.id.btnSave).setOnClickListener(v -> save());

        setupFilterLogic();
        load();

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

        return view;
    }

    private void setupFilterLogic() {
        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty() || checkedIds.contains(R.id.chipAll)) {
                currentDeptFilter = "";
                currentLevelFilter = "";
                load();
            } else if (checkedIds.contains(R.id.chipDept)) {
                showFilterDialog("Select Department", getUniqueDepts(), true);
            } else if (checkedIds.contains(R.id.chipLevel)) {
                showFilterDialog("Select Level", getUniqueLevels(), false);
            }
        });
    }

    private void showFilterDialog(String title, String[] items, boolean isDept) {
        if (items.length == 0) {
            Toast.makeText(getContext(), "No categories found", Toast.LENGTH_SHORT).show();
            return;
        }
        new androidx.appcompat.app.AlertDialog.Builder(getContext())
                .setTitle(title)
                .setItems(items, (dialog, which) -> {
                    if (isDept) {
                        currentDeptFilter = items[which];
                        currentLevelFilter = "";
                    } else {
                        currentLevelFilter = items[which];
                        currentDeptFilter = "";
                    }
                    load();
                })
                .show();
    }

    private String[] getUniqueDepts() {
        List<Student> all = db.getAllStudents();
        Set<String> set = new HashSet<>();
        for (Student s : all) set.add(s.getDepartment());
        return set.toArray(new String[0]);
    }

    private String[] getUniqueLevels() {
        List<Student> all = db.getAllStudents();
        Set<String> set = new HashSet<>();
        for (Student s : all) set.add(s.getLevel());
        return set.toArray(new String[0]);
    }

    private void load() {
        List<Student> all = db.getAllStudents();
        List<Student> filtered = new ArrayList<>();
        
        for (Student s : all) {
            boolean matchesDept = currentDeptFilter.isEmpty() || s.getDepartment().equals(currentDeptFilter);
            boolean matchesLevel = currentLevelFilter.isEmpty() || s.getLevel().equals(currentLevelFilter);
            
            if (matchesDept && matchesLevel) {
                filtered.add(s);
            }
        }
        
        adapter = new AttendanceAdapter(filtered);
        rv.setAdapter(adapter);
    }

    private void save() {
        if (adapter == null) return;
        List<Student> list = adapter.getStudents();
        String date = DateTimeUtils.getCurrentDate();
        String time = DateTimeUtils.getCurrentTime();

        int count = 0;
        for (Student s : list) {
            if (s.getStatus() != null && !s.getStatus().isEmpty()) {
                db.markAttendance(s.getStudentId(), date, s.getStatus(), time);
                count++;
            }
        }

        if (count > 0) {
            Toast.makeText(getContext(), "Saved " + count + " records", Toast.LENGTH_SHORT).show();
            // Clear or reload if desired
            load();
        } else {
            Toast.makeText(getContext(), "Please mark at least one student", Toast.LENGTH_SHORT).show();
        }
    }
}
