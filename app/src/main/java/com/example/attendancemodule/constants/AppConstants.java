package com.example.attendancemodule.constants;

public class AppConstants {
    // Database
    public static final String DATABASE_NAME = "attendance.db";
    public static final int DATABASE_VERSION = 2;

    // Table Users
    public static final String TABLE_USERS = "Users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_USER_ROLE = "role";

    // Table Students
    public static final String TABLE_STUDENTS = "Students";
    public static final String COL_STU_PK = "id";
    public static final String COL_STU_ID = "student_id";
    public static final String COL_STU_NAME = "full_name";
    public static final String COL_STU_DEPT = "department";
    public static final String COL_STU_LEVEL = "level";
    public static final String COL_STU_PHONE = "phone";

    // Table Attendance
    public static final String TABLE_ATTENDANCE = "Attendance";
    public static final String COL_ATT_ID = "id";
    public static final String COL_ATT_STU_ID = "student_id";
    public static final String COL_ATT_DATE = "attendance_date";
    public static final String COL_ATT_STATUS = "status";
    public static final String COL_ATT_TIME = "time_marked";

    // Shared Preferences / Session
    public static final String PREF_NAME = "AttendanceAppPrefs";
    public static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    public static final String KEY_LOGGED_IN_USER = "loggedInUser";
    public static final String KEY_USER_ROLE = "userRole";
    public static final String KEY_NIGHT_MODE = "nightMode";
    
    // Status Values
    public static final String STATUS_PRESENT = "Present";
    public static final String STATUS_ABSENT = "Absent";

    // User Roles
    public static final String ROLE_SUPER = "super";
    public static final String ROLE_ADMIN = "admin";

    // Intent Extras
    public static final String EXTRA_STUDENT = "student_object";
}
