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
import com.leo.fixcycle.models.UserDataUser;
import com.leo.fixcycle.networks.UserClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getUserData();

        Button saveButton = findViewById(R.id.save_btn);
        saveButton.setOnClickListener(view -> updateUserData());
    }

    private void getUserData() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accessToken", "");

        EditText nameInput = findViewById(R.id.name_input);
        EditText addressInput = findViewById(R.id.address_input);
        EditText phoneNumberInput = findViewById(R.id.phone_number_input);
        ProgressBar loading = findViewById(R.id.loading);

        UserClient call = new UserClient();
        loading.setVisibility(View.VISIBLE);

        call.getApi().getUser("Bearer " + accessToken).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserDataUser user = response.body().getData().getUser();
                    String name = user.getName();
                    String address = user.getAddress();
                    String phoneNumber = user.getPhoneNumber();

                    nameInput.setText(name);
                    addressInput.setText(address);
                    phoneNumberInput.setText(phoneNumber);
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

    private void updateUserData() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accessToken", "");

        EditText nameInput = findViewById(R.id.name_input);
        EditText addressInput = findViewById(R.id.address_input);
        EditText phoneNumberInput = findViewById(R.id.phone_number_input);
        ProgressBar loading = findViewById(R.id.loading);

        String name = nameInput.getText().toString();
        String address = addressInput.getText().toString();
        String phoneNumber = phoneNumberInput.getText().toString();

        if (!isValidUser(name, address, phoneNumber)) {
            return;
        }

        User user = new User();
        user.setName(name);
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);

        UserClient call = new UserClient();
        loading.setVisibility(View.VISIBLE);

        call.getApi().updateUser(user, "Bearer " + accessToken).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                    intent.putExtra("fragmentName", "profile");
                    startActivity(intent);
                    showToast("Profile updated successfully");
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

    private boolean isValidUser(String name, String address, String phoneNumber) {
        boolean isValid = false;

        if (name.matches("")) {
            showToast("Name input is required");
        } else if (address.matches("")) {
            showToast("Address input is required");
        } else if (phoneNumber.matches("")) {
            showToast("Phone number input is required");
        } else {
            isValid = true;
        }

        return isValid;
    }

    private void showToast(String message) {
        Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_LONG).show();
    }
}