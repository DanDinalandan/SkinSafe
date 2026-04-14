package com.example.skinsafe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(() -> {
            SessionManager session = SessionManager.getInstance(this);
            Intent intent;
            if (session.isLoggedIn()) {
                intent = new Intent(MainActivity.this, HomeActivity.class);
            } else {
                intent = new Intent(MainActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}