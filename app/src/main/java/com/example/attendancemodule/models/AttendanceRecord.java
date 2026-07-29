package com.example.attendancemodule.models;

public class AttendanceRecord {
    private String studentId;
    private String fullName;
    private String date;
    private String status;
    private String timeMarked;

    public AttendanceRecord(String studentId, String fullName, String date, String status, String timeMarked) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.date = date;
        this.status = status;
        this.timeMarked = timeMarked;
    }

    public String getStudentId() { return studentId; }
    public String getFullName() { return fullName; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
    public String getTimeMarked() { return timeMarked; }
}
