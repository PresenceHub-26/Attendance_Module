package com.example.studentmanagement.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.studentmanagement.model.Student;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database info
    private static final String DATABASE_NAME = "student_management.db";
    private static final int DATABASE_VERSION = 1;

    // Table and columns
    private static final String TABLE_STUDENTS = "students";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_STUDENT_ID = "student_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DEPARTMENT = "department";
    private static final String COLUMN_LEVEL = "level";
    private static final String COLUMN_PHONE = "phone";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table
        String CREATE_TABLE = "CREATE TABLE " + TABLE_STUDENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_STUDENT_ID + " TEXT UNIQUE NOT NULL,"
                + COLUMN_NAME + " TEXT NOT NULL,"
                + COLUMN_DEPARTMENT + " TEXT NOT NULL,"
                + COLUMN_LEVEL + " TEXT NOT NULL,"
                + COLUMN_PHONE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    // INSERT student
    public long insertStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_STUDENT_ID, student.getStudentId());
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_DEPARTMENT, student.getDepartment());
        values.put(COLUMN_LEVEL, student.getLevel());
        values.put(COLUMN_PHONE, student.getPhone());

        long result = db.insert(TABLE_STUDENTS, null, values);
        db.close();
        return result;
    }

    // READ all students
    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STUDENTS + " ORDER BY " + COLUMN_NAME + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Student student = new Student(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEPARTMENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
                );
                studentList.add(student);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return studentList;
    }

    // UPDATE student
    public int updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_STUDENT_ID, student.getStudentId());
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_DEPARTMENT, student.getDepartment());
        values.put(COLUMN_LEVEL, student.getLevel());
        values.put(COLUMN_PHONE, student.getPhone());

        int rowsAffected = db.update(TABLE_STUDENTS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(student.getId())});
        db.close();
        return rowsAffected;
    }

    // SEARCH students
    public List<Student> searchStudents(String query) {
        List<Student> studentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_STUDENTS + " WHERE "
                + COLUMN_NAME + " LIKE ? OR "
                + COLUMN_STUDENT_ID + " LIKE ? OR "
                + COLUMN_DEPARTMENT + " LIKE ?";

        String searchParam = "%" + query + "%";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{searchParam, searchParam, searchParam});

        if (cursor.moveToFirst()) {
            do {
                Student student = new Student(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEPARTMENT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
                );
                studentList.add(student);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return studentList;
    }

    // Check if student ID exists (for validation)
    public boolean isStudentIdExists(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS,
                new String[]{COLUMN_ID},
                COLUMN_STUDENT_ID + " = ?",
                new String[]{studentId},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Check if student ID exists excluding current student (for edit validation)
    public boolean isStudentIdExistsExcept(String studentId, int excludeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENTS,
                new String[]{COLUMN_ID},
                COLUMN_STUDENT_ID + " = ? AND " + COLUMN_ID + " != ?",
                new String[]{studentId, String.valueOf(excludeId)},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
}