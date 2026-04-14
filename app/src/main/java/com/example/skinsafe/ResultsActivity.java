package com.example.skinsafe;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    private TextView tvProductName, tvSummary, tvAiInsight;
    private TextView tvSafeCount, tvCautionCount, tvHarmfulCount;
    private RecyclerView rvIngredients;
    private IngredientAdapter ingredientAdapter;
    private ImageButton btnBack;
    private Button btnSave;
    private ProgressBar progressAi;

    private DatabaseHelper dbHelper;
    private SessionManager session;
    private GeminiApiClient geminiClient;

    private ScanResult currentScan;
    private int scanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        dbHelper = DatabaseHelper.getInstance(this);
        session = SessionManager.getInstance(this);
        geminiClient = new GeminiApiClient();

        scanId = getIntent().getIntExtra("scan_id", -1);
        String productName = getIntent().getStringExtra("product_name");
        String aiInsight = getIntent().getStringExtra("ai_insight");

        initViews();

        if (scanId != -1) {
            loadScanData(scanId, productName, aiInsight);
        } else {
            Toast.makeText(this, "Error loading scan results.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        tvProductName = findViewById(R.id.tv_product_name);
        tvSummary = findViewById(R.id.tv_summary);
        tvAiInsight = findViewById(R.id.tv_ai_insight);
        tvSafeCount = findViewById(R.id.tv_safe_count);
        tvCautionCount = findViewById(R.id.tv_caution_count);
        tvHarmfulCount = findViewById(R.id.tv_harmful_count);
        progressAi = findViewById(R.id.progress_ai);

        rvIngredients = findViewById(R.id.rv_ingredients);
        rvIngredients.setLayoutManager(new LinearLayoutManager(this));

        btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> toggleSave());

        Button btnEdit = findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, EditScanActivity.class);
            intent.putExtra("scan_id", scanId);
            startActivity(intent);
        });
    }

    private void loadScanData(int scanId, String productName, String aiInsight) {
        List<Ingredient> ingredients = dbHelper.getIngredientsForScan(scanId);

        currentScan = new ScanResult();
        currentScan.setId(scanId);
        currentScan.setProductName(productName != null ? productName : "Unknown Product");
        currentScan.setIngredients(ingredients);
        currentScan.setAiInsight(aiInsight);

        tvProductName.setText(currentScan.getProductName());
        tvSummary.setText(currentScan.getSummaryLine());

        tvSafeCount.setText(String.valueOf(currentScan.getSafeCount()));
        tvCautionCount.setText(String.valueOf(currentScan.getCautionCount()));
        tvHarmfulCount.setText(String.valueOf(currentScan.getHarmfulCount()));

        if (aiInsight != null && !aiInsight.isEmpty()) {
            tvAiInsight.setText(aiInsight);
            progressAi.setVisibility(View.GONE);
        } else {
            tvAiInsight.setText("Generating AI insight...");
        }

        ingredientAdapter = new IngredientAdapter(this, ingredients);
        ingredientAdapter.setOnIngredientClickListener(ingredient -> showIngredientDetail(ingredient));
        rvIngredients.setAdapter(ingredientAdapter);
    }

    private void showIngredientDetail(Ingredient ingredient) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ingredient_detail);
        dialog.getWindow().setLayout(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvName = dialog.findViewById(R.id.dialog_tv_name);
        TextView tvCategory = dialog.findViewById(R.id.dialog_tv_category);
        TextView tvSafety = dialog.findViewById(R.id.dialog_tv_safety);
        TextView tvDescription = dialog.findViewById(R.id.dialog_tv_description);
        TextView tvRisk = dialog.findViewById(R.id.dialog_tv_risk);
        TextView tvAiExplanation = dialog.findViewById(R.id.dialog_tv_ai_explanation);
        ProgressBar progressIngAi = dialog.findViewById(R.id.dialog_progress_ai);
        Button btnClose = dialog.findViewById(R.id.dialog_btn_close);

        tvName.setText(ingredient.getName());
        tvCategory.setText(ingredient.getCategory());
        tvDescription.setText(ingredient.getDescription());
        tvRisk.setText(ingredient.getRiskNote() != null ? ingredient.getRiskNote() : "No specific risks noted.");

        int color;
        switch (ingredient.getSafetyLevel()) {
            case HARMFUL: color = Color.parseColor("#E53935"); break;
            case CAUTION: color = Color.parseColor("#FB8C00"); break;
            default:      color = Color.parseColor("#2E7D32"); break;
        }
        tvSafety.setText(ingredient.getSafetyLabel());
        tvSafety.setTextColor(color);

        if (geminiClient.isApiKeyConfigured()) {
            progressIngAi.setVisibility(View.VISIBLE);
            tvAiExplanation.setText("");
            User user = dbHelper.getUserById(session.getUserId());
            List<String> skinTypes = user != null ? user.getSkinTypes() : null;
            geminiClient.explainIngredient(ingredient.getName(), skinTypes, new GeminiApiClient.AiCallback() {
                @Override
                public void onSuccess(String result) {
                    progressIngAi.setVisibility(View.GONE);
                    tvAiExplanation.setText(result);
                }
                @Override
                public void onError(String error) {
                    progressIngAi.setVisibility(View.GONE);
                    tvAiExplanation.setText(ingredient.getDescription());
                }
            });
        } else {
            progressIngAi.setVisibility(View.GONE);
            tvAiExplanation.setText(ingredient.getDescription());
        }

        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void toggleSave() {
        if (currentScan == null) return;
        boolean newSaved = !currentScan.isSaved();
        currentScan.setSaved(newSaved);
        dbHelper.toggleScanSaved(currentScan.getId(), newSaved);
        btnSave.setText(newSaved ? "✓ Saved" : "Save Product");
        Toast.makeText(this, newSaved ? "Product saved!" : "Removed from saved.", Toast.LENGTH_SHORT).show();
    }
}
