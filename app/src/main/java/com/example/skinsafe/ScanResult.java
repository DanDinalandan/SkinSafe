package com.example.skinsafe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScanResult {
    private int id;
    private int userId;
    private String productName;
    private String rawIngredientText;
    private List<Ingredient> ingredients;
    private String scanDate;
    private String inputMethod;
    private String aiInsight;
    private boolean isSaved;
    private boolean isFavorite;
    private int safeCount;
    private int cautionCount;
    private int harmfulCount;
    private String overallSafetyLabel;

    public ScanResult() {
        ingredients = new ArrayList<>();
        scanDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        isSaved = false;
        isFavorite = false;
    }

    public ScanResult(String productName, String rawIngredientText, String inputMethod) {
        this();
        this.productName = productName;
        this.rawIngredientText = rawIngredientText;
        this.inputMethod = inputMethod;
    }

    public int getSafeCount() { return safeCount; }
    public void setSafeCount(int safeCount) { this.safeCount = safeCount; }

    public int getCautionCount() { return cautionCount; }
    public void setCautionCount(int cautionCount) { this.cautionCount = cautionCount; }

    public int getHarmfulCount() { return harmfulCount; }
    public void setHarmfulCount(int harmfulCount) { this.harmfulCount = harmfulCount; }

    public String getOverallSafetyLabel() { return overallSafetyLabel; }
    public void setOverallSafetyLabel(String overallSafetyLabel) { this.overallSafetyLabel = overallSafetyLabel; }

    public int getTotalCount() {
        return safeCount + cautionCount + harmfulCount;
    }

    public String getSummaryLine() {
        return getTotalCount() + " ingredients analyzed · " +
                getCautionCount() + " concern" + (getCautionCount() != 1 ? "s" : "") + " found";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getRawIngredientText() { return rawIngredientText; }
    public void setRawIngredientText(String t) { this.rawIngredientText = t; }

    public List<Ingredient> getIngredients() { return ingredients; }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;

        int safe = 0, caution = 0, harmful = 0;
        for (Ingredient ing : ingredients) {
            if (ing.getSafetyLevel() == Ingredient.SafetyLevel.HARMFUL) harmful++;
            else if (ing.getSafetyLevel() == Ingredient.SafetyLevel.CAUTION) caution++;
            else safe++;
        }

        this.safeCount = safe;
        this.cautionCount = caution;
        this.harmfulCount = harmful;

        if (harmful > 0) {
            this.overallSafetyLabel = "HARMFUL";
        } else if (caution > 0) {
            this.overallSafetyLabel = "CAUTION";
        } else {
            this.overallSafetyLabel = "SAFE";
        }
    }

    public void addIngredient(Ingredient ingredient) { this.ingredients.add(ingredient); }

    public String getScanDate() { return scanDate; }
    public void setScanDate(String scanDate) { this.scanDate = scanDate; }

    public String getInputMethod() { return inputMethod; }
    public void setInputMethod(String inputMethod) { this.inputMethod = inputMethod; }

    public String getAiInsight() { return aiInsight; }
    public void setAiInsight(String aiInsight) { this.aiInsight = aiInsight; }

    public boolean isSaved() { return isSaved; }
    public void setSaved(boolean saved) { isSaved = saved; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}