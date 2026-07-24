package com.example.attendancemodule.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemodule.R;
import com.example.attendancemodule.models.Student;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private final List<Student> studentList;
    private final List<Student> filteredList;

    public AttendanceAdapter(List<Student> studentList) {
        this.studentList = studentList;
        this.filteredList = new ArrayList<>(studentList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = filteredList.get(position);
        holder.tvName.setText(student.getName());

        // Reset toggle state to avoid recycling issues
        holder.radioGroup.setOnCheckedChangeListener(null);
        if ("Present".equals(student.getStatus())) {
            holder.radioGroup.check(R.id.rbPresent);
        } else if ("Absent".equals(student.getStatus())) {
            holder.radioGroup.check(R.id.rbAbsent);
        } else {
            holder.radioGroup.clearCheck();
        }

        holder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbPresent) {
                student.setStatus("Present");
            } else if (checkedId == R.id.rbAbsent) {
                student.setStatus("Absent");
            } else {
                student.setStatus("");
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(studentList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Student student : studentList) {
                if (student.getName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(student);
                }
            }
        }
        notifyDataSetChanged();
    }

    public List<Student> getAllStudents() {
        return studentList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        RadioGroup radioGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStudentName);
            radioGroup = itemView.findViewById(R.id.rgStatus);
        }
    }
}
