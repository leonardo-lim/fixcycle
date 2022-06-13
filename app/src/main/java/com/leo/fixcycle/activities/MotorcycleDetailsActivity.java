package com.leo.fixcycle.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.TextView;

import com.leo.fixcycle.R;
import com.leo.fixcycle.models.MotorcycleDataMotorcycle;

public class MotorcycleDetailsActivity extends AppCompatActivity {
    MotorcycleDataMotorcycle motorcycleDataMotorcycle;

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
        TextView productionYearContent = findViewById(R.id.production_year_content);

        Intent intent = getIntent();
        if(intent.getExtras()!=null){
            motorcycleDataMotorcycle = (MotorcycleDataMotorcycle) intent.getSerializableExtra("data");

            String name = motorcycleDataMotorcycle.getName();
            String brand = motorcycleDataMotorcycle.getBrand();
            String type = motorcycleDataMotorcycle.getType();
            double cylinderCapacity = motorcycleDataMotorcycle.getCylinderCapacity();
            String licensePlate = motorcycleDataMotorcycle.getLicensePlate();
            String color = motorcycleDataMotorcycle.getColor();
            String fuelType = motorcycleDataMotorcycle.getFuelType();
            String productionYear = motorcycleDataMotorcycle.getProductionYear();
            productionYear = productionYear.substring(0,4);

            imageView.setImageResource(R.drawable.start);
            nameContent.setText(name);
            brandContent.setText(brand);
            typeContent.setText(type);
            cylinderCapacityContent.setText(String.valueOf(cylinderCapacity));
            licenseContent.setText(licensePlate);
            colorContent.setText(color);
            fuelTypeContent.setText(fuelType);
            productionYearContent.setText(productionYear);
        }

    }
}