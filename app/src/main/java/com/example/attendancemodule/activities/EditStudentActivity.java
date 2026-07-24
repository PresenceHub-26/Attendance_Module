package com.example.attendancemodule.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.attendancemodule.R;
import com.example.attendancemodule.constants.AppConstants;
import com.example.attendancemodule.database.DatabaseHelper;
import com.example.attendancemodule.models.Student;
import com.example.attendancemodule.utils.ValidationUtils;
import com.google.android.material.textfield.TextInputEditText;

public class EditStudentActivity extends AppCompatActivity {

    private TextInputEditText etName, etDept, etLevel, etPhone;
    private Student student;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        dbHelper = new DatabaseHelper(this);
        student = (Student) getIntent().getSerializableExtra(AppConstants.EXTRA_STUDENT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView tvId = findViewById(R.id.tvStudentIdStatic);
        etName = findViewById(R.id.etName);
        etDept = findViewById(R.id.etDepartment);
        etLevel = findViewById(R.id.etLevel);
        etPhone = findViewById(R.id.etPhone);

        if (student != null) {
            tvId.setText("Editing ID: " + student.getStudentId());
            etName.setText(student.getFullName());
            etDept.setText(student.getDepartment());
            etLevel.setText(student.getLevel());
            etPhone.setText(student.getPhone());
        }

        findViewById(R.id.btnUpdate).setOnClickListener(v -> update());
    }

    private void update() {
        String name = etName.getText().toString().trim();
        String dept = etDept.getText().toString().trim();
        String level = etLevel.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (ValidationUtils.isEmpty(name) || ValidationUtils.isEmpty(dept)) {
            Toast.makeText(this, "Required fields are empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Student updated = new Student(student.getStudentId(), name, dept, level, phone);
        if (dbHelper.updateStudent(updated) > 0) {
            Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
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
