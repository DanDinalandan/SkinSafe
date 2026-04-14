package com.example.skinsafe;

public class Ingredient {

    public enum SafetyLevel {
        SAFE,
        CAUTION,
        HARMFUL
    }

    private String name;
    private String normalizedName;
    private SafetyLevel safetyLevel;
    private String category;
    private String description;
    private String riskNote;
    private boolean isFlagged;
    private String aiExplanation;

    // --- NEW FIELDS FOR SCALABILITY & RATINGS ---
    private int hazardScore;
    private int comedogenicRating;
    private String allergenInfo;

    public Ingredient(String name, SafetyLevel safetyLevel, String category,
                      String description, String riskNote) {
        this.name = name;
        this.normalizedName = name.trim().toLowerCase();
        this.safetyLevel = safetyLevel;
        this.category = category;
        this.description = description;
        this.riskNote = riskNote;
        this.isFlagged = false;

        this.hazardScore = 1;
        this.comedogenicRating = 0;
        this.allergenInfo = "None";
    }

    public String getName() { return name; }
    public String getNormalizedName() { return normalizedName; }
    public SafetyLevel getSafetyLevel() { return safetyLevel; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getRiskNote() { return riskNote; }
    public boolean isFlagged() { return isFlagged; }
    public String getAiExplanation() { return aiExplanation; }

    public int getHazardScore() { return hazardScore; }
    public int getComedogenicRating() { return comedogenicRating; }
    public String getAllergenInfo() { return allergenInfo; }

    public void setName(String name) { this.name = name; this.normalizedName = name.trim().toLowerCase(); }
    public void setSafetyLevel(SafetyLevel safetyLevel) { this.safetyLevel = safetyLevel; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
    public void setRiskNote(String riskNote) { this.riskNote = riskNote; }
    public void setFlagged(boolean flagged) { this.isFlagged = flagged; }
    public void setAiExplanation(String explanation) { this.aiExplanation = explanation; }

    public void setHazardScore(int hazardScore) { this.hazardScore = hazardScore; }
    public void setComedogenicRating(int comedogenicRating) { this.comedogenicRating = comedogenicRating; }
    public void setAllergenInfo(String allergenInfo) { this.allergenInfo = allergenInfo; }

    public int getSafetyColorRes() {
        switch (safetyLevel) {
            case SAFE: return android.R.color.holo_green_dark;
            case CAUTION: return android.R.color.holo_orange_light;
            case HARMFUL: return android.R.color.holo_red_light;
            default: return android.R.color.darker_gray;
        }
    }

    public String getSafetyLabel() {
        switch (safetyLevel) {
            case SAFE: return "SAFE";
            case CAUTION: return "CAUTION";
            case HARMFUL: return "HARMFUL";
            default: return "UNKNOWN";
        }
    }
}