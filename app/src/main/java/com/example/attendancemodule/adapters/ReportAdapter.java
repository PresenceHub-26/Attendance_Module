package com.example.attendancemodule.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemodule.R;
import com.example.attendancemodule.models.AttendanceRecord;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private List<AttendanceRecord> records;
    private List<AttendanceRecord> filteredRecords;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(AttendanceRecord record);
    }

    public ReportAdapter(List<AttendanceRecord> records, OnDeleteClickListener deleteClickListener) {
        this.records = records;
        this.filteredRecords = new ArrayList<>(records);
        this.deleteClickListener = deleteClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceRecord record = filteredRecords.get(position);
        holder.tvName.setText(record.getStudentName());
        holder.tvDate.setText(record.getDate());
        holder.tvStatus.setText(record.getStatus());

        if ("Present".equals(record.getStatus())) {
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.green));
        } else {
            holder.tvStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.red));
        }

        holder.ivDelete.setOnClickListener(v -> deleteClickListener.onDeleteClick(record));
    }

    @Override
    public int getItemCount() {
        return filteredRecords.size();
    }

    public void filter(String query) {
        filteredRecords.clear();
        if (query.isEmpty()) {
            filteredRecords.addAll(records);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (AttendanceRecord record : records) {
                if (record.getStudentName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredRecords.add(record);
                }
            }
        }
        notifyDataSetChanged();
    }

    public List<AttendanceRecord> getAllRecords() {
        return records;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDate, tvStatus;
        ImageView ivDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvReportName);
            tvDate = itemView.findViewById(R.id.tvReportDate);
            tvStatus = itemView.findViewById(R.id.tvReportStatus);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}
