package com.leo.fixcycle.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.fixcycle.R;
import com.leo.fixcycle.activities.AdminOrdersActivity;
import com.leo.fixcycle.activities.LoginActivity;
import com.leo.fixcycle.activities.TopUpActivity;
import com.leo.fixcycle.models.User;
import com.leo.fixcycle.models.UserDataUser;
import com.leo.fixcycle.networks.UserClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getUserData(view);

        ImageButton topUpButton = view.findViewById(R.id.top_up_btn);
        topUpButton.setOnClickListener(v -> openActivity());

        return view;
    }

    private void openActivity() {
        Intent intent = new Intent(getActivity(), TopUpActivity.class);
        startActivity(intent);
    }

    private void getUserData(View view) {
        if (getActivity() != null) {
            SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String accessToken = sp.getString("accessToken", "");

            TextView welcomeText = view.findViewById(R.id.welcome_text);
            TextView balanceText = view.findViewById(R.id.balance_text);
            ProgressBar loading = view.findViewById(R.id.loading);

            UserClient call = new UserClient();
            loading.setVisibility(View.VISIBLE);

            call.getApi().getUser("Bearer " + accessToken).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserDataUser user = response.body().getData().getUser();
                        String name = user.getName();
                        int balance = user.getBalance();
                        boolean isAdmin = user.isAdmin();
                        String time = getTime();

                        if (isAdmin) {
                            Intent intent = new Intent(getActivity(), AdminOrdersActivity.class);
                            startActivity(intent);

                            if (getActivity() != null) {
                                getActivity().finish();
                            }
                        } else {
                            String formattedBalance = NumberFormat.getCurrencyInstance(new Locale("in", "ID")).format(balance);
                            welcomeText.setText("Good " + time + ", " + name + "!");
                            balanceText.setText(formattedBalance);
                        }
                    } else if (response.code() == 401) {
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

    private String getTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        String time;

        if (hour >= 6 && hour < 12) {
            time = "morning";
        } else if (hour >= 12 && hour < 16) {
            time = "afternoon";
        } else if (hour >= 16 && hour < 21) {
            time = "evening";
        } else {
            time = "night";
        }

        return time;
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}