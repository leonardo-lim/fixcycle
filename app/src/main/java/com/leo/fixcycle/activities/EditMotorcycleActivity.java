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
import com.leo.fixcycle.models.Motorcycle;
import com.leo.fixcycle.models.MotorcycleDataMotorcycle;
import com.leo.fixcycle.models.User;
import com.leo.fixcycle.models.UserDataUser;
import com.leo.fixcycle.networks.MotorcycleClient;
import com.leo.fixcycle.networks.UserClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMotorcycleActivity extends AppCompatActivity {
    int motorcycleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_motorcycle);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            motorcycleId = extras.getInt("motorcycleId");
        }

        getMotorcycleData();

        Button saveButton = findViewById(R.id.save_btn);
        saveButton.setOnClickListener(view -> updateMotorcycleData());
    }

    private void getMotorcycleData() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accessToken", "");

        EditText nameInput = findViewById(R.id.name_input);
        EditText brandInput = findViewById(R.id.brand_input);
        EditText licensePlateInput = findViewById(R.id.license_plate_input);
        EditText typeInput = findViewById(R.id.type_input);
        EditText cylinderCapacityInput = findViewById(R.id.cylinder_capacity_input);
        EditText productionYearInput = findViewById(R.id.production_year_input);
        EditText colorInput = findViewById(R.id.color_input);
        EditText fuelTypeInput = findViewById(R.id.fuel_type_input);
        ProgressBar loading = findViewById(R.id.loading);

        MotorcycleClient call = new MotorcycleClient();
        loading.setVisibility(View.VISIBLE);

        call.getApi().getMotorcycle(motorcycleId, "Bearer " + accessToken).enqueue(new Callback<Motorcycle>() {
            @Override
            public void onResponse(Call<Motorcycle> call, Response<Motorcycle> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MotorcycleDataMotorcycle motorcycle = response.body().getData().getMotorcycle();
                    String name = motorcycle.getName();
                    String brand = motorcycle.getBrand();
                    String licensePlate = motorcycle.getLicensePlate();
                    String type = motorcycle.getType();
                    double cylinderCapacity = motorcycle.getCylinderCapacity();
                    String productionYear = motorcycle.getProductionYear().substring(0, 4);
                    String color = motorcycle.getColor();
                    String fuelType = motorcycle.getFuelType();

                    nameInput.setText(name);
                    brandInput.setText(brand);
                    licensePlateInput.setText(licensePlate);
                    typeInput.setText(type);
                    cylinderCapacityInput.setText(String.valueOf(cylinderCapacity));
                    productionYearInput.setText(productionYear);
                    colorInput.setText(color);
                    fuelTypeInput.setText(fuelType);
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
            public void onFailure(Call<Motorcycle> call, Throwable t) {
                t.printStackTrace();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void updateMotorcycleData() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accessToken", "");

        EditText nameInput = findViewById(R.id.name_input);
        EditText brandInput = findViewById(R.id.brand_input);
        EditText licensePlateInput = findViewById(R.id.license_plate_input);
        EditText typeInput = findViewById(R.id.type_input);
        EditText cylinderCapacityInput = findViewById(R.id.cylinder_capacity_input);
        EditText productionYearInput = findViewById(R.id.production_year_input);
        EditText colorInput = findViewById(R.id.color_input);
        EditText fuelTypeInput = findViewById(R.id.fuel_type_input);
        ProgressBar loading = findViewById(R.id.loading);

        String name = nameInput.getText().toString();
        String brand = brandInput.getText().toString();
        String licensePlate = licensePlateInput.getText().toString();
        String type = typeInput.getText().toString();
        String cylinderCapacity = cylinderCapacityInput.getText().toString();
        String productionYear = productionYearInput.getText().toString();
        String color = colorInput.getText().toString();
        String fuelType = fuelTypeInput.getText().toString();

        if (!isValidMotorcycle(name, brand, licensePlate, type, cylinderCapacity, productionYear, color, fuelType)) {
            return;
        }

        Motorcycle motorcycle = new Motorcycle();
        motorcycle.setName(name);
        motorcycle.setBrand(brand);
        motorcycle.setLicensePlate(licensePlate);
        motorcycle.setType(type);
        motorcycle.setCylinderCapacity(Double.parseDouble(cylinderCapacity));
        motorcycle.setProductionYear(productionYear + "-02-02");
        motorcycle.setColor(color);
        motorcycle.setFuelType(fuelType);

        MotorcycleClient call = new MotorcycleClient();
        loading.setVisibility(View.VISIBLE);

        call.getApi().updateMotorcycle(motorcycleId, motorcycle,"Bearer " + accessToken).enqueue(new Callback<Motorcycle>() {
            @Override
            public void onResponse(Call<Motorcycle> call, Response<Motorcycle> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(EditMotorcycleActivity.this, MainActivity.class);
                    intent.putExtra("fragmentName", "myMotorcycle");
                    startActivity(intent);
                    showToast("Motorcycle updated successfully");
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
            public void onFailure(Call<Motorcycle> call, Throwable t) {
                t.printStackTrace();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean isValidMotorcycle(String name, String brand, String licensePlate, String type, String cylinderCapacity, String productionYear, String color, String fuelType) {
        boolean isValid = false;

        if (name.matches("")) {
            showToast("Name input is required");
        } else if (brand.matches("")) {
            showToast("Brand input is required");
        } else if (licensePlate.matches("")) {
            showToast("License plate input is required");
        } else if (type.matches("")) {
            showToast("Type input is required");
        } else if (cylinderCapacity.matches("")) {
            showToast("Cylinder capacity input is required");
        } else if (productionYear.matches("")) {
            showToast("Production year input is required");
        } else if (color.matches("")) {
            showToast("Color input is required");
        } else if (fuelType.matches("")) {
            showToast("Fuel type input is required");
        } else {
            isValid = true;
        }

        return isValid;
    }

    private void showToast(String message) {
        Toast.makeText(EditMotorcycleActivity.this, message, Toast.LENGTH_LONG).show();
    }
}