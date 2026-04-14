package com.example.skinsafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvInitials, tvFullName, tvEmail;
    private TextView tvTotalScans, tvFlaggedCount, tvSavedCount;
    private TextView tvSkinTypes, tvConcerns;
    private Button btnEditProfile, btnSignOut;
    private LinearLayout rowNotifications, rowPrivacy, rowHelp;
    private ImageButton btnBack;

    private DatabaseHelper dbHelper;
    private SessionManager session;
    private User currentUser;

    private View navHome, navSearch, navScan, navSaved, navProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = DatabaseHelper.getInstance(this);
        session = SessionManager.getInstance(this);

        initViews();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUser();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvInitials = findViewById(R.id.tv_initials);
        tvFullName = findViewById(R.id.tv_full_name);
        tvEmail = findViewById(R.id.tv_email);

        tvTotalScans = findViewById(R.id.tv_total_scans);
        tvFlaggedCount = findViewById(R.id.tv_flagged_count);
        tvSavedCount = findViewById(R.id.tv_saved_count);

        tvSkinTypes = findViewById(R.id.tv_skin_types);
        tvConcerns = findViewById(R.id.tv_concerns);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnSignOut = findViewById(R.id.btn_sign_out);
        rowNotifications = findViewById(R.id.row_notifications);
        rowPrivacy = findViewById(R.id.row_privacy);
        rowHelp = findViewById(R.id.row_help);

        navHome = findViewById(R.id.nav_home);
        navSearch = findViewById(R.id.nav_search);
        navScan = findViewById(R.id.nav_scan);
        navSaved = findViewById(R.id.nav_saved);
        navProfile = findViewById(R.id.nav_profile);
    }

    private void loadUser() {
        int userId = session.getUserId();
        currentUser = dbHelper.getUserById(userId);
        if (currentUser == null) { signOut(); return; }

        tvInitials.setText(currentUser.getInitials());
        tvFullName.setText(currentUser.getFullName());
        tvEmail.setText(currentUser.getEmail());

        tvTotalScans.setText(String.valueOf(dbHelper.getDynamicTotalScansCount(userId)));
        tvSavedCount.setText(String.valueOf(dbHelper.getDynamicSavedScansCount(userId)));
        tvFlaggedCount.setText(String.valueOf(dbHelper.getDynamicFlaggedIngredientsCount(userId)));

        List<String> types = currentUser.getSkinTypes();
        tvSkinTypes.setText(types.isEmpty() ? "Not set" : String.join("  ·  ", types));

        List<String> concerns = currentUser.getSkinConcerns();
        tvConcerns.setText(concerns.isEmpty() ? "None" : String.join("  ·  ", concerns));
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        btnEditProfile.setOnClickListener(v -> showEditProfileDialog());

        btnSignOut.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Sign Out")
                    .setMessage("Are you sure you want to sign out?")
                    .setPositiveButton("Sign Out", (d, w) -> signOut())
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        tvTotalScans.setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));
        tvSavedCount.setOnClickListener(v -> startActivity(new Intent(this, SavedScansActivity.class)));
        tvFlaggedCount.setOnClickListener(v -> startActivity(new Intent(this, FlaggedActivity.class)));

        rowNotifications.setOnClickListener(v -> Toast.makeText(this, "Notifications settings coming soon.", Toast.LENGTH_SHORT).show());
        rowPrivacy.setOnClickListener(v -> Toast.makeText(this, "Privacy & Safety settings coming soon.", Toast.LENGTH_SHORT).show());
        rowHelp.setOnClickListener(v -> Toast.makeText(this, "Help & Support coming soon.", Toast.LENGTH_SHORT).show());

        navHome.setOnClickListener(v -> { startActivity(new Intent(this, HomeActivity.class)); finish(); });
        navSearch.setOnClickListener(v -> { Intent i = new Intent(this, SearchActivity.class); i.putExtra("input_mode", "manual"); startActivity(i); });
        navScan.setOnClickListener(v -> { Intent i = new Intent(this, ScanActivity.class); i.putExtra("input_mode", "camera"); startActivity(i); });
        navSaved.setOnClickListener(v -> { startActivity(new Intent(this, SavedScansActivity.class)); finish(); });
        navProfile.setOnClickListener(v -> { /* already here */ });

        highlightCurrentNav();
    }

    private void highlightCurrentNav() {
        int activeColor = android.graphics.Color.parseColor("#527860");
        ImageView icon = findViewById(R.id.ic_nav_profile);
        TextView text = findViewById(R.id.tv_nav_profile);
        if (icon != null && text != null) {
            icon.setColorFilter(activeColor);
            text.setTextColor(activeColor);
            text.setTypeface(null, android.graphics.Typeface.BOLD);
        }
    }
    private void showEditProfileDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_profile, null);

        CheckBox cbOily = dialogView.findViewById(R.id.cb_oily);
        CheckBox cbDry = dialogView.findViewById(R.id.cb_dry);
        CheckBox cbCombination = dialogView.findViewById(R.id.cb_combination);
        CheckBox cbSensitive = dialogView.findViewById(R.id.cb_sensitive);
        CheckBox cbNormal = dialogView.findViewById(R.id.cb_normal);
        CheckBox cbNoFragrance = dialogView.findViewById(R.id.cb_no_fragrance);
        CheckBox cbAvoidParabens = dialogView.findViewById(R.id.cb_avoid_parabens);
        CheckBox cbAcneProne = dialogView.findViewById(R.id.cb_acne_prone);

        List<String> currentTypes = currentUser.getSkinTypes();
        List<String> currentConcerns = currentUser.getSkinConcerns();
        cbOily.setChecked(currentTypes.contains("Oily"));
        cbDry.setChecked(currentTypes.contains("Dry"));
        cbCombination.setChecked(currentTypes.contains("Combination"));
        cbSensitive.setChecked(currentTypes.contains("Sensitive"));
        cbNormal.setChecked(currentTypes.contains("Normal"));
        cbNoFragrance.setChecked(currentConcerns.contains("No Fragrance"));
        cbAvoidParabens.setChecked(currentConcerns.contains("Avoid Parabens"));
        cbAcneProne.setChecked(currentConcerns.contains("Acne-prone"));

        new AlertDialog.Builder(this)
                .setTitle("Edit Skin Profile")
                .setView(dialogView)
                .setPositiveButton("Save", (d, w) -> {
                    List<String> newTypes = new ArrayList<>();
                    List<String> newConcerns = new ArrayList<>();
                    if (cbOily.isChecked()) newTypes.add("Oily");
                    if (cbDry.isChecked()) newTypes.add("Dry");
                    if (cbCombination.isChecked()) newTypes.add("Combination");
                    if (cbSensitive.isChecked()) newTypes.add("Sensitive");
                    if (cbNormal.isChecked()) newTypes.add("Normal");
                    if (cbNoFragrance.isChecked()) newConcerns.add("No Fragrance");
                    if (cbAvoidParabens.isChecked()) newConcerns.add("Avoid Parabens");
                    if (cbAcneProne.isChecked()) newConcerns.add("Acne-prone");

                    dbHelper.updateUserProfile(currentUser.getId(), newTypes, newConcerns);
                    Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();
                    loadUser();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void signOut() {
        session.logout();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}