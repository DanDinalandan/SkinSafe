package com.example.skinsafe;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.List;

public class OcrUtils {

    private static final String TAG = "OcrUtils";

    public interface OcrCallback {
        void onSuccess(String extractedText);
        void onFailure(String error);
    }

    public static void extractTextFromBitmap(Bitmap bitmap, OcrCallback callback) {
        if (bitmap == null) {
            callback.onFailure("No image provided");
            return;
        }

        // --- CROP LOGIC: Target the center where the UI frame is ---
        Bitmap croppedBitmap = cropToScannerFrame(bitmap);

        InputImage image = InputImage.fromBitmap(croppedBitmap, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(visionText -> {
                    Log.d(TAG, "OCR full text from cropped area:\n" + visionText.getText());
                    String extracted = extractIngredientSection(visionText);
                    Log.d(TAG, "OCR extracted section:\n" + extracted);
                    callback.onSuccess(extracted);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "OCR failed", e);
                    callback.onFailure("Text recognition failed: " + e.getMessage());
                });
    }

    /**
     * Crops the high-resolution camera bitmap to the center area,
     * mimicking the green scanner frame overlay in the UI.
     */
    private static Bitmap cropToScannerFrame(Bitmap original) {
        int width = original.getWidth();
        int height = original.getHeight();

        // The UI frame is roughly 75% of the screen width and 40% of the height
        int cropWidth = (int) (width * 0.75);
        int cropHeight = (int) (height * 0.40);

        int startX = (width - cropWidth) / 2;
        int startY = (height - cropHeight) / 2;

        // Safety bounds check
        if (startX < 0) startX = 0;
        if (startY < 0) startY = 0;
        if (startX + cropWidth > width) cropWidth = width - startX;
        if (startY + cropHeight > height) cropHeight = height - startY;

        return Bitmap.createBitmap(original, startX, startY, cropWidth, cropHeight);
    }

    private static String extractIngredientSection(Text visionText) {
        String[] startMarkers = {
                "ingredients:", "ingredients :", "ingredient list:", "ingr:",
                "ingredients", "active ingredients", "inactive ingredients",
                "full ingredients", "all ingredients"
        };

        String[] stopMarkers = {
                "directions", "how to use", "usage", "warnings", "caution",
                "net wt", "net weight", "manufactured by", "distributed by",
                "made in", "exp.", "expiry", "best before", "mfg", "lot no",
                "keep out", "for external", "store in", "upc", "barcode",
                "for best results", "storage"
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
                    if (isStopLine(t, stopMarkers)) return cleanText(sb.toString());
                    if (!t.isEmpty()) { if (sb.length() > 0) sb.append(", "); sb.append(t); }
                }

                for (int rbi = bi + 1; rbi < blocks.size(); rbi++) {
                    for (Text.Line line : blocks.get(rbi).getLines()) {
                        String t = line.getText().trim();
                        if (isStopLine(t, stopMarkers)) return cleanText(sb.toString());
                        if (!t.isEmpty()) { if (sb.length() > 0) sb.append(", "); sb.append(t); }
                    }
                }

                return cleanText(sb.toString());
            }
        }

        Log.d(TAG, "No ingredient marker found — using comma-density heuristic.");
        String richest = findCommaRichestBlock(blocks);
        if (richest != null && richest.split(",").length >= 3) {
            return cleanText(richest);
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

    private static String cleanText(String raw) {
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
        return avgWords <= 4;
    }
}