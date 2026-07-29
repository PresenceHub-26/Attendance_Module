package com.example.attendancemodule.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.attendancemodule.constants.AppConstants;

public class SessionManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        if (context != null) {
            pref = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
            editor = pref.edit();
        }
    }

    public void createLoginSession(String user, String role) {
        if (editor != null) {
            editor.putBoolean(AppConstants.KEY_IS_LOGGED_IN, true);
            editor.putString(AppConstants.KEY_LOGGED_IN_USER, user);
            editor.putString(AppConstants.KEY_USER_ROLE, role);
            editor.apply();
        }
    }

    public boolean isLoggedIn() {
        return pref != null && pref.getBoolean(AppConstants.KEY_IS_LOGGED_IN, false);
    }

    public String getUsername() {
        return pref != null ? pref.getString(AppConstants.KEY_LOGGED_IN_USER, "Admin") : "Admin";
    }

    public String getUserRole() {
        return pref != null ? pref.getString(AppConstants.KEY_USER_ROLE, AppConstants.ROLE_ADMIN) : AppConstants.ROLE_ADMIN;
    }

    public boolean isSuperAdmin() {
        return AppConstants.ROLE_SUPER.equals(getUserRole());
    }

    public void setThemeMode(int mode) {
        if (editor != null) {
            editor.putInt(AppConstants.KEY_NIGHT_MODE, mode);
            editor.apply();
        }
    }

    public int getThemeMode() {
        return pref != null ? pref.getInt(AppConstants.KEY_NIGHT_MODE, -1) : -1;
    }

    public void logout() {
        if (editor != null) {
            editor.clear();
            editor.apply();
        }
    }
}
