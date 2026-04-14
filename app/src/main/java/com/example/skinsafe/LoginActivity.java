package com.example.skinsafe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignUp, tvForgotPassword;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = DatabaseHelper.getInstance(this);
        sessionManager = SessionManager.getInstance(this);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvSignUp = findViewById(R.id.tv_signup_link);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);

        btnLogin.setOnClickListener(v -> attemptLogin());

        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });

        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Password reset would be sent to your email.", Toast.LENGTH_SHORT).show();
        });
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        // Validation
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }

        User user = dbHelper.loginUser(email, password);
        if (user != null) {
            sessionManager.createSession(user.getId(), user.getFullName(), user.getEmail());
            Toast.makeText(this, "Welcome back, " + user.getFirstName() + "!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password. Please try again.", Toast.LENGTH_LONG).show();
            etPassword.setText("");
            etPassword.requestFocus();
        }
    }
}