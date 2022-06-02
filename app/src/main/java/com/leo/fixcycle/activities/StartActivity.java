package com.leo.fixcycle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.leo.fixcycle.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button getStartedButton = findViewById(R.id.get_started_btn);
        getStartedButton.setOnClickListener(view -> openActivity());
    }

    private void openActivity() {
        Intent intent = new Intent(StartActivity.this, LoadingActivity.class);
        startActivity(intent);
        finish();
    }
}