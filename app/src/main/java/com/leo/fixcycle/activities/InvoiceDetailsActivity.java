package com.leo.fixcycle.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.fixcycle.R;
import com.leo.fixcycle.models.Invoice;
import com.leo.fixcycle.models.InvoiceData;
import com.leo.fixcycle.networks.InvoiceClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceDetailsActivity extends AppCompatActivity {
    int serviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            serviceId = extras.getInt("serviceId");
        }

        getInvoiceDetails();
    }

    private void getInvoiceDetails() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accessToken", "");

        TextView serviceTypeCostContent = findViewById(R.id.service_type_cost_content);
        TextView cylinderCapacityCostContent = findViewById(R.id.cylinder_capacity_cost_content);
        TextView adminFeeContent = findViewById(R.id.admin_fee_content);
        TextView additionalFeeContent = findViewById(R.id.additional_fee_content);
        TextView totalCostContent = findViewById(R.id.total_cost_content);
        RelativeLayout unpaidServiceMessage = findViewById(R.id.unpaid_service_message);
        TextView paidServiceMessage = findViewById(R.id.paid_service_message);
        ProgressBar loading = findViewById(R.id.loading);

        InvoiceClient call = new InvoiceClient();
        loading.setVisibility(View.VISIBLE);

        call.getApi().getInvoice(serviceId, "Bearer " + accessToken).enqueue(new Callback<Invoice>() {
            @Override
            public void onResponse(Call<Invoice> call, Response<Invoice> response) {
                if (response.isSuccessful() && response.body() != null) {
                    InvoiceData invoice = response.body().getData();
                    int serviceTypeCost = invoice.getServiceTypeCost();
                    int cylinderCapacityCost = invoice.getCylinderCapacityCost();
                    int adminFee = invoice.getAdminFee();
                    int additionalFee = invoice.getAdditionalFee();
                    int totalCost = invoice.getTotalCost();
                    boolean isPaid = invoice.isPaid();

                    NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
                    String formattedServiceTypeCost = currencyInstance.format(serviceTypeCost);
                    String formattedCylinderCapacityCost = currencyInstance.format(cylinderCapacityCost);
                    String formattedAdminFee = currencyInstance.format(adminFee);
                    String formattedAdditionalFee = currencyInstance.format(additionalFee);
                    String formattedTotalCost = currencyInstance.format(totalCost);

                    serviceTypeCostContent.setText(formattedServiceTypeCost);
                    cylinderCapacityCostContent.setText(formattedCylinderCapacityCost);
                    adminFeeContent.setText(formattedAdminFee);
                    additionalFeeContent.setText(formattedAdditionalFee);
                    totalCostContent.setText(formattedTotalCost);

                    if (!isPaid) {
                        paidServiceMessage.setVisibility(View.GONE);
                        Button payButton = findViewById(R.id.pay_btn);
                        payButton.setOnClickListener(view -> showAlertDialog());
                    } else {
                        unpaidServiceMessage.setVisibility(View.GONE);
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
            public void onFailure(Call<Invoice> call, Throwable t) {
                t.printStackTrace();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InvoiceDetailsActivity.this);

        alertDialogBuilder
                .setIcon(R.drawable.logo)
                .setTitle("Pay")
                .setMessage("Are you sure want to pay this invoice?")
                .setCancelable(false)
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                })
                .setPositiveButton("Pay", (dialogInterface, i) -> {
                    payInvoice();
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void payInvoice() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accessToken", "");

        ProgressBar loading = findViewById(R.id.loading);

        InvoiceClient call = new InvoiceClient();
        loading.setVisibility(View.VISIBLE);

        call.getApi().payInvoice(serviceId, "Bearer " + accessToken).enqueue(new Callback<Invoice>() {
            @Override
            public void onResponse(Call<Invoice> call, Response<Invoice> response) {
                if (response.isSuccessful() && response.body() != null) {
                    finish();
                    startActivity(getIntent());
                    showToast("Service paid successfully");
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

    private void showToast(String message) {
        Toast.makeText(InvoiceDetailsActivity.this, message, Toast.LENGTH_LONG).show();
    }
}