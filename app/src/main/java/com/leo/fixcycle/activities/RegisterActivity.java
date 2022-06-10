package com.leo.fixcycle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.leo.fixcycle.R;
import com.leo.fixcycle.models.User;
import com.leo.fixcycle.networks.UserClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerButton = findViewById(R.id.register_btn);
        registerButton.setOnClickListener(view -> register());
    }

    private void register() {
        EditText nameInput = findViewById(R.id.name_input);
        EditText emailInput = findViewById(R.id.email_input);
        EditText passwordInput = findViewById(R.id.password_input);
        EditText confirmPasswordInput = findViewById(R.id.confirm_password_input);
        ProgressBar loading = findViewById(R.id.loading);

        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        if (!isValidUser(name, email, password, confirmPassword)) {
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        UserClient call = new UserClient();
        loading.setVisibility(View.VISIBLE);

        call.getApi().saveUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    showToast("Registration successful. You can login now");
                    finish();
                } else if (response.errorBody() != null) {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        String message = error.getString("message");
                        showToast(message);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
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

    private boolean isValidUser(String name, String email, String password, String confirmPassword) {
        boolean isValid = false;

        if (name.matches("")) {
            showToast("Name input is required");
        } else if (email.matches("")) {
            showToast("Email input is required");
        } else if (password.matches("")) {
            showToast("Password input is required");
        } else if (!password.equals(confirmPassword)) {
            showToast("Password doesn't match");
        } else {
            isValid = true;
        }

        return isValid;
    }

    private void showToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
    }
}