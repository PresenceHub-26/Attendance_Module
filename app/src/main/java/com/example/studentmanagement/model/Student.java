package com.example.studentmanagement.model;

public class Student {
    private int id;
    private String studentId;
    private String name;
    private String department;
    private String level;
    private String phone;

    // Default constructor
    public Student() {}

    // Constructor for creating a new student (without ID)
    public Student(String studentId, String name, String department, String level, String phone) {
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.level = level;
        this.phone = phone;
    }

    // Constructor for reading from database (with ID)
    public Student(int id, String studentId, String name, String department, String level, String phone) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.level = level;
        this.phone = phone;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
