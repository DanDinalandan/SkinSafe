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

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private final Context context;
    private final List<Ingredient> ingredients;
    private OnIngredientClickListener listener;

    public interface OnIngredientClickListener {
        void onIngredientClick(Ingredient ingredient);
    }

    public IngredientAdapter(Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    public void setOnIngredientClickListener(OnIngredientClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ing = ingredients.get(position);

        holder.tvName.setText(ing.getName());
        holder.tvCategory.setText(ing.getCategory());
        holder.tvDescription.setText(ing.getDescription());
        holder.tvSafetyLabel.setText(ing.getSafetyLabel());

        if (holder.tvHazardScore != null) {
            holder.tvHazardScore.setText("Hazard: " + ing.getHazardScore() + "/10");
        }
        if (holder.tvComedogenic != null) {
            holder.tvComedogenic.setText("Comedogenic: " + ing.getComedogenicRating() + "/5");
            holder.tvComedogenic.setVisibility(ing.getComedogenicRating() > 0 ? View.VISIBLE : View.GONE);
        }

        if (holder.tvAllergen != null) {
            if (ing.getAllergenInfo() != null && !ing.getAllergenInfo().equalsIgnoreCase("None") && !ing.getAllergenInfo().isEmpty()) {
                holder.tvAllergen.setVisibility(View.VISIBLE);
                holder.tvAllergen.setText("⚠️ " + ing.getAllergenInfo());
            } else {
                holder.tvAllergen.setVisibility(View.GONE);
            }
        }

        int color;
        switch (ing.getSafetyLevel()) {
            case HARMFUL:
                color = Color.parseColor("#E53935");
                break;
            case CAUTION:
                color = Color.parseColor("#FB8C00");
                break;
            default:
                color = Color.parseColor("#2E7D32");
                break;
        }
        holder.tvSafetyLabel.setTextColor(color);
        if (holder.safetyDot != null) {
            holder.safetyDot.setBackgroundColor(color);
        }

        if (ing.getRiskNote() != null && !ing.getRiskNote().isEmpty() &&
                ing.getSafetyLevel() != Ingredient.SafetyLevel.SAFE) {
            holder.tvRiskNote.setVisibility(View.VISIBLE);
            holder.tvRiskNote.setText(ing.getRiskNote());
        } else {
            holder.tvRiskNote.setVisibility(View.GONE);
        }

        holder.tvFlagged.setVisibility(ing.isFlagged() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onIngredientClick(ing);
        });
    }

    @Override
    public int getItemCount() { return ingredients.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory, tvDescription, tvSafetyLabel, tvRiskNote, tvFlagged;
        TextView tvHazardScore, tvComedogenic, tvAllergen;
        View safetyDot;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_ingredient_name);
            tvCategory = itemView.findViewById(R.id.tv_ingredient_category);
            tvDescription = itemView.findViewById(R.id.tv_ingredient_description);
            tvSafetyLabel = itemView.findViewById(R.id.tv_safety_label);
            tvRiskNote = itemView.findViewById(R.id.tv_risk_note);
            tvFlagged = itemView.findViewById(R.id.tv_flagged);
            safetyDot = itemView.findViewById(R.id.view_safety_dot);
            tvHazardScore = itemView.findViewById(R.id.tv_hazard_score);
            tvComedogenic = itemView.findViewById(R.id.tv_comedogenic);
            tvAllergen = itemView.findViewById(R.id.tv_allergen);
        }
    }
}