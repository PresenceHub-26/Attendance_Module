package com.example.attendancemodule.models;

public class Student {
    private int id;
    private String name;
    private String status; // "Present", "Absent", or null/empty

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
        this.status = ""; // Default empty
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
