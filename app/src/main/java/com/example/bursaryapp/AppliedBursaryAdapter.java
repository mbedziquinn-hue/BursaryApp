package com.example.bursaryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppliedBursaryAdapter extends RecyclerView.Adapter<AppliedBursaryAdapter.ViewHolder> {

    private List<AppliedBursary> appliedList;

    public AppliedBursaryAdapter(List<AppliedBursary> appliedList) {
        this.appliedList = appliedList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applied_bursary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppliedBursary bursary = appliedList.get(position);
        holder.tvName.setText(bursary.getName());
        holder.tvStatus.setText(bursary.getStatus());
        holder.tvDate.setText("Applied: " + bursary.getAppliedDate());
    }

    @Override
    public int getItemCount() {
        return appliedList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvStatus, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // These now match the updated XML exactly
            tvName = itemView.findViewById(R.id.tvAppliedName);
            tvStatus = itemView.findViewById(R.id.tvAppliedStatus);
            tvDate = itemView.findViewById(R.id.tvAppliedDate);
        }
    }
}