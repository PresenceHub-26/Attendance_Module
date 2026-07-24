package com.example.attendancemodule.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "AttendanceSession";
    private static final String IS_LOGGED_IN = "IsLoggedIn";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NIGHT_MODE = "NightMode";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setNightMode(int mode) {
        editor.putInt(KEY_NIGHT_MODE, mode);
        editor.commit();
    }

    public int getNightMode() {
        return pref.getInt(KEY_NIGHT_MODE, -1); // Default to -1 (system/not set)
    }

    public void createLoginSession(String username) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }
}
