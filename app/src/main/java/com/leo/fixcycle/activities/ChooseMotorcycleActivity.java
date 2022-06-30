package com.leo.fixcycle.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.leo.fixcycle.R;
import com.leo.fixcycle.adapters.ChooseMotorcycleAdapter;
import com.leo.fixcycle.models.Motorcycle;
import com.leo.fixcycle.models.MotorcycleDataMotorcycle;
import com.leo.fixcycle.networks.MotorcycleClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseMotorcycleActivity extends AppCompatActivity implements ChooseMotorcycleAdapter.OnClickShowListener {
    RecyclerView recyclerView;
    List<MotorcycleDataMotorcycle> motorcycleDataHolder;
    ChooseMotorcycleAdapter chooseMotorcycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_motorcycle);

        recyclerView = findViewById(R.id.choose_moto_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chooseMotorcycleAdapter= new ChooseMotorcycleAdapter(motorcycleDataHolder,this);
        getMotorcycle();


    }

    private void getMotorcycle(){
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accessToken", "");

        MotorcycleClient call = new MotorcycleClient();

        call.getApi().getMotorcycle("Bearer " + accessToken).enqueue(new Callback<Motorcycle>() {
            @Override
            public void onResponse(Call<Motorcycle> call, Response<Motorcycle> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MotorcycleDataMotorcycle> motorcyclesDataHolder = response.body().getData().getMotorcycles();
                    motorcyclesDataHolder.removeIf(MotorcycleDataMotorcycle::isDeleted);

                    chooseMotorcycleAdapter.setData(motorcyclesDataHolder);
                    recyclerView.setAdapter(chooseMotorcycleAdapter);
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


    @Override
    public void onClickShowListener(MotorcycleDataMotorcycle motorcycleDataMotorcycle) {
        Intent intent = new Intent(ChooseMotorcycleActivity.this, MainActivity.class).putExtra("data",motorcycleDataMotorcycle);
        intent.putExtra("fragmentName", "bookService");
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(ChooseMotorcycleActivity.this, message, Toast.LENGTH_LONG).show();
    }
}