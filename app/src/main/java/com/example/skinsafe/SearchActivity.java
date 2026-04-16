package com.example.skinsafe;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.graphics.Color;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private android.os.Handler debounceHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private Runnable debounceRunnable;
    private AutoCompleteTextView etSearch;
    private ImageButton btnBack;
    private LinearLayout layoutResult, layoutEmpty;
    private TextView tvResultName, tvResultCategory, tvResultSafety;
    private TextView tvResultDescription, tvResultRisk, tvResultAi;
    private View viewSafetyDot;
    private ProgressBar progressAi;

    private View navHome, navSearch, navScan, navSaved, navProfile;

    private SafetyClassifier classifier;
    private GeminiApiClient geminiClient;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        classifier = new SafetyClassifier();
        geminiClient = new GeminiApiClient();
        dbHelper = DatabaseHelper.getInstance(this);
        session = SessionManager.getInstance(this);

        initViews();
        setupAutoComplete();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        etSearch = findViewById(R.id.et_search);
        layoutResult = findViewById(R.id.layout_result);
        layoutEmpty = findViewById(R.id.layout_empty);

        tvResultName = findViewById(R.id.tv_result_name);
        tvResultCategory = findViewById(R.id.tv_result_category);
        tvResultSafety = findViewById(R.id.tv_result_safety);
        tvResultDescription = findViewById(R.id.tv_result_description);
        tvResultRisk = findViewById(R.id.tv_result_risk);
        tvResultAi = findViewById(R.id.tv_result_ai);
        viewSafetyDot = findViewById(R.id.view_safety_dot);
        progressAi = findViewById(R.id.progress_ai);

        navHome = findViewById(R.id.nav_home);
        navSearch = findViewById(R.id.nav_search);
        navScan = findViewById(R.id.nav_scan);
        navSaved = findViewById(R.id.nav_saved);
        navProfile = findViewById(R.id.nav_profile);
    }

    private void setupAutoComplete() {
        List<String> suggestions = IngredientDatabase.getInstance().getAllIngredientNames();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                suggestions
        );

        etSearch.setAdapter(adapter);
        etSearch.setThreshold(2);

        etSearch.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            searchIngredient(selected);
        });
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();

                if (debounceRunnable != null) {
                    debounceHandler.removeCallbacks(debounceRunnable);
                }

                if (query.length() >= 3) {
                    debounceRunnable = () -> searchIngredient(query);
                    debounceHandler.postDelayed(debounceRunnable, 600);
                } else {
                    layoutResult.setVisibility(View.GONE);
                    layoutEmpty.setVisibility(View.VISIBLE);
                }
            }
        });

        navHome.setOnClickListener(v -> { startActivity(new Intent(this, HomeActivity.class)); finish(); });
        navSearch.setOnClickListener(v -> { /* already here */ });
        navScan.setOnClickListener(v -> {
            Intent i = new Intent(this, ScanActivity.class);
            i.putExtra("input_mode", "camera");
            startActivity(i);
        });
        navSaved.setOnClickListener(v -> { startActivity(new Intent(this, SavedScansActivity.class)); });
        navProfile.setOnClickListener(v -> { startActivity(new Intent(this, ProfileActivity.class)); });

        highlightCurrentNav();
    }

    private void highlightCurrentNav() {
        int activeColor = android.graphics.Color.parseColor("#527860");
        ImageView icon = findViewById(R.id.ic_nav_search);
        TextView text = findViewById(R.id.tv_nav_search);
        if (icon != null && text != null) {
            icon.setColorFilter(activeColor);
            text.setTextColor(activeColor);
            text.setTypeface(null, android.graphics.Typeface.BOLD);
        }
    }

    private void searchIngredient(String name) {
        Ingredient ing = classifier.lookupSingle(name);
        displayIngredient(ing);
    }

    private void displayIngredient(Ingredient ing) {
        layoutResult.setVisibility(View.VISIBLE);
        layoutEmpty.setVisibility(View.GONE);

        tvResultName.setText(ing.getName());
        tvResultCategory.setText(ing.getCategory());
        tvResultDescription.setText(ing.getDescription());
        tvResultRisk.setText(ing.getRiskNote() != null && !ing.getRiskNote().isEmpty()
                ? ing.getRiskNote() : "No specific risks noted.");

        int color;
        switch (ing.getSafetyLevel()) {
            case HARMFUL: color = Color.parseColor("#E53935"); break;
            case CAUTION: color = Color.parseColor("#FB8C00"); break;
            default:      color = Color.parseColor("#2E7D32"); break;
        }
        tvResultSafety.setText(ing.getSafetyLabel());
        tvResultSafety.setTextColor(color);
        viewSafetyDot.setBackgroundColor(color);

        tvResultAi.setText("Loading AI explanation...");
        progressAi.setVisibility(View.VISIBLE);

        if (geminiClient.isApiKeyConfigured()) {
            User user = dbHelper.getUserById(session.getUserId());
            List<String> skinTypes = user != null ? user.getSkinTypes() : null;
            geminiClient.explainIngredient(ing.getName(), skinTypes, new GeminiApiClient.AiCallback() {
                @Override
                public void onSuccess(String result) {
                    progressAi.setVisibility(View.GONE);
                    tvResultAi.setText(HtmlCompat.fromHtml(result, HtmlCompat.FROM_HTML_MODE_COMPACT));
                }
                @Override
                public void onError(String error) {
                    progressAi.setVisibility(View.GONE);
                    tvResultAi.setText(ing.getDescription());
                }
            });
        } else {
            progressAi.setVisibility(View.GONE);
            tvResultAi.setText(ing.getDescription());
        }
    }
}