package com.leo.fixcycle.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.fixcycle.R;
import com.leo.fixcycle.activities.EditProfileActivity;
import com.leo.fixcycle.activities.LoginActivity;
import com.leo.fixcycle.models.User;
import com.leo.fixcycle.models.UserDataUser;
import com.leo.fixcycle.networks.UserClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        getUserData(view);

        Button editProfileButton = view.findViewById(R.id.edit_profile_btn);
        Button logoutButton = view.findViewById(R.id.logout_btn);

        editProfileButton.setOnClickListener(v -> openActivity());
        logoutButton.setOnClickListener(v -> logout(view));

        return view;
    }

    private void getUserData(View view) {
        if (getActivity() != null) {
            SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String accessToken = sp.getString("accessToken", "");

            TextView nameContent = view.findViewById(R.id.name_content);
            TextView emailContent = view.findViewById(R.id.email_content);
            TextView addressContent = view.findViewById(R.id.address_content);
            TextView phoneNumberContent = view.findViewById(R.id.phone_number_content);
            ProgressBar loading = view.findViewById(R.id.loading);

            UserClient call = new UserClient();
            loading.setVisibility(View.VISIBLE);

            call.getApi().getUser("Bearer " + accessToken).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserDataUser user = response.body().getData().getUser();
                        String name = user.getName();
                        String email = user.getEmail();
                        String address = user.getAddress();
                        String phoneNumber = user.getPhoneNumber();

                        nameContent.setText(name);
                        emailContent.setText(email);
                        addressContent.setText(address);
                        phoneNumberContent.setText(phoneNumber);
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
    }

    private void openActivity() {
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        startActivity(intent);
    }

    private void logout(View view) {
        if (getActivity() != null) {
            SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String accessToken = sp.getString("accessToken", "");

            ProgressBar loading = view.findViewById(R.id.loading);
            UserClient call = new UserClient();

            loading.setVisibility(View.VISIBLE);

            call.getApi().removeUser("Bearer " + accessToken).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);

                        if (getActivity() != null) {
                            getActivity().finish();
                        }
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
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}