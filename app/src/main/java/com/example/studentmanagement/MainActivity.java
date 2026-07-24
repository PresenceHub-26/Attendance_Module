package com.example.studentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemodule.R;
import com.example.studentmanagement.adapter.StudentAdapter;
import com.example.studentmanagement.database.DatabaseHelper;
import com.example.studentmanagement.model.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText etSearch;
    private FloatingActionButton fabAdd;
    private TextView tvEmpty;
    private StudentAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);
        fabAdd = findViewById(R.id.fabAdd);
        tvEmpty = findViewById(R.id.tvEmpty);

        // Initialize database
        dbHelper = new DatabaseHelper(this);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load all students
        loadStudents();

        // FAB click - Open Add Student
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);
            startActivity(intent);
        });

        // Search button
        findViewById(R.id.btnSearch).setOnClickListener(v -> {
            String query = etSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                searchStudents(query);
            } else {
                Toast.makeText(this, "Please enter search text", Toast.LENGTH_SHORT).show();
            }
        });

        // Refresh button
        findViewById(R.id.btnRefresh).setOnClickListener(v -> {
            etSearch.setText("");
            loadStudents();
        });

        // Real-time search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    loadStudents();
                } else {
                    searchStudents(query);
                }
            }
        });
    }

    private void loadStudents() {
        studentList = dbHelper.getAllStudents();
        updateUI();
    }

    private void searchStudents(String query) {
        studentList = dbHelper.searchStudents(query);
        updateUI();

        if (studentList.isEmpty()) {
            Toast.makeText(this, "No students found", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI() {
        if (studentList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);

            if (adapter == null) {
                adapter = new StudentAdapter(this, studentList);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.updateList(studentList);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
    }
}