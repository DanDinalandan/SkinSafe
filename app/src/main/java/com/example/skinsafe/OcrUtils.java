package com.example.skinsafe;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.List;

public class OcrUtils {

    private static final String TAG = "OcrUtils";

    private static final int MIN_WIDTH_PX = 1920;

    private static final float CONTRAST_BOOST = 1.5f;

    public interface OcrCallback {
        void onSuccess(String extractedText);
        void onFailure(String error);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // PUBLIC ENTRY POINT
    // ──────────────────────────────────────────────────────────────────────────

    public static void extractTextFromBitmap(Bitmap bitmap, OcrCallback callback) {
        if (bitmap == null) {
            callback.onFailure("No image provided");
            return;
        }

        Bitmap cropped = cropToScannerFrame(bitmap);

        Bitmap upscaled = upscaleIfNeeded(cropped);

        Bitmap enhanced = enhanceForOcr(upscaled);

        InputImage image = InputImage.fromBitmap(enhanced, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(visionText -> {
                    Log.d(TAG, "OCR raw output:\n" + visionText.getText());
                    String extracted = extractIngredientSection(visionText);
                    Log.d(TAG, "OCR extracted section:\n" + extracted);

                    String cleaned = OcrTextCleaner.clean(extracted);
                    Log.d(TAG, "OCR after cleaning:\n" + cleaned);

                    callback.onSuccess(cleaned);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "OCR failed", e);
                    callback.onFailure("Text recognition failed: " + e.getMessage());
                });
    }

    // ──────────────────────────────────────────────────────────────────────────
    // IMAGE PREPROCESSING
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Crops the bitmap to the center rectangle that the green scanner frame covers.
     * The frame is roughly 85% of width and 45% of height — adjusted from the old
     * 75%×40% because ingredient labels often extend close to the frame edges.
     */
    private static Bitmap cropToScannerFrame(Bitmap original) {
        int w = original.getWidth();
        int h = original.getHeight();

        int cropW = (int) (w * 0.85f);
        int cropH = (int) (h * 0.45f);
        int startX = (w - cropW) / 2;
        int startY = (h - cropH) / 2;

        startX = Math.max(0, startX);
        startY = Math.max(0, startY);
        if (startX + cropW > w) cropW = w - startX;
        if (startY + cropH > h) cropH = h - startY;

        return Bitmap.createBitmap(original, startX, startY, cropW, cropH);
    }

    /**
     * Upscales the bitmap so its width is at least MIN_WIDTH_PX.
     * This is critical for small ingredient-label text — ML Kit struggles badly
     * with anything smaller than ~40px character height.
     */
    private static Bitmap upscaleIfNeeded(Bitmap src) {
        if (src.getWidth() >= MIN_WIDTH_PX) return src;

        float scale = (float) MIN_WIDTH_PX / src.getWidth();
        int newW = MIN_WIDTH_PX;
        int newH = Math.round(src.getHeight() * scale);

        Matrix m = new Matrix();
        m.setScale(scale, scale);

        Bitmap scaled = Bitmap.createBitmap(newW, newH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(scaled);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(src, m, paint);
        return scaled;
    }

    /**
     * Two-pass enhancement:
     * Pass 1 — grayscale + contrast boost (makes light text on light backgrounds visible)
     * Pass 2 — 3×3 sharpening convolution (makes character edges crisp for ML Kit)
     */
    private static Bitmap enhanceForOcr(Bitmap src) {
        Bitmap contrasted = applyContrastBoost(src);

        return applySharpen(contrasted);
    }

    private static Bitmap applyContrastBoost(Bitmap src) {
        Bitmap out = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out);
        Paint paint = new Paint();

        float[] grayscale = {
                0.299f, 0.587f, 0.114f, 0, 0,
                0.299f, 0.587f, 0.114f, 0, 0,
                0.299f, 0.587f, 0.114f, 0, 0,
                0,      0,      0,      1, 0
        };

        float c = CONTRAST_BOOST;
        float t = (1f - c) * 128f;
        float[] contrast = {
                c, 0, 0, 0, t,
                0, c, 0, 0, t,
                0, 0, c, 0, t,
                0, 0, 0, 1, 0
        };

        ColorMatrix cm = new ColorMatrix(grayscale);
        ColorMatrix contrastCm = new ColorMatrix(contrast);
        cm.postConcat(contrastCm);

        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(src, 0, 0, paint);
        return out;
    }

    /**
     * Sharpening kernel (3×3 unsharp mask style):
     *  0  -1   0
     * -1   5  -1
     *  0  -1   0
     * Applied manually via pixel array for reliability across all API levels.
     */
    private static Bitmap applySharpen(Bitmap src) {
        int w = src.getWidth();
        int h = src.getHeight();
        int[] pixels = new int[w * h];
        src.getPixels(pixels, 0, w, 0, 0, w, h);

        int[] output = new int[w * h];

        for (int y = 1; y < h - 1; y++) {
            for (int x = 1; x < w - 1; x++) {
                int idx = y * w + x;

                int center = pixels[idx];
                int top    = pixels[(y - 1) * w + x];
                int bottom = pixels[(y + 1) * w + x];
                int left   = pixels[y * w + (x - 1)];
                int right  = pixels[y * w + (x + 1)];

                int r = clamp(5 * r(center) - r(top) - r(bottom) - r(left) - r(right));
                int g = clamp(5 * g(center) - g(top) - g(bottom) - g(left) - g(right));
                int b = clamp(5 * b(center) - b(top) - b(bottom) - b(left) - b(right));

                output[idx] = Color.rgb(r, g, b);
            }
        }

        for (int x = 0; x < w; x++) { output[x] = pixels[x]; output[(h-1)*w+x] = pixels[(h-1)*w+x]; }
        for (int y = 0; y < h; y++) { output[y*w] = pixels[y*w]; output[y*w+w-1] = pixels[y*w+w-1]; }

        Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        result.setPixels(output, 0, w, 0, 0, w, h);
        return result;
    }

