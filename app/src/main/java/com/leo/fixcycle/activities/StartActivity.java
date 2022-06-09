package com.leo.fixcycle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.leo.fixcycle.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        SharedPreferences sp = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        String firstTime = sp.getString("isFirstTime", "");

        if (firstTime.equals("true")) {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("isFirstTime", "true");
            editor.apply();

            Button getStartedButton = findViewById(R.id.get_started_btn);
            getStartedButton.setOnClickListener(view -> openActivity());
        }
    }

    private void openActivity() {
        Intent intent = new Intent(StartActivity.this, LoadingActivity.class);
        startActivity(intent);
        finish();
    }
}