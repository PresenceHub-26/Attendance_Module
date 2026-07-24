package com.example.attendancemodule.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.attendancemodule.R;
import com.example.attendancemodule.database.DatabaseHelper;
import com.example.attendancemodule.models.Student;
import com.example.attendancemodule.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddStudentActivity extends AppCompatActivity {

    private TextInputEditText etId, etName, etDept, etLevel, etPhone;
    private TextInputLayout tilId;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        dbHelper = new DatabaseHelper(this);
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etId = findViewById(R.id.etStudentId);
        etName = findViewById(R.id.etName);
        etDept = findViewById(R.id.etDepartment);
        etLevel = findViewById(R.id.etLevel);
        etPhone = findViewById(R.id.etPhone);
        tilId = findViewById(R.id.tilStudentId);
        MaterialButton btnSave = findViewById(R.id.btnSave);

        if (btnSave != null) {
            btnSave.setOnClickListener(v -> save());
        }
    }

    private void save() {
        String id = etId.getText() != null ? etId.getText().toString().trim() : "";
        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String dept = etDept.getText() != null ? etDept.getText().toString().trim() : "";
        String level = etLevel.getText() != null ? etLevel.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";

        if (ValidationUtils.isEmpty(id) || ValidationUtils.isEmpty(name) || ValidationUtils.isEmpty(dept)) {
            Toast.makeText(this, "Required fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.isStudentIdExists(id)) {
            tilId.setError("Student ID already exists");
            return;
        }

        Student s = new Student(id, name, dept, level, phone);
        if (dbHelper.insertStudent(s) != -1) {
            Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error adding student", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
