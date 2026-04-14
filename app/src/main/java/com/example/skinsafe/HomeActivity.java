package com.example.skinsafe;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private TextView tvGreeting, tvUserName, tvInitials;
    private CardView cardScan, cardFavorites, cardManual, cardVoice;
    private RecyclerView rvRecentScans;
    private HistoryAdapter historyAdapter;
    private List<ScanResult> recentScans;

    private DatabaseHelper dbHelper;
    private SessionManager session;
    private int userId;
    private TextView navHome, navSearch, navScan, navHistory, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = DatabaseHelper.getInstance(this);
        session = SessionManager.getInstance(this);
        userId = session.getUserId();

        initViews();
        loadUserData();
        loadRecentScans();
        setupNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecentScans();
        loadUserData();
    }

    private void initViews() {
        tvGreeting = findViewById(R.id.tv_greeting);
        tvUserName = findViewById(R.id.tv_user_name);
        tvInitials = findViewById(R.id.tv_initials);

        cardScan = findViewById(R.id.card_scan);
        cardFavorites = findViewById(R.id.card_favorites);
        cardManual = findViewById(R.id.card_manual);
        cardVoice = findViewById(R.id.card_voice);

        rvRecentScans = findViewById(R.id.rv_recent_scans);
        rvRecentScans.setLayoutManager(new LinearLayoutManager(this));
        rvRecentScans.setNestedScrollingEnabled(false);
    }

    private void loadUserData() {
        User user = dbHelper.getUserById(userId);
        if (user != null) {
            tvUserName.setText(user.getFullName());
            tvInitials.setText(user.getInitials());
        } else {
            String name = session.getUserName();
            tvUserName.setText(name);
            if (name != null && !name.isEmpty()) {
                String[] parts = name.split(" ");
                String initials = parts[0].substring(0, 1)
                        + (parts.length > 1 ? parts[1].substring(0, 1) : "");
                tvInitials.setText(initials.toUpperCase());
            }
        }

        int hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        if (hour < 12) tvGreeting.setText("Good morning,");
        else if (hour < 18) tvGreeting.setText("Good afternoon,");
        else tvGreeting.setText("Good evening,");
    }

    private void loadRecentScans() {
        List<ScanResult> allScans = dbHelper.getUserScans(userId);
        recentScans = new ArrayList<>(allScans.subList(0, Math.min(3, allScans.size())));

        historyAdapter = new HistoryAdapter(this, recentScans);
        historyAdapter.setOnScanClickListener(new HistoryAdapter.OnScanClickListener() {
            @Override
            public void onScanClick(ScanResult scan) {
                scan.setIngredients(dbHelper.getIngredientsForScan(scan.getId()));
                Intent intent = new Intent(HomeActivity.this, ResultsActivity.class);
                intent.putExtra("scan_id", scan.getId());
                intent.putExtra("product_name", scan.getProductName());
                intent.putExtra("ai_insight", scan.getAiInsight());
                startActivity(intent);
            }

            @Override
            public void onScanDelete(ScanResult scan, int position) {
            }
        });
        rvRecentScans.setAdapter(historyAdapter);
    }

    private void setupNavigation() {
        cardScan.setOnClickListener(v -> openScan("camera"));
        cardManual.setOnClickListener(v -> openScan("manual"));
        cardVoice.setOnClickListener(v -> openScan("voice"));
        cardFavorites.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));

        findViewById(R.id.nav_home).setOnClickListener(v -> { /* already here */ });
        findViewById(R.id.et_home_search).setOnClickListener(v ->
                startActivity(new Intent(this, SearchActivity.class)));
        findViewById(R.id.nav_search).setOnClickListener(v ->
                startActivity(new Intent(this, SearchActivity.class)));
        findViewById(R.id.nav_scan).setOnClickListener(v -> openScan("camera"));
        findViewById(R.id.nav_history).setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));
        findViewById(R.id.nav_profile).setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
        highlightCurrentNav();
    }

    private void highlightCurrentNav() {
        int activeColor = android.graphics.Color.parseColor("#527860");

        ImageView icHome = findViewById(R.id.ic_nav_home);
        TextView tvHome = findViewById(R.id.tv_nav_home);

        if (icHome != null && tvHome != null) {
            icHome.setColorFilter(activeColor);
            tvHome.setTextColor(activeColor);
            tvHome.setTypeface(null, android.graphics.Typeface.BOLD);
        }
    }

    private void openScan(String mode) {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra("input_mode", mode);
        startActivity(intent);
    }
}