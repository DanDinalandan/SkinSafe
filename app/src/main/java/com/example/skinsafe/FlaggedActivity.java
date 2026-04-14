package com.example.skinsafe;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FlaggedActivity extends AppCompatActivity {

    private RecyclerView rvFlagged;
    private TextView tvEmpty;
    private DatabaseHelper dbHelper;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flagged);

        dbHelper = DatabaseHelper.getInstance(this);
        session = SessionManager.getInstance(this);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        rvFlagged = findViewById(R.id.rv_flagged_ingredients);
        rvFlagged.setLayoutManager(new LinearLayoutManager(this));
        tvEmpty = findViewById(R.id.tv_empty_flagged);

        loadFlaggedIngredients();
    }

    private void loadFlaggedIngredients() {
        List<Ingredient> flaggedList = dbHelper.getFlaggedIngredientsForUser(session.getUserId());

        if (flaggedList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvFlagged.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvFlagged.setVisibility(View.VISIBLE);

            IngredientAdapter adapter = new IngredientAdapter(this, flaggedList);
            rvFlagged.setAdapter(adapter);
        }
    }
}