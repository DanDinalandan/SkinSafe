package com.example.skinsafe;

import com.example.skinsafe.Ingredient.SafetyLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngredientDatabase {

    private static IngredientDatabase instance;
    private final Map<String, Ingredient> database = new HashMap<>();

    public static synchronized IngredientDatabase getInstance() {
        if (instance == null) {
            instance = new IngredientDatabase();
            instance.populate();
        }
        return instance;
    }

    public List<String> getAllIngredientNames() {
        return new ArrayList<>(database.keySet());
    }

    private void populate() {
        // ===================== SAFE INGREDIENTS =====================
        // ═══════════════════════════════════════════════════════════════════
        //  SOLVENTS & BASE INGREDIENTS
        // ═══════════════════════════════════════════════════════════════════

        add("water", SafetyLevel.SAFE, "Solvent",
                "Universal solvent and carrier for other ingredients.",
                "No known risks. Universally safe.");
        add("aqua", SafetyLevel.SAFE, "Solvent",
                "Water (INCI name). Universal solvent base.",
                "No known risks.");
        add("deionized water", SafetyLevel.SAFE, "Solvent",
                "Purified water free of minerals used as a formula base.",
                "No known risks.");
        add("demineral water", SafetyLevel.SAFE, "Solvent",
                "Demineralized water used as a base.",
                "No known risks.");
        add("demineralized water", SafetyLevel.SAFE, "Solvent",
                "Purified water base for formulations.",
                "No known risks.");

        // ═══════════════════════════════════════════════════════════════════
        //  HUMECTANTS & MOISTURIZERS
        // ═══════════════════════════════════════════════════════════════════

        add("glycerin", SafetyLevel.SAFE, "Humectant",
                "Draws moisture into the skin. Safe and gentle.",
                "Safe for all skin types.");
        add("glycerol", SafetyLevel.SAFE, "Humectant",
                "Humectant that attracts water to the skin.",
                "Safe for all skin types.");
        add("glycerine", SafetyLevel.SAFE, "Humectant",
                "Plant-derived humectant that retains moisture.",
                "Safe for all skin types.");
        add("vegetable glycerin", SafetyLevel.SAFE, "Humectant",
                "Plant-sourced glycerin that moisturizes and softens.",
                "Safe for all skin types.");
        add("hyaluronic acid", SafetyLevel.SAFE, "Humectant",
                "Hydration booster. Can hold 1000x its weight in water.",
                "Well-tolerated. No known risks.");
        add("sodium hyaluronate", SafetyLevel.SAFE, "Humectant",
                "Smaller form of hyaluronic acid, penetrates deeper.",
                "Safe and well-tolerated.");
        add("hydrolyzed hyaluronic acid", SafetyLevel.SAFE, "Humectant",
                "Fragmented hyaluronic acid for deep penetration.",
                "Safe. Suitable for all skin types.");
        add("sodium hyaluronate crosspolymer", SafetyLevel.SAFE, "Humectant",
                "Cross-linked form that stays on the skin surface longer.",
                "Safe and well-tolerated.");
        add("panthenol", SafetyLevel.SAFE, "Conditioning Agent",
                "Pro-vitamin B5. Soothing and moisturizing.",
                "Safe for all skin types.");
        add("sodium pca", SafetyLevel.SAFE, "Humectant",
                "Natural moisturizing factor that attracts water.",
                "Safe for all skin types.");
        add("betaine", SafetyLevel.SAFE, "Humectant",
                "Sugar beet–derived humectant. Soothing and hydrating.",
                "Safe for all skin types.");
        add("sorbitol", SafetyLevel.SAFE, "Humectant",
                "Sugar-derived humectant that retains moisture.",
                "Safe for all skin types.");
        add("urea", SafetyLevel.SAFE, "Humectant",
                "Naturally occurring humectant. Also gently exfoliates at higher concentrations.",
                "Safe. Can be mildly exfoliating at >10%.");
        add("carbamide", SafetyLevel.SAFE, "Humectant",
                "Another name for urea. Moisturizing and skin softening.",
                "Safe at cosmetic concentrations.");
        add("xylitol", SafetyLevel.SAFE, "Humectant",
                "Sugar alcohol humectant that moisturizes and conditions.",
                "Safe for all skin types.");
        add("xylitylglucoside", SafetyLevel.SAFE, "Humectant",
                "Provides hydration and skin barrier protection.",
                "Safe for all skin types.");
        add("anhydroxylitol", SafetyLevel.SAFE, "Humectant",
                "Moisturizing agent that balances skin hydration.",
                "Safe for all skin types.");
        add("trehalose", SafetyLevel.SAFE, "Humectant",
                "Sugar-based moisturizer that protects skin barrier.",
                "Safe for all skin types.");
        add("fructose", SafetyLevel.SAFE, "Humectant",
                "Natural sugar that moisturizes and conditions.",
                "Safe for all skin types.");
        add("sucrose", SafetyLevel.SAFE, "Humectant",
                "Common sugar used as a humectant.",
                "Safe for all skin types.");
        add("propanediol", SafetyLevel.SAFE, "Humectant",
                "Naturally derived (corn) moisturizing agent. Milder than propylene glycol.",
                "Safe for most skin types.");
        add("dipropylene glycol", SafetyLevel.SAFE, "Solvent",
                "Mild solvent that helps dissolve other ingredients.",
                "Safe. Low irritation potential.");
        add("pentylene glycol", SafetyLevel.SAFE, "Humectant",
                "Moisturizing agent with mild antimicrobial properties.",
                "Generally safe. Mild preservative boost.");
        add("butylene glycol", SafetyLevel.SAFE, "Humectant",
                "Gentler alternative to propylene glycol. Draws moisture.",
                "Generally safe. Mild irritation possible.");
        add("methyl gluceth-20", SafetyLevel.SAFE, "Humectant",
                "Glucose-derived humectant that hydrates and softens.",
                "Safe for all skin types.");
        add("hydroxyethyl urea", SafetyLevel.SAFE, "Humectant",
                "Moisturizing agent that also enhances skin texture.",
                "Safe for most skin types.");
        add("saccharide isomerate", SafetyLevel.SAFE, "Humectant",
                "Long-lasting humectant that binds to skin proteins.",
                "Safe for all skin types.");
        add("glycogen", SafetyLevel.SAFE, "Humectant",
                "Retains moisture and boosts skin energy.",
                "Safe. Suitable for dry and aging skin.");
        add("ectoin", SafetyLevel.SAFE, "Humectant",
                "Protects against environmental stress and hydrates.",
                "Safe. Excellent for sensitive or stressed skin.");
        add("inulin", SafetyLevel.SAFE, "Humectant",
                "Plant-based prebiotic that maintains skin hydration.",
                "Safe for all skin types.");
        add("pullulan", SafetyLevel.SAFE, "Film Former",
                "Natural polysaccharide that tightens and provides texture.",
                "Safe for all skin types.");
        add("methylpropanediol", SafetyLevel.SAFE, "Humectant",
                "Enhances skin hydration and ingredient penetration.",
                "Safe for all skin types.");
        add("isopentyldiol", SafetyLevel.SAFE, "Humectant",
                "Skin conditioning and moisturizing agent.",
                "Safe for all skin types.");

        // ═══════════════════════════════════════════════════════════════════
        //  EMOLLIENTS & OILS
        // ═══════════════════════════════════════════════════════════════════

        add("squalane", SafetyLevel.SAFE, "Emollient",
                "Plant-derived moisturizer that mimics skin's natural oils.",
                "Safe and non-comedogenic.");
        add("jojoba oil", SafetyLevel.SAFE, "Emollient",
                "Balancing plant oil similar to skin's natural sebum.",
                "Safe. Non-comedogenic.");
        add("simmondsia chinensis seed oil", SafetyLevel.SAFE, "Emollient",
                "Jojoba oil (INCI name). Moisturizes and balances.",
                "Safe for all skin types.");
        add("simmondsia chinensis (jojoba) seed oil", SafetyLevel.SAFE, "Emollient",
                "Jojoba oil. Moisturizing and balancing.",
                "Safe for all skin types.");
        add("shea butter", SafetyLevel.SAFE, "Emollient",
                "Rich, nourishing butter for dry skin.",
                "Safe. Mildly comedogenic at high concentrations.");
        add("butyrospermum parkii butter", SafetyLevel.SAFE, "Emollient",
                "Shea butter (INCI name). Nourishes and moisturizes.",
                "Safe for dry and sensitive skin.");
        add("butyrospermum parkii (shea) butter", SafetyLevel.SAFE, "Emollient",
                "Shea butter. Deep moisturizing and soothing.",
                "Safe for dry and sensitive skin.");
        add("caprylic/capric triglyceride", SafetyLevel.SAFE, "Emollient",
                "Derived from coconut oil. Lightweight moisturizer.",
                "Safe for all skin types.");
        add("caprylic/capric triglycerides", SafetyLevel.SAFE, "Emollient",
                "Coconut-derived lightweight emollient.",
                "Safe for all skin types.");
        add("rosehip oil", SafetyLevel.SAFE, "Emollient",
                "Rich in vitamins A and C. Anti-aging and brightening.",
                "Safe for most. Patch test recommended for oily skin.");
        add("rosa canina seed oil", SafetyLevel.SAFE, "Emollient",
                "Rosehip oil. Anti-aging and skin-regenerating.",
                "Safe. Great for dry and mature skin.");
        add("rosa canina fruit oil", SafetyLevel.SAFE, "Emollient",
                "Rosehip oil. Nourishing and brightening.",
                "Safe for dry and mature skin.");
        add("argan oil", SafetyLevel.SAFE, "Emollient",
                "Rich in vitamin E and fatty acids. Nourishing and anti-aging.",
                "Safe. Non-comedogenic for most.");
        add("argania spinosa kernel oil", SafetyLevel.SAFE, "Emollient",
                "Argan oil (INCI). Nourishing, moisturizing, antioxidant.",
                "Safe for all skin types.");
        add("helianthus annuus (sunflower) seed oil", SafetyLevel.SAFE, "Emollient",
                "Lightweight, non-comedogenic plant oil.",
                "Safe for all skin types.");
        add("sunflower oil", SafetyLevel.SAFE, "Emollient",
                "Lightweight oil rich in linoleic acid.",
                "Safe and non-comedogenic.");
        add("cocos nucifera (coconut) oil", SafetyLevel.SAFE, "Emollient",
                "Moisturizing and antimicrobial plant oil.",
                "Comedogenic for acne-prone skin.");
        add("coconut oil", SafetyLevel.SAFE, "Emollient",
                "Moisturizing but comedogenic plant oil.",
                "May clog pores. Avoid if acne-prone.");
        add("olea europaea fruit oil", SafetyLevel.SAFE, "Emollient",
                "Olive oil. Nourishing and antioxidant-rich.",
                "Safe. Can feel heavy on oily skin.");
        add("olive oil", SafetyLevel.SAFE, "Emollient",
                "Rich nourishing oil. May feel heavy.",
                "Good for dry skin. May not suit oily skin.");
        add("persea gratissima (avocado) oil", SafetyLevel.SAFE, "Emollient",
                "Avocado oil. Rich in fatty acids and vitamins.",
                "Safe. Great for dry and mature skin.");
        add("avocado oil", SafetyLevel.SAFE, "Emollient",
                "Nourishing oil rich in vitamins A, D, and E.",
                "Safe for dry and mature skin.");
        add("vitis vinifera (grape) seed oil", SafetyLevel.SAFE, "Emollient",
                "Lightweight antioxidant oil.",
                "Safe and non-comedogenic.");
        add("camellia sinensis seed oil", SafetyLevel.SAFE, "Emollient",
                "Green tea seed oil. Antioxidant and moisturizing.",
                "Safe for all skin types.");
        add("macadamia ternifolia seed oil", SafetyLevel.SAFE, "Emollient",
                "Nourishing oil with a fatty acid profile similar to skin sebum.",
                "Safe for dry and sensitive skin.");
        add("macadamia integrifolia seed oil", SafetyLevel.SAFE, "Emollient",
                "Macadamia oil. Moisturizing and skin softening.",
                "Safe for dry and sensitive skin.");
        add("limnanthes alba (meadowfoam) seed oil", SafetyLevel.SAFE, "Emollient",
                "Stable, moisturizing oil rich in fatty acids.",
                "Safe for dry and sensitive skin.");
        add("moringa oleifera seed oil", SafetyLevel.SAFE, "Emollient",
                "Anti-aging, antioxidant moisturizing oil.",
                "Safe for all skin types.");
        add("simmondsia chinensis (jojoba) seed extract", SafetyLevel.SAFE, "Emollient",
                "Jojoba extract. Moisturizing and soothing.",
                "Safe for all skin types.");
        add("rice bran oil", SafetyLevel.SAFE, "Emollient",
                "Oryza sativa bran oil. Moisturizing and brightening.",
                "Safe for all skin types.");
        add("oryza sativa bran oil", SafetyLevel.SAFE, "Emollient",
                "Rice bran oil. Antioxidant and moisturizing.",
                "Safe for all skin types.");
        add("evening primrose oil", SafetyLevel.SAFE, "Emollient",
                "Anti-inflammatory oil rich in GLA.",
                "Safe for dry and sensitive skin.");
        add("oenothera biennis oil", SafetyLevel.SAFE, "Emollient",
                "Evening primrose oil. Anti-inflammatory.",
                "Safe for dry and sensitive skin.");
        add("dimethicone", SafetyLevel.SAFE, "Silicone",
                "Creates smooth, protective barrier on skin surface.",
                "Safe for most. May be comedogenic for acne-prone skin.");
        add("cyclopentasiloxane", SafetyLevel.SAFE, "Silicone",
                "Light silicone for smooth texture and spreadability.",
                "Safe for most. Possible bioaccumulation concern.");
        add("cyclohexasiloxane", SafetyLevel.SAFE, "Silicone",
                "Volatile silicone providing smooth feel.",
                "Safe for most skin types.");
        add("cyclomethicone", SafetyLevel.SAFE, "Silicone",
                "Blend of cyclic silicones for smooth texture.",
                "Safe for most skin types.");
        add("dimethiconol", SafetyLevel.SAFE, "Silicone",
                "Silicone that conditions and smooths skin.",
                "Safe for most skin types.");
        add("mineral oil", SafetyLevel.CAUTION, "Emollient",
                "Petroleum-derived occlusive moisturizer.",
                "Comedogenic for some. Safe in cosmetic-grade refined form.");
        add("petrolatum", SafetyLevel.SAFE, "Emollient",
                "White petroleum jelly. Creates moisture barrier.",
                "Safe. Non-comedogenic. Great for dry/sensitive skin.");
        add("paraffinum liquidum", SafetyLevel.CAUTION, "Emollient",
                "Liquid mineral oil. Occlusive moisturizer.",
                "Safe in purified form. May clog pores.");
        add("isododecane", SafetyLevel.SAFE, "Emollient",
                "Lightweight silky emollient.",
                "Safe. Gives a smooth, non-greasy finish.");
        add("isohexadecane", SafetyLevel.SAFE, "Emollient",
                "Lightweight emollient that improves texture.",
                "Safe for all skin types.");
        add("ethylhexyl palmitate", SafetyLevel.SAFE, "Emollient",
                "Ester that softens skin and enhances texture.",
                "Safe for most skin types.");
        add("isopropyl palmitate", SafetyLevel.CAUTION, "Emollient",
                "Lightweight emollient and penetration enhancer.",
                "Comedogenic. May clog pores for acne-prone skin.");
        add("isopropyl myristate", SafetyLevel.CAUTION, "Emollient",
                "Lightweight emollient and penetration enhancer.",
                "Comedogenic. May clog pores for acne-prone skin.");

        // ═══════════════════════════════════════════════════════════════════
        //  VITAMINS & ANTIOXIDANTS
        // ═══════════════════════════════════════════════════════════════════

        add("niacinamide", SafetyLevel.SAFE, "Vitamin B3 Derivative",
                "Brightening, pore-minimizing, anti-inflammatory.",
                "Widely safe. Rare sensitivity at very high concentrations.");
        add("vitamin c", SafetyLevel.SAFE, "Antioxidant",
                "Brightening antioxidant. Boosts collagen production.",
                "Can cause tingling. Always use sunscreen when applying.");
        add("ascorbic acid", SafetyLevel.SAFE, "Antioxidant",
                "Pure Vitamin C. Brightening and antioxidant.",
                "Unstable. May cause mild irritation at high concentrations.");
        add("l-ascorbic acid", SafetyLevel.SAFE, "Antioxidant",
                "Active form of Vitamin C. Brightening and collagen-boosting.",
                "May tingle on sensitive skin. Use SPF.");
        add("sodium ascorbyl phosphate", SafetyLevel.SAFE, "Antioxidant",
                "Stable Vitamin C derivative. Brightening and protective.",
                "Safe and stable form of Vitamin C.");
        add("ascorbyl glucoside", SafetyLevel.SAFE, "Antioxidant",
                "Stable Vitamin C derivative. Brightens and protects.",
                "Safe for all skin types.");
        add("ascorbyl tetraisopalmitate", SafetyLevel.SAFE, "Antioxidant",
                "Oil-soluble stable Vitamin C. Penetrates deeply.",
                "Safe for all skin types.");
        add("3-o-ethyl ascorbic acid", SafetyLevel.SAFE, "Antioxidant",
                "Stable Vitamin C ether. Brightening and antioxidant.",
                "Safe. One of the most stable Vitamin C forms.");
        add("tetrahexyldecyl ascorbate", SafetyLevel.SAFE, "Antioxidant",
                "Oil-soluble Vitamin C. Penetrates deeply.",
                "Safe and well-tolerated.");
        add("ascorbyl palmitate", SafetyLevel.SAFE, "Antioxidant",
                "Fat-soluble Vitamin C ester. Antioxidant protection.",
                "Safe. May be less potent than pure L-ascorbic acid.");
        add("tocopherol", SafetyLevel.SAFE, "Antioxidant",
                "Vitamin E. Protects skin from oxidative damage.",
                "Generally safe. Rare sensitivity.");
        add("vitamin e", SafetyLevel.SAFE, "Antioxidant",
                "Antioxidant protecting against free radicals.",
                "Safe for most. Comedogenic at high concentrations.");
        add("tocopheryl acetate", SafetyLevel.SAFE, "Antioxidant",
                "Vitamin E ester. Stable antioxidant form.",
                "Safe for all skin types.");
        add("retinol", SafetyLevel.CAUTION, "Retinoid",
                "Vitamin A derivative. Promotes cell turnover.",
                "May cause dryness and peeling. Avoid during pregnancy. Always use SPF.");
        add("retinyl palmitate", SafetyLevel.CAUTION, "Retinoid",
                "Ester form of Vitamin A. Milder than retinol.",
                "May increase sun sensitivity. Use SPF daily.");
        add("retinal", SafetyLevel.CAUTION, "Retinoid",
                "Retinaldehyde — stronger than retinol, milder than retinoic acid.",
                "Start slowly. May cause irritation. Use SPF.");
        add("ferulic acid", SafetyLevel.SAFE, "Antioxidant",
                "Plant antioxidant. Boosts efficacy of vitamins C and E.",
                "Safe for all skin types.");
        add("resveratrol", SafetyLevel.SAFE, "Antioxidant",
                "Polyphenol antioxidant with anti-aging properties.",
                "Safe for all skin types.");
        add("coenzyme q10", SafetyLevel.SAFE, "Antioxidant",
                "Ubiquinone. Protects against aging and oxidative stress.",
                "Safe for all skin types.");
        add("ubiquinone", SafetyLevel.SAFE, "Antioxidant",
                "Coenzyme Q10. Anti-aging and antioxidant.",
                "Safe for all skin types.");
        add("bakuchiol", SafetyLevel.SAFE, "Anti-Aging",
                "Plant-based retinol alternative. Gentler with similar benefits.",
                "Safe. Great option during pregnancy instead of retinol.");
        add("caffeine", SafetyLevel.SAFE, "Active Ingredient",
                "Reduces puffiness and dark circles. Anti-inflammatory.",
                "Safe for all skin types.");
        add("adenosine", SafetyLevel.SAFE, "Anti-Aging",
                "Anti-wrinkle active. Relaxes expression lines.",
                "Safe for all skin types.");

        // ═══════════════════════════════════════════════════════════════════
        //  CERAMIDES & SKIN BARRIER
        // ═══════════════════════════════════════════════════════════════════

        add("ceramide np", SafetyLevel.SAFE, "Skin-Identical Lipid",
                "Restores skin barrier function.",
                "Safe for all skin types.");
        add("ceramide ap", SafetyLevel.SAFE, "Skin-Identical Lipid",
                "Supports the skin's protective barrier.",
                "Safe for all skin types.");
        add("ceramide eop", SafetyLevel.SAFE, "Skin-Identical Lipid",
                "Essential fatty acid ceramide for barrier repair.",
                "Safe for all skin types.");
        add("ceramide 3", SafetyLevel.SAFE, "Skin-Identical Lipid",
                "Restores and repairs skin barrier.",
                "Safe for dry and sensitive skin.");
        add("ceramide 6 ii", SafetyLevel.SAFE, "Skin-Identical Lipid",
                "Skin barrier repair and moisturizing lipid.",
                "Safe for dry and sensitive skin.");
        add("ceramide 1", SafetyLevel.SAFE, "Skin-Identical Lipid",
                "Long-chain ceramide critical for barrier integrity.",
                "Safe for dry and sensitive skin.");
        add("cholesterol", SafetyLevel.SAFE, "Skin-Identical Lipid",
                "Supports skin barrier and moisturization.",
                "Safe for dry and sensitive skin.");
        add("phytosphingosine", SafetyLevel.SAFE, "Skin-Identical Lipid",
                "Soothing lipid that supports barrier repair.",
                "Safe for sensitive and oily skin.");
        add("sphingolipids", SafetyLevel.SAFE, "Skin-Identical Lipid",
                "Natural lipids critical for maintaining the skin barrier.",
                "Safe for all skin types.");
        add("lecithin", SafetyLevel.SAFE, "Emollient",
                "Natural phospholipid emollient and emulsifier.",
                "Safe for all skin types.");
        add("hydrogenated lecithin", SafetyLevel.SAFE, "Emollient",
                "Stabilized lecithin. Skin barrier support.",
                "Safe for all skin types.");

        // ═══════════════════════════════════════════════════════════════════
        //  BOTANICAL EXTRACTS — SAFE
        // ═══════════════════════════════════════════════════════════════════

        add("centella asiatica extract", SafetyLevel.SAFE, "Botanical Extract",
                "Soothing, anti-inflammatory, wound-healing.",
                "Safe. Rare contact allergy possible.");
        add("centella asiatica leaf extract", SafetyLevel.SAFE, "Botanical Extract",
                "Healing and soothing plant extract.",
                "Safe for sensitive and dry skin.");
        add("aloe vera", SafetyLevel.SAFE, "Botanical Extract",
                "Soothing and moisturizing plant extract.",
                "Safe for most. Rare allergy.");
        add("aloe barbadensis leaf juice", SafetyLevel.SAFE, "Botanical Extract",
                "Aloe vera gel. Soothing and hydrating.",
                "Safe for most skin types.");
        add("aloe barbadensis leaf extract", SafetyLevel.SAFE, "Botanical Extract",
                "Aloe extract. Calms and moisturizes.",
                "Safe for sensitive skin.");
        add("allantoin", SafetyLevel.SAFE, "Soothing Agent",
                "Calms irritation and promotes skin healing.",
                "Safe for all skin types.");
        add("zinc pca", SafetyLevel.SAFE, "Sebum Regulator",
                "Controls oil production. Anti-bacterial.",
                "Safe for oily and acne-prone skin.");
        add("green tea extract", SafetyLevel.SAFE, "Antioxidant",
                "Potent antioxidant and anti-inflammatory.",
                "Safe. Rare sensitivity.");
        add("camellia sinensis leaf extract", SafetyLevel.SAFE, "Antioxidant",
                "Green tea extract. Rich in antioxidants.",
                "Safe for most skin types.");
        add("titanium dioxide", SafetyLevel.SAFE, "Physical Sunscreen",
                "Mineral UV filter. Sits on top of skin to block sun.",
                "Safe for most. Non-irritating.");
        add("zinc oxide", SafetyLevel.SAFE, "Physical Sunscreen",
                "Broad-spectrum mineral sun protection.",
                "Safe, especially for sensitive skin.");
        add("bisabolol", SafetyLevel.SAFE, "Soothing Agent",
                "Plant-derived anti-inflammatory that calms skin.",
                "Safe for all skin types.");
        add("allantoin", SafetyLevel.SAFE, "Soothing Agent",
                "Promotes skin healing and soothes irritation.",
                "Safe for all skin types.");
        add("dipotassium glycyrrhizate", SafetyLevel.SAFE, "Soothing Agent",
                "Licorice-derived anti-inflammatory.",
                "Safe. Ideal for sensitive and redness-prone skin.");
        add("glycyrrhiza glabra root extract", SafetyLevel.SAFE, "Botanical Extract",
                "Licorice root. Brightening and anti-inflammatory.",
                "Safe for all skin types.");
        add("glycyrrhiza glabra (licorice) root extract", SafetyLevel.SAFE, "Botanical Extract",
                "Licorice root extract. Brightens and soothes.",
                "Safe for all skin types.");
        add("licorice root extract", SafetyLevel.SAFE, "Botanical Extract",
                "Brightening, soothing anti-inflammatory.",
                "Safe for sensitive and uneven skin tone.");
        add("niacinamide", SafetyLevel.SAFE, "Vitamin B3 Derivative",
                "Brightening, pore-minimizing, and barrier-strengthening.",
                "Safe for all skin types.");
        add("chamomilla recutita (matricaria) flower extract", SafetyLevel.SAFE, "Botanical Extract",
                "Chamomile. Soothes and calms irritated skin.",
                "Safe for sensitive skin.");
        add("chamomilla recutita flower extract", SafetyLevel.SAFE, "Botanical Extract",
                "Chamomile extract. Anti-inflammatory and soothing.",
                "Safe for sensitive skin.");
        add("calendula officinalis flower extract", SafetyLevel.SAFE, "Botanical Extract",
                "Calendula. Healing, anti-inflammatory, soothing.",
                "Safe for sensitive and dry skin.");
        add("camellia sinensis (green tea) leaf extract", SafetyLevel.SAFE, "Antioxidant",
                "Green tea polyphenols. Antioxidant and anti-inflammatory.",
                "Safe for all skin types.");
        add("camellia sinensis (white tea) leaf extract", SafetyLevel.SAFE, "Antioxidant",
                "White tea. Antioxidant and anti-aging.",
                "Safe for all skin types.");
        add("aspalathus linearis (rooibos) leaf extract", SafetyLevel.SAFE, "Antioxidant",
                "Rooibos. Antioxidant and soothing.",
                "Safe for sensitive skin.");
        add("alpha-arbutin", SafetyLevel.SAFE, "Active Ingredient",
                "Skin brightener that inhibits melanin production.",
                "Safe for all skin types.");
        add("arbutin", SafetyLevel.SAFE, "Active Ingredient",
                "Brightening agent. Reduces hyperpigmentation.",
                "Safe for most skin types.");
        add("azelaic acid", SafetyLevel.SAFE, "Active Ingredient",
                "Anti-acne and brightening. Anti-inflammatory.",
                "Safe for sensitive and acne-prone skin.");
        add("alpha arbutin", SafetyLevel.SAFE, "Active Ingredient",
                "Brightening active that blocks melanin synthesis.",
                "Safe for all skin types.");
        add("tranexamic acid", SafetyLevel.SAFE, "Active Ingredient",
                "Skin brightening and anti-inflammatory.",
                "Safe for all skin types.");
        add("kojic acid dipalmitate", SafetyLevel.SAFE, "Active Ingredient",
                "Stable kojic acid form. Skin brightening.",
                "Safe. More stable than pure kojic acid.");
        add("madecassoside", SafetyLevel.SAFE, "Botanical Extract",
                "Centella asiatica active. Anti-inflammatory and healing.",
                "Safe for sensitive and dry skin.");
        add("asiaticoside", SafetyLevel.SAFE, "Botanical Extract",
                "Centella asiatica active. Soothing and healing.",
                "Safe for sensitive and dry skin.");
        add("rosmarinus officinalis leaf extract", SafetyLevel.SAFE, "Antioxidant",
                "Rosemary leaf. Antioxidant and mildly antimicrobial.",
                "Safe. May be irritating at very high concentrations.");
        add("ginkgo biloba leaf extract", SafetyLevel.SAFE, "Antioxidant",
                "Antioxidant that improves circulation.",
                "Safe for all skin types.");
        add("panax ginseng root extract", SafetyLevel.SAFE, "Botanical Extract",
                "Revitalizes and energizes the skin.",
                "Safe for all skin types.");
        add("punica granatum (pomegranate) extract", SafetyLevel.SAFE, "Antioxidant",
                "Pomegranate. Antioxidant, anti-aging, brightening.",
                "Safe for all skin types.");
        add("vaccinium myrtillus (blueberry) fruit extract", SafetyLevel.SAFE, "Antioxidant",
                "Blueberry antioxidants that protect and brighten.",
                "Safe for all skin types.");
        add("terminalia ferdinandiana (kakadu plum) fruit extract", SafetyLevel.SAFE, "Antioxidant",
                "Richest known source of Vitamin C. Brightening.",
                "Safe for all skin types.");
        add("cucumis sativus (cucumber) fruit extract", SafetyLevel.SAFE, "Botanical Extract",
                "Cooling, soothing, and hydrating.",
                "Safe for sensitive and dry skin.");
        add("hypericum perforatum flower extract", SafetyLevel.SAFE, "Botanical Extract",
                "St. John's Wort. Soothing and anti-inflammatory.",
                "Safe for sensitive skin.");
        add("sambucus nigra flower extract", SafetyLevel.SAFE, "Antioxidant",
                "Elderflower. Antioxidant and soothing.",
                "Safe for all skin types.");
        add("portulaca oleracea extract", SafetyLevel.SAFE, "Botanical Extract",
                "Green purslane. Antioxidant and soothing.",
                "Safe for sensitive and dry skin.");
        add("boswellia serrata extract", SafetyLevel.SAFE, "Botanical Extract",
                "Reduces inflammation and improves skin elasticity.",
                "Safe for sensitive skin.");
        add("hamamelis virginiana water", SafetyLevel.CAUTION, "Botanical Extract",
                "Witch hazel. Astringent and anti-inflammatory.",
                "Can be drying. Avoid on very dry or sensitive skin.");
        add("hamamelis virginiana leaf extract", SafetyLevel.CAUTION, "Botanical Extract",
                "Witch hazel extract. Astringent and toning.",
                "Potentially drying for sensitive skin.");
        add("saccharomyces cerevisiae extract", SafetyLevel.SAFE, "Ferment",
                "Yeast extract. Antioxidant and barrier-repairing.",
                "Safe for all skin types.");
        add("lactobacillus ferment", SafetyLevel.SAFE, "Ferment",
                "Probiotic ferment. Balances skin microbiome.",
                "Safe for sensitive and acne-prone skin.");
        add("madecassoside", SafetyLevel.SAFE, "Active Ingredient",
                "Anti-inflammatory skin healing active.",
                "Safe for sensitive skin.");

        // ═══════════════════════════════════════════════════════════════════
        //  EXFOLIANTS (AHAs & BHAs)
        // ═══════════════════════════════════════════════════════════════════

        add("lactic acid", SafetyLevel.SAFE, "AHA Exfoliant",
                "Gentle alpha-hydroxy acid that exfoliates dead skin cells.",
                "Safe at low concentrations. Can cause sun sensitivity.");
        add("glycolic acid", SafetyLevel.CAUTION, "AHA Exfoliant",
                "Smallest AHA — highly effective exfoliant.",
                "Can cause irritation. Use SPF and start low.");
        add("salicylic acid", SafetyLevel.CAUTION, "BHA Exfoliant",
                "Beta-hydroxy acid. Unclogs pores. Good for acne.",
                "Can be drying. Avoid in pregnancy at high concentrations.");
        add("citric acid", SafetyLevel.SAFE, "pH Adjuster / AHA",
                "Adjusts pH and mildly exfoliates.",
                "Safe at low levels. Higher concentrations may irritate.");
        add("malic acid", SafetyLevel.SAFE, "AHA Exfoliant",
                "Mild AHA from apples. Exfoliates and brightens.",
                "Safe for most. Gentler than glycolic acid.");
        add("tartaric acid", SafetyLevel.SAFE, "AHA Exfoliant",
                "AHA from grapes. Exfoliates and improves texture.",
                "Safe for most skin types.");
        add("gluconolactone", SafetyLevel.SAFE, "PHA Exfoliant",
                "Polyhydroxy acid. Gentle exfoliant with moisturizing properties.",
                "Safe. Ideal for sensitive skin.");
        add("lactobionic acid", SafetyLevel.SAFE, "PHA Exfoliant",
                "Gentle polyhydroxy acid. Exfoliates without irritation.",
                "Safe for sensitive and dry skin.");
        add("phytic acid", SafetyLevel.SAFE, "Exfoliant",
                "Antioxidant exfoliant that brightens skin.",
                "Safe for sensitive skin.");
        add("capryloyl salicylic acid", SafetyLevel.CAUTION, "Exfoliant",
                "Lipid-soluble salicylic acid. Deep exfoliation.",
                "Gentler than regular salicylic acid but still exfoliating.");
        add("betaine salicylate", SafetyLevel.CAUTION, "Exfoliant",
                "Gentle salicylate ester. Exfoliating and anti-inflammatory.",
                "Milder than salicylic acid. Good for sensitive acne-prone skin.");
        add("papain", SafetyLevel.SAFE, "Enzyme Exfoliant",
                "Papaya enzyme that dissolves dead skin cells.",
                "Safe at cosmetic concentrations. Patch test first.");

        // ═══════════════════════════════════════════════════════════════════
        //  PEPTIDES
        // ═══════════════════════════════════════════════════════════════════

        add("palmitoyl pentapeptide-4", SafetyLevel.SAFE, "Peptide",
                "Anti-aging peptide that stimulates collagen production.",
                "Safe for all skin types.");
        add("palmitoyl tripeptide-38", SafetyLevel.SAFE, "Peptide",
                "Reduces wrinkles and promotes collagen synthesis.",
                "Safe for all skin types.");
        add("acetyl hexapeptide-8", SafetyLevel.SAFE, "Peptide",
                "Anti-aging peptide that relaxes facial muscles.",
                "Safe for all skin types.");
        add("acetyl hexapeptide-3", SafetyLevel.SAFE, "Peptide",
                "Reduces appearance of expression lines.",
                "Safe for all skin types.");
        add("palmitoyl oligopeptide", SafetyLevel.SAFE, "Peptide",
                "Stimulates skin rejuvenation and elasticity.",
                "Safe for all skin types.");
        add("palmitoyl tetrapeptide-7", SafetyLevel.SAFE, "Peptide",
                "Anti-aging, skin repair, and collagen synthesis.",
                "Safe for mature and all skin types.");
        add("dipeptide diaminobutyroyl benzylamide diacetate", SafetyLevel.SAFE, "Peptide",
                "Reduces puffiness and improves skin elasticity.",
                "Safe for sensitive skin.");
        add("tetrapeptide-14", SafetyLevel.SAFE, "Peptide",
                "Reduces inflammation and boosts collagen.",
                "Safe for all skin types.");
        add("acetyl tetrapeptide-40", SafetyLevel.SAFE, "Peptide",
                "Anti-aging and skin conditioning peptide.",
                "Safe for all skin types.");
        add("palmitoyl tripeptide-8", SafetyLevel.SAFE, "Peptide",
                "Anti-inflammatory anti-aging peptide.",
                "Safe for all skin types.");

        // ═══════════════════════════════════════════════════════════════════
        //  AMINO ACIDS
        // ═══════════════════════════════════════════════════════════════════

        add("arginine", SafetyLevel.SAFE, "Amino Acid",
                "Natural amino acid that helps skin repair.",
                "Safe for all skin types.");
        add("glycine", SafetyLevel.SAFE, "Amino Acid",
                "Smallest amino acid. Hydrates and nourishes.",
                "Safe for all skin types.");
        add("serine", SafetyLevel.SAFE, "Amino Acid",
                "Moisturizing amino acid that supports skin barrier.",
                "Safe for all skin types.");
        add("proline", SafetyLevel.SAFE, "Amino Acid",
                "Supports skin structure and collagen production.",
                "Safe for all skin types.");
        add("alanine", SafetyLevel.SAFE, "Amino Acid",
                "Skin conditioning amino acid.",
                "Safe for all skin types.");
        add("valine", SafetyLevel.SAFE, "Amino Acid",
                "Enhances skin barrier function.",
                "Safe for all skin types.");
        add("lysine", SafetyLevel.SAFE, "Amino Acid",
                "Supports collagen synthesis and skin conditioning.",
                "Safe for all skin types.");
        add("threonine", SafetyLevel.SAFE, "Amino Acid",
                "Moisturizing amino acid.",
                "Safe for all skin types.");
        add("glutamic acid", SafetyLevel.SAFE, "Amino Acid",
                "Moisturizing amino acid that supports skin barrier.",
                "Safe for all skin types.");
        add("aspartic acid", SafetyLevel.SAFE, "Amino Acid",
                "Supports skin hydration and elasticity.",
                "Safe for all skin types.");
        add("histidine", SafetyLevel.SAFE, "Amino Acid",
                "Protects skin from oxidative stress.",
                "Safe for all skin types.");

        // ═══════════════════════════════════════════════════════════════════
        //  THICKENERS, STABILIZERS & POLYMERS
        // ═══════════════════════════════════════════════════════════════════

        add("xanthan gum", SafetyLevel.SAFE, "Thickener",
                "Natural thickening agent derived from fermentation.",
                "Safe for all skin types.");
        add("carbomer", SafetyLevel.SAFE, "Thickener",
                "Synthetic polymer used to thicken formulas.",
                "Safe for most. Rarely irritating.");
        add("hydroxyethylcellulose", SafetyLevel.SAFE, "Thickener",
                "Cellulose-derived thickener and film former.",
                "Safe for all skin types.");
        add("hydroxypropyl methylcellulose", SafetyLevel.SAFE, "Thickener",
                "Plant-derived thickener and stabilizer.",
                "Safe for all skin types.");
        add("acrylates/c10-30 alkyl acrylate crosspolymer", SafetyLevel.SAFE, "Thickener",
                "Synthetic polymer that thickens and stabilizes formulas.",
                "Safe for all skin types.");
        add("hydroxyethyl acrylate/sodium acryloyldimethyl taurate copolymer", SafetyLevel.SAFE, "Thickener",
                "Stabilizer and emulsifier.",
                "Safe for all skin types.");
        add("ammonium acryloyldimethyltaurate/vp copolymer", SafetyLevel.SAFE, "Thickener",
                "Film-forming and viscosity control agent.",
                "Safe for all skin types.");
        add("sodium polyacrylate", SafetyLevel.SAFE, "Thickener",
                "Thickening polymer for moisture retention.",
                "Safe for all skin types.");
        add("sclerotium gum", SafetyLevel.SAFE, "Thickener",
                "Natural polysaccharide thickener and skin conditioner.",
                "Safe for all skin types.");
        add("polyacrylate crosspolymer-6", SafetyLevel.SAFE, "Thickener",
                "Stabilizer and texture enhancer.",
                "Safe for all skin types.");
        add("acacia senegal gum", SafetyLevel.SAFE, "Thickener",
                "Natural gum that thickens and stabilizes.",
                "Safe for all skin types.");

        // ═══════════════════════════════════════════════════════════════════
        //  EMULSIFIERS
        // ═══════════════════════════════════════════════════════════════════

        add("glyceryl stearate", SafetyLevel.SAFE, "Emulsifier",
                "Natural emulsifier derived from glycerol and stearic acid.",
                "Safe for all skin types.");
        add("cetearyl alcohol", SafetyLevel.SAFE, "Emulsifier",
                "Fatty alcohol emulsifier and emollient. Not drying.",
                "Safe for all skin types.");
        add("cetyl alcohol", SafetyLevel.SAFE, "Emulsifier",
                "Fatty alcohol that softens and stabilizes.",
                "Safe for all skin types.");
        add("stearyl alcohol", SafetyLevel.SAFE, "Emulsifier",
                "Fatty alcohol used as emulsifier and emollient.",
                "Safe for all skin types.");
        add("polysorbate 20", SafetyLevel.SAFE, "Emulsifier",
                "Mild surfactant and emulsifier.",
                "Safe for all skin types.");
        add("polysorbate 60", SafetyLevel.SAFE, "Emulsifier",
                "Emulsifier that stabilizes oil/water formulas.",
                "Safe for all skin types.");
        add("polysorbate 80", SafetyLevel.SAFE, "Emulsifier",
                "Emulsifier that stabilizes formulas and aids penetration.",
                "Safe for all skin types.");
        add("peg-100 stearate", SafetyLevel.SAFE, "Emulsifier",
                "Mild emulsifier for oil/water emulsions.",
                "Safe for all skin types.");
        add("cetearyl glucoside", SafetyLevel.SAFE, "Emulsifier",
                "Natural sugar-derived emulsifier.",
                "Safe for all skin types.");
        add("sorbitan olivate", SafetyLevel.SAFE, "Emulsifier",
                "Olive oil-derived emulsifier.",
                "Safe for dry and sensitive skin.");
        add("cetearyl olivate", SafetyLevel.SAFE, "Emulsifier",
                "Olive-derived emulsifier and skin conditioner.",
                "Safe for dry and sensitive skin.");

        // ═══════════════════════════════════════════════════════════════════
        //  SURFACTANTS (CLEANSERS)
        // ═══════════════════════════════════════════════════════════════════

        add("decyl glucoside", SafetyLevel.SAFE, "Surfactant",
                "Very mild plant-derived cleanser.",
                "Safe. Excellent for sensitive skin.");
        add("coco-glucoside", SafetyLevel.SAFE, "Surfactant",
                "Coconut-derived mild surfactant.",
                "Safe for sensitive and dry skin.");
        add("lauryl glucoside", SafetyLevel.SAFE, "Surfactant",
                "Gentle plant-derived cleansing agent.",
                "Safe for sensitive skin.");
        add("sodium cocoyl glutamate", SafetyLevel.SAFE, "Surfactant",
                "Amino acid-based mild cleanser.",
                "Safe for sensitive skin.");
        add("sodium lauroyl sarcosinate", SafetyLevel.SAFE, "Surfactant",
                "Mild amino acid-derived surfactant.",
                "Safe for sensitive skin.");
        add("sodium cocoamphoacetate", SafetyLevel.SAFE, "Surfactant",
                "Amphoteric mild cleanser.",
                "Safe for sensitive skin.");
        add("disodium cocoamphodiacetate", SafetyLevel.SAFE, "Surfactant",
                "Mild amphoteric surfactant from coconut.",
                "Safe. Low irritation potential.");
        add("sodium lauryl sulfate", SafetyLevel.CAUTION, "Surfactant",
                "Powerful cleansing/foaming agent.",
                "Can disrupt skin barrier. Irritating for sensitive or dry skin.");
        add("sodium laureth sulfate", SafetyLevel.CAUTION, "Surfactant",
                "Milder surfactant than SLS. Common in cleansers.",
                "May contain trace 1,4-dioxane contaminant. Irritating at high levels.");
        add("cocamidopropyl betaine", SafetyLevel.CAUTION, "Surfactant",
                "Coconut-derived cleansing agent.",
                "Can cause contact allergy in some individuals.");
        add("sodium lauroyl methyl isethionate", SafetyLevel.SAFE, "Surfactant",
                "Mild cleansing surfactant.",
                "Safe for all skin types.");
        add("sodium methyl oleoyl taurate", SafetyLevel.SAFE, "Surfactant",
                "Mild surfactant from oleic acid.",
                "Safe for sensitive skin.");
        add("sodium cocoyl isethionate", SafetyLevel.SAFE, "Surfactant",
                "Very mild coconut-derived cleanser.",
                "Safe for dry and sensitive skin.");

        // ═══════════════════════════════════════════════════════════════════
        //  PH ADJUSTERS
        // ═══════════════════════════════════════════════════════════════════

        add("sodium hydroxide", SafetyLevel.SAFE, "pH Adjuster",
                "Used in tiny amounts to neutralize pH.",
                "Safe at trace amounts used in cosmetics.");
        add("potassium hydroxide", SafetyLevel.SAFE, "pH Adjuster",
                "pH balancer used in tiny amounts.",
                "Safe at trace amounts.");
        add("triethanolamine", SafetyLevel.CAUTION, "pH Adjuster",
                "Used to neutralize acids in formulas.",
                "May form harmful nitrosamines. Restricted at some concentrations.");
        add("tromethamine", SafetyLevel.SAFE, "pH Adjuster",
                "pH balancing agent.",
                "Safe for all skin types.");
        add("aminomethyl propanol", SafetyLevel.SAFE, "pH Adjuster",
                "pH balancing and skin conditioning.",
                "Safe for all skin types.");
        add("sodium citrate", SafetyLevel.SAFE, "pH Adjuster",
                "Buffering agent that stabilizes product pH.",
                "Safe for all skin types.");
        add("sodium bicarbonate", SafetyLevel.SAFE, "pH Adjuster",
                "Balances pH and provides mild soothing.",
                "Safe at cosmetic concentrations.");

        // ═══════════════════════════════════════════════════════════════════
        //  CHELATING AGENTS
        // ═══════════════════════════════════════════════════════════════════

        add("disodium edta", SafetyLevel.SAFE, "Chelating Agent",
                "Binds metal ions to prevent product degradation.",
                "Safe for all skin types.");
        add("tetrasodium edta", SafetyLevel.SAFE, "Chelating Agent",
                "Stabilizes formulas by binding metal ions.",
                "Safe for all skin types.");
        add("sodium gluconate", SafetyLevel.SAFE, "Chelating Agent",
                "Natural chelating agent that enhances formula stability.",
                "Safe for all skin types.");
        add("sodium phytate", SafetyLevel.SAFE, "Chelating Agent",
                "Plant-derived chelating agent.",
                "Safe for all skin types.");
        add("trisodium ethylenediamine disuccinate", SafetyLevel.SAFE, "Chelating Agent",
                "Biodegradable chelating agent.",
                "Safe for all skin types.");

        // ═══════════════════════════════════════════════════════════════════
        //  PRESERVATIVES — SAFE / LOW RISK
        // ═══════════════════════════════════════════════════════════════════

        add("sodium benzoate", SafetyLevel.SAFE, "Preservative",
                "Antimicrobial preservative from benzoic acid.",
                "Safe at cosmetic concentrations.");
        add("potassium sorbate", SafetyLevel.SAFE, "Preservative",
                "Natural antimicrobial preservative.",
                "Safe for all skin types.");
        add("ethylhexylglycerin", SafetyLevel.SAFE, "Preservative",
                "Skin-conditioning preservative booster.",
                "Safe for all skin types.");
        add("caprylhydroxamic acid", SafetyLevel.SAFE, "Preservative",
                "Chelating preservative from caprylic acid.",
                "Safe for all skin types.");
        add("dehydroacetic acid", SafetyLevel.SAFE, "Preservative",
                "Antifungal and antimicrobial preservative.",
                "Safe for all skin types.");
        add("sodium dehydroacetate", SafetyLevel.SAFE, "Preservative",
                "Water-soluble form of dehydroacetic acid.",
                "Safe for all skin types.");
        add("benzoic acid", SafetyLevel.SAFE, "Preservative",
                "Naturally occurring antimicrobial preservative.",
                "Safe at cosmetic concentrations.");
        add("chlorphenesin", SafetyLevel.CAUTION, "Preservative",
                "Broad-spectrum preservative.",
                "Generally safe. Rare sensitivity reported.");
        add("caprylyl glycol", SafetyLevel.SAFE, "Humectant",
                "Moisturizing agent with mild preservative properties.",
                "Safe for all skin types.");
        add("hydroxyacetophenone", SafetyLevel.SAFE, "Antioxidant",
                "Preservative booster with antioxidant properties.",
                "Safe for sensitive skin.");
        add("sodium levulinate", SafetyLevel.SAFE, "Preservative",
                "Natural fermentation-derived preservative.",
                "Safe for all skin types.");
        add("sodium anisate", SafetyLevel.SAFE, "Preservative",
                "Anise-derived natural preservative.",
                "Safe for all skin types.");
        add("p anisic acid", SafetyLevel.SAFE, "Preservative",
                "Natural antimicrobial from anise.",
                "Safe for all skin types.");
        add("leuconostoc/radish root filtrate", SafetyLevel.SAFE, "Preservative",
                "Natural fermented radish preservative.",
                "Safe for all skin types. Natural alternative.");
        add("phenylpropanol", SafetyLevel.SAFE, "Preservative",
                "Antimicrobial preservative with skin conditioning.",
                "Safe for all skin types.");

        // ═══════════════════════════════════════════════════════════════════
        //  PRESERVATIVES — CAUTION
        // ═══════════════════════════════════════════════════════════════════

        add("phenoxyethanol", SafetyLevel.CAUTION, "Preservative",
                "Synthetic preservative. Extends product shelf life.",
                "May cause irritation on sensitive skin. Restricted in some regions.");
        add("benzyl alcohol", SafetyLevel.CAUTION, "Preservative",
                "Aromatic alcohol used as preservative and fragrance.",
                "Can cause contact allergy. Not recommended for sensitive skin.");
        add("methylisothiazolinone", SafetyLevel.CAUTION, "Preservative",
                "Synthetic preservative used to prevent microbial growth.",
                "Known sensitizer. Banned in rinse-off products in EU.");
        add("methylchloroisothiazolinone", SafetyLevel.CAUTION, "Preservative",
                "Preservative with antimicrobial properties.",
                "Strong sensitizer. Restricted in leave-on products.");
        add("iodopropynyl butylcarbamate", SafetyLevel.CAUTION, "Preservative",
                "Antifungal preservative.",
                "Can cause irritation. Restricted in some countries.");
        add("imidazolidinyl urea", SafetyLevel.HARMFUL, "Preservative",
                "Formaldehyde-releasing preservative.",
                "Allergen. Releases formaldehyde over time.");
        add("diazolidinyl urea", SafetyLevel.HARMFUL, "Preservative",
                "Formaldehyde-releasing antimicrobial.",
                "Allergen. Formaldehyde releaser.");

        // ═══════════════════════════════════════════════════════════════════
        //  FRAGRANCE & FRAGRANCE COMPONENTS — CAUTION
        // ═══════════════════════════════════════════════════════════════════

        add("fragrance", SafetyLevel.CAUTION, "Fragrance",
                "Undisclosed blend of fragrance chemicals.",
                "Potential allergen for many skin types. Not recommended for sensitive skin.");
        add("parfum", SafetyLevel.CAUTION, "Fragrance",
                "Fragrance mixture. Ingredients often undisclosed.",
                "Common allergen. Avoid if you have sensitive skin.");
        add("perfume", SafetyLevel.CAUTION, "Fragrance",
                "Undisclosed fragrance blend.",
                "May cause allergic reactions on sensitive skin.");
        add("limonene", SafetyLevel.CAUTION, "Fragrance Component",
                "Naturally occurring fragrance found in citrus peel.",
                "Common allergen when oxidized. Irritating for some.");
        add("linalool", SafetyLevel.CAUTION, "Fragrance Component",
                "Naturally occurring fragrance compound.",
                "Potential contact allergen. May irritate sensitive skin.");
        add("eugenol", SafetyLevel.CAUTION, "Fragrance Component",
                "Fragrance chemical found in clove oil.",
                "Potential allergen and irritant.");
        add("geraniol", SafetyLevel.CAUTION, "Fragrance Component",
                "Floral fragrance compound. Known allergen.",
                "May cause contact allergy on sensitive skin.");
        add("citronellol", SafetyLevel.CAUTION, "Fragrance Component",
                "Floral fragrance from rose and citronella.",
                "Potential allergen. Avoid on sensitive skin.");
        add("hexyl cinnamal", SafetyLevel.CAUTION, "Fragrance Component",
                "Cinnamal fragrance compound. Common allergen.",
                "May cause contact sensitivity.");
        add("citral", SafetyLevel.CAUTION, "Fragrance Component",
                "Citrus fragrance compound. Known irritant.",
                "Can cause contact allergy on sensitive skin.");
        add("benzyl salicylate", SafetyLevel.CAUTION, "Fragrance Component",
                "Sweet-scented fragrance ingredient. Potential allergen.",
                "May cause sensitivity.");
        add("alpha-isomethyl ionone", SafetyLevel.CAUTION, "Fragrance Component",
                "Violet-scented fragrance ingredient.",
                "Potential contact allergen.");
        add("coumarin", SafetyLevel.CAUTION, "Fragrance Component",
                "Sweet-scented fragrance ingredient.",
                "Potential allergen. May cause photosensitivity.");
        add("hydroxycitronellal", SafetyLevel.CAUTION, "Fragrance Component",
                "Floral fragrance ingredient. Potential allergen.",
                "May cause contact sensitivity.");
        add("linalol", SafetyLevel.CAUTION, "Fragrance Component",
                "Floral fragrance compound. Potential allergen.",
                "Avoid on sensitive skin.");

        // ═══════════════════════════════════════════════════════════════════
        //  ESSENTIAL OILS — CAUTION
        // ═══════════════════════════════════════════════════════════════════

        add("lavandula angustifolia oil", SafetyLevel.CAUTION, "Essential Oil",
                "Lavender oil. Soothing fragrance with sensitization risk.",
                "Potential allergen. Patch test before use on sensitive skin.");
        add("lavender oil", SafetyLevel.CAUTION, "Essential Oil",
                "Popular essential oil with skin sensitization potential.",
                "Can cause contact allergy. Avoid on broken skin.");
        add("mentha piperita (peppermint) oil", SafetyLevel.CAUTION, "Essential Oil",
                "Cooling peppermint oil. Potential irritant.",
                "Can irritate mucous membranes. Avoid near eyes.");
        add("peppermint oil", SafetyLevel.CAUTION, "Essential Oil",
                "Cooling essential oil. May cause irritation.",
                "Avoid on sensitive and young skin.");
        add("tea tree oil", SafetyLevel.CAUTION, "Essential Oil",
                "Antimicrobial essential oil. Can cause sensitization.",
                "Dilute before use. Patch test recommended.");
        add("melaleuca alternifolia leaf oil", SafetyLevel.CAUTION, "Essential Oil",
                "Tea tree oil. Antimicrobial but potential sensitizer.",
                "Patch test first. Avoid at high concentrations.");
        add("eucalyptus globulus oil", SafetyLevel.CAUTION, "Essential Oil",
                "Antimicrobial and refreshing. Potential irritant.",
                "Avoid near eyes and on sensitive skin.");
        add("citrus sinensis oil", SafetyLevel.CAUTION, "Essential Oil",
                "Sweet orange oil. Fragrance and potential photosensitizer.",
                "May cause photosensitivity. Avoid before sun exposure.");
        add("citrus aurantium bergamia fruit oil", SafetyLevel.CAUTION, "Essential Oil",
                "Bergamot oil. Fragrance with photosensitization risk.",
                "May cause photosensitivity unless bergapten-free.");

        // ═══════════════════════════════════════════════════════════════════
        //  ALCOHOL — CAUTION
        // ═══════════════════════════════════════════════════════════════════

        add("alcohol denat.", SafetyLevel.CAUTION, "Solvent / Astringent",
                "Denatured alcohol. Helps formulas dry quickly.",
                "Can be drying and disruptive to skin barrier.");
        add("alcohol denat", SafetyLevel.CAUTION, "Solvent",
                "Denatured alcohol. Drying and antimicrobial.",
                "Weakens skin barrier with repeated use.");
        add("denatured alcohol", SafetyLevel.CAUTION, "Solvent",
                "Alcohol that helps other ingredients penetrate.",
                "Drying. Weakens skin barrier with repeated use.");
        add("alcohol", SafetyLevel.CAUTION, "Solvent",
                "Ethyl alcohol. Can be drying and disruptive to the skin barrier.",
                "Avoid on dry, sensitive, or compromised skin.");
        add("isopropyl alcohol", SafetyLevel.CAUTION, "Solvent",
                "Rubbing alcohol. Drying and potentially irritating.",
                "Drying. May disrupt the skin barrier.");

        // ═══════════════════════════════════════════════════════════════════
        //  SUNSCREEN AGENTS — CAUTION (CHEMICAL)
        // ═══════════════════════════════════════════════════════════════════

        add("oxybenzone", SafetyLevel.CAUTION, "Chemical Sunscreen",
                "UV filter for sun protection.",
                "Possible hormone disruptor. Coral reef concerns. Restricted in some areas.");
        add("octinoxate", SafetyLevel.CAUTION, "Chemical Sunscreen",
                "UV-B absorbing sunscreen filter.",
                "Possible endocrine disruption. Environmental concerns.");
        add("ethylhexyl methoxycinnamate", SafetyLevel.CAUTION, "Chemical Sunscreen",
                "Octinoxate. UV-B filter. Potential endocrine disruptor.",
                "Apply SPF daily. Some concerns over absorption.");
        add("benzophenone-4", SafetyLevel.CAUTION, "Chemical Sunscreen",
                "UV filter used in water-resistant formulas.",
                "May cause sensitization. Environmental concerns.");
        add("benzophenone-3", SafetyLevel.CAUTION, "Chemical Sunscreen",
                "Oxybenzone. Broad UV filter.",
                "Possible hormone disruptor. Coral reef concerns.");
        add("octocrylene", SafetyLevel.CAUTION, "Chemical Sunscreen",
                "UV-B filter that also stabilizes other sunscreen actives.",
                "Can cause contact allergy. Some environmental concerns.");
        add("butyl methoxydibenzoylmethane", SafetyLevel.SAFE, "Chemical Sunscreen",
                "Avobenzone. Best UVA chemical filter.",
                "Safe. May degrade in sunlight — needs photostabilizer.");
        add("homosalate", SafetyLevel.CAUTION, "Chemical Sunscreen",
                "UVB filter. Suspected endocrine disruptor.",
                "Restricted in EU at high concentrations.");

        // ═══════════════════════════════════════════════════════════════════
        //  HARMFUL INGREDIENTS
        // ═══════════════════════════════════════════════════════════════════

        add("formaldehyde", SafetyLevel.HARMFUL, "Preservative",
                "Antimicrobial preservative.",
                "Known carcinogen. Banned or restricted in many countries.");
        add("quaternium-15", SafetyLevel.HARMFUL, "Preservative",
                "Preservative that releases formaldehyde.",
                "Formaldehyde releaser. Potent allergen and carcinogen risk.");
        add("dmdm hydantoin", SafetyLevel.HARMFUL, "Preservative",
                "Preservative that slowly releases formaldehyde.",
                "Formaldehyde releaser. Linked to hair loss and sensitization.");
        add("parabens", SafetyLevel.HARMFUL, "Preservative",
                "Class of synthetic preservatives.",
                "Potential endocrine disruptors. Avoid during pregnancy.");
        add("methylparaben", SafetyLevel.HARMFUL, "Preservative",
                "Common paraben preservative.",
                "Possible hormone disruption. Many brands now paraben-free.");
        add("ethylparaben", SafetyLevel.HARMFUL, "Preservative",
                "Paraben-class preservative.",
                "Endocrine disruptor concern. Avoid during pregnancy.");
        add("propylparaben", SafetyLevel.HARMFUL, "Preservative",
                "Paraben-class preservative.",
                "Endocrine disruptor concern. Restricted in some regions.");
        add("butylparaben", SafetyLevel.HARMFUL, "Preservative",
                "Paraben with strongest estrogenic activity.",
                "Strongest endocrine disruption concern among parabens.");
        add("isobutylparaben", SafetyLevel.HARMFUL, "Preservative",
                "Paraben preservative with endocrine concern.",
                "Potential hormone disruptor. Avoid during pregnancy.");
        add("triclosan", SafetyLevel.HARMFUL, "Antimicrobial",
                "Broad-spectrum antimicrobial agent.",
                "Banned in soaps in US. Endocrine disruptor. Environmental toxin.");
        add("hydroquinone", SafetyLevel.HARMFUL, "Skin Lightener",
                "Melanin-inhibiting skin brightening agent.",
                "Potential carcinogen. Banned in EU cosmetics. Prescription-only in some countries.");
        add("mercury", SafetyLevel.HARMFUL, "Skin Lightener",
                "Heavy metal used in some lightening products.",
                "Extremely toxic. Banned worldwide in cosmetics.");
        add("thimerosal", SafetyLevel.HARMFUL, "Preservative",
                "Mercury-containing preservative.",
                "Mercury-based. Toxic and banned in many cosmetics.");
        add("coal tar", SafetyLevel.HARMFUL, "Colorant / Medicinal",
                "Byproduct of coal processing used in dandruff products.",
                "Potential carcinogen. Restricted in EU.");
        add("lead acetate", SafetyLevel.HARMFUL, "Colorant",
                "Lead-based color additive.",
                "Lead is a heavy metal neurotoxin. Banned in many countries.");
        add("diethanolamine", SafetyLevel.HARMFUL, "pH Adjuster",
                "Ethanolamine used to adjust pH.",
                "Restricted in EU. May form carcinogenic nitrosamines.");
        add("butylphenyl methylpropional", SafetyLevel.CAUTION, "Fragrance Component",
                "Lilial — a floral synthetic fragrance.",
                "Banned in EU. Potential reproductive toxicant.");
        add("talc", SafetyLevel.CAUTION, "Filler / Absorbent",
                "Mineral powder used in cosmetics.",
                "Asbestos contamination concern in some sources. Avoid in powder products near face.");
        add("propylene glycol", SafetyLevel.CAUTION, "Humectant / Solvent",
                "Penetration enhancer and humectant.",
                "Can irritate sensitive skin at higher concentrations.");
        add("isopropyl myristate", SafetyLevel.CAUTION, "Emollient",
                "Lightweight emollient and penetration enhancer.",
                "Comedogenic. May clog pores for acne-prone skin.");
        add("retinyl palmitate", SafetyLevel.CAUTION, "Retinoid",
                "Ester of retinol and palmitic acid. Anti-aging.",
                "May increase sun sensitivity. Controversial safety profile.");
        add("menthol", SafetyLevel.CAUTION, "Active Ingredient",
                "Cooling agent derived from mint.",
                "Can irritate mucous membranes. Avoid in large amounts on broken skin.");
        add("camphor", SafetyLevel.CAUTION, "Active Ingredient",
                "Strong cooling and antimicrobial agent.",
                "Can be toxic if absorbed in large amounts. Avoid on broken skin.");
        add("witch hazel", SafetyLevel.CAUTION, "Botanical Extract",
                "Astringent plant extract. Can be drying.",
                "Can strip moisture from skin. Use sparingly on dry/sensitive skin.");

        // ═══════════════════════════════════════════════════════════════════
        //  SUNSCREEN UV FILTERS — EXPANDED
        //  Covers EU, Asian (Korean/Japanese), and global market filters
        //  not present in the original database.
        // ═══════════════════════════════════════════════════════════════════

        // ── Tinosorb family (Basf; common in EU & Asian sunscreens) ────────
        add("bis-ethylhexyloxyphenol methoxyphenyl triazine", SafetyLevel.SAFE, "UV Filter",
                "Tinosorb S. Broad-spectrum UVA/UVB filter. Photostable.",
                "One of the safest and most effective broad-spectrum filters. Common in EU and Korean sunscreens.");
        add("tinosorb s", SafetyLevel.SAFE, "UV Filter",
                "Broad-spectrum UVA/UVB photostable filter.",
                "Highly effective. Safe for most skin types.");
        add("methylene bis-benzotriazolyl tetramethylbutylphenol", SafetyLevel.SAFE, "UV Filter",
                "Tinosorb M. Hybrid physical/chemical broad-spectrum UV filter.",
                "Photostable and water-resistant. Safe for sensitive skin.");
        add("tinosorb m", SafetyLevel.SAFE, "UV Filter",
                "Hybrid physical/chemical broad-spectrum UV filter.",
                "Photostable. Safe for most skin types including sensitive.");

        // ── Mexoryl family (L'Oréal; common in EU & Canadian sunscreens) ───
        add("terephthalylidene dicamphor sulfonic acid", SafetyLevel.SAFE, "UV Filter",
                "Mexoryl SX. Water-soluble UVA filter. Photostable.",
                "Excellent UVA protection. Safe and well-tolerated.");
        add("mexoryl sx", SafetyLevel.SAFE, "UV Filter",
                "Water-soluble UVA filter. Photostable.",
                "Safe. Pairs well with Mexoryl XL for full-spectrum coverage.");
        add("drometrizole trisiloxane", SafetyLevel.SAFE, "UV Filter",
                "Mexoryl XL. Oil-soluble UVA/UVB filter. Photostable.",
                "Safe. Effective broad-spectrum coverage.");
        add("mexoryl xl", SafetyLevel.SAFE, "UV Filter",
                "Oil-soluble UVA/UVB photostable filter.",
                "Safe. Excellent broad-spectrum protection.");

        // ── Uvinul family (BASF; common in EU & Asian sunscreens) ──────────
        add("diethylamino hydroxybenzoyl hexyl benzoate", SafetyLevel.SAFE, "UV Filter",
                "Uvinul A Plus. Oil-soluble UVA filter. Photostable.",
                "Highly photostable UVA filter. Safe for most skin types.");
        add("uvinul a plus", SafetyLevel.SAFE, "UV Filter",
                "Oil-soluble UVA filter. Photostable.",
                "One of the most stable UVA filters available.");
        add("ethylhexyl triazone", SafetyLevel.SAFE, "UV Filter",
                "Uvinul T 150. UVB filter. Photostable and water-resistant.",
                "Safe. Highly effective UVB protection. Common in Asian sunscreens.");
        add("uvinul t 150", SafetyLevel.SAFE, "UV Filter",
                "UVB filter. Photostable and water-resistant.",
                "Safe. High SPF boosting efficiency.");
        add("iscotrizinol", SafetyLevel.SAFE, "UV Filter",
                "Uvinul Plus. UVB filter with high photostability.",
                "Safe. Common in European and Asian sunscreens.");
        add("diethylhexyl butamido triazone", SafetyLevel.SAFE, "UV Filter",
                "Uvasorb HEB. Photostable UVB filter.",
                "Safe. High SPF efficiency. Common in EU sunscreens.");

        // ── Bemotrizinol / Parsol family ────────────────────────────────────
        add("bemotrizinol", SafetyLevel.SAFE, "UV Filter",
                "Tinosorb S (alternate name). Broad-spectrum photostable filter.",
                "Safe. Excellent UV coverage.");
        add("parsol 1789", SafetyLevel.SAFE, "UV Filter",
                "Avobenzone. Best-in-class UVA chemical filter.",
                "Effective UVA protection. Needs photostabilizer to prevent degradation.");
        add("butyl methoxydibenzoylmethane", SafetyLevel.SAFE, "UV Filter",
                "Avobenzone / Parsol 1789. Premier UVA chemical filter.",
                "Safe. Pairs with octocrylene or Tinosorb to prevent photodegradation.");

        // ── Asian sunscreen–specific filters ────────────────────────────────
        add("polysilicone-15", SafetyLevel.SAFE, "UV Filter",
                "Parsol SLX. Photostable UVB filter. Silicone-based.",
                "Safe. Common in Japanese sunscreens. Water-resistant.");
        add("parsol slx", SafetyLevel.SAFE, "UV Filter",
                "Silicone-based photostable UVB filter.",
                "Safe. Excellent photostability.");
        add("dea-methoxycinnamate", SafetyLevel.CAUTION, "UV Filter",
                "UVB filter. DEA-based compound.",
                "May form nitrosamines. Restricted in some countries.");
        add("4-methylbenzylidene camphor", SafetyLevel.CAUTION, "UV Filter",
                "UVB filter with suspected endocrine-disrupting activity.",
                "Banned in US. Restricted in EU. Avoid if possible.");
        add("3-benzylidene camphor", SafetyLevel.CAUTION, "UV Filter",
                "UVB filter. Suspected endocrine disruptor.",
                "Not approved in US or EU cosmetics. Avoid.");
        add("octyl dimethyl paba", SafetyLevel.CAUTION, "UV Filter",
                "PABA-derived UVB filter. Older generation sunscreen.",
                "Can cause contact allergy. Being phased out.");
        add("paba", SafetyLevel.CAUTION, "UV Filter",
                "Para-aminobenzoic acid. Early-generation UVB filter.",
                "High allergy potential. Largely discontinued.");
        add("ethylhexyl dimethyl paba", SafetyLevel.CAUTION, "UV Filter",
                "Padimate O. UVB filter derived from PABA.",
                "May cause contact sensitivity. Use with caution.");

        // ── Additional photostabilizers & helpers ───────────────────────────
        add("polyester-8", SafetyLevel.SAFE, "UV Filter Helper",
                "Photostabilizer that prevents avobenzone degradation.",
                "Safe. Improves the stability of UVA filters.");
        add("sodium benzotriazolyl butylphenol sulfonate", SafetyLevel.SAFE, "UV Filter",
                "Water-soluble broad-spectrum UV filter.",
                "Safe. Common in Asian sunscreen formulas.");
        add("phenylbenzimidazole sulfonic acid", SafetyLevel.SAFE, "UV Filter",
                "Ensulizole. Water-soluble UVB filter. Lightweight feel.",
                "Safe. Often used in daily moisturizers with SPF.");
        add("ensulizole", SafetyLevel.SAFE, "UV Filter",
                "Water-soluble UVB filter with light, non-greasy feel.",
                "Safe. Common in SPF moisturizers.");
        add("padimate o", SafetyLevel.CAUTION, "UV Filter",
                "PABA ester UVB filter. Older generation.",
                "Potential sensitizer. Being phased out of modern formulas.");
        add("triethanolamine salicylate", SafetyLevel.CAUTION, "UV Filter",
                "Trolamine salicylate. UVB filter.",
                "Weaker UV protection. May cause sensitivity.");
        add("octyl salicylate", SafetyLevel.SAFE, "UV Filter",
                "Octisalate. UVB filter with good safety profile.",
                "Safe. Enhances water resistance.");
        add("octisalate", SafetyLevel.SAFE, "UV Filter",
                "UVB filter. Safe and well-tolerated.",
                "Enhances spreadability and water resistance.");
        add("homosalate", SafetyLevel.CAUTION, "UV Filter",
                "UVB filter. Suspected weak endocrine disruptor.",
                "Restricted in EU at high concentrations. Use at low levels.");
        add("octocrylene", SafetyLevel.CAUTION, "UV Filter",
                "UVB/short UVA filter. Photostabilizes avobenzone.",
                "Can cause contact allergy. Generates free radicals when exposed to light.");

        // ═══════════════════════════════════════════════════════════════════
        //  CLEANSER SURFACTANTS — EXPANDED
        //  Common in facial washes, foam cleansers, micellar waters,
        //  cleansing oils, and bar soaps.
        // ═══════════════════════════════════════════════════════════════════

        // ── Mild / gentle surfactants ───────────────────────────────────────
        add("sodium lauroamphoacetate", SafetyLevel.SAFE, "Surfactant",
                "Very gentle amphoteric surfactant. Good foam with low irritation.",
                "Safe for sensitive and dry skin.");
        add("disodium laureth sulfosuccinate", SafetyLevel.SAFE, "Surfactant",
                "Ultra-mild surfactant. Very low irritation potential.",
                "Safe for sensitive, dry, and baby skin.");
        add("sodium lauroyl oat amino acids", SafetyLevel.SAFE, "Surfactant",
                "Oat-derived amino acid surfactant. Gentle and conditioning.",
                "Safe for sensitive skin. Adds skin-conditioning benefits.");
        add("potassium cocoyl glycinate", SafetyLevel.SAFE, "Surfactant",
                "Coconut-derived amino acid cleanser. Mild and conditioning.",
                "Safe for all skin types including sensitive.");
        add("sodium cocoyl glycinate", SafetyLevel.SAFE, "Surfactant",
                "Gentle coconut-derived amino acid surfactant.",
                "Safe for sensitive skin.");
        add("lauramidopropyl betaine", SafetyLevel.SAFE, "Surfactant",
                "Mild foam-boosting surfactant with conditioning properties.",
                "Safe for sensitive skin.");
        add("sodium cocoyl apple amino acids", SafetyLevel.SAFE, "Surfactant",
                "Apple-derived amino acid cleanser. Mild and skin-friendly.",
                "Safe. Great for sensitive and dry skin.");
        add("sodium olivamphoacetate", SafetyLevel.SAFE, "Surfactant",
                "Olive-derived mild amphoteric cleanser.",
                "Safe for sensitive skin.");
        add("sodium lauroyl hydrolyzed silk", SafetyLevel.SAFE, "Surfactant",
                "Silk protein-based cleanser. Gentle with conditioning properties.",
                "Safe. Leaves skin feeling smooth.");
        add("sodium cocoyl hydrolyzed soy protein", SafetyLevel.SAFE, "Surfactant",
                "Soy protein-derived mild cleanser.",
                "Safe. Adds conditioning benefits to cleansers.");
        add("sodium laurylglucosides hydroxypropylsulfonate", SafetyLevel.SAFE, "Surfactant",
                "Ultra-mild glucose-derived surfactant.",
                "Safe for all skin types.");
        add("capryl/capramidopropyl betaine", SafetyLevel.SAFE, "Surfactant",
                "Mild caprylic-derived betaine. Gentle foam.",
                "Safe for sensitive skin.");
        add("sodium coco-sulfate", SafetyLevel.CAUTION, "Surfactant",
                "Coconut-derived sulfate. Stronger than SLS alternatives.",
                "Can irritate. Less harsh than SLS but still drying for some.");

        // ── Moderate surfactants ────────────────────────────────────────────
        add("ammonium lauryl sulfate", SafetyLevel.CAUTION, "Surfactant",
                "Foaming cleanser similar to SLS. Slightly milder.",
                "Can be drying. Not recommended for dry or sensitive skin.");
        add("sodium c14-16 olefin sulfonate", SafetyLevel.CAUTION, "Surfactant",
                "Effective foaming agent. Can be drying.",
                "Moderate irritation potential. Avoid on dry or sensitive skin.");
        add("cocamide mea", SafetyLevel.CAUTION, "Surfactant",
                "Foam booster and thickener derived from coconut.",
                "Can form nitrosamines. Use with caution.");
        add("cocamide dea", SafetyLevel.CAUTION, "Surfactant",
                "Foam booster. Potential nitrosamine-forming ingredient.",
                "Restricted in EU. May form carcinogenic nitrosamines.");
        add("cocamide diethanolamine", SafetyLevel.CAUTION, "Surfactant",
                "Foam stabilizer. Nitrosamine concern.",
                "Restricted in some regions. Use sparingly.");
        add("sodium myreth sulfate", SafetyLevel.CAUTION, "Surfactant",
                "Milder alternative to SLES. Common in shampoos and cleansers.",
                "Less irritating than SLS. Can still be drying.");
        add("tipa-laureth sulfate", SafetyLevel.CAUTION, "Surfactant",
                "Triethanolamine-based surfactant.",
                "May form nitrosamines. Use with caution.");
        add("sodium laureth-2 sulfate", SafetyLevel.CAUTION, "Surfactant",
                "Ethoxylated sulfate cleanser.",
                "May contain trace 1,4-dioxane. Moderately irritating.");

        // ── Cleansing oil / micellar emulsifiers ───────────────────────────
        add("peg-6 caprylic/capric glycerides", SafetyLevel.SAFE, "Cleansing Emulsifier",
                "Used in cleansing oils and balms to emulsify makeup.",
                "Safe. Self-emulsifying — rinses cleanly with water.");
        add("peg-20 glyceryl triisostearate", SafetyLevel.SAFE, "Cleansing Emulsifier",
                "Emulsifier in cleansing oils. Breaks down sunscreen and makeup.",
                "Safe. Common in Japanese cleansing oils.");
        add("polyglyceryl-10 laurate", SafetyLevel.SAFE, "Cleansing Emulsifier",
                "Mild natural emulsifier for micellar and oil cleansers.",
                "Safe for sensitive skin.");
        add("polyglyceryl-10 myristate", SafetyLevel.SAFE, "Cleansing Emulsifier",
                "Gentle emulsifier used in micellar waters and cleansing oils.",
                "Safe for all skin types.");
        add("polyglyceryl-4 laurate", SafetyLevel.SAFE, "Cleansing Emulsifier",
                "Natural emulsifier for gentle cleansers.",
                "Safe for all skin types.");
        add("sorbeth-30 tetraoleate", SafetyLevel.SAFE, "Cleansing Emulsifier",
                "Used in cleansing oils to emulsify and rinse off cleanly.",
                "Safe. Common in Korean cleansing oils.");
        add("peg-10 isostearate", SafetyLevel.SAFE, "Cleansing Emulsifier",
                "Emulsifier that helps cleansing oils remove sunscreen.",
                "Safe for most skin types.");

        // ── Micellar water ingredients ──────────────────────────────────────
        add("poloxamer 184", SafetyLevel.SAFE, "Surfactant",
                "Block copolymer surfactant used in micellar waters.",
                "Safe. Very mild and well-tolerated.");
        add("poloxamer 338", SafetyLevel.SAFE, "Surfactant",
                "Mild non-ionic surfactant used in micellar formulas.",
                "Safe for all skin types.");
        add("ppg-5-ceteth-20", SafetyLevel.SAFE, "Surfactant",
                "Non-ionic emulsifier used in micellar and gentle cleansers.",
                "Safe for all skin types.");
        add("hexylene glycol", SafetyLevel.SAFE, "Solvent",
                "Helps dissolve and solubilize ingredients in cleansers.",
                "Safe at cosmetic concentrations.");

        // ── Bar soap / solid cleanser ingredients ──────────────────────────
        add("sodium palmate", SafetyLevel.CAUTION, "Surfactant",
                "Saponified palm oil. Common in bar soaps.",
                "Can be drying. Comedogenic for some skin types.");
        add("sodium cocoate", SafetyLevel.CAUTION, "Surfactant",
                "Saponified coconut oil. Lathering agent in bar soaps.",
                "Can be drying and irritating for sensitive skin.");
        add("sodium tallowate", SafetyLevel.CAUTION, "Surfactant",
                "Animal fat-derived soap. Traditional bar soap ingredient.",
                "Can be drying. Not vegan.");
        add("potassium palmate", SafetyLevel.CAUTION, "Surfactant",
                "Soft soap from palm oil. Less drying than sodium soaps.",
                "Still potentially drying for sensitive skin.");
        add("sodium stearate", SafetyLevel.SAFE, "Surfactant",
                "Sodium salt of stearic acid. Used to harden bar soaps.",
                "Safe. Mild. Non-irritating.");
        add("glyceryl oleate", SafetyLevel.SAFE, "Emollient",
                "Conditioning agent in cleansers that counters dryness.",
                "Safe for all skin types.");

        // ═══════════════════════════════════════════════════════════════════
        //  TONER ACTIVES — EXPANDED
        //  Ingredients found in essences, toners, astringents,
        //  exfoliating toners, and hydrating toners.
        // ═══════════════════════════════════════════════════════════════════

        // ── Ferment / probiotic toner ingredients ──────────────────────────
        add("galactomyces ferment filtrate", SafetyLevel.SAFE, "Ferment",
                "Yeast-derived filtrate rich in vitamins, minerals, and amino acids. Brightening and hydrating.",
                "Safe. A staple in Korean glass-skin toners. Excellent tolerance.");
        add("galactomyces", SafetyLevel.SAFE, "Ferment",
                "Yeast ferment known for brightening and improving skin texture.",
                "Safe for all skin types.");
        add("saccharomyces ferment filtrate", SafetyLevel.SAFE, "Ferment",
                "Brewer's yeast ferment. Rich in B vitamins and antioxidants.",
                "Safe. Strengthens skin barrier.");
        add("saccharomyces/camellia sinensis leaf ferment filtrate", SafetyLevel.SAFE, "Ferment",
                "Green tea fermented with yeast for enhanced antioxidant activity.",
                "Safe for all skin types.");
        add("bifida ferment lysate", SafetyLevel.SAFE, "Ferment",
                "Probiotic ferment that reinforces the skin microbiome and barrier.",
                "Safe. Excellent for sensitive and reactive skin.");
        add("lactobacillus/kelp ferment filtrate", SafetyLevel.SAFE, "Ferment",
                "Seaweed fermented with lactobacillus. Hydrating and soothing.",
                "Safe for all skin types.");
        add("aspergillus ferment", SafetyLevel.SAFE, "Ferment",
                "Koji mold ferment used in Japanese skincare. Brightening.",
                "Safe. Traditional Japanese skincare active.");
        add("sake filtrate", SafetyLevel.SAFE, "Ferment",
                "Rice wine ferment. Brightening and hydrating.",
                "Safe. Contains kojic acid and amino acids naturally.");
        add("rice ferment filtrate", SafetyLevel.SAFE, "Ferment",
                "Fermented rice water. Brightening and conditioning.",
                "Safe for all skin types.");

        // ── Hydrating toner actives ─────────────────────────────────────────
        add("polyglutamic acid", SafetyLevel.SAFE, "Humectant",
                "Fermentation-derived humectant. Holds more moisture than hyaluronic acid.",
                "Safe for all skin types. Excellent layering ingredient.");
        add("sodium polyglutamate", SafetyLevel.SAFE, "Humectant",
                "Salt form of polyglutamic acid. Super-hydrating.",
                "Safe. Great for dehydrated skin.");
        add("beta-glucan", SafetyLevel.SAFE, "Soothing Agent",
                "Oat-derived polysaccharide. Deep hydration and wound healing.",
                "Safe for all skin types. Excellent for sensitive skin.");
        add("hyaluronic acid cross-polymer", SafetyLevel.SAFE, "Humectant",
                "Cross-linked HA for sustained surface hydration.",
                "Safe and well-tolerated.");
        add("low molecular weight hyaluronic acid", SafetyLevel.SAFE, "Humectant",
                "Small HA fragments that penetrate deeper into skin.",
                "Safe for all skin types.");
        add("hydrolyzed sodium hyaluronate", SafetyLevel.SAFE, "Humectant",
                "Fragmented HA with improved penetration.",
                "Safe. Plumps and hydrates.");
        add("acetyl glucosamine", SafetyLevel.SAFE, "Active Ingredient",
                "Brightening humectant. Reduces hyperpigmentation over time.",
                "Safe for all skin types.");
        add("sodium lactate", SafetyLevel.SAFE, "Humectant",
                "Natural moisturizing factor derived from lactic acid.",
                "Safe. Gently exfoliates while hydrating.");

        // ── Exfoliating toner actives ───────────────────────────────────────
        add("mandelic acid", SafetyLevel.SAFE, "AHA Exfoliant",
                "Large-molecule AHA from bitter almonds. Gentle and antibacterial.",
                "Safe even for sensitive skin. Less irritating than glycolic acid.");
        add("willow bark water", SafetyLevel.SAFE, "Botanical Extract",
                "Natural source of salicylates. Mild exfoliant.",
                "Safe. Gentler than pure salicylic acid.");
        add("salix alba (willow) bark extract", SafetyLevel.SAFE, "Botanical Extract",
                "Natural source of salicin. Mild exfoliating and anti-inflammatory.",
                "Safe for sensitive and acne-prone skin.");
        add("gluconodeltalactone", SafetyLevel.SAFE, "PHA Exfoliant",
                "Polyhydroxy acid exfoliant. Also functions as an antioxidant.",
                "Safe even for very sensitive skin. Does not increase sun sensitivity.");
        add("azeloglicina", SafetyLevel.SAFE, "Active Ingredient",
                "Potassium azeloyl diglycinate. Stable azelaic acid derivative.",
                "Safe for sensitive and acne-prone skin.");
        add("potassium azeloyl diglycinate", SafetyLevel.SAFE, "Active Ingredient",
                "Water-soluble azelaic acid derivative. Brightening and anti-acne.",
                "Safe for sensitive skin. More stable than pure azelaic acid.");

        // ── Astringent / clarifying toner ingredients ───────────────────────
        add("hamamelis virginiana (witch hazel) water", SafetyLevel.CAUTION, "Botanical Extract",
                "Distilled witch hazel. Astringent and anti-inflammatory.",
                "Tannin-based version can be drying. Seek alcohol-free versions.");
        add("zinc sulfate", SafetyLevel.SAFE, "Active Ingredient",
                "Astringent mineral that reduces sebum and soothes.",
                "Safe for oily and acne-prone skin.");
        add("aluminum acetate", SafetyLevel.CAUTION, "Astringent",
                "Strong astringent sometimes used in clarifying toners.",
                "Can be very drying. Use sparingly.");
        add("niacinamide 10%", SafetyLevel.CAUTION, "Vitamin B3 Derivative",
                "High-concentration niacinamide. Very effective but may cause flushing.",
                "Can cause temporary redness in some users. Start at 5% first.");

        // ── Essence / watery toner ingredients ─────────────────────────────
        add("mugwort extract", SafetyLevel.SAFE, "Botanical Extract",
                "Artemisia vulgaris. Calming, antioxidant, and microbiome-supporting.",
                "Safe. A K-beauty staple for sensitive and acne-prone skin.");
        add("artemisia vulgaris extract", SafetyLevel.SAFE, "Botanical Extract",
                "Mugwort extract. Soothing and anti-inflammatory.",
                "Safe for sensitive skin.");
        add("heartleaf extract", SafetyLevel.SAFE, "Botanical Extract",
                "Houttuynia cordata. Anti-inflammatory and antibacterial.",
                "Safe. Popular in Korean toners for acne-prone skin.");
        add("houttuynia cordata extract", SafetyLevel.SAFE, "Botanical Extract",
                "Heartleaf extract. Soothes, anti-inflammatory, and antimicrobial.",
                "Safe for acne-prone and sensitive skin.");
        add("cica extract", SafetyLevel.SAFE, "Botanical Extract",
                "Centella asiatica. Soothing, healing, and barrier-repairing.",
                "Safe for sensitive skin. Excellent for calming redness.");
        add("sea buckthorn water", SafetyLevel.SAFE, "Botanical Extract",
                "Hippophae rhamnoides. Rich in vitamins and antioxidants.",
                "Safe and nourishing for dry and sensitive skin.");
        add("hippophae rhamnoides water", SafetyLevel.SAFE, "Botanical Extract",
                "Sea buckthorn plant water. Antioxidant and nourishing.",
                "Safe for all skin types.");
        add("kombucha", SafetyLevel.SAFE, "Ferment",
                "Fermented black tea. Antioxidant and microbiome-supporting.",
                "Safe for all skin types.");
        add("saccharomyces/xylinum/black tea ferment", SafetyLevel.SAFE, "Ferment",
                "Kombucha ferment. Antioxidant and anti-aging.",
                "Safe for all skin types.");
        add("noni extract", SafetyLevel.SAFE, "Botanical Extract",
                "Morinda citrifolia. Antioxidant and soothing.",
                "Safe for sensitive skin.");
        add("birch sap", SafetyLevel.SAFE, "Botanical Extract",
                "Betula alba juice. Hydrating and mineral-rich.",
                "Safe for all skin types. Common in Scandinavian skincare.");
        add("betula alba juice", SafetyLevel.SAFE, "Botanical Extract",
                "Birch tree sap. Hydrating and soothing.",
                "Safe for all skin types.");

        // ═══════════════════════════════════════════════════════════════════
        //  SERUM ACTIVES — EXPANDED
        //  Targeted actives found in treatment serums, concentrates,
        //  and ampoules.
        // ═══════════════════════════════════════════════════════════════════

        // ── Brightening / pigmentation actives ─────────────────────────────
        add("kojic acid", SafetyLevel.CAUTION, "Active Ingredient",
                "Fungus-derived melanin inhibitor. Effective brightener.",
                "Can cause irritation and contact dermatitis at high concentrations. Patch test first.");
        add("kojic dipalmitate", SafetyLevel.SAFE, "Active Ingredient",
                "Stable, oil-soluble kojic acid ester. Gentler than pure kojic acid.",
                "Safe. Less irritating than kojic acid.");
        add("phytic acid", SafetyLevel.SAFE, "Active Ingredient",
                "Naturally occurring antioxidant that brightens and reduces pigmentation.",
                "Safe for sensitive skin.");
        add("diacetyl boldine", SafetyLevel.SAFE, "Active Ingredient",
                "Brightening active from boldo leaf. Inhibits melanin production.",
                "Safe for all skin types.");
        add("thiamidol", SafetyLevel.SAFE, "Active Ingredient",
                "Highly effective melanin inhibitor from Beiersdorf research.",
                "Safe. Clinically proven to reduce hyperpigmentation.");
        add("alpha arbutin 2%", SafetyLevel.SAFE, "Active Ingredient",
                "Effective brightening concentration. Inhibits tyrosinase.",
                "Safe for all skin types.");
        add("4-butylresorcinol", SafetyLevel.CAUTION, "Active Ingredient",
                "Potent brightening active (rucinol). Inhibits melanin synthesis.",
                "Effective but can cause irritation at higher concentrations. Patch test.");
        add("undecylenoyl phenylalanine", SafetyLevel.SAFE, "Active Ingredient",
                "Sepiwhite. Brightening active that reduces pigment synthesis signal.",
                "Safe for all skin types including sensitive.");
        add("phenylethyl resorcinol", SafetyLevel.SAFE, "Active Ingredient",
                "Symwhite 377. Potent brightener that inhibits tyrosinase.",
                "Safe. More potent than kojic acid with better tolerability.");
        add("hexylresorcinol", SafetyLevel.SAFE, "Active Ingredient",
                "Brightening active. Also used as a preservative.",
                "Safe at cosmetic concentrations.");
        add("magnesium ascorbyl phosphate", SafetyLevel.SAFE, "Antioxidant",
                "Stable, water-soluble Vitamin C derivative. Brightening and antioxidant.",
                "Safe and gentle. Good for sensitive skin.");
        add("ethyl ascorbic acid", SafetyLevel.SAFE, "Antioxidant",
                "3-O-Ethyl Ascorbic Acid. Stable Vitamin C with good penetration.",
                "Safe for all skin types. One of the most effective stable forms.");

        // ── Anti-aging / firming serums ────────────────────────────────────
        add("copper peptide", SafetyLevel.SAFE, "Peptide",
                "GHK-Cu. Promotes collagen and elastin. Wound healing.",
                "Safe. Do not layer with pure Vitamin C — can cause oxidation.");
        add("ghk-cu", SafetyLevel.SAFE, "Peptide",
                "Copper tripeptide-1. Regenerates skin and boosts collagen.",
                "Safe. Avoid mixing directly with high-concentration Vitamin C.");
        add("copper tripeptide-1", SafetyLevel.SAFE, "Peptide",
                "GHK-Cu. Promotes collagen synthesis and skin renewal.",
                "Safe. One of the most studied anti-aging peptides.");
        add("idebenone", SafetyLevel.SAFE, "Antioxidant",
                "Synthetic coenzyme Q10 analogue. Potent antioxidant.",
                "Safe. More stable and potentially more effective than CoQ10.");
        add("epidermal growth factor", SafetyLevel.CAUTION, "Active Ingredient",
                "EGF. Promotes cell renewal and wound healing.",
                "Generally safe but can stimulate all cell types — avoid with active breakouts.");
        add("egf", SafetyLevel.CAUTION, "Active Ingredient",
                "Epidermal growth factor. Stimulates skin cell regeneration.",
                "Use with caution. Avoid during active skin infections or cancer.");
        add("sh-oligopeptide-1", SafetyLevel.CAUTION, "Peptide",
                "Recombinant EGF. Promotes cell turnover and healing.",
                "Effective but use with caution — stimulates cell proliferation.");
        add("sh-polypeptide-1", SafetyLevel.SAFE, "Peptide",
                "Fibroblast growth factor. Stimulates collagen production.",
                "Safe at cosmetic concentrations.");
        add("snap-8", SafetyLevel.SAFE, "Peptide",
                "Acetyl glutamyl heptapeptide-3. Reduces expression lines.",
                "Safe for all skin types.");
        add("acetyl glutamyl heptapeptide-3", SafetyLevel.SAFE, "Peptide",
                "SNAP-8 peptide. Relaxes facial muscles to reduce wrinkles.",
                "Safe. Topical alternative to botulinum-based treatments.");
        add("leuphasyl", SafetyLevel.SAFE, "Peptide",
                "Pentapeptide-18. Synergistic with SNAP-8 for wrinkle reduction.",
                "Safe for all skin types.");
        add("pentapeptide-18", SafetyLevel.SAFE, "Peptide",
                "Opioid peptide that relaxes facial muscle contractions.",
                "Safe for all skin types.");
        add("matrixyl 3000", SafetyLevel.SAFE, "Peptide",
                "Blend of palmitoyl tetrapeptide-7 and palmitoyl oligopeptide.",
                "Safe. Well-researched anti-aging peptide complex.");
        add("argireline", SafetyLevel.SAFE, "Peptide",
                "Acetyl hexapeptide-8. Reduces depth of expression lines.",
                "Safe for all skin types.");
        add("syn-ake", SafetyLevel.SAFE, "Peptide",
                "Dipeptide diaminobutyroyl benzylamide diacetate. Snake venom mimic.",
                "Safe. Reduces muscle contraction-related wrinkles.");
        add("eyeliss", SafetyLevel.SAFE, "Peptide",
                "Hesperidin methyl chalcone + dipeptide-2 + palmitoyl tetrapeptide-7.",
                "Safe. Clinically shown to reduce under-eye puffiness.");
        add("leuphasyl", SafetyLevel.SAFE, "Peptide",
                "Pentapeptide that relaxes micro-contractions.",
                "Safe for all skin types.");

        // ── Retinoid family (serums) ────────────────────────────────────────
        add("retinal", SafetyLevel.CAUTION, "Retinoid",
                "Retinaldehyde. 11x stronger than retinol but gentler than tretinoin.",
                "Start low. May cause irritation. Always use SPF.");
        add("hydroxypinacolone retinoate", SafetyLevel.SAFE, "Retinoid",
                "Granactive Retinoid. Next-gen retinoid that doesn't convert to retinoic acid.",
                "Much gentler than retinol. Safe for sensitive skin. Still use SPF.");
        add("granactive retinoid", SafetyLevel.SAFE, "Retinoid",
                "Hydroxypinacolone retinoate. Next-generation gentler retinoid.",
                "Safe even for sensitive skin. Still use SPF.");
        add("retinol acetate", SafetyLevel.CAUTION, "Retinoid",
                "Ester form of retinol. Gentler but less potent.",
                "May cause irritation. Start slowly. Always use SPF.");
        add("bakuchiol", SafetyLevel.SAFE, "Active Ingredient",
                "Plant-based retinol alternative from babchi seeds.",
                "Safe in pregnancy. Gentler option with similar anti-aging effects.");

        // ── Exfoliating serum actives ───────────────────────────────────────
        add("polyhydroxy acid", SafetyLevel.SAFE, "PHA Exfoliant",
                "Next-generation AHA. Gentle exfoliation with humectant properties.",
                "Safe for sensitive skin. Does not increase sun sensitivity.");
        add("pha", SafetyLevel.SAFE, "PHA Exfoliant",
                "Polyhydroxy acid. Gentle exfoliant suitable for sensitive skin.",
                "Safe. Ideal for reactive or compromised skin.");
        add("tca", SafetyLevel.HARMFUL, "Chemical Exfoliant",
                "Trichloroacetic acid. Professional-grade chemical peel agent.",
                "Only for professional use. Can cause severe burns at high concentration.");
        add("trichloroacetic acid", SafetyLevel.HARMFUL, "Chemical Exfoliant",
                "Professional chemical peel. Not for at-home use.",
                "Can cause burns and scarring if misused. Professional use only.");
        add("jessner's solution", SafetyLevel.HARMFUL, "Chemical Exfoliant",
                "Combination of salicylic acid, lactic acid, and resorcinol.",
                "Professional peel. Not for at-home use.");

        // ── Anti-acne serum actives ─────────────────────────────────────────
        add("benzoyl peroxide", SafetyLevel.CAUTION, "Active Ingredient",
                "Kills acne bacteria (C. acnes). Effective for inflammatory acne.",
                "Can bleach fabric and hair. Very drying. Start at 2.5%.");
        add("sulfur", SafetyLevel.CAUTION, "Active Ingredient",
                "Antimicrobial mineral. Reduces excess oil and unclogs pores.",
                "Can have a strong odor. May be drying at high concentrations.");
        add("resorcinol", SafetyLevel.CAUTION, "Active Ingredient",
                "Keratolytic and antimicrobial. Used in acne and dandruff treatments.",
                "Can cause irritation. Potential endocrine disruptor at high doses.");
        add("chlorhexidine gluconate", SafetyLevel.CAUTION, "Antimicrobial",
                "Broad-spectrum antimicrobial used in some acne serums.",
                "Can disrupt skin microbiome with prolonged use. Use as directed.");
        add("tea tree extract", SafetyLevel.CAUTION, "Botanical Extract",
                "Antimicrobial plant extract effective against C. acnes.",
                "Dilute before use. Can cause sensitization.");
        add("bakuchiol serum", SafetyLevel.SAFE, "Active Ingredient",
                "High-concentration bakuchiol for anti-aging and acne.",
                "Safe in pregnancy. Gentle on sensitive skin.");

        // ── Serum soothing / barrier actives ───────────────────────────────
        add("ceramide complex", SafetyLevel.SAFE, "Skin-Identical Lipid",
                "Blend of multiple ceramide types for comprehensive barrier repair.",
                "Safe for all skin types. Essential for dry and compromised skin.");
        add("oat extract", SafetyLevel.SAFE, "Botanical Extract",
                "Avena sativa. Anti-inflammatory and deeply soothing.",
                "Safe. The gold standard for sensitive and eczema-prone skin.");
        add("avena sativa kernel extract", SafetyLevel.SAFE, "Botanical Extract",
                "Colloidal oat extract. Calms inflammation and soothes.",
                "Safe for all skin types including eczema-prone.");
        add("colloidal oatmeal", SafetyLevel.SAFE, "Soothing Agent",
                "FDA-approved skin protectant. Reduces itching and irritation.",
                "Safe for all skin types including babies.");
        add("feverfew extract", SafetyLevel.SAFE, "Botanical Extract",
                "Chrysanthemum parthenium. Soothes redness and irritation.",
                "Safe for sensitive skin.");
        add("chrysanthemum parthenium extract", SafetyLevel.SAFE, "Botanical Extract",
                "Feverfew. Anti-inflammatory and redness-reducing.",
                "Safe for reactive and sensitive skin.");
        add("madecassic acid", SafetyLevel.SAFE, "Active Ingredient",
                "Centella asiatica derivative. Anti-inflammatory and healing.",
                "Safe for sensitive and acne-prone skin.");
        add("asiatic acid", SafetyLevel.SAFE, "Active Ingredient",
                "Centella asiatica triterpenoid. Boosts collagen and soothes.",
                "Safe for all skin types.");
        add("escin", SafetyLevel.SAFE, "Active Ingredient",
                "Horse chestnut-derived active. Reduces puffiness and strengthens capillaries.",
                "Safe. Commonly used in eye serums.");
        add("dipeptide-2", SafetyLevel.SAFE, "Peptide",
                "Valine-tryptophan. Reduces under-eye puffiness and dark circles.",
                "Safe. Common in eye serum formulas.");
        add("hesperidin methyl chalcone", SafetyLevel.SAFE, "Active Ingredient",
                "Strengthens capillary walls. Reduces under-eye darkness.",
                "Safe for all skin types.");

        // ── Serum hydration boosters ────────────────────────────────────────
        add("ceramide ng", SafetyLevel.SAFE, "Skin-Identical Lipid",
                "Type 2 ceramide. Restores skin barrier.",
                "Safe for dry and sensitive skin.");
        add("ceramide ns", SafetyLevel.SAFE, "Skin-Identical Lipid",
                "Type 1 ceramide. Key component of the skin barrier.",
                "Safe for all skin types.");
        add("ceramide as", SafetyLevel.SAFE, "Skin-Identical Lipid",
                "Type 6 ceramide. Supports skin barrier integrity.",
                "Safe for dry and sensitive skin.");
        add("sphingomyelin", SafetyLevel.SAFE, "Skin-Identical Lipid",
                "Natural lipid that supports the skin barrier.",
                "Safe for all skin types.");
        add("sea kelp extract", SafetyLevel.SAFE, "Botanical Extract",
                "Laminaria digitata. Hydrating and mineral-rich.",
                "Safe for all skin types.");
        add("laminaria digitata extract", SafetyLevel.SAFE, "Botanical Extract",
                "Sea kelp. Provides minerals, hydrates, and soothes.",
                "Safe for all skin types.");
        add("marine collagen", SafetyLevel.SAFE, "Active Ingredient",
                "Fish-derived collagen peptides. Hydrating and film-forming.",
                "Safe. Note: topical collagen cannot replace internal collagen.");
        add("hydrolyzed collagen", SafetyLevel.SAFE, "Active Ingredient",
                "Broken-down collagen fragments. Hydrating and film-forming.",
                "Safe for all skin types.");
        add("fibronectin", SafetyLevel.SAFE, "Active Ingredient",
                "Skin matrix protein that supports adhesion and wound healing.",
                "Safe. Used in high-performance serums.");
        add("epidermal lipids", SafetyLevel.SAFE, "Skin-Identical Lipid",
                "Blend of skin-native lipids including ceramides, cholesterol, and fatty acids.",
                "Safe. Ideal for dry and compromised skin.");

        // ── Special / next-gen serum actives ───────────────────────────────
        add("niacinamide 20%", SafetyLevel.CAUTION, "Vitamin B3 Derivative",
                "Very high concentration niacinamide. Highly effective but risk of flushing.",
                "Can cause skin flushing in some users. Start at lower concentrations first.");
        add("spermidine", SafetyLevel.SAFE, "Active Ingredient",
                "Polyamine that supports skin cell renewal and autophagy.",
                "Safe. Emerging anti-aging active.");
        add("ergothioneine", SafetyLevel.SAFE, "Antioxidant",
                "Amino acid antioxidant with exceptional stability.",
                "Safe. Protects cells from oxidative stress.");
        add("carnosine", SafetyLevel.SAFE, "Antioxidant",
                "Dipeptide that inhibits glycation and fights aging.",
                "Safe for all skin types.");
        add("acetyl carnitine", SafetyLevel.SAFE, "Active Ingredient",
                "Energizes skin cells and supports mitochondrial function.",
                "Safe. Used in anti-aging and brightening serums.");
        add("creatine", SafetyLevel.SAFE, "Active Ingredient",
                "Energizes skin cells and supports barrier function.",
                "Safe. Found in anti-fatigue and firming serums.");
        add("dmae", SafetyLevel.CAUTION, "Active Ingredient",
                "Dimethylaminoethanol. Claimed to firm skin; some evidence of cellular effects.",
                "Controversial safety profile. Some studies suggest it may damage cell membranes.");
        add("dimethylaminoethanol", SafetyLevel.CAUTION, "Active Ingredient",
                "DMAE. Claimed firming active. Safety debated.",
                "Some research suggests possible cell membrane disruption. Use with caution.");
        add("stem cell extract", SafetyLevel.SAFE, "Active Ingredient",
                "Plant stem cell extract. Antioxidant and protective.",
                "Safe. Topical plant stem cells do not interact with human stem cells.");
        add("exosome", SafetyLevel.CAUTION, "Active Ingredient",
                "Cell-derived vesicles used as delivery systems for actives.",
                "Emerging ingredient. Long-term safety not fully established.");
        add("snail mucin", SafetyLevel.SAFE, "Active Ingredient",
                "Snail secretion filtrate. Hydrating, healing, and brightening.",
                "Safe for most. Rare contact allergy possible.");
        add("snail secretion filtrate", SafetyLevel.SAFE, "Active Ingredient",
                "Helix aspersa mucin. Multi-functional healing active.",
                "Safe. Excellent tolerance in studies.");
        add("helix aspersa muus filtrate", SafetyLevel.SAFE, "Active Ingredient",
                "Snail mucin. Promotes healing and hydration.",
                "Safe for all skin types.");
        add("bee venom", SafetyLevel.CAUTION, "Active Ingredient",
                "Apitoxin. Used as a natural botox alternative.",
                "Avoid if allergic to bee stings. Patch test essential.");
        add("royal jelly", SafetyLevel.CAUTION, "Active Ingredient",
                "Bee-produced compound rich in fatty acids and vitamins.",
                "Can cause allergic reactions in people sensitive to bee products.");
        add("propolis extract", SafetyLevel.SAFE, "Botanical Extract",
                "Bee resin. Antimicrobial, anti-inflammatory, and soothing.",
                "Safe. Avoid if allergic to bee products.");
        add("centella asiatica water", SafetyLevel.SAFE, "Botanical Extract",
                "Distilled cica water. Soothing and barrier-supporting.",
                "Safe for sensitive skin.");
    }


    /** Lookup an ingredient by name (case-insensitive) */
    public Ingredient lookup(String name) {
        if (name == null) return null;
        String key = name.trim().toLowerCase();
        if (database.containsKey(key)) return database.get(key);
        for (Map.Entry<String, Ingredient> entry : database.entrySet()) {
            if (key.contains(entry.getKey()) || entry.getKey().contains(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /** Returns unknown/unclassified ingredient entry */
    public Ingredient getUnknownIngredient(String name) {
        Ingredient unknown = new Ingredient(name, SafetyLevel.CAUTION, "Unknown",
                "This ingredient is not in our database.",
                "Safety profile unknown. Research recommended.");

        unknown.setHazardScore(5);
        unknown.setComedogenicRating(0);
        unknown.setAllergenInfo("Unknown");
        return unknown;
    }

    /** Legacy add method for backwards compatibility with existing populate() calls */
    private void add(String name, SafetyLevel level, String category,
                     String description, String risk) {
        Ingredient ing = new Ingredient(name, level, category, description, risk);

        if (level == SafetyLevel.HARMFUL) ing.setHazardScore(8);
        else if (level == SafetyLevel.CAUTION) ing.setHazardScore(4);
        else ing.setHazardScore(1);

        ing.setComedogenicRating(0);
        ing.setAllergenInfo("None");

        database.put(name.toLowerCase(), ing);
    }
    public void addDetailed(String name, SafetyLevel level, String category,
                            String description, String risk, int hazard, int comedogenic, String allergens) {
        Ingredient ing = new Ingredient(name, level, category, description, risk);
        ing.setHazardScore(hazard);
        ing.setComedogenicRating(comedogenic);
        ing.setAllergenInfo(allergens);
        database.put(name.toLowerCase(), ing);
    }

    public int getDatabaseSize() { return database.size(); }
}