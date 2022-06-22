package com.leo.fixcycle.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.leo.fixcycle.R;
import com.leo.fixcycle.activities.ServiceDetailsActivity;
import com.leo.fixcycle.adapters.UserOrdersAdapter;
import com.leo.fixcycle.models.Motorcycle;
import com.leo.fixcycle.models.MotorcycleDataMotorcycle;
import com.leo.fixcycle.models.Service;
import com.leo.fixcycle.models.ServiceDataService;
import com.leo.fixcycle.networks.MotorcycleClient;
import com.leo.fixcycle.networks.ServiceClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersFragment extends Fragment implements UserOrdersAdapter.OnClickOrdersListener {
    RecyclerView pendingRecyclerView;
    RecyclerView ongoingRecyclerView;
    RecyclerView finishedRecyclerView;
    RecyclerView canceledRecyclerView;

    UserOrdersAdapter pendingMotorcycleAdapter;
    UserOrdersAdapter ongoingMotorcycleAdapter;
    UserOrdersAdapter finishedMotorcycleAdapter;
    UserOrdersAdapter canceledMotorcycleAdapter;

    List<ServiceDataService> servicesDataHolder;
    List<MotorcycleDataMotorcycle> motorcyclesDataHolder;
    List<ServiceDataService> pendingOrderDataHolder = new ArrayList<>();
    List<ServiceDataService> ongoingOrderDataHolder = new ArrayList<>();
    List<ServiceDataService> finishedOrderDataHolder = new ArrayList<>();
    List<ServiceDataService> canceledOrderDataHolder = new ArrayList<>();
    List<Integer> pendingStatus = new ArrayList<>();
    List<Integer> ongoingStatus = new ArrayList<>();
    List<Integer> finishedStatus = new ArrayList<>();
    List<Integer> canceledStatus = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        pendingRecyclerView = view.findViewById(R.id.pending_recycler_view);
        ongoingRecyclerView = view.findViewById(R.id.ongoing_recycler_view);
        finishedRecyclerView = view.findViewById(R.id.finished_recycler_view);
        canceledRecyclerView = view.findViewById(R.id.canceled_recycler_view);

        pendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ongoingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        finishedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        canceledRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        pendingMotorcycleAdapter = new UserOrdersAdapter(motorcyclesDataHolder, servicesDataHolder,this);
        ongoingMotorcycleAdapter = new UserOrdersAdapter(motorcyclesDataHolder, servicesDataHolder,this);
        finishedMotorcycleAdapter = new UserOrdersAdapter(motorcyclesDataHolder, servicesDataHolder,this);
        canceledMotorcycleAdapter = new UserOrdersAdapter(motorcyclesDataHolder, servicesDataHolder,this);
        getService();

        return view;
    }

    private void getService() {
        if (getActivity() != null) {
            SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String accessToken = sp.getString("accessToken", "");

            ServiceClient call = new ServiceClient();

            call.getApi().getService("Bearer " + accessToken).enqueue(new Callback<Service>() {
                @Override
                public void onResponse(Call<Service> call, Response<Service> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<ServiceDataService> services = response.body().getData().getServices();
                        servicesDataHolder = services;

                        for (ServiceDataService service : services) {
                            int serviceStatus = service.getServiceStatus();
                            int motorcycleId = service.getMotorcycleId();

                            if (serviceStatus == 1) {
                                pendingStatus.add(motorcycleId);
                                pendingOrderDataHolder.add(service);
                            } else if (serviceStatus == 2) {
                                ongoingStatus.add(motorcycleId);
                                ongoingOrderDataHolder.add(service);
                            } else if (serviceStatus == 3) {
                                finishedStatus.add(motorcycleId);
                                finishedOrderDataHolder.add(service);
                            } else {
                                canceledStatus.add(motorcycleId);
                                canceledOrderDataHolder.add(service);
                            }
                        }

                        getMotorcycle();
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
                public void onFailure(Call<Service> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void getMotorcycle() {
        if (getActivity() != null) {
            SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String accessToken = sp.getString("accessToken", "");

            MotorcycleClient call = new MotorcycleClient();

            call.getApi().getMotorcycle("Bearer " + accessToken).enqueue(new Callback<Motorcycle>() {
                @Override
                public void onResponse(Call<Motorcycle> call, Response<Motorcycle> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        motorcyclesDataHolder = response.body().getData().getMotorcycles();
                        groupServicesByStatus();
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
    }

    private void groupServicesByStatus() {
        List<MotorcycleDataMotorcycle> pendingServicedMotorcyclesDataHolder = new ArrayList<>();
        List<MotorcycleDataMotorcycle> ongoingServicedMotorcyclesDataHolder = new ArrayList<>();
        List<MotorcycleDataMotorcycle> finishedServicedMotorcyclesDataHolder = new ArrayList<>();
        List<MotorcycleDataMotorcycle> canceledServicedMotorcyclesDataHolder = new ArrayList<>();

        for (int motorcycleId : pendingStatus) {
            for (MotorcycleDataMotorcycle motorcycle : motorcyclesDataHolder) {
                if (motorcycleId == motorcycle.getId()) {
                    pendingServicedMotorcyclesDataHolder.add(motorcycle);
                }
            }
        }

        for (int motorcycleId : ongoingStatus) {
            for (MotorcycleDataMotorcycle motorcycle : motorcyclesDataHolder) {
                if (motorcycleId == motorcycle.getId()) {
                    ongoingServicedMotorcyclesDataHolder.add(motorcycle);
                }
            }
        }

        for (int motorcycleId : finishedStatus) {
            for (MotorcycleDataMotorcycle motorcycle : motorcyclesDataHolder) {
                if (motorcycleId == motorcycle.getId()) {
                    finishedServicedMotorcyclesDataHolder.add(motorcycle);
                }
            }
        }

        for (int motorcycleId : canceledStatus) {
            for (MotorcycleDataMotorcycle motorcycle : motorcyclesDataHolder) {
                if (motorcycleId == motorcycle.getId()) {
                    canceledServicedMotorcyclesDataHolder.add(motorcycle);
                }
            }
        }

        pendingMotorcycleAdapter.setData(pendingServicedMotorcyclesDataHolder,pendingOrderDataHolder);
        pendingRecyclerView.setAdapter(pendingMotorcycleAdapter);
        ongoingMotorcycleAdapter.setData(ongoingServicedMotorcyclesDataHolder,ongoingOrderDataHolder);
        ongoingRecyclerView.setAdapter(ongoingMotorcycleAdapter);
        finishedMotorcycleAdapter.setData(finishedServicedMotorcyclesDataHolder,finishedOrderDataHolder);
        finishedRecyclerView.setAdapter(finishedMotorcycleAdapter);
        canceledMotorcycleAdapter.setData(canceledServicedMotorcyclesDataHolder,canceledOrderDataHolder);
        canceledRecyclerView.setAdapter(canceledMotorcycleAdapter);
    }

    @Override
    public void onClickShowListener(MotorcycleDataMotorcycle motorcycleDataMotorcycle,ServiceDataService serviceDataService) {
        Intent intent =  new Intent(getActivity(), ServiceDetailsActivity.class);
        intent.putExtra("motorcycleData", motorcycleDataMotorcycle);
        intent.putExtra("serviceData",serviceDataService);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}