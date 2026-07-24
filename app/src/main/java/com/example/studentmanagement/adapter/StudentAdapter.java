package com.example.studentmanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studentmanagement.EditStudentActivity;
import com.example.attendancemodule.R;
import com.example.studentmanagement.model.Student;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private Context context;
    private List<Student> studentList;

    public StudentAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = studentList.get(position);

        holder.tvName.setText(student.getName());
        holder.tvStudentId.setText("ID: " + student.getStudentId());
        holder.tvDepartment.setText(student.getDepartment());
        holder.tvLevel.setText("Level: " + student.getLevel());
        holder.tvPhone.setText("Phone: " + student.getPhone());

        // Handle click to edit
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditStudentActivity.class);
            intent.putExtra("STUDENT_ID", student.getId());
            intent.putExtra("STUDENT_CODE", student.getStudentId());
            intent.putExtra("STUDENT_NAME", student.getName());
            intent.putExtra("STUDENT_DEPT", student.getDepartment());
            intent.putExtra("STUDENT_LEVEL", student.getLevel());
            intent.putExtra("STUDENT_PHONE", student.getPhone());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    // Update the list with new data
    public void updateList(List<Student> newList) {
        this.studentList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvStudentId, tvDepartment, tvLevel, tvPhone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvStudentId = itemView.findViewById(R.id.tvStudentId);
            tvDepartment = itemView.findViewById(R.id.tvDepartment);
            tvLevel = itemView.findViewById(R.id.tvLevel);
            tvPhone = itemView.findViewById(R.id.tvPhone);
        }
    }
}