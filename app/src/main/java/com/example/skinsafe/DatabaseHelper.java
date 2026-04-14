package com.example.skinsafe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "skinsafe.db";

    private static final int DATABASE_VERSION = 2;

    // --- TABLE NAMES ---
    private static final String TABLE_USERS = "users";
    private static final String TABLE_SCANS = "scans";
    private static final String TABLE_SCAN_INGREDIENTS = "scan_ingredients";

    // --- USERS TABLE COLUMNS ---
    private static final String COL_USER_ID = "id";
    private static final String COL_USER_FIRST = "first_name";
    private static final String COL_USER_LAST = "last_name";
    private static final String COL_USER_EMAIL = "email";
    private static final String COL_USER_PASS = "password_hash";
    private static final String COL_USER_SKIN_TYPES = "skin_types";
    private static final String COL_USER_CONCERNS = "skin_concerns";
    private static final String COL_USER_TOTAL_SCANS = "total_scans";
    private static final String COL_USER_FLAGGED = "flagged_count";
    private static final String COL_USER_SAVED = "saved_count";

    // --- SCANS TABLE COLUMNS ---
    private static final String COL_SCAN_ID = "id";
    private static final String COL_SCAN_USER_ID = "user_id";
    private static final String COL_SCAN_PRODUCT = "product_name";
    private static final String COL_SCAN_RAW = "raw_text";
    private static final String COL_SCAN_DATE = "scan_date";
    private static final String COL_SCAN_METHOD = "input_method";
    private static final String COL_SCAN_AI_INSIGHT = "ai_insight";
    private static final String COL_SCAN_SAVED = "is_saved";
    private static final String COL_SCAN_FAVORITE = "is_favorite";
    private static final String COL_SCAN_SAFE_COUNT = "safe_count";
    private static final String COL_SCAN_CAUTION_COUNT = "caution_count";
    private static final String COL_SCAN_HARMFUL_COUNT = "harmful_count";
    private static final String COL_SCAN_OVERALL = "overall_safety";

    // --- SCAN INGREDIENTS TABLE COLUMNS (UPDATED) ---
    private static final String COL_ING_ID = "id";
    private static final String COL_ING_SCAN_ID = "scan_id";
    private static final String COL_ING_NAME = "name";
    private static final String COL_ING_SAFETY = "safety_level";
    private static final String COL_ING_CATEGORY = "category";
    private static final String COL_ING_DESCRIPTION = "description";
    private static final String COL_ING_RISK = "risk_note";
    private static final String COL_ING_FLAGGED = "is_flagged";
    private static final String COL_ING_HAZARD = "hazard_score";
    private static final String COL_ING_COMEDOGENIC = "comedogenic_rating";
    private static final String COL_ING_ALLERGEN = "allergen_info";
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users table
        String createUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_FIRST + " TEXT NOT NULL, " +
                COL_USER_LAST + " TEXT NOT NULL, " +
                COL_USER_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COL_USER_PASS + " TEXT NOT NULL, " +
                COL_USER_SKIN_TYPES + " TEXT DEFAULT '', " +
                COL_USER_CONCERNS + " TEXT DEFAULT '', " +
                COL_USER_TOTAL_SCANS + " INTEGER DEFAULT 0, " +
                COL_USER_FLAGGED + " INTEGER DEFAULT 0, " +
                COL_USER_SAVED + " INTEGER DEFAULT 0)";

        // Scans table
        String createScans = "CREATE TABLE " + TABLE_SCANS + " (" +
                COL_SCAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SCAN_USER_ID + " INTEGER NOT NULL, " +
                COL_SCAN_PRODUCT + " TEXT DEFAULT 'Unknown Product', " +
                COL_SCAN_RAW + " TEXT, " +
                COL_SCAN_DATE + " TEXT NOT NULL, " +
                COL_SCAN_METHOD + " TEXT DEFAULT 'manual', " +
                COL_SCAN_AI_INSIGHT + " TEXT DEFAULT '', " +
                COL_SCAN_SAVED + " INTEGER DEFAULT 0, " +
                COL_SCAN_FAVORITE + " INTEGER DEFAULT 0, " +
                COL_SCAN_SAFE_COUNT + " INTEGER DEFAULT 0, " +
                COL_SCAN_CAUTION_COUNT + " INTEGER DEFAULT 0, " +
                COL_SCAN_HARMFUL_COUNT + " INTEGER DEFAULT 0, " +
                COL_SCAN_OVERALL + " TEXT DEFAULT 'SAFE', " +
                "FOREIGN KEY(" + COL_SCAN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";

        // Scan ingredients table (ADDED NEW COLUMNS)
        String createIngredients = "CREATE TABLE " + TABLE_SCAN_INGREDIENTS + " (" +
                COL_ING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ING_SCAN_ID + " INTEGER NOT NULL, " +
                COL_ING_NAME + " TEXT NOT NULL, " +
                COL_ING_SAFETY + " TEXT NOT NULL, " +
                COL_ING_CATEGORY + " TEXT DEFAULT '', " +
                COL_ING_DESCRIPTION + " TEXT DEFAULT '', " +
                COL_ING_RISK + " TEXT DEFAULT '', " +
                COL_ING_FLAGGED + " INTEGER DEFAULT 0, " +
                COL_ING_HAZARD + " INTEGER DEFAULT 1, " +
                COL_ING_COMEDOGENIC + " INTEGER DEFAULT 0, " +
                COL_ING_ALLERGEN + " TEXT DEFAULT '', " +
                "FOREIGN KEY(" + COL_ING_SCAN_ID + ") REFERENCES " + TABLE_SCANS + "(" + COL_SCAN_ID + "))";

        db.execSQL(createUsers);
        db.execSQL(createScans);
        db.execSQL(createIngredients);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCAN_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCANS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ======================= USER OPERATIONS =======================

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return password;
        }
    }

    public long registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_FIRST, user.getFirstName());
        cv.put(COL_USER_LAST, user.getLastName());
        cv.put(COL_USER_EMAIL, user.getEmail().toLowerCase().trim());
        cv.put(COL_USER_PASS, hashPassword(user.getPasswordHash()));
        cv.put(COL_USER_SKIN_TYPES, String.join(",", user.getSkinTypes()));
        cv.put(COL_USER_CONCERNS, String.join(",", user.getSkinConcerns()));
        long id = db.insert(TABLE_USERS, null, cv);
        db.close();
        return id;
    }

    public User loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedPass = hashPassword(password);
        Cursor cursor = db.query(TABLE_USERS, null,
                COL_USER_EMAIL + "=? AND " + COL_USER_PASS + "=?",
                new String[]{email.toLowerCase().trim(), hashedPass},
                null, null, null);

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = cursorToUser(cursor);
            cursor.close();
        }
        db.close();
        return user;
    }

    public boolean emailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_USER_ID},
                COL_USER_EMAIL + "=?", new String[]{email.toLowerCase().trim()},
                null, null, null);
        boolean exists = cursor != null && cursor.getCount() > 0;
        if (cursor != null) cursor.close();
        db.close();
        return exists;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COL_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null);
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = cursorToUser(cursor);
            cursor.close();
        }
        db.close();
        return user;
    }

    public void updateUserProfile(int userId, List<String> skinTypes, List<String> concerns) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_SKIN_TYPES, String.join(",", skinTypes));
        cv.put(COL_USER_CONCERNS, String.join(",", concerns));
        db.update(TABLE_USERS, cv, COL_USER_ID + "=?", new String[]{String.valueOf(userId)});
        db.close();
    }

    public void incrementScanCount(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_USERS + " SET " + COL_USER_TOTAL_SCANS +
                " = " + COL_USER_TOTAL_SCANS + " + 1 WHERE " + COL_USER_ID + " = " + userId);
        db.close();
    }

    private User cursorToUser(Cursor c) {
        User user = new User();
        user.setId(c.getInt(c.getColumnIndexOrThrow(COL_USER_ID)));
        user.setFirstName(c.getString(c.getColumnIndexOrThrow(COL_USER_FIRST)));
        user.setLastName(c.getString(c.getColumnIndexOrThrow(COL_USER_LAST)));
        user.setEmail(c.getString(c.getColumnIndexOrThrow(COL_USER_EMAIL)));
        user.setPasswordHash(c.getString(c.getColumnIndexOrThrow(COL_USER_PASS)));
        user.setTotalScans(c.getInt(c.getColumnIndexOrThrow(COL_USER_TOTAL_SCANS)));
        user.setFlaggedCount(c.getInt(c.getColumnIndexOrThrow(COL_USER_FLAGGED)));
        user.setSavedCount(c.getInt(c.getColumnIndexOrThrow(COL_USER_SAVED)));

        String skinTypesStr = c.getString(c.getColumnIndexOrThrow(COL_USER_SKIN_TYPES));
        if (skinTypesStr != null && !skinTypesStr.isEmpty()) {
            user.setSkinTypes(new ArrayList<>(Arrays.asList(skinTypesStr.split(","))));
        }
        String concernsStr = c.getString(c.getColumnIndexOrThrow(COL_USER_CONCERNS));
        if (concernsStr != null && !concernsStr.isEmpty()) {
            user.setSkinConcerns(new ArrayList<>(Arrays.asList(concernsStr.split(","))));
        }
        return user;
    }

    // ======================= SCAN OPERATIONS =======================

    public long saveScanResult(ScanResult result) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(COL_SCAN_USER_ID, result.getUserId());
            cv.put(COL_SCAN_PRODUCT, result.getProductName());
            cv.put(COL_SCAN_RAW, result.getRawIngredientText());
            cv.put(COL_SCAN_DATE, result.getScanDate());
            cv.put(COL_SCAN_METHOD, result.getInputMethod());
            cv.put(COL_SCAN_AI_INSIGHT, result.getAiInsight() != null ? result.getAiInsight() : "");
            cv.put(COL_SCAN_SAVED, result.isSaved() ? 1 : 0);
            cv.put(COL_SCAN_FAVORITE, result.isFavorite() ? 1 : 0);
            cv.put(COL_SCAN_SAFE_COUNT, result.getSafeCount());
            cv.put(COL_SCAN_CAUTION_COUNT, result.getCautionCount());
            cv.put(COL_SCAN_HARMFUL_COUNT, result.getHarmfulCount());
            cv.put(COL_SCAN_OVERALL, result.getOverallSafetyLabel());

            long scanId = db.insert(TABLE_SCANS, null, cv);

            for (Ingredient ing : result.getIngredients()) {
                ContentValues icv = new ContentValues();
                icv.put(COL_ING_SCAN_ID, scanId);
                icv.put(COL_ING_NAME, ing.getName());
                icv.put(COL_ING_SAFETY, ing.getSafetyLevel().name());
                icv.put(COL_ING_CATEGORY, ing.getCategory() != null ? ing.getCategory() : "");
                icv.put(COL_ING_DESCRIPTION, ing.getDescription() != null ? ing.getDescription() : "");
                icv.put(COL_ING_RISK, ing.getRiskNote() != null ? ing.getRiskNote() : "");
                icv.put(COL_ING_FLAGGED, ing.isFlagged() ? 1 : 0);
                icv.put(COL_ING_HAZARD, ing.getHazardScore());
                icv.put(COL_ING_COMEDOGENIC, ing.getComedogenicRating());
                icv.put(COL_ING_ALLERGEN, ing.getAllergenInfo() != null ? ing.getAllergenInfo() : "None");
                db.insert(TABLE_SCAN_INGREDIENTS, null, icv);
            }

            db.setTransactionSuccessful();
            return scanId;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public List<ScanResult> getUserScans(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<ScanResult> results = new ArrayList<>();
        Cursor c = db.query(TABLE_SCANS, null, COL_SCAN_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, COL_SCAN_ID + " DESC");

        if (c != null) {
            while (c.moveToNext()) {
                results.add(cursorToScanResult(c));
            }
            c.close();
        }
        db.close();
        return results;
    }

    public List<Ingredient> getIngredientsForScan(int scanId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Ingredient> ingredients = new ArrayList<>();
        Cursor c = db.query(TABLE_SCAN_INGREDIENTS, null, COL_ING_SCAN_ID + "=?",
                new String[]{String.valueOf(scanId)}, null, null, null);

        if (c != null) {
            while (c.moveToNext()) {
                String name = c.getString(c.getColumnIndexOrThrow(COL_ING_NAME));
                String safetyStr = c.getString(c.getColumnIndexOrThrow(COL_ING_SAFETY));
                Ingredient.SafetyLevel safety;
                try { safety = Ingredient.SafetyLevel.valueOf(safetyStr); }
                catch (Exception e) { safety = Ingredient.SafetyLevel.SAFE; }

                Ingredient ing = new Ingredient(
                        name, safety,
                        c.getString(c.getColumnIndexOrThrow(COL_ING_CATEGORY)),
                        c.getString(c.getColumnIndexOrThrow(COL_ING_DESCRIPTION)),
                        c.getString(c.getColumnIndexOrThrow(COL_ING_RISK))
                );

                ing.setFlagged(c.getInt(c.getColumnIndexOrThrow(COL_ING_FLAGGED)) == 1);
                ing.setHazardScore(c.getInt(c.getColumnIndexOrThrow(COL_ING_HAZARD)));
                ing.setComedogenicRating(c.getInt(c.getColumnIndexOrThrow(COL_ING_COMEDOGENIC)));
                ing.setAllergenInfo(c.getString(c.getColumnIndexOrThrow(COL_ING_ALLERGEN)));

                ingredients.add(ing);
            }
            c.close();
        }
        db.close();
        return ingredients;
    }

    public void toggleScanSaved(int scanId, boolean saved) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_SCAN_SAVED, saved ? 1 : 0);
        db.update(TABLE_SCANS, cv, COL_SCAN_ID + "=?", new String[]{String.valueOf(scanId)});
        db.close();
    }

    public void deleteScan(int scanId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SCAN_INGREDIENTS, COL_ING_SCAN_ID + "=?", new String[]{String.valueOf(scanId)});
        db.delete(TABLE_SCANS, COL_SCAN_ID + "=?", new String[]{String.valueOf(scanId)});
        db.close();
    }

    private ScanResult cursorToScanResult(Cursor c) {
        ScanResult r = new ScanResult();
        r.setId(c.getInt(c.getColumnIndexOrThrow(COL_SCAN_ID)));
        r.setUserId(c.getInt(c.getColumnIndexOrThrow(COL_SCAN_USER_ID)));
        r.setProductName(c.getString(c.getColumnIndexOrThrow(COL_SCAN_PRODUCT)));
        r.setRawIngredientText(c.getString(c.getColumnIndexOrThrow(COL_SCAN_RAW)));
        r.setScanDate(c.getString(c.getColumnIndexOrThrow(COL_SCAN_DATE)));
        r.setInputMethod(c.getString(c.getColumnIndexOrThrow(COL_SCAN_METHOD)));
        r.setAiInsight(c.getString(c.getColumnIndexOrThrow(COL_SCAN_AI_INSIGHT)));
        r.setSaved(c.getInt(c.getColumnIndexOrThrow(COL_SCAN_SAVED)) == 1);
        r.setFavorite(c.getInt(c.getColumnIndexOrThrow(COL_SCAN_FAVORITE)) == 1);

        r.setSafeCount(c.getInt(c.getColumnIndexOrThrow(COL_SCAN_SAFE_COUNT)));
        r.setCautionCount(c.getInt(c.getColumnIndexOrThrow(COL_SCAN_CAUTION_COUNT)));
        r.setHarmfulCount(c.getInt(c.getColumnIndexOrThrow(COL_SCAN_HARMFUL_COUNT)));
        r.setOverallSafetyLabel(c.getString(c.getColumnIndexOrThrow(COL_SCAN_OVERALL)));

        return r;
    }
}