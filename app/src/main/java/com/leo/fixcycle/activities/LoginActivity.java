package com.leo.fixcycle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.fixcycle.R;
import com.leo.fixcycle.models.User;
import com.leo.fixcycle.networks.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.login_btn);
        TextView registerText = findViewById(R.id.register_text);

        loginButton.setOnClickListener(view -> login());
        registerText.setOnClickListener(view -> register());
    }

    private void login() {
        EditText emailInput = findViewById(R.id.email_input);
        EditText passwordInput = findViewById(R.id.password_input);
        ProgressBar loading = findViewById(R.id.loading);

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (!isValidUser(email, password)) {
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        UserClient call = new UserClient();
        loading.setVisibility(View.VISIBLE);

        call.getApi().getUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String accessToken = response.body().getData().getAccessToken();

                    SharedPreferences sp = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("accessToken", accessToken);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else if (response.code() == 400) {
                    showToast("Incorrect password");
                } else if (response.code() == 404) {
                    showToast("User doesn't exist");
                } else {
                    showToast("Unexpected server error");
                }

                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void register() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private boolean isValidUser(String email, String password) {
        boolean isValid = false;

        if (email.matches("")) {
            showToast("Email input is required");
        } else if (password.matches("")) {
            showToast("Password input is required");
        } else {
            isValid = true;
        }

        return isValid;
    }

    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }
}