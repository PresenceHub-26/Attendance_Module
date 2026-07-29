package com.example.attendancemodule.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.attendancemodule.constants.AppConstants;
import com.example.attendancemodule.models.AttendanceRecord;
import com.example.attendancemodule.models.Student;
import com.example.attendancemodule.models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, AppConstants.DATABASE_NAME, null, AppConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + AppConstants.TABLE_USERS + " (" +
                AppConstants.COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AppConstants.COL_USERNAME + " TEXT UNIQUE, " +
                AppConstants.COL_PASSWORD + " TEXT, " +
                AppConstants.COL_USER_ROLE + " TEXT)");

        db.execSQL("CREATE TABLE " + AppConstants.TABLE_STUDENTS + " (" +
                AppConstants.COL_STU_PK + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AppConstants.COL_STU_ID + " TEXT UNIQUE, " +
                AppConstants.COL_STU_NAME + " TEXT, " +
                AppConstants.COL_STU_DEPT + " TEXT, " +
                AppConstants.COL_STU_LEVEL + " TEXT, " +
                AppConstants.COL_STU_PHONE + " TEXT)");

        db.execSQL("CREATE TABLE " + AppConstants.TABLE_ATTENDANCE + " (" +
                AppConstants.COL_ATT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AppConstants.COL_ATT_STU_ID + " TEXT, " +
                AppConstants.COL_ATT_DATE + " TEXT, " +
                AppConstants.COL_ATT_STATUS + " TEXT, " +
                AppConstants.COL_ATT_TIME + " TEXT)");

        ContentValues admin = new ContentValues();
        admin.put(AppConstants.COL_USERNAME, "admin");
        admin.put(AppConstants.COL_PASSWORD, "admin123");
        admin.put(AppConstants.COL_USER_ROLE, AppConstants.ROLE_SUPER);
        db.insert(AppConstants.TABLE_USERS, null, admin);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AppConstants.TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + AppConstants.TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + AppConstants.TABLE_ATTENDANCE);
        onCreate(db);
    }

    public boolean login(String u, String p) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(AppConstants.TABLE_USERS, null, AppConstants.COL_USERNAME + "=? AND " + AppConstants.COL_PASSWORD + "=?", new String[]{u, p}, null, null, null);
        boolean s = c.getCount() > 0;
        c.close();
        return s;
    }

    public String getUserRole(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(AppConstants.TABLE_USERS, new String[]{AppConstants.COL_USER_ROLE}, AppConstants.COL_USERNAME + "=?", new String[]{username}, null, null, null);
        String role = AppConstants.ROLE_ADMIN;
        if (c.moveToFirst()) {
            role = c.getString(0);
        }
        c.close();
        return role;
    }

    public boolean updatePassword(String user, String oldPass, String newPass) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(AppConstants.TABLE_USERS, null, AppConstants.COL_USERNAME + "=? AND " + AppConstants.COL_PASSWORD + "=?", new String[]{user, oldPass}, null, null, null);
        if (c.getCount() > 0) {
            c.close();
            ContentValues v = new ContentValues();
            v.put(AppConstants.COL_PASSWORD, newPass);
            int rows = db.update(AppConstants.TABLE_USERS, v, AppConstants.COL_USERNAME + "=?", new String[]{user});
            return rows > 0;
        }
        c.close();
        return false;
    }

    public long addAdmin(String u, String p) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(AppConstants.COL_USERNAME, u);
        v.put(AppConstants.COL_PASSWORD, p);
        v.put(AppConstants.COL_USER_ROLE, AppConstants.ROLE_ADMIN);
        return db.insert(AppConstants.TABLE_USERS, null, v);
    }

    public boolean deleteAdmin(String u) {
        if ("admin".equals(u)) return false; // Prevent deleting super admin
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(AppConstants.TABLE_USERS, AppConstants.COL_USERNAME + "=?", new String[]{u}) > 0;
    }

    public List<User> getAllAdmins() {
        List<User> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        // Exclude the logged-in super admin (usually 'admin')
        Cursor c = db.query(AppConstants.TABLE_USERS, null, AppConstants.COL_USER_ROLE + "=?", new String[]{AppConstants.ROLE_ADMIN}, null, null, AppConstants.COL_USERNAME + " ASC");
        if (c.moveToFirst()) {
            do {
                list.add(new User(
                        c.getString(c.getColumnIndexOrThrow(AppConstants.COL_USERNAME)),
                        c.getString(c.getColumnIndexOrThrow(AppConstants.COL_PASSWORD)),
                        c.getString(c.getColumnIndexOrThrow(AppConstants.COL_USER_ROLE))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public long insertStudent(Student s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(AppConstants.COL_STU_ID, s.getStudentId());
        v.put(AppConstants.COL_STU_NAME, s.getFullName());
        v.put(AppConstants.COL_STU_DEPT, s.getDepartment());
        v.put(AppConstants.COL_STU_LEVEL, s.getLevel());
        v.put(AppConstants.COL_STU_PHONE, s.getPhone());
        return db.insert(AppConstants.TABLE_STUDENTS, null, v);
    }

    public int updateStudent(Student s) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(AppConstants.COL_STU_NAME, s.getFullName());
        v.put(AppConstants.COL_STU_DEPT, s.getDepartment());
        v.put(AppConstants.COL_STU_LEVEL, s.getLevel());
        v.put(AppConstants.COL_STU_PHONE, s.getPhone());
        return db.update(AppConstants.TABLE_STUDENTS, v, AppConstants.COL_STU_ID + "=?", new String[]{s.getStudentId()});
    }

    public void deleteStudent(String id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(AppConstants.TABLE_STUDENTS, AppConstants.COL_STU_ID + "=?", new String[]{id});
        db.delete(AppConstants.TABLE_ATTENDANCE, AppConstants.COL_ATT_STU_ID + "=?", new String[]{id});
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + AppConstants.TABLE_STUDENTS + " ORDER BY " + AppConstants.COL_STU_NAME + " ASC", null);
        if (c.moveToFirst()) {
            do {
                list.add(new Student(
                        c.getString(c.getColumnIndexOrThrow(AppConstants.COL_STU_ID)),
                        c.getString(c.getColumnIndexOrThrow(AppConstants.COL_STU_NAME)),
                        c.getString(c.getColumnIndexOrThrow(AppConstants.COL_STU_DEPT)),
                        c.getString(c.getColumnIndexOrThrow(AppConstants.COL_STU_LEVEL)),
                        c.getString(c.getColumnIndexOrThrow(AppConstants.COL_STU_PHONE))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public boolean isStudentIdExists(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(AppConstants.TABLE_STUDENTS, null, AppConstants.COL_STU_ID + "=?", new String[]{id}, null, null, null);
        boolean ex = c.getCount() > 0;
        c.close();
        return ex;
    }

    public long markAttendance(String id, String date, String status, String time) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(AppConstants.TABLE_ATTENDANCE, AppConstants.COL_ATT_STU_ID + "=? AND " + AppConstants.COL_ATT_DATE + "=?", new String[]{id, date});
        ContentValues v = new ContentValues();
        v.put(AppConstants.COL_ATT_STU_ID, id);
        v.put(AppConstants.COL_ATT_DATE, date);
        v.put(AppConstants.COL_ATT_STATUS, status);
        v.put(AppConstants.COL_ATT_TIME, time);
        return db.insert(AppConstants.TABLE_ATTENDANCE, null, v);
    }

    public Cursor getDashboardStats(String date) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT " +
                "(SELECT COUNT(*) FROM " + AppConstants.TABLE_STUDENTS + ") as total, " +
                "(SELECT COUNT(*) FROM " + AppConstants.TABLE_ATTENDANCE + " WHERE " + AppConstants.COL_ATT_DATE + "=? AND " + AppConstants.COL_ATT_STATUS + "='Present') as present, " +
                "(SELECT COUNT(*) FROM " + AppConstants.TABLE_ATTENDANCE + " WHERE " + AppConstants.COL_ATT_DATE + "=? AND " + AppConstants.COL_ATT_STATUS + "='Absent') as absent",
                new String[]{date, date});
    }

    public List<AttendanceRecord> getHistory(String query, String dateQ) {
        List<AttendanceRecord> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder sql = new StringBuilder("SELECT s." + AppConstants.COL_STU_NAME + ", a.* FROM " + 
                AppConstants.TABLE_STUDENTS + " s JOIN " + AppConstants.TABLE_ATTENDANCE + " a ON s." + 
                AppConstants.COL_STU_ID + " = a." + AppConstants.COL_ATT_STU_ID);
        
        List<String> params = new ArrayList<>();
        boolean hasFilter = false;

        if (query != null && !query.isEmpty()) {
            sql.append(" WHERE (s.").append(AppConstants.COL_STU_NAME).append(" LIKE ? OR s.")
               .append(AppConstants.COL_STU_ID).append(" LIKE ?)");
            params.add("%" + query + "%");
            params.add("%" + query + "%");
            hasFilter = true;
        }

        if (dateQ != null && !dateQ.isEmpty()) {
            sql.append(hasFilter ? " AND " : " WHERE ").append("a.").append(AppConstants.COL_ATT_DATE).append(" = ?");
            params.add(dateQ);
        }
        
        sql.append(" ORDER BY a.").append(AppConstants.COL_ATT_DATE).append(" DESC, a.")
           .append(AppConstants.COL_ATT_TIME).append(" DESC");
           
        Cursor c = db.rawQuery(sql.toString(), params.toArray(new String[0]));
        if (c.moveToFirst()) {
            do {
                list.add(new AttendanceRecord(
                        c.getString(c.getColumnIndexOrThrow(AppConstants.COL_ATT_STU_ID)),
                        c.getString(c.getColumnIndexOrThrow(AppConstants.COL_STU_NAME)),
                        c.getString(c.getColumnIndexOrThrow(AppConstants.COL_ATT_DATE)),
                        c.getString(c.getColumnIndexOrThrow(AppConstants.COL_ATT_STATUS)),
                        c.getString(c.getColumnIndexOrThrow(AppConstants.COL_ATT_TIME))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public String exportStudentsToJson() {
        try {
            List<Student> students = getAllStudents();
            JSONArray array = new JSONArray();
            for (Student s : students) {
                JSONObject obj = new JSONObject();
                obj.put("id", s.getStudentId());
                obj.put("name", s.getFullName());
                obj.put("dept", s.getDepartment());
                obj.put("level", s.getLevel());
                obj.put("phone", s.getPhone());
                array.put(obj);
            }
            return array.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public int importStudentsFromJson(String json) {
        int count = 0;
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Student s = new Student(
                        obj.getString("id"),
                        obj.getString("name"),
                        obj.getString("dept"),
                        obj.getString("level"),
                        obj.getString("phone")
                );
                if (!isStudentIdExists(s.getStudentId())) {
                    if (insertStudent(s) != -1) count++;
                }
            }
        } catch (Exception e) {
            return -1;
        }
        return count;
    }
}
