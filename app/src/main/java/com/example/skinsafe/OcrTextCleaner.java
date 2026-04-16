package com.example.skinsafe;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class OcrTextCleaner {

    private static final String TAG = "OcrTextCleaner";

    // ──────────────────────────────────────────────────────────────────────────
    // INGREDIENT NAME CORRECTION TABLE
    // ──────────────────────────────────────────────────────────────────────────
    private static final Map<String, String> NAME_FIXES = new HashMap<>();
    static {
        // --- Water / basics ---
        NAME_FIXES.put("aqua",              "Aqua");
        NAME_FIXES.put("aquo",              "Aqua");
        NAME_FIXES.put("aque",              "Aqua");
        NAME_FIXES.put("waler",             "Water");
        NAME_FIXES.put("wafer",             "Water");
        NAME_FIXES.put("vvater",            "Water");

        // --- Common humectants ---
        NAME_FIXES.put("glycenn",           "Glycerin");
        NAME_FIXES.put("glycetin",          "Glycerin");
        NAME_FIXES.put("glycerine",         "Glycerin");
        NAME_FIXES.put("glycerol",          "Glycerin");
        NAME_FIXES.put("giycerin",          "Glycerin");
        NAME_FIXES.put("butylene glyco",    "Butylene Glycol");
        NAME_FIXES.put("butyleneglyco",     "Butylene Glycol");
        NAME_FIXES.put("propylene glyco",   "Propylene Glycol");
        NAME_FIXES.put("pentylene glyco",   "Pentylene Glycol");
        NAME_FIXES.put("hyaluronic acld",   "Hyaluronic Acid");
        NAME_FIXES.put("hyaluronicacid",    "Hyaluronic Acid");
        NAME_FIXES.put("hyaiuronic acid",   "Hyaluronic Acid");

        // --- Niacinamide variants ---
        NAME_FIXES.put("niacinarnide",      "Niacinamide");
        NAME_FIXES.put("niacinarride",      "Niacinamide");
        NAME_FIXES.put("niacinamide",       "Niacinamide");
        NAME_FIXES.put("niaci namide",      "Niacinamide");
        NAME_FIXES.put("niacin amide",      "Niacinamide");

        // --- Preservatives ---
        NAME_FIXES.put("phenoxyethano",     "Phenoxyethanol");
        NAME_FIXES.put("phenoxyethanol",    "Phenoxyethanol");
        NAME_FIXES.put("phenoxy ethanol",   "Phenoxyethanol");
        NAME_FIXES.put("methylparaben",     "Methylparaben");
        NAME_FIXES.put("rnethylparaben",    "Methylparaben");
        NAME_FIXES.put("propylparaben",     "Propylparaben");
        NAME_FIXES.put("propylparab",       "Propylparaben");
        NAME_FIXES.put("ethylparaben",      "Ethylparaben");
        NAME_FIXES.put("butylparaben",      "Butylparaben");

        // --- Surfactants ---
        NAME_FIXES.put("sodium lauryl sulf",   "Sodium Lauryl Sulfate");
        NAME_FIXES.put("sodlum lauryl",        "Sodium Lauryl Sulfate");
        NAME_FIXES.put("sodium laureth",       "Sodium Laureth Sulfate");
        NAME_FIXES.put("sodlum laureth",       "Sodium Laureth Sulfate");
        NAME_FIXES.put("cocamidopropyl betaln","Cocamidopropyl Betaine");
        NAME_FIXES.put("cocamldopropyl",       "Cocamidopropyl Betaine");

        // --- Alcohols ---
        NAME_FIXES.put("cetyl alcoho",      "Cetyl Alcohol");
        NAME_FIXES.put("cetyl alcohoi",     "Cetyl Alcohol");
        NAME_FIXES.put("stearyl alcoho",    "Stearyl Alcohol");
        NAME_FIXES.put("stearyi alcohol",   "Stearyl Alcohol");
        NAME_FIXES.put("behenyl alcoho",    "Behenyl Alcohol");

        // --- Emollients / esters ---
        NAME_FIXES.put("dimethicone",       "Dimethicone");
        NAME_FIXES.put("dirrlethicone",     "Dimethicone");
        NAME_FIXES.put("cyclomethicone",    "Cyclomethicone");
        NAME_FIXES.put("cyclopentasiloxan", "Cyclopentasiloxane");
        NAME_FIXES.put("isopropyl myristat","Isopropyl Myristate");
        NAME_FIXES.put("isopropyl rnyrista","Isopropyl Myristate");
        NAME_FIXES.put("caprylic capric",   "Caprylic/Capric Triglyceride");
        NAME_FIXES.put("caprylic/capric",   "Caprylic/Capric Triglyceride");
        NAME_FIXES.put("cetearyl alcoho",   "Cetearyl Alcohol");
        NAME_FIXES.put("cetearyi alcohol",  "Cetearyl Alcohol");

        // --- Antioxidants ---
        NAME_FIXES.put("tocopherol",        "Tocopherol");
        NAME_FIXES.put("tocopheroi",        "Tocopherol");
        NAME_FIXES.put("vitamin e",         "Tocopherol");
        NAME_FIXES.put("ascorbic acld",     "Ascorbic Acid");
        NAME_FIXES.put("ascorbic acid",     "Ascorbic Acid");
        NAME_FIXES.put("retinol",           "Retinol");
        NAME_FIXES.put("retlnol",           "Retinol");

        // --- Acids / exfoliants ---
        NAME_FIXES.put("glycolic acld",     "Glycolic Acid");
        NAME_FIXES.put("glycolic acid",     "Glycolic Acid");
        NAME_FIXES.put("salicylic acld",    "Salicylic Acid");
        NAME_FIXES.put("salicylic acid",    "Salicylic Acid");
        NAME_FIXES.put("lactic acld",       "Lactic Acid");
        NAME_FIXES.put("lactic acid",       "Lactic Acid");
        NAME_FIXES.put("azelaic acld",      "Azelaic Acid");
        NAME_FIXES.put("mandelic acld",     "Mandelic Acid");
        NAME_FIXES.put("polyglutamic acld", "Polyglutamic Acid");
        NAME_FIXES.put("citric acld",       "Citric Acid");
        NAME_FIXES.put("citric acid",       "Citric Acid");

        // --- Botanical / ferment ---
        NAME_FIXES.put("galactomyces ferrnent",    "Galactomyces Ferment Filtrate");
        NAME_FIXES.put("galactomyces ferment",     "Galactomyces Ferment Filtrate");
        NAME_FIXES.put("bifida ferrnent",          "Bifida Ferment Lysate");
        NAME_FIXES.put("bifida ferment",           "Bifida Ferment Lysate");
        NAME_FIXES.put("centella asiatica",        "Centella Asiatica Extract");
        NAME_FIXES.put("centella asiatlca",        "Centella Asiatica Extract");
        NAME_FIXES.put("aloe vera",                "Aloe Barbadensis Leaf Extract");
        NAME_FIXES.put("aloe barbadensis",         "Aloe Barbadensis Leaf Extract");
        NAME_FIXES.put("green tea extract",        "Camellia Sinensis Leaf Extract");
        NAME_FIXES.put("com starch", "Corn Starch");
        NAME_FIXES.put("hydrolyzed com starch", "Hydrolyzed Corn Starch");
        NAME_FIXES.put("hydrolyzed corn starch", "Hydrolyzed Corn Starch");
        NAME_FIXES.put("sderotium gum", "Sclerotium Gum");
        NAME_FIXES.put("sclerotium gum", "Sclerotium Gum");
        NAME_FIXES.put("beta vulgaris", "Beta Vulgaris (Beet) Root Extract");

        // --- Sunscreen filters ---
        NAME_FIXES.put("avobenzone",               "Avobenzone");
        NAME_FIXES.put("avob enzone",              "Avobenzone");
        NAME_FIXES.put("octocrylene",              "Octocrylene");
        NAME_FIXES.put("octocry lene",             "Octocrylene");
        NAME_FIXES.put("octinoxate",               "Octinoxate");
        NAME_FIXES.put("homosalate",               "Homosalate");
        NAME_FIXES.put("zinc oxlde",               "Zinc Oxide");
        NAME_FIXES.put("zinc oxide",               "Zinc Oxide");
        NAME_FIXES.put("titanium dloxide",         "Titanium Dioxide");
        NAME_FIXES.put("titanium dioxide",         "Titanium Dioxide");

        // --- Ceramides ---
        NAME_FIXES.put("ceramide np",              "Ceramide NP");
        NAME_FIXES.put("ceramide ap",              "Ceramide AP");
        NAME_FIXES.put("ceramide eop",             "Ceramide EOP");
        NAME_FIXES.put("ceramlde",                 "Ceramide");

        // --- Peptides ---
        NAME_FIXES.put("palmitoyl tripeptide",     "Palmitoyl Tripeptide-1");
        NAME_FIXES.put("acetyl hexapeptlde",       "Acetyl Hexapeptide-8");
        NAME_FIXES.put("copper tripeptlde",        "Copper Tripeptide-1");

        // --- Miscellaneous ---
        NAME_FIXES.put("carbomer",                 "Carbomer");
        NAME_FIXES.put("carborner",                "Carbomer");
        NAME_FIXES.put("xanthan gum",              "Xanthan Gum");
        NAME_FIXES.put("xanthan gurri",            "Xanthan Gum");
        NAME_FIXES.put("sodium hyaluronate",       "Sodium Hyaluronate");
        NAME_FIXES.put("sodium hyalurornate",      "Sodium Hyaluronate");
        NAME_FIXES.put("sodium hydroxide",         "Sodium Hydroxide");
        NAME_FIXES.put("sodlum hydroxide",         "Sodium Hydroxide");
        NAME_FIXES.put("disodium edta",            "Disodium EDTA");
        NAME_FIXES.put("dlsodium edta",            "Disodium EDTA");
        NAME_FIXES.put("fragrance",                "Fragrance");
        NAME_FIXES.put("parfum",                   "Parfum");
        NAME_FIXES.put("mica",                     "Mica");
    }

    private static final Set<String> JUNK_TOKENS = new HashSet<>(Arrays.asList(
            "ingredients", "ingredient", "contains", "inci", "list", "the", "and",
            "with", "free", "in", "for", "of", "a", "an", "by", "per",
            "active", "inactive", "other", "formula", "full", "all"
    ));

    private static final Pattern JUNK_PATTERN =
            Pattern.compile("^[\\d\\s\\-\\.\\%\\*\\+\\/\\(\\)]+$|^[a-zA-Z]$");

    // ──────────────────────────────────────────────────────────────────────────
    // PUBLIC ENTRY POINT
    // ──────────────────────────────────────────────────────────────────────────

    public static String clean(String raw) {
        if (raw == null || raw.trim().isEmpty()) return "";

        String text = stripHeader(raw);

        text = normalizeSeparators(text);

        String[] tokens = text.split(",");
        List<String> cleaned = new ArrayList<>();

        for (String token : tokens) {
            String t = token.trim();
            if (t.isEmpty()) continue;
            t = applyNameFix(t);

            if (isJunk(t)) {
                Log.d(TAG, "Dropping junk token: '" + t + "'");
                continue;
            }

            cleaned.add(t);
        }

        String result = String.join(", ", cleaned);
        Log.d(TAG, "OcrTextCleaner output: " + result);
        return result;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // PRIVATE HELPERS
    // ──────────────────────────────────────────────────────────────────────────

    private static String stripHeader(String text) {
        return text.replaceAll("(?i)^\\s*(full\\s+)?ingredients?\\s*(list)?\\s*[:\\-]?\\s*", "").trim();
    }

    private static String normalizeSeparators(String text) {
        return text
                .replaceAll("\r\n|\r|\n", ", ")
                .replaceAll("[•·;|]", ",")
                .replaceAll(",\\s*,+", ",")
                .replaceAll("\\s{2,}", " ")
                .trim();
    }

    private static String applyNameFix(String token) {
        String lower = token.toLowerCase().trim();

        if (NAME_FIXES.containsKey(lower)) return NAME_FIXES.get(lower);

        for (Map.Entry<String, String> entry : NAME_FIXES.entrySet()) {
            if (lower.startsWith(entry.getKey())) {
                String suffix = token.substring(entry.getKey().length()).trim();
                if (suffix.isEmpty() || suffix.matches("^[\\d\\s%\\.\\-\\(\\)]+$")) {
                    return entry.getValue();
                }
            }
        }

        return token;
    }

    private static boolean isJunk(String token) {
        if (token.length() <= 1) return true;
        if (JUNK_TOKENS.contains(token.toLowerCase())) return true;
        if (JUNK_PATTERN.matcher(token).matches()) return true;
        if (token.length() > 60 && !token.contains(" ")) return true;
        return false;
    }
}