    private static int r(int px) { return (px >> 16) & 0xFF; }
    private static int g(int px) { return (px >>  8) & 0xFF; }
    private static int b(int px) { return  px        & 0xFF; }
    private static int clamp(int v) { return Math.max(0, Math.min(255, v)); }

    // ──────────────────────────────────────────────────────────────────────────
    // TEXT EXTRACTION
    // ──────────────────────────────────────────────────────────────────────────

    private static String extractIngredientSection(Text visionText) {
        String[] startMarkers = {
                "ingredients:", "ingredients :", "ingredient list:", "ingr:",
                "ingredients", "active ingredients:", "inactive ingredients:",
                "full ingredients:", "all ingredients:", "содержит", "inci:"
        };

        String[] stopMarkers = {
                "directions", "how to use", "usage", "warnings", "caution", "keep out", "for external", "for best results",
                "manufactured by", "distributed by", "made in", "imported by", "gandang kalikasan", "inc.", "ltd", "excellence ave",
                "net wt", "net weight", "exp.", "expiry", "for expiry", "best before", "mfg", "lot no", "upc", "barcode", "stamp",
                "store in", "storage", "store below", "away from", "direct sunlight",
                "crueltyfree", "cruelty free", "vegan", "naturally derived", "natural", "recyclable", "ldpe"
        };

        List<Text.TextBlock> blocks = visionText.getTextBlocks();

        for (int bi = 0; bi < blocks.size(); bi++) {
            List<Text.Line> lines = blocks.get(bi).getLines();

            for (int li = 0; li < lines.size(); li++) {
                String lineText  = lines.get(li).getText();
                String lineLower = lineText.toLowerCase().trim();

                String matched = null;
                for (String marker : startMarkers) {
                    if (lineLower.equals(marker) || lineLower.startsWith(marker)) {
                        matched = marker;
                        break;
                    }
                }

                if (matched == null) continue;

                Log.d(TAG, "Ingredient marker found: '" + matched + "'");
                StringBuilder sb = new StringBuilder();

                int afterMarker = lineLower.indexOf(matched) + matched.length();
                if (afterMarker < lineText.length()) {
                    String rest = lineText.substring(afterMarker).trim();
                    if (!rest.isEmpty()) sb.append(rest);
                }

                for (int rli = li + 1; rli < lines.size(); rli++) {
                    String t = lines.get(rli).getText().trim();
                    if (isStopLine(t, stopMarkers)) return cleanRawText(sb.toString());
                    if (!t.isEmpty()) { if (sb.length() > 0) sb.append(", "); sb.append(t); }
                }

                for (int rbi = bi + 1; rbi < blocks.size(); rbi++) {
                    for (Text.Line line : blocks.get(rbi).getLines()) {
                        String t = line.getText().trim();
                        if (isStopLine(t, stopMarkers)) return cleanRawText(sb.toString());
                        if (!t.isEmpty()) { if (sb.length() > 0) sb.append(", "); sb.append(t); }
                    }
                }

                return cleanRawText(sb.toString());
            }
        }

        Log.d(TAG, "No ingredient marker found — using comma-density heuristic.");
        String richest = findCommaRichestBlock(blocks);
        if (richest != null && richest.split(",").length >= 3) {
            return cleanRawText(richest);
        }

        return visionText.getText();
    }

    private static boolean isStopLine(String line, String[] stops) {
        String lower = line.toLowerCase().trim();
        for (String stop : stops) if (lower.startsWith(stop)) return true;
        return false;
    }

    private static String findCommaRichestBlock(List<Text.TextBlock> blocks) {
        String best = null;
        int bestCommas = 0;
        for (Text.TextBlock block : blocks) {
            StringBuilder sb = new StringBuilder();
            for (Text.Line line : block.getLines()) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(line.getText().trim());
            }
            String text = sb.toString();
            int commas = (int) text.chars().filter(c -> c == ',').count();
            if (commas > bestCommas) { bestCommas = commas; best = text; }
        }
        return best;
    }

    private static String cleanRawText(String raw) {
        if (raw == null || raw.trim().isEmpty()) return "";
        return raw
                .replaceAll("\r\n|\r|\n", ", ")
                .replaceAll(",\\s*,+", ",")
                .replaceAll("\\s{2,}", " ")
                .replaceAll("^[,\\s]+", "")
                .replaceAll("[,\\s]+$", "")
                .trim();
    }

    public static boolean looksLikeIngredientList(String text) {
        if (text == null || text.length() < 10) return false;
        String[] segments = text.split(",");
        if (segments.length < 3) return false;
        int totalWords = 0;
        for (String seg : segments) totalWords += seg.trim().split("\\s+").length;
        double avgWords = (double) totalWords / segments.length;
        return avgWords <= 5;
    }
}
