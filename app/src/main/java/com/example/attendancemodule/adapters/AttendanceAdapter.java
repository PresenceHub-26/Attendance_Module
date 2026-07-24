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

    private List<Student> students;
    private List<Student> filtered;

    public AttendanceAdapter(List<Student> students) {
        this.students = students;
        this.filtered = new ArrayList<>(students);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student s = filtered.get(position);
        holder.tvName.setText(s.getFullName());
        holder.tvId.setText("ID: " + s.getStudentId());

        holder.rg.setOnCheckedChangeListener(null);
        holder.rg.clearCheck();
        
        if ("Present".equals(s.getStatus())) {
            holder.rbP.setChecked(true);
        } else if ("Absent".equals(s.getStatus())) {
            holder.rbA.setChecked(true);
        }

        holder.rg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbPresent) {
                s.setStatus("Present");
            } else if (checkedId == R.id.rbAbsent) {
                s.setStatus("Absent");
            }
        });
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }

    public void filter(String query) {
        filtered.clear();
        if (query.isEmpty()) {
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

    public List<Student> getStudents() {
        return students;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId;
        RadioGroup rg;
        RadioButton rbP, rbA;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvStudentName);
            tvId = itemView.findViewById(R.id.tvStudentId);
            rg = itemView.findViewById(R.id.rgStatus);
            rbP = itemView.findViewById(R.id.rbPresent);
            rbA = itemView.findViewById(R.id.rbAbsent);
        }
    }
}
