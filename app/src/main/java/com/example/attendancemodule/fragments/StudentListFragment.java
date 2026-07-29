package com.example.attendancemodule.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemodule.R;
import com.example.attendancemodule.activities.EditStudentActivity;
import com.example.attendancemodule.adapters.StudentAdapter;
import com.example.attendancemodule.constants.AppConstants;
import com.example.attendancemodule.database.DatabaseHelper;
import com.example.attendancemodule.interfaces.OnStudentActionListener;
import com.example.attendancemodule.models.Student;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class StudentListFragment extends Fragment implements OnStudentActionListener {

    private DatabaseHelper db;
    private StudentAdapter adapter;
    private RecyclerView rv;
    private View emptyState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_list, container, false);

        db = new DatabaseHelper(getContext());
        rv = view.findViewById(R.id.rvStudents);
        emptyState = view.findViewById(R.id.layoutEmpty);
        EditText etSearch = view.findViewById(R.id.etSearch);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        load();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null) adapter.filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void load() {
        List<Student> list = db.getAllStudents();
        emptyState.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        rv.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
        adapter = new StudentAdapter(list, this);
        rv.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    @Override
    public void onEdit(Student s) {
        Intent i = new Intent(getActivity(), EditStudentActivity.class);
        i.putExtra(AppConstants.EXTRA_STUDENT, s);
        startActivity(i);
    }

    @Override
    public void onDelete(Student s) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_student)
                .setMessage(getString(R.string.delete_confirm) + "\n" + s.getFullName())
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.deleteStudent(s.getStudentId());
                    load();
                    Snackbar.make(rv, "Deleted successfully", Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
