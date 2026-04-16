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
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_insight, null);
        bottomSheetDialog.setContentView(sheetView);

        if (bottomSheetDialog.getWindow() != null) {
            bottomSheetDialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet)
                    .setBackgroundResource(android.R.color.transparent);
        }

        TextView tvName = sheetView.findViewById(R.id.tv_sheet_ingredient_name);
        TextView tvSafety = sheetView.findViewById(R.id.tv_sheet_safety);
        TextView tvCategory = sheetView.findViewById(R.id.tv_sheet_category);
        TextView tvRiskNote = sheetView.findViewById(R.id.tv_sheet_risk_note);

        TextView tvInsight = sheetView.findViewById(R.id.tv_sheet_insight_text);
        TextView tvViewMore = sheetView.findViewById(R.id.tv_view_more);
        View layoutLoading = sheetView.findViewById(R.id.layout_sheet_loading);
        View btnClose = sheetView.findViewById(R.id.btn_sheet_close);

        tvName.setText(ingredient.getName());
        tvCategory.setText(ingredient.getCategory() != null ? ingredient.getCategory() : "Cosmetic Ingredient");

        String risk = ingredient.getRiskNote();
        tvRiskNote.setText((risk != null && !risk.isEmpty() && !risk.equalsIgnoreCase("None"))
                ? risk : "No specific risks noted");

        int color;
        switch (ingredient.getSafetyLevel()) {
            case HARMFUL: color = android.graphics.Color.parseColor("#D32F2F"); break;
            case CAUTION: color = android.graphics.Color.parseColor("#FBC02D"); break;
            default:      color = android.graphics.Color.parseColor("#388E3C"); break;
        }
        tvSafety.setTextColor(color);
        tvSafety.setText("⊙ " + ingredient.getSafetyLabel());

        btnClose.setOnClickListener(v -> bottomSheetDialog.dismiss());
        tvViewMore.setOnClickListener(v -> {
            tvInsight.setMaxLines(Integer.MAX_VALUE);
            tvViewMore.setVisibility(View.GONE);
        });

        bottomSheetDialog.show();

        User user = dbHelper.getUserById(session.getUserId());
        List<String> skinTypes = user != null ? user.getSkinTypes() : null;

        if (geminiClient.isApiKeyConfigured()) {
            layoutLoading.setVisibility(View.VISIBLE);
            tvInsight.setVisibility(View.GONE);
            tvViewMore.setVisibility(View.GONE);

            geminiClient.explainIngredient(ingredient.getName(), skinTypes, new GeminiApiClient.AiCallback() {
                @Override
                public void onSuccess(String result) {
                    layoutLoading.setVisibility(View.GONE);
                    tvInsight.setVisibility(View.VISIBLE);

                    tvInsight.setMaxLines(3);
                    tvViewMore.setVisibility(View.VISIBLE);

                    tvInsight.setText(androidx.core.text.HtmlCompat.fromHtml(result, androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT));
                }

                @Override
                public void onError(String error) {
                    layoutLoading.setVisibility(View.GONE);
                    tvInsight.setVisibility(View.VISIBLE);
                    tvInsight.setMaxLines(Integer.MAX_VALUE);
                    tvInsight.setText(ingredient.getDescription() + "\n\n(AI analysis temporarily unavailable)");
                }
            });
        } else {
            layoutLoading.setVisibility(View.GONE);
            tvInsight.setVisibility(View.VISIBLE);
            tvInsight.setMaxLines(Integer.MAX_VALUE);
            tvInsight.setText(ingredient.getDescription());
        }
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
