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
    private static final String API_KEY = "";

    private static final String BASE_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/" +
                    "gemini-3.1-flash-lite-preview:generateContent?key=";

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    /** Cleans messy OCR text and extracts only valid ingredients */
    public void cleanOcrText(String messyText, AiCallback callback) {
        String prompt = "You are an expert cosmetic chemist and data parser. "
                + "I will provide raw, typo-filled OCR text from a skincare label. "
                + "Your ONLY job is to extract the actual cosmetic ingredients, fix the spelling mistakes, and return them as a clean, comma-separated list.\n\n"
                + "STRICT RULES:\n"
                + "1. DESTROY all garbage text: remove instructions, expiry dates, barcodes, temperatures (e.g., 'STORE BELOW 35°C', '4\"806535\"23', 'natural bte', 'FOR EXPIRY DATE').\n"
                + "2. FIX OCR typos: correct 'giycerin' to 'Glycerin', 'calcİum glucongte' to 'Calcium Gluconate', 'potassiura soroag' to 'Potassium Sorbate', 'tocopber' to 'Tocopherol', etc.\n"
                + "3. SEPARATE merged words intelligently (e.g., 'glyceryi cGprylatecetearytacons' -> 'Glyceryl Caprylate, Cetearyl Alcohol').\n"
                + "4. Output ONLY the final comma-separated list. Absolutely no intro, no outro, no markdown.\n\n"
                + "OCR Text:\n" + messyText;

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

    public void fetchMissingIngredientsData(List<String> missingIngredients, AiCallback callback) {
        String names = String.join(", ", missingIngredients);

        String prompt = "You are a cosmetic chemistry database. I need the safety profiles for the following ingredients: " + names + "\n\n"
                + "Respond ONLY with a valid JSON array of objects. Do not include markdown, do not include intro text. Just the raw JSON.\n\n"
                + "Format EXACTLY like this example:\n"
                + "[\n"
                + "  {\n"
                + "    \"name\": \"Ingredient Name\",\n"
                + "    \"category\": \"Humectant / Preservative / Surfactant etc\",\n"
                + "    \"description\": \"A brief 1-sentence description of what it does.\",\n"
                + "    \"safety_level\": \"SAFE\" (must be exactly SAFE, CAUTION, or HARMFUL),\n"
                + "    \"risk_note\": \"Clogs pores / Skin irritant / None\",\n"
                + "    \"comedogenic_rating\": 0 (integer 0-5)\n"
                + "  }\n"
                + "]";

        callGemini(prompt, callback);
    }

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
                ? " The user's skin profile is: " + String.join(", ", skinTypes) + "." : "";

        String prompt = "You are an expert cosmetic dermatologist. Provide a detailed analysis for the skincare ingredient '" + ingredientName + "'."
                + skinCtx
                + "\n\nFormat your response EXACTLY following this HTML template. Do NOT use markdown. Use these exact tags:\n\n"
                + "<b><font color='#1A1A1A'>VERDICT:</font></b> (One sentence summary: Safe, Use with Caution, or Avoid. Add a brief reason.)<br><br>"
                + "<b><font color='#1A1A1A'>WHAT IT DOES:</font></b><br>(Explain its chemical function in 1-2 simple sentences)<br><br>"
                + "<b><font color='#1A1A1A'>HOW IT AFFECTS YOUR SKIN:</font></b><br>(Explain exactly what it will do to the user's specific skin profile)<br><br>"
                + "<b><font color='#1A1A1A'>CAUTIONS & RISKS:</font></b><br>(Mention any pore-clogging risks, sun sensitivities, or allergies)";

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
        return !API_KEY.equals("YOUR_API_KEY_HERE") && !API_KEY.trim().isEmpty();
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
                JSONObject part = new JSONObject().put("text", prompt);
                JSONArray parts = new JSONArray().put(part);
                JSONObject contentObj = new JSONObject().put("parts", parts);
                JSONArray contents = new JSONArray().put(contentObj);
                JSONObject body = new JSONObject().put("contents", contents);

                JSONObject genConfig = new JSONObject();
                genConfig.put("temperature", 0.1);
                body.put("generationConfig", genConfig);

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
                        if (response.code() == 429) {
                            mainHandler.post(() -> callback.onError("Whoa there! AI speed limit reached. Please wait 60 seconds and try again."));
                            return;
                        }

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