package com.leo.fixcycle.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.fixcycle.R;
import com.leo.fixcycle.models.Motorcycle;
import com.leo.fixcycle.models.MotorcycleDataMotorcycle;
import com.leo.fixcycle.networks.MotorcycleClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MotorcycleDetailsActivity extends AppCompatActivity {
    Button editButton, deleteButton;
    MotorcycleDataMotorcycle motorcycle;
    int motorcycleId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motorcycle_details);

        editButton = findViewById(R.id.edit_btn);
        deleteButton = findViewById(R.id.delete_btn);

        editButton.setOnClickListener(view -> openActivity());
        deleteButton.setOnClickListener(view -> showAlertDialog());

        getMotorcycleDetails();
    }

    private void getMotorcycleDetails() {
        ImageView imageView = findViewById(R.id.motorcycle_details_img);
        TextView nameContent = findViewById(R.id.name_content);
        TextView brandContent = findViewById(R.id.brand_content);
        TextView typeContent = findViewById(R.id.type_content);
        TextView licensePlateContent = findViewById(R.id.license_plate_content);
        TextView cylinderCapacityContent = findViewById(R.id.cylinder_capacity_content);
        TextView colorContent = findViewById(R.id.color_content);
        TextView fuelTypeContent = findViewById(R.id.fuel_type_content);
        TextView productionYearContent = findViewById(R.id.production_year_content);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            motorcycle = (MotorcycleDataMotorcycle) getIntent().getSerializableExtra("data");

            motorcycleId = motorcycle.getId();
            String name = motorcycle.getName();
            String brand = motorcycle.getBrand();
            String licensePlate = motorcycle.getLicensePlate();
            String type = motorcycle.getType();
            double cylinderCapacity = motorcycle.getCylinderCapacity();
            String productionYear = motorcycle.getProductionYear().substring(0, 4);
            String color = motorcycle.getColor();
            String fuelType = motorcycle.getFuelType();
            boolean isDeleted = motorcycle.isDeleted();

            imageView.setImageResource(R.drawable.start);
            nameContent.setText(name);
            brandContent.setText(brand);
            licensePlateContent.setText(licensePlate);
            typeContent.setText(type);
            cylinderCapacityContent.setText(String.valueOf(cylinderCapacity));
            productionYearContent.setText(productionYear);
            colorContent.setText(color);
            fuelTypeContent.setText(fuelType);

            if (isDeleted) {
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
            }
        }
    }

    private void openActivity() {
        Intent intent = new Intent(MotorcycleDetailsActivity.this, EditMotorcycleActivity.class);
        intent.putExtra("motorcycleId", motorcycleId);
        startActivity(intent);
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setIcon(R.drawable.logo)
                .setTitle("Delete")
                .setMessage("Are you sure want to delete this motorcycle?")
                .setCancelable(false)
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                })
                .setPositiveButton("Delete", (dialogInterface, i) -> {
                    deleteMotorcycleData();
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteMotorcycleData() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accessToken", "");

        MotorcycleClient call = new MotorcycleClient();

        call.getApi().removeMotorcycle(motorcycleId, "Bearer " + accessToken).enqueue(new Callback<Motorcycle>() {
            @Override
            public void onResponse(Call<Motorcycle> call, Response<Motorcycle> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(MotorcycleDetailsActivity.this, MainActivity.class);
                    intent.putExtra("fragmentName", "myMotorcycle");
                    startActivity(intent);
                    showToast("Motorcycle deleted successfully");
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
            }

            @Override
            public void onFailure(Call<Motorcycle> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(MotorcycleDetailsActivity.this, message, Toast.LENGTH_LONG).show();
    }
}