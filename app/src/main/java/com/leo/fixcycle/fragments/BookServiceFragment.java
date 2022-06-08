package com.leo.fixcycle.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.leo.fixcycle.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BookServiceFragment extends Fragment {
    Button selectDatebtn;
    Button selectTimebtn;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    int year,month,day;
    int hours,minute;
    Calendar calendar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_service, container, false);
        selectDatebtn = view.findViewById(R.id.service_date_btn);
        selectTimebtn = view.findViewById(R.id.service_time_btn);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        selectDatebtn.setOnClickListener(v -> {
            datePickerDialog = new DatePickerDialog(getContext(),
                    (DatePickerDialog.OnDateSetListener) (datePicker, year, month, day) -> selectDatebtn.setText(day + "/" + month + "/" + year),year,month,day);
            datePickerDialog.show();
        });

        selectTimebtn.setOnClickListener(v -> {
            timePickerDialog = new TimePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog_NoActionBar,
                    (TimePickerDialog.OnTimeSetListener) (timePicker, i, i1) -> {
                        hours= i;
                        minute= i1;
                        String time= hours + ":" + minute;

                        SimpleDateFormat f24hours = new SimpleDateFormat(
                                "HH:mm"
                        );
                        try {
                            Date date = f24hours.parse(time);

                            SimpleDateFormat f12hours =  new SimpleDateFormat(
                                    "hh:mm aa"
                            );
                            selectTimebtn.setText(f12hours.format(date));
                        }catch (ParseException e){
                            e.printStackTrace();
                        }

                    },12,0,false);
            timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            timePickerDialog.updateTime(hours,minute);
            timePickerDialog.show();
        });

        return view;
    }

}