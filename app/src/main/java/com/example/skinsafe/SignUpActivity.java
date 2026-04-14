package com.example.skinsafe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etPassword;
    private Button btnCreateAccount;
    private TextView tvSignIn;
    private ImageButton btnBack;

    private CheckBox cbOily, cbDry, cbCombination, cbSensitive, cbNormal;
    private CheckBox cbNoFragrance, cbAvoidParabens, cbAcneProne;

    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHelper = DatabaseHelper.getInstance(this);
        sessionManager = SessionManager.getInstance(this);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnCreateAccount = findViewById(R.id.btn_create_account);
        tvSignIn = findViewById(R.id.tv_signin_link);
        btnBack = findViewById(R.id.btn_back);

        cbOily = findViewById(R.id.cb_oily);
        cbDry = findViewById(R.id.cb_dry);
        cbCombination = findViewById(R.id.cb_combination);
        cbSensitive = findViewById(R.id.cb_sensitive);
        cbNormal = findViewById(R.id.cb_normal);

        cbNoFragrance = findViewById(R.id.cb_no_fragrance);
        cbAvoidParabens = findViewById(R.id.cb_avoid_parabens);
        cbAcneProne = findViewById(R.id.cb_acne_prone);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnCreateAccount.setOnClickListener(v -> attemptSignUp());

        tvSignIn.setOnClickListener(v -> {
            finish();
        });
    }

    private void attemptSignUp() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(firstName)) { etFirstName.setError("First name is required"); return; }
        if (TextUtils.isEmpty(lastName)) { etLastName.setError("Last name is required"); return; }
        if (TextUtils.isEmpty(email)) { etEmail.setError("Email is required"); return; }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            return;
        }
        if (TextUtils.isEmpty(password)) { etPassword.setError("Password is required"); return; }
        if (password.length() < 6) { etPassword.setError("Minimum 6 characters"); return; }

        if (dbHelper.emailExists(email)) {
            etEmail.setError("This email is already registered");
            return;
        }

        List<String> skinTypes = new ArrayList<>();
        if (cbOily.isChecked()) skinTypes.add("Oily");
        if (cbDry.isChecked()) skinTypes.add("Dry");
        if (cbCombination.isChecked()) skinTypes.add("Combination");
        if (cbSensitive.isChecked()) skinTypes.add("Sensitive");
        if (cbNormal.isChecked()) skinTypes.add("Normal");

        List<String> skinConcerns = new ArrayList<>();
        if (cbNoFragrance.isChecked()) skinConcerns.add("No Fragrance");
        if (cbAvoidParabens.isChecked()) skinConcerns.add("Avoid Parabens");
        if (cbAcneProne.isChecked()) skinConcerns.add("Acne-prone");

        User user = new User(firstName, lastName, email, password);
        user.setSkinTypes(skinTypes);
        user.setSkinConcerns(skinConcerns);

        long userId = dbHelper.registerUser(user);
        if (userId != -1) {
            sessionManager.createSession((int) userId, firstName + " " + lastName, email);
            Toast.makeText(this, "Account created! Welcome, " + firstName + "!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Account creation failed. Please try again.", Toast.LENGTH_LONG).show();
        }
    }
}