package com.example.attendancemodule.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemodule.R;
import com.example.attendancemodule.interfaces.OnStudentActionListener;
import com.example.attendancemodule.models.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<Student> students;
    private List<Student> filtered;
    private OnStudentActionListener listener;

    public StudentAdapter(List<Student> students, OnStudentActionListener listener) {
        this.students = students;
        this.filtered = new ArrayList<>(students);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_directory, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student s = filtered.get(position);
        holder.tvName.setText(s.getFullName());
        holder.tvId.setText("ID: " + s.getStudentId());
        holder.tvInfo.setText(s.getDepartment() + " | Level " + s.getLevel());
        holder.tvPhone.setText(s.getPhone());

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(s));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(s));
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }

    public void filter(String query) {
        filtered.clear();
        if (query == null || query.isEmpty()) {
            filtered.addAll(students);
        } else {
            String q = query.toLowerCase();
            for (Student s : students) {
                if (s.getFullName().toLowerCase().contains(q) || s.getStudentId().toLowerCase().contains(q)) {
                    filtered.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId, tvInfo, tvPhone;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStudentName);
            tvId = itemView.findViewById(R.id.tvStudentId);
            tvInfo = itemView.findViewById(R.id.tvStudentInfo);
            tvPhone = itemView.findViewById(R.id.tvStudentPhone);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
