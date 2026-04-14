package com.example.skinsafe;

import java.util.ArrayList;
import java.util.List;

public class IngredientParser {

    /**
     * Parse a raw ingredient string into a clean list.
     * Handles: comma-separated, newline-separated, slash-separated lists.
     * Also handles common OCR artifacts.
     */
    public static List<String> parse(String rawText) {
        List<String> ingredients = new ArrayList<>();
        if (rawText == null || rawText.trim().isEmpty()) return ingredients;

        // Normalize: remove "Ingredients:" prefix if present
        String text = rawText.replaceAll("(?i)^(ingredients|ingr[e]?dients?)[:\\s]*", "").trim();

        // Replace newlines and semicolons with commas
        text = text.replace("\n", ",").replace(";", ",").replace(" / ", ",");

        // Split by comma
        String[] parts = text.split(",");
        for (String part : parts) {
            String cleaned = cleanIngredient(part);
            if (!cleaned.isEmpty() && cleaned.length() > 1) {
                ingredients.add(cleaned);
            }
        }
        return ingredients;
    }

    /**
     * Clean a single ingredient string.
     * Removes: asterisks, parenthetical notes like "(may contain)", percentages, numbers.
     */
    private static String cleanIngredient(String raw) {
        String cleaned = raw.trim();
        // Remove percentage and concentrations like "2%", "0.5%"
        cleaned = cleaned.replaceAll("\\d+(\\.\\d+)?%", "").trim();
        // Remove asterisks and plus signs
        cleaned = cleaned.replaceAll("[*+†‡]", "").trim();
        // Remove parenthetical notes
        cleaned = cleaned.replaceAll("\\(.*?\\)", "").trim();
        // Remove leading numbers like "1." or "2)"
        cleaned = cleaned.replaceAll("^\\d+[.)]\\s*", "").trim();
        // Remove trailing punctuation
        cleaned = cleaned.replaceAll("[.:]$", "").trim();
        // Collapse multiple spaces
        cleaned = cleaned.replaceAll("\\s+", " ").trim();
        return cleaned;
    }

    /**
     * Parse voice input: typically a sentence like "water, glycerin, niacinamide".
     * Voice input often includes filler words or punctuation.
     */
    public static List<String> parseVoiceInput(String spokenText) {
        if (spokenText == null || spokenText.trim().isEmpty()) return new ArrayList<>();
        // Remove common fillers
        String text = spokenText
                .replaceAll("(?i)\\b(and|also|then|plus|with|the|a|an)\\b", ",")
                .replaceAll("\\s+", " ").trim();
        return parse(text);
    }

    /**
     * Format a list of ingredient names back into a readable string.
     */
    public static String formatList(List<String> ingredients) {
        return String.join(", ", ingredients);
    }
}
