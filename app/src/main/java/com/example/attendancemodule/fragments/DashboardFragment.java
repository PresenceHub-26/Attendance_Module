package com.example.attendancemodule.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.attendancemodule.R;
import com.example.attendancemodule.activities.DashboardActivity;
import com.example.attendancemodule.database.DatabaseHelper;
import com.example.attendancemodule.utils.DateTimeUtils;
import com.example.attendancemodule.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

public class DashboardFragment extends Fragment {

    private DatabaseHelper db;
    private SessionManager session;
    private TextView tvTotal, tvPresent, tvAbsent, tvRate, tvWelcome;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        db = new DatabaseHelper(getContext());
        session = new SessionManager(getContext());

        tvTotal = view.findViewById(R.id.tvDashTotal);
        tvPresent = view.findViewById(R.id.tvDashPresent);
        tvAbsent = view.findViewById(R.id.tvDashAbsent);
        tvRate = view.findViewById(R.id.tvDashRate);
        tvWelcome = view.findViewById(R.id.tvWelcomeHeader);

        tvWelcome.setText("Welcome, " + session.getUsername());

        MaterialButton btnAttendance = view.findViewById(R.id.btnMarkAttendance);
        MaterialButton btnAnalytics = view.findViewById(R.id.btnViewAnalytics);

        btnAttendance.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardActivity) {
                ((DashboardActivity) getActivity()).switchTab(R.id.nav_attendance);
            }
        });

        btnAnalytics.setOnClickListener(v -> {
            if (getActivity() instanceof DashboardActivity) {
                ((DashboardActivity) getActivity()).switchTab(R.id.nav_reports);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateStats();
    }

    private void updateStats() {
        String today = DateTimeUtils.getCurrentDate();
        Cursor c = db.getDashboardStats(today);

        if (c != null && c.moveToFirst()) {
            int tot = c.getInt(c.getColumnIndexOrThrow("total"));
            int pre = c.getInt(c.getColumnIndexOrThrow("present"));
            int abs = c.getInt(c.getColumnIndexOrThrow("absent"));

            tvTotal.setText(String.valueOf(tot));
            tvPresent.setText(String.valueOf(pre));
            tvAbsent.setText(String.valueOf(abs));

            if (tot > 0) {
                tvRate.setText(((pre * 100) / tot) + "%");
            } else {
                tvRate.setText("0%");
            }
            c.close();
        }
    }
}
