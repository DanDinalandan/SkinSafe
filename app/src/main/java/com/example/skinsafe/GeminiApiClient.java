package com.example.skinsafe;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeminiApiClient {

    private static final String TAG = "GeminiApiClient";

    // Replace with your key from https://aistudio.google.com (free)
    private static final String API_KEY = "AIzaSyD08KlhGyijFe4KcgMu9Z4iMEva69Y8-mA";

    private static final String BASE_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/" +
                    "gemini-2.0-flash-lite:generateContent?key=";

    private final OkHttpClient client = new OkHttpClient();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    /** Cleans messy OCR text and extracts only valid ingredients */
    public void cleanOcrText(String messyText, AiCallback callback) {
        String prompt = "You are a data extraction tool. Extract ONLY the cosmetic ingredient names from this messy OCR text. Fix any obvious spelling errors. Return a clean, comma-separated list of ingredients. Do not include marketing text, directions, or the product name. If no ingredients exist, return 'ERROR'. Text: " + messyText;
        callGemini(prompt, callback);
    }

    /** Formats spoken voice input into precise chemical names */
    public void formatVoiceInput(String voiceText, AiCallback callback) {
        String prompt = "Format this spoken text into a clean, comma-separated list of cosmetic ingredients. Remove filler words like 'and', 'uh', 'also'. Fix the spelling of chemical names. Return ONLY the comma-separated list. Text: " + voiceText;
        callGemini(prompt, callback);
    }

    public interface AiCallback {
        void onSuccess(String result);
        void onError(String error);
    }

    // ─────────────────────────────────────────────────────────────
    // PUBLIC API
    // ─────────────────────────────────────────────────────────────

    /** Generates an overall AI insight for a scanned product. */
    public void generateProductInsight(String productName,
                                       List<String> ingredients,
                                       List<String> harmfulNames,
                                       List<String> skinTypes,
                                       AiCallback callback) {
        callGemini(buildProductInsightPrompt(productName, ingredients, harmfulNames, skinTypes),
                callback);
    }

    /** Generates a 2-3 sentence explanation for a single ingredient. */
    public void explainIngredient(String ingredientName,
                                  List<String> skinTypes,
                                  AiCallback callback) {
        String skinCtx = (skinTypes != null && !skinTypes.isEmpty())
                ? " The user has " + String.join(", ", skinTypes) + " skin." : "";
        String prompt = "Explain the skincare ingredient '" + ingredientName + "' in 2-3 simple "
                + "sentences. Cover what it does, who should be careful, and whether it is "
                + "generally safe." + skinCtx + " Keep it friendly and non-technical. No markdown.";
        callGemini(prompt, callback);
    }
    public void testConnection(AiCallback callback) {
        Log.d(TAG, "testConnection() called. API key configured=" + isApiKeyConfigured());
        callGemini("Reply with exactly: SKINSAFE_OK", new AiCallback() {
            @Override public void onSuccess(String result) {
                Log.d(TAG, "✅ Gemini API test PASSED. Response: " + result);
                callback.onSuccess("Gemini API is working. Response: " + result);
            }
            @Override public void onError(String error) {
                Log.e(TAG, "❌ Gemini API test FAILED. Error: " + error);
                callback.onError("Gemini API test failed: " + error);
            }
        });
    }

    /** True when a real API key has been set (not the placeholder). */
    public boolean isApiKeyConfigured() {
        return !API_KEY.equals("YOUR_GEMINI_API_KEY_HERE") && !API_KEY.trim().isEmpty();
    }

    // ─────────────────────────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────────────────────────

    private String buildProductInsightPrompt(String productName, List<String> ingredients,
                                             List<String> harmful, List<String> skinTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append("You are a skincare safety expert. Analyze this product:\n");
        sb.append("Product: ").append(productName != null ? productName : "Unknown Product").append("\n");
        sb.append("Ingredients: ").append(String.join(", ", ingredients)).append("\n");
        if (harmful != null && !harmful.isEmpty())
            sb.append("Flagged ingredients: ").append(String.join(", ", harmful)).append("\n");
        if (skinTypes != null && !skinTypes.isEmpty())
            sb.append("User skin type: ").append(String.join(", ", skinTypes)).append("\n");
        sb.append("\nWrite a brief 2-3 sentence AI insight about this product's safety. ");
        sb.append("Mention the most concerning ingredient if any. Be clear and friendly. No markdown.");
        return sb.toString();
    }

    private void callGemini(String prompt, AiCallback callback) {
        if (!isApiKeyConfigured()) {
            Log.w(TAG, "callGemini() skipped — API key not configured.");
            mainHandler.post(() -> callback.onError("API key not configured."));
            return;
        }

        executor.execute(() -> {
            String url = BASE_URL + API_KEY;
            Log.d(TAG, "Calling Gemini: " + url.substring(0, Math.min(80, url.length())) + "…");
            Log.d(TAG, "Prompt (first 200 chars): "
                    + prompt.substring(0, Math.min(200, prompt.length())));

            try {
                // Build request body
                JSONObject part = new JSONObject().put("text", prompt);
                JSONArray parts = new JSONArray().put(part);
                JSONObject contentObj = new JSONObject().put("parts", parts);
                JSONArray contents = new JSONArray().put(contentObj);
                JSONObject body = new JSONObject().put("contents", contents);

                // Safety settings
                JSONArray safety = new JSONArray();
                for (String cat : new String[]{
                        "HARM_CATEGORY_HARASSMENT", "HARM_CATEGORY_HATE_SPEECH",
                        "HARM_CATEGORY_SEXUALLY_EXPLICIT", "HARM_CATEGORY_DANGEROUS_CONTENT"}) {
                    safety.put(new JSONObject()
                            .put("category", cat)
                            .put("threshold", "BLOCK_MEDIUM_AND_ABOVE"));
                }
                body.put("safetySettings", safety);

                Request request = new Request.Builder()
                        .url(url)
                        .post(RequestBody.create(body.toString(),
                                MediaType.parse("application/json")))
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    String responseBody = response.body() != null
                            ? response.body().string() : "";

                    Log.d(TAG, "HTTP " + response.code() + " response body (first 400): "
                            + responseBody.substring(0, Math.min(400, responseBody.length())));

                    if (!response.isSuccessful()) {
                        String msg = "HTTP " + response.code() + ": " + responseBody;
                        Log.e(TAG, "Gemini API error: " + msg);
                        mainHandler.post(() -> callback.onError(msg));
                        return;
                    }

                    String text = extractText(responseBody);
                    Log.d(TAG, "Gemini parsed text: " + text);
                    mainHandler.post(() -> callback.onSuccess(text));
                }

            } catch (JSONException | IOException e) {
                Log.e(TAG, "Gemini network/parse exception", e);
                mainHandler.post(() -> callback.onError("Exception: " + e.getMessage()));
            }
        });
    }

    private String extractText(String json) throws JSONException {
        JSONObject root = new JSONObject(json);
        JSONArray candidates = root.optJSONArray("candidates");
        if (candidates == null || candidates.length() == 0)
            return "AI analysis is temporarily unavailable.";
        JSONObject content = candidates.getJSONObject(0).optJSONObject("content");
        if (content == null) return "AI analysis is temporarily unavailable.";
        JSONArray parts = content.optJSONArray("parts");
        if (parts == null || parts.length() == 0) return "AI analysis is temporarily unavailable.";
        return parts.getJSONObject(0).optString("text", "AI analysis is temporarily unavailable.");
    }
}