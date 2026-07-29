package com.example.attendancemodule.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemodule.R;
import com.example.attendancemodule.activities.LoginActivity;
import com.example.attendancemodule.adapters.AdminAdapter;
import com.example.attendancemodule.database.DatabaseHelper;
import com.example.attendancemodule.models.User;
import com.example.attendancemodule.utils.SessionManager;
import com.example.attendancemodule.utils.ValidationUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AccountFragment extends Fragment implements AdminAdapter.OnAdminDeleteListener {

    private SessionManager session;
    private DatabaseHelper db;
    private TextInputEditText etOld, etNew, etConfirm, etAdminUser, etAdminPass;
    private TextView tvUser;
    private View layoutManageAdmins;
    private RecyclerView rvAdmins;
    private AdminAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        session = new SessionManager(getContext());
        db = new DatabaseHelper(getContext());

        tvUser = view.findViewById(R.id.tvCurrentUsername);
        etOld = view.findViewById(R.id.etOldPassword);
        etNew = view.findViewById(R.id.etNewPassword);
        etConfirm = view.findViewById(R.id.etConfirmPassword);
        
        layoutManageAdmins = view.findViewById(R.id.layoutManageAdmins);
        rvAdmins = view.findViewById(R.id.rvOtherAdmins);
        etAdminUser = view.findViewById(R.id.etNewAdminUser);
        etAdminPass = view.findViewById(R.id.etNewAdminPass);

        tvUser.setText("Logged in as: " + session.getUsername());

        view.findViewById(R.id.btnUpdatePassword).setOnClickListener(v -> handleUpdate());
        view.findViewById(R.id.btnAccountLogout).setOnClickListener(v -> logout());
        view.findViewById(R.id.btnAddAdmin).setOnClickListener(v -> handleAddAdmin());

        if (session.isSuperAdmin()) {
            layoutManageAdmins.setVisibility(View.VISIBLE);
            setupAdminList();
        }

        return view;
    }

    private void setupAdminList() {
        rvAdmins.setLayoutManager(new LinearLayoutManager(getContext()));
        loadAdmins();
    }

    private void loadAdmins() {
        List<User> list = db.getAllAdmins();
        adapter = new AdminAdapter(list, this);
        rvAdmins.setAdapter(adapter);
    }

    private void handleUpdate() {
        String user = session.getUsername();
        String oldP = etOld.getText() != null ? etOld.getText().toString().trim() : "";
        String newP = etNew.getText() != null ? etNew.getText().toString().trim() : "";
        String confP = etConfirm.getText() != null ? etConfirm.getText().toString().trim() : "";

        if (ValidationUtils.isEmpty(oldP) || ValidationUtils.isEmpty(newP) || ValidationUtils.isEmpty(confP)) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newP.equals(confP)) {
            Toast.makeText(getContext(), "New passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.updatePassword(user, oldP, newP)) {
            Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
            etOld.setText("");
            etNew.setText("");
            etConfirm.setText("");
        } else {
            Toast.makeText(getContext(), "Failed to update. Check current password.", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleAddAdmin() {
        String u = etAdminUser.getText() != null ? etAdminUser.getText().toString().trim() : "";
        String p = etAdminPass.getText() != null ? etAdminPass.getText().toString().trim() : "";

        if (u.isEmpty() || p.isEmpty()) {
            Toast.makeText(getContext(), "Username and Password required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.addAdmin(u, p) != -1) {
            Toast.makeText(getContext(), "Admin created", Toast.LENGTH_SHORT).show();
            etAdminUser.setText("");
            etAdminPass.setText("");
            loadAdmins();
        } else {
            Toast.makeText(getContext(), "Error creating admin. Username might exist.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDelete(User user) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Admin")
                .setMessage("Remove account for " + user.getUsername() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (db.deleteAdmin(user.getUsername())) {
                        Toast.makeText(getContext(), "Admin deleted", Toast.LENGTH_SHORT).show();
                        loadAdmins();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void logout() {
        session.logout();
        Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        if (getActivity() != null) getActivity().finish();
    }
}
