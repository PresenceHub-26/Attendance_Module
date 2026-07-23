package com.example.studentmanagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.studentmanagement.database.DatabaseHelper;
import com.example.studentmanagement.model.Student;
import com.example.attendancemodule.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddStudentActivity extends AppCompatActivity {

    private TextInputLayout tilStudentId, tilName, tilDepartment, tilLevel, tilPhone;
    private TextInputEditText etStudentId, etName, etDepartment, etLevel, etPhone;
    private Button btnSave, btnCancel;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        // Initialize database
        dbHelper = new DatabaseHelper(this);

        // Initialize views
        tilStudentId = findViewById(R.id.tilStudentId);
        tilName = findViewById(R.id.tilName);
        tilDepartment = findViewById(R.id.tilDepartment);
        tilLevel = findViewById(R.id.tilLevel);
        tilPhone = findViewById(R.id.tilPhone);

        etStudentId = findViewById(R.id.etStudentId);
        etName = findViewById(R.id.etName);
        etDepartment = findViewById(R.id.etDepartment);
        etLevel = findViewById(R.id.etLevel);
        etPhone = findViewById(R.id.etPhone);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Save button
        btnSave.setOnClickListener(v -> saveStudent());

        // Cancel button
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveStudent() {
        // Clear previous errors
        clearErrors();

        // Get values
        String studentId = etStudentId.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String department = etDepartment.getText().toString().trim();
        String level = etLevel.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(studentId, name, department, level, phone)) {
            return;
        }

        // Create student object
        Student student = new Student(studentId, name, department, level, phone);

        // Insert into database
        long result = dbHelper.insertStudent(student);

        if (result != -1) {
            Toast.makeText(this, "Student added successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add student. ID may already exist.", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateInputs(String studentId, String name, String department, String level, String phone) {
        boolean isValid = true;

        // Validate Student ID
        if (TextUtils.isEmpty(studentId)) {
            tilStudentId.setError("Student ID is required");
            isValid = false;
        } else if (studentId.length() < 3) {
            tilStudentId.setError("Student ID must be at least 3 characters");
            isValid = false;
        } else if (dbHelper.isStudentIdExists(studentId)) {
            tilStudentId.setError("This Student ID already exists");
            isValid = false;
        }

        // Validate Name
        if (TextUtils.isEmpty(name)) {
            tilName.setError("Name is required");
            isValid = false;
        } else if (name.length() < 2) {
            tilName.setError("Name must be at least 2 characters");
            isValid = false;
        }

        // Validate Department
        if (TextUtils.isEmpty(department)) {
            tilDepartment.setError("Department is required");
            isValid = false;
        }

        // Validate Level
        if (TextUtils.isEmpty(level)) {
            tilLevel.setError("Level is required");
            isValid = false;
        } else {
            try {
                int levelNum = Integer.parseInt(level);
                if (levelNum < 100 || levelNum > 800) {
                    tilLevel.setError("Level must be between 100 and 800");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                tilLevel.setError("Please enter a valid number");
                isValid = false;
            }
        }

        // Validate Phone (optional but validate if entered)
        if (!TextUtils.isEmpty(phone)) {
            if (phone.length() < 10) {
                tilPhone.setError("Phone number must be at least 10 digits");
                isValid = false;
            } else if (!phone.matches("[0-9]+")) {
                tilPhone.setError("Phone number should contain only digits");
                isValid = false;
            }
        }

        return isValid;
    }

    private void clearErrors() {
        tilStudentId.setError(null);
        tilName.setError(null);
        tilDepartment.setError(null);
        tilLevel.setError(null);
        tilPhone.setError(null);
    }
}