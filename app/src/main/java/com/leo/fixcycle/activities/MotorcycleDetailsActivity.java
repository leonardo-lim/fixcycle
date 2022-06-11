package com.leo.fixcycle.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.TextView;

import com.leo.fixcycle.R;

public class MotorcycleDetailsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motorcycle_details);

        ImageView imageView = findViewById(R.id.motorcycle_details_img);
        TextView nameContent = findViewById(R.id.name_content);
        TextView brandContent = findViewById(R.id.brand_content);
        TextView typeContent = findViewById(R.id.type_content);
        TextView licenseContent = findViewById(R.id.license_plate_content);
        TextView cylinderCapacityContent = findViewById(R.id.cylinder_capacity_content);
        TextView colorContent = findViewById(R.id.color_content);
        TextView fuelTypeContent = findViewById(R.id.fuel_type_content);
        TextView dateContent = findViewById(R.id.production_year_content);

        Intent intent = getIntent();
        int image = intent.getIntExtra("image",0);
        String name = intent.getStringExtra("name");
        String brand = intent.getStringExtra("brand");
        String type = intent.getStringExtra("type");
        int cylinderCapacity = intent.getIntExtra("cylinder_capacity",0);
        String licensePlate = intent.getStringExtra("license_plate");
        String color = intent.getStringExtra("color");
        String fuelType = intent.getStringExtra("fuel_type");
        String date = intent.getStringExtra("production_year");

        imageView.setImageResource(image);
        nameContent.setText(name);
        brandContent.setText(brand);
        typeContent.setText(type);
        cylinderCapacityContent.setText(String.valueOf(cylinderCapacity));
        licenseContent.setText(licensePlate);
        colorContent.setText(color);
        fuelTypeContent.setText(fuelType);
        dateContent.setText(date);
    }
}