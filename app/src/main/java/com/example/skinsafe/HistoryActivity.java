package com.example.skinsafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView rvHistory;
    private HistoryAdapter historyAdapter;
    private TextView tvEmpty;
    private ImageButton btnBack;
    private DatabaseHelper dbHelper;
    private SessionManager session;
    private List<ScanResult> scans;
    private View navHome, navSearch, navScan, navHistory, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = DatabaseHelper.getInstance(this);
        session = SessionManager.getInstance(this);

        initViews();
        loadHistory();
        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistory();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());
        rvHistory = findViewById(R.id.rv_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        tvEmpty = findViewById(R.id.tv_empty);

        navHome = findViewById(R.id.nav_home);
        navSearch = findViewById(R.id.nav_search);
        navScan = findViewById(R.id.nav_scan);
        navHistory = findViewById(R.id.nav_history);
        navProfile = findViewById(R.id.nav_profile);
    }

    private void loadHistory() {
        scans = dbHelper.getUserScans(session.getUserId());
        if (scans.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvHistory.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvHistory.setVisibility(View.VISIBLE);
            historyAdapter = new HistoryAdapter(this, scans);
            historyAdapter.setOnScanClickListener(new HistoryAdapter.OnScanClickListener() {
                @Override
                public void onScanClick(ScanResult scan) {
                    scan.setIngredients(dbHelper.getIngredientsForScan(scan.getId()));
                    Intent intent = new Intent(HistoryActivity.this, ResultsActivity.class);
                    intent.putExtra("scan_id", scan.getId());
                    intent.putExtra("product_name", scan.getProductName());
                    intent.putExtra("ai_insight", scan.getAiInsight());
                    startActivity(intent);
                }

                @Override
                public void onScanDelete(ScanResult scan, int position) {
                    new AlertDialog.Builder(HistoryActivity.this)
                            .setTitle("Delete Scan")
                            .setMessage("Remove \"" + scan.getProductName() + "\" from your history?")
                            .setPositiveButton("Delete", (d, w) -> {
                                dbHelper.deleteScan(scan.getId());
                                historyAdapter.removeItem(position);
                                Toast.makeText(HistoryActivity.this, "Scan deleted.", Toast.LENGTH_SHORT).show();
                                if (scans.isEmpty()) {
                                    tvEmpty.setVisibility(View.VISIBLE);
                                    rvHistory.setVisibility(View.GONE);
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            });
            rvHistory.setAdapter(historyAdapter);
        }
    }

    private void setupBottomNav() {
        navHome.setOnClickListener(v -> { startActivity(new Intent(this, HomeActivity.class)); finish(); });
        navSearch.setOnClickListener(v -> { Intent i = new Intent(this, SearchActivity.class); i.putExtra("input_mode","manual"); startActivity(i); });
        navScan.setOnClickListener(v -> { Intent i = new Intent(this, ScanActivity.class); i.putExtra("input_mode","camera"); startActivity(i); });
        navHistory.setOnClickListener(v -> { /* already here */ });
        navProfile.setOnClickListener(v -> { startActivity(new Intent(this, ProfileActivity.class)); });
        highlightCurrentNav();
    }

    private void highlightCurrentNav() {
        int activeColor = android.graphics.Color.parseColor("#527860");
        ImageView icon = findViewById(R.id.ic_nav_history);
        TextView text = findViewById(R.id.tv_nav_history);
        if (icon != null && text != null) {
            icon.setColorFilter(activeColor);
            text.setTextColor(activeColor);
            text.setTypeface(null, android.graphics.Typeface.BOLD);
        }
    }
}
