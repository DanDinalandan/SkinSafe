package com.example.skinsafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SavedScansActivity extends AppCompatActivity {

    private RecyclerView rvSavedScans;
    private TextView tvEmptySaved;
    private HistoryAdapter adapter;
    private List<ScanResult> savedScans;

    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_scans);

        dbHelper = DatabaseHelper.getInstance(this);
        session = SessionManager.getInstance(this);

        initViews();
        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSavedScans();
    }

    private void initViews() {
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        tvEmptySaved = findViewById(R.id.tv_empty_saved);
        rvSavedScans = findViewById(R.id.rv_saved_scans);
        rvSavedScans.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadSavedScans() {
        savedScans = dbHelper.getSavedScans(session.getUserId());

        if (savedScans.isEmpty()) {
            tvEmptySaved.setVisibility(View.VISIBLE);
            rvSavedScans.setVisibility(View.GONE);
        } else {
            tvEmptySaved.setVisibility(View.GONE);
            rvSavedScans.setVisibility(View.VISIBLE);

            adapter = new HistoryAdapter(this, savedScans);
            adapter.setOnScanClickListener(new HistoryAdapter.OnScanClickListener() {
                @Override
                public void onScanClick(ScanResult scan) {
                    scan.setIngredients(dbHelper.getIngredientsForScan(scan.getId()));
                    Intent intent = new Intent(SavedScansActivity.this, ResultsActivity.class);
                    intent.putExtra("scan_id", scan.getId());
                    intent.putExtra("product_name", scan.getProductName());
                    intent.putExtra("ai_insight", scan.getAiInsight());
                    startActivity(intent);
                }

                @Override
                public void onScanDelete(ScanResult scan, int position) {
                    showRemoveDialog(scan, position);
                }
            });
            rvSavedScans.setAdapter(adapter);
        }
    }

    private void showRemoveDialog(ScanResult scan, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Remove from Saved?")
                .setMessage("Are you sure you want to remove " + scan.getProductName() + " from your favorites? It will still be available in your general History.")
                .setPositiveButton("Remove", (dialog, which) -> {
                    dbHelper.toggleScanSaved(scan.getId(), false);
                    adapter.removeItem(position);

                    if (adapter.getItemCount() == 0) {
                        tvEmptySaved.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void setupBottomNav() {
        findViewById(R.id.nav_home).setOnClickListener(v -> { startActivity(new Intent(this, HomeActivity.class)); finish(); });
        findViewById(R.id.nav_search).setOnClickListener(v -> startActivity(new Intent(this, SearchActivity.class)));
        findViewById(R.id.nav_scan).setOnClickListener(v -> {
            Intent i = new Intent(this, ScanActivity.class);
            i.putExtra("input_mode", "camera");
            startActivity(i);
        });
        findViewById(R.id.nav_saved).setOnClickListener(v -> startActivity(new Intent(this, SavedScansActivity.class)));
        findViewById(R.id.nav_profile).setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

    }
}