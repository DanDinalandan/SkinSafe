package com.example.skinsafe;

import java.util.ArrayList;
import java.util.List;

public class SafetyClassifier {

    private final IngredientDatabase db;

    public SafetyClassifier() {
        this.db = IngredientDatabase.getInstance();
    }
    public List<Ingredient> classifyWithSkinProfile(List<String> ingredientNames,
                                                    List<String> userSkinTypes,
                                                    List<String> userConcerns) {
        List<Ingredient> classified = new ArrayList<>();

        for (String rawName : ingredientNames) {
            if (rawName == null || rawName.trim().isEmpty()) continue;

            String name = rawName.trim();
            Ingredient masterIng = db.lookup(name);
            Ingredient ing;

            if (masterIng == null) {
                ing = db.getUnknownIngredient(name);
            } else {
                ing = new Ingredient(masterIng.getName(), masterIng.getSafetyLevel(),
                        masterIng.getCategory(), masterIng.getDescription(), masterIng.getRiskNote());
                ing.setHazardScore(masterIng.getHazardScore());
                ing.setComedogenicRating(masterIng.getComedogenicRating());
                ing.setAllergenInfo(masterIng.getAllergenInfo());
            }

            String n = ing.getNormalizedName();

            if (userSkinTypes != null && userSkinTypes.contains("Acne-prone")) {
                if (ing.getComedogenicRating() >= 3) {
                    ing.setFlagged(true);
                    ing.setSafetyLevel(Ingredient.SafetyLevel.HARMFUL);
                    ing.setRiskNote("High pore-clogging potential for acne-prone skin.");
                }
            }

            if (userConcerns != null && userConcerns.contains("No Fragrance")) {
                if (ing.getCategory().equalsIgnoreCase("Fragrance") ||
                        ing.getAllergenInfo().toLowerCase().contains("fragrance") ||
                        n.contains("parfum") || n.contains("limonene") || n.contains("linalool")) {
                    ing.setFlagged(true);
                    ing.setSafetyLevel(Ingredient.SafetyLevel.CAUTION);
                    if(ing.getRiskNote() == null || ing.getRiskNote().isEmpty()) {
                        ing.setRiskNote("Contains fragrance components.");
                    }
                }
            }

            if (userSkinTypes != null && userSkinTypes.contains("Sensitive")) {
                if (ing.getHazardScore() >= 4 || n.contains("alcohol denat")) {
                    ing.setFlagged(true);
                    if (ing.getSafetyLevel() == Ingredient.SafetyLevel.SAFE) {
                        ing.setSafetyLevel(Ingredient.SafetyLevel.CAUTION);
                        ing.setRiskNote("May be too harsh for sensitive skin.");
                    }
                }
            }

            if (ing.getHazardScore() >= 8) {
                ing.setSafetyLevel(Ingredient.SafetyLevel.HARMFUL);
            } else if (ing.getHazardScore() >= 5 && ing.getSafetyLevel() == Ingredient.SafetyLevel.SAFE) {
                ing.setSafetyLevel(Ingredient.SafetyLevel.CAUTION);
            }

            classified.add(ing);
        }
        return classified;
    }

    public Ingredient lookupSingle(String name) {
        Ingredient found = db.lookup(name);
        return found != null ? found : db.getUnknownIngredient(name);
    }
}