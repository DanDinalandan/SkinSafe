package com.example.skinsafe;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final Context context;
    private final List<ScanResult> scans;
    private OnScanClickListener listener;

    public interface OnScanClickListener {
        void onScanClick(ScanResult scan);
        void onScanDelete(ScanResult scan, int position);
    }

    public HistoryAdapter(Context context, List<ScanResult> scans) {
        this.context = context;
        this.scans = scans;
    }

    public void setOnScanClickListener(OnScanClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScanResult scan = scans.get(position);

        holder.tvProductName.setText(scan.getProductName());
        holder.tvDate.setText(scan.getScanDate());

        String safetyLabel = scan.getOverallSafetyLabel();
        if (safetyLabel == null) {
            safetyLabel = "SAFE";
        }

        holder.tvSafetyBadge.setText(safetyLabel);

        int color;
        switch (safetyLabel.toUpperCase()) {
            case "HARMFUL":
                color = Color.parseColor("#E53935");
                break;
            case "CAUTION":
                color = Color.parseColor("#FB8C00");
                break;
            default:
                color = Color.parseColor("#2E7D32");
                break;
        }
        holder.tvSafetyBadge.setTextColor(color);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onScanClick(scan);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) listener.onScanDelete(scan, position);
            return true;
        });
    }

    @Override
    public int getItemCount() { return scans.size(); }

    public void removeItem(int position) {
        scans.remove(position);
        notifyItemRemoved(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvDate, tvSafetyBadge;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tv_history_product_name);
            tvDate = itemView.findViewById(R.id.tv_history_date);
            tvSafetyBadge = itemView.findViewById(R.id.tv_history_safety_badge);
        }
    }
}