package com.example.skinsafe;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
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
    private ImageButton btnHomeHelp;


    private DatabaseHelper dbHelper;
    private SessionManager session;
    private int userId;
    private TextView navHome, navSearch, navScan, navSaved, navProfile;


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


        checkFirstTimeLaunch();


    }


    private void checkFirstTimeLaunch() {
        android.content.SharedPreferences prefs = getSharedPreferences("SkinSafePrefs", MODE_PRIVATE);
        boolean hasSeenTutorial = prefs.getBoolean("has_seen_tutorial", false);


        if (!hasSeenTutorial) {
            showInstructionDialog();
            prefs.edit().putBoolean("has_seen_tutorial", true).apply();
        }
    }


    private void showInstructionDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_custom_alert, null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }


        TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
        TextView tvMessage = view.findViewById(R.id.tv_dialog_message);
        Button btnNeg = view.findViewById(R.id.btn_dialog_negative);
        Button btnPos = view.findViewById(R.id.btn_dialog_positive);


        tvTitle.setText("Welcome to SkinSafe! 🌱");
        tvMessage.setText("Here is how to check if a product matches your Skin Profile:\n\n" +
                "📷 Camera: Snap a photo of an ingredient label.\n" +
                "✍️ Manual: Type or paste ingredients.\n" +
                "🎤 Voice: Read the ingredients out loud.\n\n" +
                "Your Flagged Ingredients list will automatically warn you about things to avoid!");


        btnNeg.setVisibility(View.GONE);
        btnPos.setText("Let's Go!");


        btnPos.setOnClickListener(v -> dialog.dismiss());


        dialog.show();
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


        btnHomeHelp = findViewById(R.id.btn_home_help);
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
                startActivity(new Intent(this, SavedScansActivity.class)));


        findViewById(R.id.tv_see_all).setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));


        findViewById(R.id.nav_home).setOnClickListener(v -> { /* already here */ });
        findViewById(R.id.et_home_search).setOnClickListener(v ->
                startActivity(new Intent(this, SearchActivity.class)));
        findViewById(R.id.nav_search).setOnClickListener(v ->
                startActivity(new Intent(this, SearchActivity.class)));
        findViewById(R.id.nav_scan).setOnClickListener(v -> openScan("camera"));
        findViewById(R.id.nav_saved).setOnClickListener(v -> startActivity(new Intent(this, SavedScansActivity.class)));
        findViewById(R.id.nav_profile).setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));
        highlightCurrentNav();
        btnHomeHelp.setOnClickListener(v -> showInstructionDialog());
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
