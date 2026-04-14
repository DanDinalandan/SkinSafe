package com.example.skinsafe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ScanActivity extends AppCompatActivity {

    private static final String TAG = "ScanActivity";
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int AUDIO_PERMISSION_CODE  = 101;

    private Button btnTabCamera, btnTabManual, btnTabVoice;
    private LinearLayout layoutCamera, layoutVoice;
    private ScrollView layoutManual;

    private PreviewView previewView;
    private ImageCapture imageCapture;
    private Button btnCapture;

    private EditText etProductName, etIngredients;
    private Button btnAnalyzeManual;

    private ImageButton btnVoiceStart;
    private Button btnVoiceAnalyze, btnVoiceClear;
    private TextView tvVoiceStatus, tvVoiceResult;
    private LinearLayout layoutVoiceResult;

    private ProgressBar progressBar;
    private ImageButton btnBack;

    private String currentMode;
    private String capturedVoiceText = "";

    private DatabaseHelper dbHelper;
    private SessionManager session;
    private SafetyClassifier classifier;
    private GeminiApiClient geminiClient;
    private VoiceInputHelper voiceInputHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        dbHelper       = DatabaseHelper.getInstance(this);
        session        = SessionManager.getInstance(this);
        classifier     = new SafetyClassifier();
        geminiClient   = new GeminiApiClient();
        voiceInputHelper = new VoiceInputHelper(this);

        currentMode = getIntent().getStringExtra("input_mode");
        if (currentMode == null) currentMode = "camera";

        initViews();
        setupTabSwitching();

        String editRawText = getIntent().getStringExtra("edit_text");
        String editName = getIntent().getStringExtra("edit_name");

        if (editRawText != null && !editRawText.isEmpty()) {
            switchToMode("manual");
            etIngredients.setText(editRawText);
            if (editName != null && !editName.equals("Unknown Product")) {
                etProductName.setText(editName);
            }
        } else {
            switchToMode(currentMode);
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        btnTabCamera = findViewById(R.id.btn_tab_camera);
        btnTabManual = findViewById(R.id.btn_tab_manual);
        btnTabVoice  = findViewById(R.id.btn_tab_voice);

        layoutCamera = findViewById(R.id.layout_camera);
        layoutManual = findViewById(R.id.layout_manual);
        layoutVoice  = findViewById(R.id.layout_voice);

        etProductName    = findViewById(R.id.et_product_name);

        // Camera
        previewView = findViewById(R.id.preview_view);
        btnCapture  = findViewById(R.id.btn_capture);
        btnCapture.setOnClickListener(v -> captureAndAnalyze());

        // Manual
        etIngredients    = findViewById(R.id.et_ingredients);
        btnAnalyzeManual = findViewById(R.id.btn_analyze_manual);
        btnAnalyzeManual.setOnClickListener(v -> analyzeManualInput());

        // Voice
        btnVoiceStart     = (ImageButton) findViewById(R.id.btn_voice_start);
        btnVoiceAnalyze   = findViewById(R.id.btn_voice_analyze);
        btnVoiceClear     = findViewById(R.id.btn_voice_clear);
        tvVoiceStatus     = findViewById(R.id.tv_voice_status);
        layoutVoiceResult = findViewById(R.id.layout_voice_result);

        btnVoiceStart.setOnClickListener(v   -> startVoiceInput());
        btnVoiceAnalyze.setOnClickListener(v -> analyzeVoiceInput());

        btnVoiceClear.setOnClickListener(v -> {
            voiceInputHelper.clearAccumulated();
            capturedVoiceText = "";
            updateVoiceIngredientsUI();
            btnVoiceClear.setVisibility(View.GONE);
            tvVoiceStatus.setText("Tap the microphone and say ingredient names separated by commas");
        });

        progressBar = findViewById(R.id.progress_bar);
    }

    private void setupTabSwitching() {
        btnTabCamera.setOnClickListener(v -> switchToMode("camera"));
        btnTabManual.setOnClickListener(v -> switchToMode("manual"));
        btnTabVoice.setOnClickListener(v  -> {
            voiceInputHelper.clearAccumulated();
            capturedVoiceText = "";
            btnVoiceClear.setVisibility(View.GONE);
            layoutVoiceResult.setVisibility(View.GONE);
            switchToMode("voice");
        });
    }

    private void switchToMode(String mode) {
        currentMode = mode;
        layoutCamera.setVisibility(View.GONE);
        layoutManual.setVisibility(View.GONE);
        layoutVoice.setVisibility(View.GONE);

        resetTabAppearance();

        switch (mode) {
            case "camera":
                layoutCamera.setVisibility(View.VISIBLE);
                highlightActiveTab(btnTabCamera);
                requestCameraAndStart();
                break;
            case "manual":
                layoutManual.setVisibility(View.VISIBLE);
                highlightActiveTab(btnTabManual);
                break;
            case "voice":
                layoutVoice.setVisibility(View.VISIBLE);
                highlightActiveTab(btnTabVoice);
                break;
        }
    }
    private void resetTabAppearance() {
        int inactiveTextColor = android.graphics.Color.parseColor("#4E735B");

        btnTabCamera.setBackgroundResource(R.drawable.bg_setting_item);
        btnTabCamera.setTextColor(inactiveTextColor);

        btnTabManual.setBackgroundResource(R.drawable.bg_setting_item);
        btnTabManual.setTextColor(inactiveTextColor);

        btnTabVoice.setBackgroundResource(R.drawable.bg_setting_item);
        btnTabVoice.setTextColor(inactiveTextColor);
    }

    private void highlightActiveTab(Button activeBtn) {
        activeBtn.setBackgroundResource(R.drawable.button_green);
        activeBtn.setTextColor(android.graphics.Color.WHITE);
    }

    // ==================== CAMERA ====================

    private void requestCameraAndStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> future =
                ProcessCameraProvider.getInstance(this);
        future.addListener(() -> {
            try {
                ProcessCameraProvider provider = future.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());
                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                        .build();
                provider.unbindAll();
                provider.bindToLifecycle(this,
                        CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, "Camera failed to start.", Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void captureAndAnalyze() {
        if (imageCapture == null) {
            Toast.makeText(this, "Camera not ready.", Toast.LENGTH_SHORT).show();
            return;
        }
        showLoading(true);

        File photoFile = new File(getCacheDir(), "scan_" + System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions options =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(options, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults results) {
                        android.graphics.BitmapFactory.Options opts = new android.graphics.BitmapFactory.Options();
                        opts.inSampleSize = 1;
                        Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(photoFile.getAbsolutePath(), opts);

                        if (bitmap == null) {
                            showLoading(false);
                            Toast.makeText(ScanActivity.this, "Could not process image.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        OcrUtils.extractTextFromBitmap(bitmap, new OcrUtils.OcrCallback() {
                            @Override
                            public void onSuccess(String extractedText) {
                                if (extractedText.isEmpty()) {
                                    showLoading(false);
                                    showOcrFailDialog();
                                    return;
                                }

                                String prodName = etProductName.getText().toString().trim();
                                if (prodName.isEmpty()) prodName = "Unknown Product";

                                final String finalProdName = prodName;

                                if (geminiClient.isApiKeyConfigured()) {
                                    tvVoiceStatus.setText("AI is cleaning text...");
                                    geminiClient.cleanOcrText(extractedText, new GeminiApiClient.AiCallback() {
                                        @Override
                                        public void onSuccess(String cleanText) {
                                            if (cleanText.equals("ERROR")) {
                                                showLoading(false);
                                                showOcrReviewDialog(extractedText);
                                            } else {
                                                processIngredientText(cleanText, finalProdName, "camera");
                                            }
                                        }
                                        @Override
                                        public void onError(String error) {
                                            processIngredientText(extractedText, finalProdName, "camera");
                                        }
                                    });
                                } else {
                                    processIngredientText(extractedText, finalProdName, "camera");
                                }
                            }

                            @Override
                            public void onFailure(String error) {
                                showLoading(false);
                                Toast.makeText(ScanActivity.this, "OCR failed: " + error, Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException e) {
                        showLoading(false);
                        Toast.makeText(ScanActivity.this, "Capture failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showOcrFailDialog() {
        new AlertDialog.Builder(this)
                .setTitle("No Text Found")
                .setMessage("We couldn't detect any ingredient text.\n\n"
                        + "Tips:\n"
                        + "• Hold the camera VERY close to the ingredient label\n"
                        + "• Make sure the label is well-lit and in focus\n"
                        + "• Try to frame ONLY the ingredient list inside the green box\n\n"
                        + "Or switch to Manual Input and type/paste the ingredients.")
                .setPositiveButton("Try Again", (d, w) -> d.dismiss())
                .setNegativeButton("Manual Input", (d, w) -> switchToMode("manual"))
                .show();
    }

    private void showOcrReviewDialog(String text) {
        new AlertDialog.Builder(this)
                .setTitle("Review Detected Text")
                .setMessage("We detected text — does this look like an ingredient list?\n\n" + text)
                .setPositiveButton("Yes, Analyze",
                        (d, w) -> {
                            String prodName = etProductName.getText().toString().trim();
                            if (prodName.isEmpty()) prodName = "Unknown Product";
                            processIngredientText(text, prodName, "camera");
                        })
                .setNegativeButton("Edit Manually", (d, w) -> {
                    switchToMode("manual");
                    etIngredients.setText(text);
                })
                .show();
    }

    // ==================== MANUAL ====================

    private void analyzeManualInput() {
        String productName     = etProductName.getText().toString().trim();
        String ingredientsText = etIngredients.getText().toString().trim();

        if (TextUtils.isEmpty(ingredientsText)) {
            etIngredients.setError("Please enter ingredient text");
            return;
        }
        if (productName.isEmpty()) productName = "Unknown Product";
        processIngredientText(ingredientsText, productName, "manual");
    }

    // ==================== VOICE ====================

    private void startVoiceInput() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_PERMISSION_CODE);
            return;
        }
        if (!VoiceInputHelper.isAvailable(this)) {
            Toast.makeText(this, "Voice recognition not available on this device.", Toast.LENGTH_LONG).show();
            return;
        }

        tvVoiceStatus.setText("Listening… say one or more ingredients, then pause");
        btnVoiceStart.setEnabled(false);

        voiceInputHelper.startListening(new VoiceInputHelper.VoiceCallback() {

            @Override
            public void onResult(String spokenText, String fullText) {
                if (!spokenText.trim().isEmpty()) {
                    if (!capturedVoiceText.isEmpty()) capturedVoiceText += ", ";
                    capturedVoiceText += spokenText;
                }

                voiceInputHelper.clearAccumulated();

                tvVoiceStatus.setText("✓ Heard! Tap mic to add more, or Analyze.");
                updateVoiceIngredientsUI();
                btnVoiceStart.setEnabled(true);
                btnVoiceClear.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String errorMessage) {
                tvVoiceStatus.setText("Error: " + errorMessage + "\nTap mic to try again.");
                btnVoiceStart.setEnabled(true);
            }

            @Override
            public void onReadyForSpeech() {
                tvVoiceStatus.setText("🎤 Ready — speak now!");
            }

            @Override
            public void onEndOfSpeech() {
                tvVoiceStatus.setText("Processing…");
            }
        });
    }

    private void analyzeVoiceInput() {
        if (TextUtils.isEmpty(capturedVoiceText)) {
            Toast.makeText(this, "No voice input detected. Tap the mic and speak.", Toast.LENGTH_SHORT).show();
            return;
        }

        String prodName = etProductName.getText().toString().trim();
        if (prodName.isEmpty()) prodName = "Unknown Product";
        final String finalProdName = prodName;

        showLoading(true);

        if (geminiClient.isApiKeyConfigured()) {
            geminiClient.formatVoiceInput(capturedVoiceText, new GeminiApiClient.AiCallback() {
                @Override
                public void onSuccess(String cleanText) {
                    processIngredientText(cleanText, finalProdName, "voice");
                }
                @Override
                public void onError(String error) {
                    List<String> parsed = IngredientParser.parseVoiceInput(capturedVoiceText);
                    processIngredientText(IngredientParser.formatList(parsed), finalProdName, "voice");
                }
            });
        } else {
            List<String> parsed = IngredientParser.parseVoiceInput(capturedVoiceText);
            processIngredientText(IngredientParser.formatList(parsed), finalProdName, "voice");
        }
    }

    // ==================== CORE ANALYSIS ====================

    private void processIngredientText(String rawText, String productName, String method) {
        showLoading(true);

        List<String> ingredientNames = IngredientParser.parse(rawText);
        if (ingredientNames.isEmpty()) {
            showLoading(false);
            Toast.makeText(this,
                    "No ingredients could be parsed from the text.\n"
                            + "Make sure ingredients are separated by commas.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        User user = dbHelper.getUserById(session.getUserId());
        List<String> skinTypes    = user != null ? user.getSkinTypes()    : new ArrayList<>();
        List<String> skinConcerns = user != null ? user.getSkinConcerns() : new ArrayList<>();

        List<Ingredient> classified =
                classifier.classifyWithSkinProfile(ingredientNames, skinTypes, skinConcerns);

        ScanResult result = new ScanResult(productName, rawText, method);
        result.setUserId(session.getUserId());
        result.setIngredients(classified);

        List<String> concernNames = new ArrayList<>();
        for (Ingredient ing : classified) {
            if (ing.getSafetyLevel() != Ingredient.SafetyLevel.SAFE)
                concernNames.add(ing.getName());
        }

        if (geminiClient.isApiKeyConfigured()) {
            geminiClient.generateProductInsight(productName, ingredientNames, concernNames,
                    skinTypes, new GeminiApiClient.AiCallback() {
                        @Override public void onSuccess(String aiText) {
                            result.setAiInsight(aiText);
                            saveAndNavigate(result);
                        }
                        @Override public void onError(String error) {
                            result.setAiInsight(generateFallbackInsight(classified));
                            saveAndNavigate(result);
                        }
                    });
        } else {
            result.setAiInsight(generateFallbackInsight(classified));
            saveAndNavigate(result);
        }
    }

    private String generateFallbackInsight(List<Ingredient> ingredients) {
        int safe = 0, caution = 0, harmful = 0;
        String worstName = null, worstRisk = null;
        for (Ingredient i : ingredients) {
            switch (i.getSafetyLevel()) {
                case SAFE: safe++; break;
                case CAUTION:
                    caution++;
                    if (worstName == null) { worstName = i.getName(); worstRisk = i.getRiskNote(); }
                    break;
                case HARMFUL:
                    harmful++;
                    worstName = i.getName(); worstRisk = i.getRiskNote();
                    break;
            }
        }
        if (harmful > 0)
            return "This product contains " + harmful + " harmful ingredient(s), including "
                    + worstName + ". " + (worstRisk != null ? worstRisk : "")
                    + " Consider switching to a safer alternative.";
        if (caution > 0)
            return "This product contains " + caution + " ingredient(s) requiring caution, including "
                    + worstName + ". " + (worstRisk != null ? worstRisk : "")
                    + " Monitor for any skin reactions.";
        return "All " + safe + " analyzed ingredients appear safe and well-tolerated. "
                + "This product looks like a good choice.";
    }

    private void saveAndNavigate(ScanResult result) {
        long scanId = dbHelper.saveScanResult(result);
        dbHelper.incrementScanCount(session.getUserId());
        result.setId((int) scanId);
        showLoading(false);

        voiceInputHelper.clearAccumulated();

        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("scan_id",      (int) scanId);
        intent.putExtra("product_name", result.getProductName());
        intent.putExtra("ai_insight",   result.getAiInsight());
        startActivity(intent);

        finish();
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnCapture.setEnabled(!show);
        btnAnalyzeManual.setEnabled(!show);
        btnVoiceStart.setEnabled(!show);
    }

    private void updateVoiceIngredientsUI() {
        LinearLayout llVoiceIngredients = findViewById(R.id.ll_voice_ingredients);
        llVoiceIngredients.removeAllViews();

        List<String> parsed = IngredientParser.parseVoiceInput(capturedVoiceText);

        if (parsed.isEmpty()) {
            layoutVoiceResult.setVisibility(View.GONE);
            btnVoiceAnalyze.setEnabled(false);
            btnVoiceClear.setVisibility(View.GONE);
            tvVoiceStatus.setText("Tap the microphone and say ingredient names separated by commas");
            return;
        }

        layoutVoiceResult.setVisibility(View.VISIBLE);
        btnVoiceAnalyze.setEnabled(true);

        for (int i = 0; i < parsed.size(); i++) {
            String ingredient = parsed.get(i);
            final int index = i;

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(0, 16, 0, 16);
            row.setGravity(android.view.Gravity.CENTER_VERTICAL);

            TextView tvName = new TextView(this);
            tvName.setText("• " + ingredient);
            tvName.setTextColor(android.graphics.Color.parseColor("#1A1A1A"));
            tvName.setTextSize(15f);
            tvName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

            TextView tvRemove = new TextView(this);
            tvRemove.setText("✕ Remove");
            tvRemove.setTextColor(android.graphics.Color.parseColor("#D32F2F"));
            tvRemove.setTextSize(12f);
            tvRemove.setTypeface(null, android.graphics.Typeface.BOLD);
            tvRemove.setPadding(24, 8, 8, 8);

            android.util.TypedValue outValue = new android.util.TypedValue();
            getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            tvRemove.setBackgroundResource(outValue.resourceId);
            tvRemove.setClickable(true);
            tvRemove.setFocusable(true);

            tvRemove.setOnClickListener(v -> {
                parsed.remove(index);
                capturedVoiceText = IngredientParser.formatList(parsed);
                updateVoiceIngredientsUI();
            });

            row.addView(tvName);
            row.addView(tvRemove);
            llVoiceIngredients.addView(row);

            if (i < parsed.size() - 1) {
                View divider = new View(this);
                divider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
                divider.setBackgroundColor(android.graphics.Color.parseColor("#E0E0E0"));
                llVoiceIngredients.addView(divider);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startCamera();
            else {
                Toast.makeText(this, "Camera permission is required to scan products.", Toast.LENGTH_LONG).show();
                switchToMode("manual");
            }
        } else if (requestCode == AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startVoiceInput();
            else
                Toast.makeText(this, "Microphone permission is required for voice input.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (voiceInputHelper != null) voiceInputHelper.destroy();
    }
}