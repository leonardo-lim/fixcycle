package com.leo.fixcycle.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.fixcycle.R;
import com.leo.fixcycle.models.Motorcycle;
import com.leo.fixcycle.models.MotorcycleDataMotorcycle;
import com.leo.fixcycle.models.ServiceDataService;
import com.leo.fixcycle.models.User;
import com.leo.fixcycle.models.UserDataUser;
import com.leo.fixcycle.networks.ServiceClient;
import com.leo.fixcycle.networks.UserClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceDetailsActivity extends AppCompatActivity {
    MotorcycleDataMotorcycle motorcycle;
    ServiceDataService service;
    Button seeMotorcycleDetailsButton, seeInvoiceDetailsButton, cancelServiceButton;
    int serviceId;
    boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        seeMotorcycleDetailsButton = findViewById(R.id.see_motorcycle_details_btn);
        seeInvoiceDetailsButton = findViewById(R.id.see_invoice_details_btn);
        cancelServiceButton = findViewById(R.id.cancel_service_btn);

        seeMotorcycleDetailsButton.setOnClickListener(view -> openMotorcycleDetailsActivity());
        seeInvoiceDetailsButton.setOnClickListener(view -> openInvoiceDetailsActivity());
        cancelServiceButton.setOnClickListener(view -> showAlertDialog());

        checkIsAdmin();
    }

    private void checkIsAdmin() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accessToken", "");

        ProgressBar loading = findViewById(R.id.loading);

        UserClient call = new UserClient();
        loading.setVisibility(View.VISIBLE);

        call.getApi().getUser("Bearer " + accessToken).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserDataUser user = response.body().getData().getUser();
                    isAdmin = user.isAdmin();
                    getServiceDetails();
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

    public void getServiceDetails() {
        ImageView imageView = findViewById(R.id.service_details_img);
        TextView statusContent = findViewById(R.id.status_content);
        TextView typeContent = findViewById(R.id.type_content);
        TextView dateContent = findViewById(R.id.date_content);
        TextView timeContent = findViewById(R.id.time_content);
        TextView requestContent = findViewById(R.id.request_content);
        TextView pickUpAndDropMessage = findViewById(R.id.pick_up_and_drop_message);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            service = (ServiceDataService) getIntent().getSerializableExtra("serviceData");

            serviceId = service.getId();
            int serviceStatus = service.getServiceStatus();
            int type = service.getType();
            String serviceDateTime = service.getServiceTime();
            String request = service.getRequest();
            boolean isPickUpAndDrop = service.isPickUpAndDrop();

            String status;
            String serviceType;

            if (serviceStatus == 1) {
                status = "Pending";
            } else if (serviceStatus == 2) {
                status = "Ongoing";
            } else if (serviceStatus == 3) {
                status = "Finished";
            } else {
                status = "Canceled";
            }

            if (type == 1) {
                serviceType = "Light Service";
            } else {
                serviceType = "Full Service";
            }

            SimpleDateFormat tzDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat commonDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = null;

            try {
                Date d = tzDateFormat.parse(serviceDateTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d);
                calendar.add(Calendar.HOUR_OF_DAY, 7);
                formattedDate = commonDateFormat.format(calendar.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            imageView.setImageResource(R.drawable.start);
            statusContent.setText(status);
            typeContent.setText(serviceType);
            dateContent.setText(formattedDate.substring(0,10));
            timeContent.setText(formattedDate.substring(11,16));
            requestContent.setText(request);

            if (serviceStatus != 3 || isAdmin) {
                seeInvoiceDetailsButton.setVisibility(View.GONE);
            }

            if (serviceStatus != 1 || isAdmin) {
                cancelServiceButton.setVisibility(View.GONE);
            }

            if (!isPickUpAndDrop) {
                pickUpAndDropMessage.setVisibility(View.GONE);
            }
        }
    }

    private void openMotorcycleDetailsActivity() {
        motorcycle = (MotorcycleDataMotorcycle) getIntent().getSerializableExtra("motorcycleData");
        Intent intent = new Intent(ServiceDetailsActivity.this, MotorcycleDetailsActivity.class);
        intent.putExtra("data", motorcycle);
        startActivity(intent);
    }

    private void openInvoiceDetailsActivity() {
        Intent intent = new Intent(ServiceDetailsActivity.this, InvoiceDetailsActivity.class);
        intent.putExtra("serviceId", serviceId);
        startActivity(intent);
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder
                .setIcon(R.drawable.logo)
                .setTitle("Cancel")
                .setMessage("Are you sure want to cancel this service?")
                .setCancelable(false)
                .setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                })
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    deleteServiceData();
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteServiceData() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accessToken", "");

        ProgressBar loading = findViewById(R.id.loading);

        ServiceClient call = new ServiceClient();
        loading.setVisibility(View.VISIBLE);

        call.getApi().removeService(serviceId, "Bearer " + accessToken).enqueue(new Callback<Motorcycle>() {
            @Override
            public void onResponse(Call<Motorcycle> call, Response<Motorcycle> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Intent intent = new Intent(ServiceDetailsActivity.this, MainActivity.class);
                    intent.putExtra("fragmentName", "orders");
                    startActivity(intent);
                    showToast("Service canceled successfully");
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

    private void showToast(String message) {
        Toast.makeText(ServiceDetailsActivity.this, message, Toast.LENGTH_LONG).show();
    }
}