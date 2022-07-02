package com.leo.fixcycle.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.leo.fixcycle.R;
import com.leo.fixcycle.adapters.IncomingOrdersAdapter;
import com.leo.fixcycle.adapters.OnProcessOrdersAdapter;
import com.leo.fixcycle.models.Invoice;
import com.leo.fixcycle.models.Motorcycle;
import com.leo.fixcycle.models.MotorcycleDataMotorcycle;
import com.leo.fixcycle.models.Service;
import com.leo.fixcycle.models.ServiceDataService;
import com.leo.fixcycle.models.User;
import com.leo.fixcycle.networks.InvoiceClient;
import com.leo.fixcycle.networks.MotorcycleClient;
import com.leo.fixcycle.networks.ServiceClient;
import com.leo.fixcycle.networks.UserClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrdersActivity extends AppCompatActivity implements IncomingOrdersAdapter.OnIncomingOrdersListener, OnProcessOrdersAdapter.OnProcessOrdersListener {
    RecyclerView incomingRecyclerView;
    RecyclerView onProcessRecyclerView;

    IncomingOrdersAdapter incomingMotorcycleAdapter;
    OnProcessOrdersAdapter onProcessMotorcycleAdapter;

    ProgressBar loading;

    List<MotorcycleDataMotorcycle> motorcyclesDataHolder;
    List<ServiceDataService> servicesDataHolder;
    List<ServiceDataService> incomingServicesDataHolder = new ArrayList<>();
    List<ServiceDataService> onProcessServicesDataHolder = new ArrayList<>();
    List<Integer> incomingStatus = new ArrayList<>();
    List<Integer> onProcessStatus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        incomingRecyclerView = findViewById(R.id.incoming_recycler_view);
        onProcessRecyclerView = findViewById(R.id.on_process_recycler_view);
        loading = findViewById(R.id.loading);

        getService();

        Button logoutButton = findViewById(R.id.logout_btn);
        logoutButton.setOnClickListener(view -> showAlertDialog("logout this user", "Logout", 0));
    }

    private void getService() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accessToken", "");

        ServiceClient call = new ServiceClient();
        loading.setVisibility(View.VISIBLE);

        call.getApi().getServices("Bearer " + accessToken).enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
                if (response.isSuccessful() && response.body() != null) {
                    servicesDataHolder = response.body().getData().getServices();

                    for (ServiceDataService service : servicesDataHolder) {
                        int serviceStatus = service.getServiceStatus();
                        int motorcycleId = service.getMotorcycleId();

                        if (serviceStatus == 1) {
                            incomingStatus.add(motorcycleId);
                            incomingServicesDataHolder.add(service);
                        } else if (serviceStatus == 2) {
                            onProcessStatus.add(motorcycleId);
                            onProcessServicesDataHolder.add(service);
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

                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {
                t.printStackTrace();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void getMotorcycle() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accessToken", "");

        MotorcycleClient call = new MotorcycleClient();
        loading.setVisibility(View.VISIBLE);

        call.getApi().getMotorcycles("Bearer " + accessToken).enqueue(new Callback<Motorcycle>() {
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

                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<Motorcycle> call, Throwable t) {
                t.printStackTrace();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void groupServicesByStatus() {
        List<MotorcycleDataMotorcycle> incomingServicedMotorcyclesDataHolder = new ArrayList<>();
        List<MotorcycleDataMotorcycle> onProcessServicedMotorcyclesDataHolder = new ArrayList<>();

        for (int motorcycleId : incomingStatus) {
            for (MotorcycleDataMotorcycle motorcycle : motorcyclesDataHolder) {
                if (motorcycleId == motorcycle.getId()) {
                    incomingServicedMotorcyclesDataHolder.add(motorcycle);
                }
            }
        }

        for (int motorcycleId : onProcessStatus) {
            for (MotorcycleDataMotorcycle motorcycle : motorcyclesDataHolder) {
                if (motorcycleId == motorcycle.getId()) {
                    onProcessServicedMotorcyclesDataHolder.add(motorcycle);
                }
            }
        }

        incomingMotorcycleAdapter = new IncomingOrdersAdapter(incomingServicedMotorcyclesDataHolder, incomingServicesDataHolder, this);
        incomingRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        incomingRecyclerView.setAdapter(incomingMotorcycleAdapter);

        onProcessMotorcycleAdapter = new OnProcessOrdersAdapter(onProcessServicedMotorcyclesDataHolder, onProcessServicesDataHolder, this);
        onProcessRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        onProcessRecyclerView.setAdapter(onProcessMotorcycleAdapter);
    }

    private void updateServiceData(int serviceId, int serviceStatus) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accessToken", "");

        Service service = new Service();
        service.setServiceStatus(serviceStatus);

        ServiceClient call = new ServiceClient();
        loading.setVisibility(View.VISIBLE);

        call.getApi().updateService(serviceId, service, "Bearer " + accessToken).enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
                if (response.isSuccessful() && response.body() != null) {
                    finish();
                    startActivity(getIntent());

                    if (serviceStatus == 2) {
                        showToast("Order accepted successfully");
                    } else if (serviceStatus == 3) {
                        addInvoice(serviceId);
                    } else {
                        showToast("Order rejected successfully");
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
            public void onFailure(Call<Service> call, Throwable t) {
                t.printStackTrace();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void addInvoice(int serviceId) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accessToken", "");

        InvoiceClient call = new InvoiceClient();
        loading.setVisibility(View.VISIBLE);

        call.getApi().saveInvoice(serviceId, "Bearer " + accessToken).enqueue(new Callback<Invoice>() {
            @Override
            public void onResponse(Call<Invoice> call, Response<Invoice> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showToast("Service finished successfully");
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
            public void onFailure(Call<Invoice> call, Throwable t) {
                t.printStackTrace();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void showAlertDialog(String message, String actionName, int serviceId) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminOrdersActivity.this);

        alertDialogBuilder
                .setIcon(R.drawable.logo)
                .setTitle(actionName)
                .setMessage("Are you sure want to " + message + "?")
                .setCancelable(false)
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                })
                .setPositiveButton(actionName, (dialogInterface, i) -> {
                    switch (actionName) {
                        case "Logout":
                            logout();
                            break;
                        case "Reject":
                            updateServiceData(serviceId, 4);
                            break;
                        case "Accept":
                            updateServiceData(serviceId, 2);
                            break;
                        default:
                            updateServiceData(serviceId, 3);
                            break;
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void logout() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accessToken", "");

        UserClient call = new UserClient();
        loading.setVisibility(View.VISIBLE);

        call.getApi().removeUser("Bearer " + accessToken).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(AdminOrdersActivity.this, LoginActivity.class);
                    startActivity(intent);
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
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(AdminOrdersActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRejectButtonClick(int incomingServiceId) {
        showAlertDialog("reject this service", "Reject", incomingServiceId);
    }

    @Override
    public void onAcceptButtonClick(int incomingServiceId) {
        showAlertDialog("accept this service", "Accept", incomingServiceId);
    }

    @Override
    public void onFinishServiceButtonClick(int onProcessServiceId) {
        showAlertDialog("finish this service", "Finish", onProcessServiceId);
    }

    @Override
    public void onClickShowListener(MotorcycleDataMotorcycle motorcycleDataMotorcycle, ServiceDataService serviceDataService) {
        Intent intent = new Intent(AdminOrdersActivity.this,ServiceDetailsActivity.class);
        intent.putExtra("motorcycleData",motorcycleDataMotorcycle);
        intent.putExtra("serviceData", serviceDataService);
        startActivity(intent);
    }
}