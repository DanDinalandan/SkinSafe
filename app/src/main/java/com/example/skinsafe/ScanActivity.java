package com.example.skinsafe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    private static final int MIN_CONFIDENT_INGREDIENTS = 4;

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
    private TextView tvScanStatus;
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

        dbHelper         = DatabaseHelper.getInstance(this);
        session          = SessionManager.getInstance(this);
        classifier       = new SafetyClassifier();
        geminiClient     = new GeminiApiClient();
        voiceInputHelper = new VoiceInputHelper(this);

        currentMode = getIntent().getStringExtra("input_mode");
        if (currentMode == null) currentMode = "camera";

        initViews();
        setupTabSwitching();

        String editRawText = getIntent().getStringExtra("edit_text");
        String editName    = getIntent().getStringExtra("edit_name");

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

        etProductName = findViewById(R.id.et_product_name);

        // Camera
        previewView = findViewById(R.id.preview_view);
        btnCapture  = findViewById(R.id.btn_capture);
        btnCapture.setOnClickListener(v -> captureAndAnalyze());

        // Manual
        etIngredients    = findViewById(R.id.et_ingredients);
        btnAnalyzeManual = findViewById(R.id.btn_analyze_manual);
        btnAnalyzeManual.setOnClickListener(v -> analyzeManualInput());

        // Voice
        btnVoiceStart     = findViewById(R.id.btn_voice_start);
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

        progressBar  = findViewById(R.id.progress_bar);

        tvScanStatus = findViewById(R.id.tv_scan_status);
        if (tvScanStatus == null) {
            tvScanStatus = new TextView(this);
        }
    }

    private void setupTabSwitching() {
        btnTabCamera.setOnClickListener(v -> switchToMode("camera"));
        btnTabManual.setOnClickListener(v -> switchToMode("manual"));
        btnTabVoice.setOnClickListener(v -> {
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
        int inactive = android.graphics.Color.parseColor("#4E735B");
        btnTabCamera.setBackgroundResource(R.drawable.bg_setting_item);
        btnTabCamera.setTextColor(inactive);
        btnTabManual.setBackgroundResource(R.drawable.bg_setting_item);
        btnTabManual.setTextColor(inactive);
        btnTabVoice.setBackgroundResource(R.drawable.bg_setting_item);
        btnTabVoice.setTextColor(inactive);
    }

    private void highlightActiveTab(Button btn) {
        btn.setBackgroundResource(R.drawable.button_green);
        btn.setTextColor(android.graphics.Color.WHITE);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CAMERA
    // ═══════════════════════════════════════════════════════════════════════════

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
        ListenableFuture<ProcessCameraProvider> future = ProcessCameraProvider.getInstance(this);
        future.addListener(() -> {
            try {
                ProcessCameraProvider provider = future.get();
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                        .build();

                provider.unbindAll();
                provider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA,
                        preview, imageCapture);
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
        setStatus("Capturing image…");
        showLoading(true);

        File photoFile = new File(getCacheDir(), "scan_" + System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions options =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(options, ContextCompat.getMainExecutor(this),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults results) {
                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        opts.inSampleSize = 1; // full resolution
                        Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), opts);

                        if (bitmap == null) {
                            showLoading(false);
                            Toast.makeText(ScanActivity.this,
                                    "Could not process image.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        setStatus("Reading ingredients…");
                        runOcrWithFallback(bitmap);
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException e) {
                        showLoading(false);
                        Toast.makeText(ScanActivity.this,
                                "Capture failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Two-pass OCR strategy:
     * Pass 1 — normal preprocessing (upscale + contrast + sharpen)
     * Pass 2 — only if pass 1 gives < MIN_CONFIDENT_INGREDIENTS results;
     *          runs a heavier contrast pass (inverts if background is dark)
     */
    private void runOcrWithFallback(Bitmap original) {
        OcrUtils.extractTextFromBitmap(original, new OcrUtils.OcrCallback() {
            @Override
            public void onSuccess(String pass1Text) {
                List<String> parsed = IngredientParser.parse(pass1Text);

                if (parsed.size() >= MIN_CONFIDENT_INGREDIENTS) {
                    Log.d(TAG, "Pass 1 OCR confident: " + parsed.size() + " ingredients");
                    handleOcrResult(pass1Text, false);
                } else {
                    Log.d(TAG, "Pass 1 weak (" + parsed.size() + " ingredients), running pass 2");
                    setStatus("Enhancing image for small text…");
                    Bitmap inverted = invertBitmap(original);
                    OcrUtils.extractTextFromBitmap(inverted, new OcrUtils.OcrCallback() {
                        @Override
                        public void onSuccess(String pass2Text) {
                            List<String> parsed2 = IngredientParser.parse(pass2Text);
                            String best = parsed2.size() > parsed.size() ? pass2Text : pass1Text;
                            Log.d(TAG, "Pass 2 found " + parsed2.size() + " ingredients");
                            handleOcrResult(best, parsed2.size() < 3 && parsed.size() < 3);
                        }
                        @Override
                        public void onFailure(String error) {
                            handleOcrResult(pass1Text, parsed.size() < 3);
                        }
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                showLoading(false);
                Toast.makeText(ScanActivity.this,
                        "OCR failed: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Inverts a bitmap's colors — helps when ingredients are printed on dark/colored backgrounds.
     */
    private Bitmap invertBitmap(Bitmap src) {
        android.graphics.ColorMatrix cm = new android.graphics.ColorMatrix(new float[]{
                -1,  0,  0, 0, 255,
                0, -1,  0, 0, 255,
                0,  0, -1, 0, 255,
                0,  0,  0, 1,   0
        });
        Bitmap inv = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(inv);
        android.graphics.Paint paint = new android.graphics.Paint();
        paint.setColorFilter(new android.graphics.ColorMatrixColorFilter(cm));
        canvas.drawBitmap(src, 0, 0, paint);
        return inv;
    }

    /**
     * After OCR is done: either show a preview/review dialog, or go straight to AI cleaning.
     * @param requiresReview  true when we didn't find enough ingredients and want the user to verify
     */
    private void handleOcrResult(String ocrText, boolean requiresReview) {
        if (ocrText == null || ocrText.trim().isEmpty()) {
            showLoading(false);
            showOcrFailDialog();
            return;
        }

        String prodName = etProductName.getText().toString().trim();
        if (prodName.isEmpty()) prodName = "Unknown Product";
        final String finalProdName = prodName;

        if (requiresReview) {
            showLoading(false);
            showOcrReviewDialog(ocrText);
            return;
        }

        List<String> detected = IngredientParser.parse(ocrText);
        String preview = buildPreviewString(detected);
        showOcrPreviewDialog(ocrText, preview, finalProdName);
    }
    private void showOcrPreviewDialog(String ocrText, String preview, String prodName) {
        showLoading(false);

        View view = getLayoutInflater().inflate(R.layout.dialog_custom_alert, null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
        TextView tvMessage = view.findViewById(R.id.tv_dialog_message);
        Button btnNeg = view.findViewById(R.id.btn_dialog_negative);
        Button btnPos = view.findViewById(R.id.btn_dialog_positive);

        int count = IngredientParser.parse(ocrText).size();
        tvTitle.setText("Ingredients Detected");
        tvMessage.setText("Found " + count + " ingredients:\n\n" + preview + "\n\nDoes this look correct?");

        btnNeg.setText("Edit Manually");
        btnPos.setText("Yes, Analyze");

        btnNeg.setOnClickListener(v -> {
            dialog.dismiss();
            switchToMode("manual");
            etIngredients.setText(ocrText);
        });

        btnPos.setOnClickListener(v -> {
            dialog.dismiss();
            showLoading(true);
            setStatus("Analyzing…");

            if (geminiClient.isApiKeyConfigured()) {
                setStatus("AI cleaning text…");
                geminiClient.cleanOcrText(ocrText, new GeminiApiClient.AiCallback() {
                    @Override public void onSuccess(String cleanText) {
                        if ("ERROR".equals(cleanText)) {
                            processIngredientText(ocrText, prodName, "camera");
                        } else {
                            processIngredientText(cleanText, prodName, "camera");
                        }
                    }
                    @Override public void onError(String error) {
                        processIngredientText(ocrText, prodName, "camera");
                    }
                });
            } else {
                processIngredientText(ocrText, prodName, "camera");
            }
        });

        dialog.show();
    }

    private String buildPreviewString(List<String> ingredients) {
        int max = Math.min(ingredients.size(), 8);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < max; i++) {
            sb.append("• ").append(ingredients.get(i)).append("\n");
        }
        if (ingredients.size() > max) {
            sb.append("… and ").append(ingredients.size() - max).append(" more");
        }
        return sb.toString();
    }

    private void showOcrFailDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_custom_alert, null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
        TextView tvMessage = view.findViewById(R.id.tv_dialog_message);
        Button btnNeg = view.findViewById(R.id.btn_dialog_negative);
        Button btnPos = view.findViewById(R.id.btn_dialog_positive);

        tvTitle.setText("No Text Found");
        tvMessage.setText("We couldn't detect any ingredient text.\n\n• Hold the camera VERY close to the label\n• Make sure it's well-lit and in focus\n• Frame ONLY the ingredient list");

        btnNeg.setText("Manual Input");
        btnPos.setText("Try Again");

        btnNeg.setOnClickListener(v -> {
            dialog.dismiss();
            switchToMode("manual");
        });
        btnPos.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showOcrReviewDialog(String text) {
        View view = getLayoutInflater().inflate(R.layout.dialog_custom_alert, null);
        AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
        TextView tvMessage = view.findViewById(R.id.tv_dialog_message);
        Button btnNeg = view.findViewById(R.id.btn_dialog_negative);
        Button btnPos = view.findViewById(R.id.btn_dialog_positive);

        tvTitle.setText("Review Detected Text");
        tvMessage.setText("We detected text — does this look like an ingredient list?\n\n" + text);

        btnNeg.setText("Edit Manually");
        btnPos.setText("Analyze");

        btnNeg.setOnClickListener(v -> {
            dialog.dismiss();
            switchToMode("manual");
            etIngredients.setText(text);
        });
        btnPos.setOnClickListener(v -> {
            dialog.dismiss();
            String prodName = etProductName.getText().toString().trim();
            if (prodName.isEmpty()) prodName = "Unknown Product";
            processIngredientText(text, prodName, "camera");
        });

        dialog.show();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MANUAL
    // ═══════════════════════════════════════════════════════════════════════════

    private void analyzeManualInput() {
        String productName     = etProductName.getText().toString().trim();
        String ingredientsText = etIngredients.getText().toString().trim();
        if (TextUtils.isEmpty(ingredientsText)) {
            etIngredients.setError("Please enter ingredient text");
            return;
        }
        if (productName.isEmpty()) productName = "Unknown Product";
        String cleaned = OcrTextCleaner.clean(ingredientsText);
        processIngredientText(cleaned.isEmpty() ? ingredientsText : cleaned, productName, "manual");
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // VOICE
    // ═══════════════════════════════════════════════════════════════════════════

    private void startVoiceInput() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_PERMISSION_CODE);
            return;
        }
        if (!VoiceInputHelper.isAvailable(this)) {
            Toast.makeText(this, "Voice recognition not available on this device.",
                    Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, "No voice input detected. Tap the mic and speak.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String prodName = etProductName.getText().toString().trim();
        if (prodName.isEmpty()) prodName = "Unknown Product";
        final String finalProdName = prodName;

        showLoading(true);

        if (geminiClient.isApiKeyConfigured()) {
            geminiClient.formatVoiceInput(capturedVoiceText, new GeminiApiClient.AiCallback() {
                @Override public void onSuccess(String cleanText) {
                    processIngredientText(cleanText, finalProdName, "voice");
                }
                @Override public void onError(String error) {
                    List<String> parsed = IngredientParser.parseVoiceInput(capturedVoiceText);
                    processIngredientText(IngredientParser.formatList(parsed), finalProdName, "voice");
                }
            });
        } else {
            List<String> parsed = IngredientParser.parseVoiceInput(capturedVoiceText);
            processIngredientText(IngredientParser.formatList(parsed), finalProdName, "voice");
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // CORE ANALYSIS
    // ═══════════════════════════════════════════════════════════════════════════

    private void processIngredientText(String rawText, String productName, String method) {
        showLoading(true);
        setStatus("Checking database…");
        String tempText = OcrTextCleaner.clean(rawText);
        final String finalText = tempText.isEmpty() ? rawText : tempText;

        List<String> ingredientNames = IngredientParser.parse(finalText);
        if (ingredientNames.isEmpty()) {
            showLoading(false);
            Toast.makeText(this,
                    "No ingredients could be parsed.\nMake sure they are separated by commas.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        IngredientDatabase ingDb = IngredientDatabase.getInstance();

        List<String> missingIngredients = new ArrayList<>();
        for (String name : ingredientNames) {
            if (!ingDb.ingredientExists(name)) {
                missingIngredients.add(name);
            }
        }

        if (missingIngredients.isEmpty() || !geminiClient.isApiKeyConfigured()) {
            classifyAndFinish(ingredientNames, productName, method, finalText);
            return;
        }

        setStatus("Researching " + missingIngredients.size() + " unknown ingredients…");

        geminiClient.fetchMissingIngredientsData(missingIngredients, new GeminiApiClient.AiCallback() {
            @Override
            public void onSuccess(String jsonResult) {
                try {
                    jsonResult = jsonResult.replace("```json", "").replace("```", "").trim();
                    org.json.JSONArray newItems = new org.json.JSONArray(jsonResult);

                    for (int i = 0; i < newItems.length(); i++) {
                        org.json.JSONObject item = newItems.getJSONObject(i);

                        Ingredient.SafetyLevel level;
                        try {
                            level = Ingredient.SafetyLevel.valueOf(item.optString("safety_level", "SAFE").toUpperCase());
                        } catch (Exception e) {
                            level = Ingredient.SafetyLevel.SAFE;
                        }

                        Ingredient newIng = new Ingredient(
                                item.optString("name", "Unknown"),
                                level,
                                item.optString("category", "Unknown"),
                                item.optString("description", "No description available."),
                                item.optString("risk_note", "None")
                        );

                        if (level == Ingredient.SafetyLevel.HARMFUL) newIng.setHazardScore(8);
                        else if (level == Ingredient.SafetyLevel.CAUTION) newIng.setHazardScore(4);
                        else newIng.setHazardScore(1);

                        newIng.setComedogenicRating(item.optInt("comedogenic_rating", 0));
                        newIng.setAllergenInfo("None");

                        ingDb.insertIngredient(newIng);
                    }

                    classifyAndFinish(ingredientNames, productName, method, finalText);

                } catch (org.json.JSONException e) {
                    Log.e(TAG, "Failed to parse Gemini JSON: " + e.getMessage());
                    classifyAndFinish(ingredientNames, productName, method, finalText);
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Gemini fetch error: " + error);
                classifyAndFinish(ingredientNames, productName, method, finalText);
            }
        });
    }

    /** * Helper method to finish the classification process
     * AFTER the database has been updated by Gemini.
     */
    private void classifyAndFinish(List<String> ingredientNames, String productName, String method, String originalText) {
        setStatus("Classifying ingredients…");
        User user = dbHelper.getUserById(session.getUserId());
        List<String> skinTypes    = user != null ? user.getSkinTypes()    : new ArrayList<>();
        List<String> skinConcerns = user != null ? user.getSkinConcerns() : new ArrayList<>();

        List<Ingredient> classified = classifier.classifyWithSkinProfile(ingredientNames, skinTypes, skinConcerns);

        ScanResult result = new ScanResult(productName, originalText, method);
        result.setUserId(session.getUserId());
        result.setIngredients(classified);

        List<String> concernNames = new ArrayList<>();
        for (Ingredient ing : classified) {
            if (ing.getSafetyLevel() != Ingredient.SafetyLevel.SAFE)
                concernNames.add(ing.getName());
        }

        if (geminiClient.isApiKeyConfigured()) {
            setStatus("Generating AI insight…");
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

    // ═══════════════════════════════════════════════════════════════════════════
    // UI HELPERS
    // ═══════════════════════════════════════════════════════════════════════════

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnCapture.setEnabled(!show);
        btnAnalyzeManual.setEnabled(!show);
        btnVoiceStart.setEnabled(!show);
        if (!show) setStatus("");
    }

    private void setStatus(String message) {
        if (tvScanStatus != null) {
            tvScanStatus.setText(message);
            tvScanStatus.setVisibility(message.isEmpty() ? View.GONE : View.VISIBLE);
        }
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
            tvName.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

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
                divider.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1));
                divider.setBackgroundColor(android.graphics.Color.parseColor("#E0E0E0"));
                llVoiceIngredients.addView(divider);
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PERMISSIONS
    // ═══════════════════════════════════════════════════════════════════════════

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startCamera();
            else {
                Toast.makeText(this, "Camera permission is required to scan products.",
                        Toast.LENGTH_LONG).show();
                switchToMode("manual");
            }
        } else if (requestCode == AUDIO_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startVoiceInput();
            else
                Toast.makeText(this, "Microphone permission is required for voice input.",
                        Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (voiceInputHelper != null) voiceInputHelper.destroy();
    }
}
