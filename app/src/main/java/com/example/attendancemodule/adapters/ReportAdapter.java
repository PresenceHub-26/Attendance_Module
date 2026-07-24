package com.example.attendancemodule.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemodule.R;
import com.example.attendancemodule.models.AttendanceRecord;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    private final List<AttendanceRecord> filtered;

    public ReportAdapter(List<AttendanceRecord> records) {
        this.filtered = records;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceRecord r = filtered.get(position);
        holder.tvName.setText(r.getFullName());
        holder.tvId.setText("ID: " + r.getStudentId());
        holder.tvDate.setText(r.getDate() + " | " + r.getTimeMarked());
        holder.tvStatus.setText(r.getStatus());

        int color = "Present".equals(r.getStatus()) ? 
                holder.itemView.getContext().getResources().getColor(R.color.success) :
                holder.itemView.getContext().getResources().getColor(R.color.error);
        holder.tvStatus.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }

    public List<AttendanceRecord> getFilteredRecords() {
        return filtered;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvId, tvDate, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvReportName);
            tvId = itemView.findViewById(R.id.tvReportId);
            tvDate = itemView.findViewById(R.id.tvReportDate);
            tvStatus = itemView.findViewById(R.id.tvReportStatus);
        }
    }
}
