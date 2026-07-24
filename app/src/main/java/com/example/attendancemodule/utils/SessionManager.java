package com.example.attendancemodule.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.attendancemodule.constants.AppConstants;

public class SessionManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(String user) {
        editor.putBoolean(AppConstants.KEY_IS_LOGGED_IN, true);
        editor.putString(AppConstants.KEY_LOGGED_IN_USER, user);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(AppConstants.KEY_IS_LOGGED_IN, false);
    }

    public String getUsername() {
        return pref.getString(AppConstants.KEY_LOGGED_IN_USER, "Admin");
    }

    public void setThemeMode(int mode) {
        editor.putInt(AppConstants.KEY_NIGHT_MODE, mode);
        editor.apply();
    }

    public int getThemeMode() {
        return pref.getInt(AppConstants.KEY_NIGHT_MODE, -1);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
