package com.example.attendancemodule.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.attendancemodule.models.Student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AttendanceDB";
    private static final int DATABASE_VERSION = 2;

    // Table names
    private static final String TABLE_STUDENTS = "students";
    private static final String TABLE_ATTENDANCE = "attendance";
    private static final String TABLE_USERS = "users";

    // Students table columns
    private static final String KEY_STUDENT_ID = "id";
    private static final String KEY_STUDENT_NAME = "name";

    // Attendance table columns
    private static final String KEY_ATT_ID = "id";
    private static final String KEY_ATT_STUDENT_ID = "student_id";
    private static final String KEY_ATT_DATE = "attendance_date";
    private static final String KEY_ATT_STATUS = "status";

    // Users table columns
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_USER_PASS = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STUDENTS_TABLE = "CREATE TABLE " + TABLE_STUDENTS + "("
                + KEY_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_STUDENT_NAME + " TEXT" + ")";
        
        String CREATE_ATTENDANCE_TABLE = "CREATE TABLE " + TABLE_ATTENDANCE + "("
                + KEY_ATT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ATT_STUDENT_ID + " INTEGER,"
                + KEY_ATT_DATE + " TEXT,"
                + KEY_ATT_STATUS + " TEXT" + ")";

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USER_NAME + " TEXT,"
                + KEY_USER_PASS + " TEXT" + ")";

        db.execSQL(CREATE_STUDENTS_TABLE);
        db.execSQL(CREATE_ATTENDANCE_TABLE);
        db.execSQL(CREATE_USERS_TABLE);

        // Seed default admin user
        ContentValues userValues = new ContentValues();
        userValues.put(KEY_USER_NAME, "admin");
        userValues.put(KEY_USER_PASS, "admin123");
        db.insert(TABLE_USERS, null, userValues);

        // Seed initial students
        String[] dummyStudents = {"John Mensah", "Mary Owusu", "Kwame Boadu", "Akua Ansah", "Kofi Annan", "Ama Serwaa", "Yaw Osei", "Abena Appiah"};
        for (String name : dummyStudents) {
            ContentValues values = new ContentValues();
            values.put(KEY_STUDENT_NAME, name);
            db.insert(TABLE_STUDENTS, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STUDENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Student student = new Student(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_STUDENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_STUDENT_NAME))
                );
                studentList.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return studentList;
    }

    public List<Student> searchStudents(String query) {
        List<Student> studentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS, new String[]{KEY_STUDENT_ID, KEY_STUDENT_NAME},
                KEY_STUDENT_NAME + " LIKE ?", new String[]{"%" + query + "%"},
                null, null, KEY_STUDENT_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Student student = new Student(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_STUDENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_STUDENT_NAME))
                );
                studentList.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return studentList;
    }

    public boolean addAttendance(int studentId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Check if already exists for today
        if (isAttendanceMarkedToday(studentId, date)) {
            // Update existing
            ContentValues values = new ContentValues();
            values.put(KEY_ATT_STATUS, status);
            return db.update(TABLE_ATTENDANCE, values, KEY_ATT_STUDENT_ID + " = ? AND " + KEY_ATT_DATE + " = ?",
                    new String[]{String.valueOf(studentId), date}) > 0;
        } else {
            // Insert new
            ContentValues values = new ContentValues();
            values.put(KEY_ATT_STUDENT_ID, studentId);
            values.put(KEY_ATT_DATE, date);
            values.put(KEY_ATT_STATUS, status);
            return db.insert(TABLE_ATTENDANCE, null, values) != -1;
        }
    }

    public boolean isAttendanceMarkedToday(int studentId, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ATTENDANCE, new String[]{KEY_ATT_ID},
                KEY_ATT_STUDENT_ID + " = ? AND " + KEY_ATT_DATE + " = ?",
                new String[]{String.valueOf(studentId), date}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void seedStudents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_STUDENTS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count == 0) {
            String[] dummyStudents = {"John Mensah", "Mary Owusu", "Kwame Boadu", "Akua Ansah", "Kofi Annan", "Ama Serwaa", "Yaw Osei", "Abena Appiah"};
            for (String name : dummyStudents) {
                ContentValues values = new ContentValues();
                values.put(KEY_STUDENT_NAME, name);
                db.insert(TABLE_STUDENTS, null, values);
            }
        }
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_USER_ID},
                KEY_USER_NAME + " = ? AND " + KEY_USER_PASS + " = ?",
                new String[]{username, password}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STUDENTS, KEY_STUDENT_ID + " = ?", new String[]{String.valueOf(id)});
        db.delete(TABLE_ATTENDANCE, KEY_ATT_STUDENT_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Cursor getAttendanceSummary(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +
                "(SELECT COUNT(*) FROM " + TABLE_STUDENTS + ") as total, " +
                "(SELECT COUNT(*) FROM " + TABLE_ATTENDANCE + " WHERE " + KEY_ATT_DATE + " = ? AND " + KEY_ATT_STATUS + " = 'Present') as present, " +
                "(SELECT COUNT(*) FROM " + TABLE_ATTENDANCE + " WHERE " + KEY_ATT_DATE + " = ? AND " + KEY_ATT_STATUS + " = 'Absent') as absent";
        return db.rawQuery(query, new String[]{date, date});
    }

    public Cursor getAttendanceHistory(String queryText) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT s." + KEY_STUDENT_NAME + ", a." + KEY_ATT_DATE + ", a." + KEY_ATT_STATUS + ", s." + KEY_STUDENT_ID +
                " FROM " + TABLE_STUDENTS + " s " +
                " JOIN " + TABLE_ATTENDANCE + " a ON s." + KEY_STUDENT_ID + " = a." + KEY_ATT_STUDENT_ID;

        if (queryText != null && !queryText.isEmpty()) {
            query += " WHERE s." + KEY_STUDENT_NAME + " LIKE ?";
            return db.rawQuery(query, new String[]{"%" + queryText + "%"});
        }
        return db.rawQuery(query, null);
    }
}
