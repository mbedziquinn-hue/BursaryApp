package com.example.bursaryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertViewHolder> {

    private List<BursaryData> list;

    public AlertAdapter(List<BursaryData> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert, parent, false);
        return new AlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        BursaryData data = list.get(position);
        // FIX: Changed getBursaryName() to getName()
        holder.name.setText(data.getName());
        holder.deadline.setText("Deadline: " + data.getDeadline());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class AlertViewHolder extends RecyclerView.ViewHolder {
        TextView name, deadline;
        public AlertViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvAlertBursaryName);
            deadline = itemView.findViewById(R.id.tvAlertDeadline);
        }
    }
}