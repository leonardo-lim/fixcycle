package com.leo.fixcycle.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.leo.fixcycle.R;
import com.leo.fixcycle.activities.AddMotorcycleActivity;
import com.leo.fixcycle.activities.MotorcycleDetailsActivity;
import com.leo.fixcycle.models.Motorcycle;
import com.leo.fixcycle.adapters.MotorcycleAdapter;
import com.leo.fixcycle.models.MotorcycleDataMotorcycle;
import com.leo.fixcycle.networks.MotorcycleClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyMotorcycleFragment extends Fragment implements MotorcycleAdapter.OnClickShowListener {
    RecyclerView recyclerView;
    List<MotorcycleDataMotorcycle> motorcycleDataHolder;
    Button addMotorcycleButton;
    MotorcycleAdapter motorcycleAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_motorcycle, container, false);

        addMotorcycleButton =  view.findViewById(R.id.add_motorcycle_btn);
        recyclerView = view.findViewById(R.id.my_motorcycle_recycler_view);

        addMotorcycleButton.setOnClickListener(v -> openActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        motorcycleAdapter = new MotorcycleAdapter(motorcycleDataHolder,this);

        getMotorcycle(view);

        return view;
    }

    private void openActivity() {
        Intent intent = new Intent(getActivity(), AddMotorcycleActivity.class);
        startActivity(intent);
    }

    private void getMotorcycle(View view) {
        if (getActivity() != null) {
            SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String accessToken = sp.getString("accessToken", "");

            ProgressBar loading = view.findViewById(R.id.loading);

            MotorcycleClient call = new MotorcycleClient();
            loading.setVisibility(View.VISIBLE);

            call.getApi().getMotorcycle("Bearer " + accessToken).enqueue(new Callback<Motorcycle>() {
                @Override
                public void onResponse(Call<Motorcycle> call, Response<Motorcycle> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<MotorcycleDataMotorcycle> motorcyclesDataHolder = response.body().getData().getMotorcycles();
                        motorcyclesDataHolder.removeIf(MotorcycleDataMotorcycle::isDeleted);

                        if (motorcyclesDataHolder.size() > 0) {
                            ConstraintLayout noMotorcycleLayout = view.findViewById(R.id.no_motorcycle_message);
                            noMotorcycleLayout.setVisibility(View.GONE);
                            motorcycleAdapter.setData(motorcyclesDataHolder);
                            recyclerView.setAdapter(motorcycleAdapter);
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
                public void onFailure(Call<Motorcycle> call, Throwable t) {
                    t.printStackTrace();
                    loading.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public void onClickShowListener(MotorcycleDataMotorcycle motorcycleDataMotorcycle) {
        Intent intent =  new Intent(getActivity(), MotorcycleDetailsActivity.class);
        intent.putExtra("data", motorcycleDataMotorcycle);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}