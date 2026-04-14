package com.example.skinsafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class EditScanActivity extends AppCompatActivity {

    private EditText etName, etIngredients;
    private Button btnSave;
    private ImageButton btnBack;
    private ProgressBar progressBar;

    private DatabaseHelper dbHelper;
    private SessionManager session;
    private SafetyClassifier classifier;
    private GeminiApiClient geminiClient;

    private int existingScanId = -1;
    private ScanResult currentScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_scan);

        dbHelper = DatabaseHelper.getInstance(this);
        session = SessionManager.getInstance(this);
        classifier = new SafetyClassifier();
        geminiClient = new GeminiApiClient();

        existingScanId = getIntent().getIntExtra("scan_id", -1);

        initViews();
        loadScanData();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        etName = findViewById(R.id.et_edit_name);
        etIngredients = findViewById(R.id.et_edit_ingredients);
        btnSave = findViewById(R.id.btn_save_changes);
        progressBar = findViewById(R.id.progress_bar);

        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> reAnalyzeAndSave());
    }

    private void loadScanData() {
        if (existingScanId == -1) {
            Toast.makeText(this, "Error loading scan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentScan = dbHelper.getScanById(existingScanId);
        if (currentScan != null) {
            etName.setText(currentScan.getProductName());
            etIngredients.setText(currentScan.getRawIngredientText());
        }
    }

    private void reAnalyzeAndSave() {
        String newName = etName.getText().toString().trim();
        String newText = etIngredients.getText().toString().trim();

        if (newText.isEmpty()) {
            etIngredients.setError("Ingredients cannot be empty");
            return;
        }

        showLoading(true);

        List<String> ingredientNames = IngredientParser.parse(newText);

        User user = dbHelper.getUserById(session.getUserId());
        List<String> skinTypes = user != null ? user.getSkinTypes() : new ArrayList<>();
        List<String> skinConcerns = user != null ? user.getSkinConcerns() : new ArrayList<>();

        List<Ingredient> classified = classifier.classifyWithSkinProfile(ingredientNames, skinTypes, skinConcerns);

        currentScan.setProductName(newName.isEmpty() ? "Unknown Product" : newName);
        currentScan.setRawIngredientText(newText);
        currentScan.setIngredients(classified);

        List<String> concernNames = new ArrayList<>();
        for (Ingredient ing : classified) {
            if (ing.getSafetyLevel() != Ingredient.SafetyLevel.SAFE) concernNames.add(ing.getName());
        }

        if (geminiClient.isApiKeyConfigured()) {
            geminiClient.generateProductInsight(currentScan.getProductName(), ingredientNames, concernNames, skinTypes, new GeminiApiClient.AiCallback() {
                @Override
                public void onSuccess(String aiText) {
                    currentScan.setAiInsight(aiText);
                    finalizeUpdate();
                }

                @Override
                public void onError(String error) {
                    currentScan.setAiInsight("Unable to generate AI insight at this time.");
                    finalizeUpdate();
                }
            });
        } else {
            currentScan.setAiInsight("AI not configured.");
            finalizeUpdate();
        }
    }

    private void finalizeUpdate() {
        dbHelper.updateScanResult(currentScan);

        showLoading(false);
        Toast.makeText(this, "Scan updated!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("scan_id", currentScan.getId());
        intent.putExtra("product_name", currentScan.getProductName());
        intent.putExtra("ai_insight", currentScan.getAiInsight());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnSave.setEnabled(!show);
    }
}