package com.example.attendancemodule.models;

import java.io.Serializable;

public class Student implements Serializable {
    private String studentId;
    private String fullName;
    private String department;
    private String level;
    private String phone;
    private String status;

    public Student(String studentId, String fullName, String department, String level, String phone) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.department = department;
        this.level = level;
        this.phone = phone;
        this.status = "";
    }

    public String getStudentId() { return studentId; }
    public String getFullName() { return fullName; }
    public String getDepartment() { return department; }
    public String getLevel() { return level; }
    public String getPhone() { return phone; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
