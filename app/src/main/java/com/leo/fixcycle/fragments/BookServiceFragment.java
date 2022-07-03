package com.leo.fixcycle.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.leo.fixcycle.R;
import com.leo.fixcycle.activities.ChooseMotorcycleActivity;
import com.leo.fixcycle.activities.MainActivity;
import com.leo.fixcycle.models.MotorcycleDataMotorcycle;
import com.leo.fixcycle.models.Service;
import com.leo.fixcycle.networks.ServiceClient;

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

public class BookServiceFragment extends Fragment {
    MotorcycleDataMotorcycle motorcycle;
    Button chooseMotorcycleButton;
    Button serviceDateButton;
    Button serviceTimeButton;
    Button bookServiceButton;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    int year, month, day, hour, minute;
    Calendar calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_service, container, false);

        chooseMotorcycleButton = view.findViewById(R.id.motorcycle_choose_btn);
        serviceDateButton = view.findViewById(R.id.service_date_btn);
        serviceTimeButton = view.findViewById(R.id.service_time_btn);
        bookServiceButton = view.findViewById(R.id.book_service_btn);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        Bundle data = getArguments();

        if (data != null) {
            motorcycle = (MotorcycleDataMotorcycle) getArguments().getSerializable("data");
            String name = motorcycle.getName();
            chooseMotorcycleButton.setText(name);
        }

        chooseMotorcycleButton.setOnClickListener(v -> openActivity());
        serviceDateButton.setOnClickListener(v -> showDate());
        serviceTimeButton.setOnClickListener(v -> showTime());

        bookServiceButton.setOnClickListener(v -> {
            if (data != null) {
                int motorcycleId = motorcycle.getId();
                showAlertDialog(view, motorcycleId);
            } else {
                showToast("Please complete the input");
            }
        });

        return view;
    }

    private void openActivity() {
        Intent intent = new Intent(getActivity(), ChooseMotorcycleActivity.class);
        startActivity(intent);
    }

    private void showDate() {
        datePickerDialog = new DatePickerDialog(
            getContext(),
            (DatePickerDialog.OnDateSetListener) (datePicker, year, month, day) -> {
                serviceDateButton.setText(year + "-" + (month + 1) + "-" + day);
            }, year, month, day);

        datePickerDialog.show();
    }

    private void showTime() {
        timePickerDialog = new TimePickerDialog(
            getContext(),
            android.R.style.Theme_Holo_Dialog_NoActionBar,
            (TimePickerDialog.OnTimeSetListener) (timePicker, i, i1) -> {
                hour = i;
                minute = i1;

                String time = hour + ":" + minute;
                SimpleDateFormat f24hours = new SimpleDateFormat("HH:mm");

                try {
                    Date date = f24hours.parse(time);
                    SimpleDateFormat f12hours =  new SimpleDateFormat("hh:mm aa");
                    serviceTimeButton.setText(f12hours.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            },12,0,false);

        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.updateTime(hour, minute);
        timePickerDialog.show();
    }

    private void showAlertDialog(View view, int motorcycleId) {
        if (getActivity() != null) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

            alertDialogBuilder
                    .setIcon(R.drawable.logo)
                    .setTitle("Book Service")
                    .setMessage("Are you sure want to book this service?")
                    .setCancelable(false)
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        dialogInterface.cancel();
                    })
                    .setPositiveButton("Book", (dialogInterface, i) -> {
                        bookService(view, motorcycleId);
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void bookService(View view,int motorcycleId) {
        if (getActivity() != null) {
            SharedPreferences sp = getActivity().getApplicationContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String accessToken = sp.getString("accessToken", "");

            Button chooseMotorcycleButton = view.findViewById(R.id.motorcycle_choose_btn);
            RadioGroup serviceRadio = view.findViewById(R.id.service_type_radio);
            TextInputEditText serviceRequest = view.findViewById(R.id.service_request);
            Button serviceDateButton = view.findViewById(R.id.service_date_btn);
            Button serviceTimeButton = view.findViewById(R.id.service_time_btn);
            CheckBox pickUpAndDropCheckBox = view.findViewById(R.id.pick_up_and_drop_check_box);
            ProgressBar loading = view.findViewById(R.id.loading);

            String name = chooseMotorcycleButton.getText().toString();
            int serviceRadioChecked = serviceRadio.getCheckedRadioButtonId();

            if (serviceRadioChecked != -1) {
                RadioButton selectedService = view.findViewById(serviceRadioChecked);
                String selectedServiceText = selectedService.getText().toString();

                if (selectedServiceText.matches("Light Service")) {
                    serviceRadioChecked = 1;
                } else {
                    serviceRadioChecked = 2;
                }
            }

            String serviceReq = serviceRequest.getText().toString();
            String serviceDate = serviceDateButton.getText().toString();
            String serviceTime = serviceTimeButton.getText().toString();
            String serviceTimeFormat = serviceDate + " " + serviceTime;

            if (!isValidService(name, serviceRadioChecked, serviceDate, serviceTime)) {
                return;
            }

            Service service = new Service();
            service.setMotorcycleId(motorcycleId);
            service.setType(serviceRadioChecked);
            service.setRequest(serviceReq);
            service.setServiceTime(serviceTimeFormat);
            service.setPickUpAndDrop(pickUpAndDropCheckBox.isChecked());
            service.setServiceStatus(1);

            ServiceClient call = new ServiceClient();
            loading.setVisibility(View.VISIBLE);

            call.getApi().saveService(service, "Bearer " + accessToken).enqueue(new Callback<Service>() {
                @Override
                public void onResponse(Call<Service> call, Response<Service> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("fragmentName", "orders");
                        startActivity(intent);
                        showToast("Service booked successfully");
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
    }

    private boolean isValidService(String name, int serviceRadioCheck, String serviceDate, String serviceTime) {
        boolean isValid = false;

        if (name.matches("Choose motorcycle")) {
            showToast("Name input is required");
        } else if (serviceRadioCheck==-1) {
            showToast("Service type input is required");
        } else if (serviceDate.matches("Choose date")) {
            showToast("Service date input is required");
        } else if (serviceTime.matches("Choose time")) {
            showToast("Service time input is required");
        } else {
            isValid = true;
        }

        return isValid;
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}