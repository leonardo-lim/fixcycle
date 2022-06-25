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

public class TopUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        Button topUpButton = findViewById(R.id.top_up_btn);
        topUpButton.setOnClickListener(view -> updateBalance());
    }

    private void updateBalance() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accessToken", "");

        EditText amountInput = findViewById(R.id.amount_input);
        ProgressBar loading = findViewById(R.id.loading);

        String amount = amountInput.getText().toString();

        if (!isValidTopUp(amount)) {
            return;
        }

        User user = new User();
        user.setBalance(Integer.parseInt(amount));

        UserClient call = new UserClient();
        loading.setVisibility(View.VISIBLE);

        call.getApi().updateBalance(user, "Bearer " + accessToken).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(TopUpActivity.this, MainActivity.class);
                    intent.putExtra("fragmentName", "home");
                    startActivity(intent);
                    showToast("Balance updated successfully");
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

    private boolean isValidTopUp(String amount) {
        boolean isValid = false;

        if (amount.matches("")) {
            showToast("Amount input is required");
        } else {
            isValid = true;
        }

        return isValid;
    }

    private void showToast(String message) {
        Toast.makeText(TopUpActivity.this, message, Toast.LENGTH_LONG).show();
    }
}