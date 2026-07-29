package com.example.attendancemodule.utils;

import android.text.TextUtils;

public class ValidationUtils {

    public static boolean isEmpty(String text) {
        return TextUtils.isEmpty(text) || text.trim().isEmpty();
    }

    public static boolean isValidPhone(String phone) {
        return !isEmpty(phone) && phone.length() >= 10 && phone.matches("[0-9]+");
    }
}
