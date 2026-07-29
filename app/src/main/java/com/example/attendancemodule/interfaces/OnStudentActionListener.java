package com.example.attendancemodule.interfaces;

import com.example.attendancemodule.models.Student;

public interface OnStudentActionListener {
    void onEdit(Student student);
    void onDelete(Student student);
}
