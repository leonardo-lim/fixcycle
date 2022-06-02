package com.leo.fixcycle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.leo.fixcycle.R;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Handler handler = new Handler();
        handler.postDelayed(this::openActivity,2000L);
    }

    private void openActivity() {
        Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}