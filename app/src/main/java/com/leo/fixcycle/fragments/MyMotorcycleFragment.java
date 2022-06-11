package com.leo.fixcycle.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.leo.fixcycle.R;
import com.leo.fixcycle.activities.AddMotorcycleActivity;
import com.leo.fixcycle.activities.MotorcycleDetailsActivity;
import com.leo.fixcycle.models.Motorcycle;
import com.leo.fixcycle.adapters.MotorcycleAdapter;

import java.util.ArrayList;

public class MyMotorcycleFragment extends Fragment implements MotorcycleAdapter.OnClickShowListener{
    RecyclerView recyclerView;
    ArrayList<Motorcycle> motorcycleDataHolder;
    View view;
    Button button;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_motorcycle, container, false);

        button =  view.findViewById(R.id.add_motorcycle_btn);
        recyclerView = view.findViewById(R.id.mymoto_recycle_view);

        button.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddMotorcycleActivity.class);
            startActivity(intent);
        });

        insertMotor();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MotorcycleAdapter(motorcycleDataHolder, this));

        return view;
    }


    public void insertMotor(){
        motorcycleDataHolder = new ArrayList<>();

        Motorcycle motor1 = new Motorcycle(R.drawable.logo,1,"Motorola","D 2323 FX","Honda","Motor mainan","Duamax","Gradien blue ocean","2018-01-01 00:00");
        motorcycleDataHolder.add(motor1);

        Motorcycle motor2 = new Motorcycle(R.drawable.logo,1,"CBR 150 KW","D 3423 MX","Kawasaki","Motor mainan","Duamax","Gradien blue ocean","2018-01-01 00:00");
        motorcycleDataHolder.add(motor2);

        Motorcycle motor3 = new Motorcycle(R.drawable.logo,1,"R15","F 233 L","Yamaha","Motor mainan","Duamax","Gradien blue ocean","2018-01-01 00:00");
        motorcycleDataHolder.add(motor3);

        Motorcycle motor4 = new Motorcycle(R.drawable.logo,1,"PCX","392838","Honda","Motor mainan","Duamax","Gradien blue ocean","2018-01-01 00:00");
        motorcycleDataHolder.add(motor4);

        Motorcycle motor5 = new Motorcycle(R.drawable.logo,1,"CBR 150 XRS","392838","KawasakingRX","Motor mainan","Duamax","Gradien blue ocean","2018-01-01 00:00");
        motorcycleDataHolder.add(motor5);

        Motorcycle motor6 = new Motorcycle(R.drawable.logo,1,"Motorola","392838","Kawasaki","Motor mainan","Duamax","Gradien blue ocean","2018-01-01 00:00");
        motorcycleDataHolder.add(motor6);
        motorcycleDataHolder.add(motor6);
        motorcycleDataHolder.add(motor6);
        motorcycleDataHolder.add(motor6);
        motorcycleDataHolder.add(motor6);
        motorcycleDataHolder.add(motor6);
        motorcycleDataHolder.add(motor6);
        motorcycleDataHolder.add(motor6);

    }

    @Override
    public void onClickShowListener(int position) {
        int image = motorcycleDataHolder.get(position).getImage();
        int cylinderCapacity = motorcycleDataHolder.get(position).getCylinderCapacity();
        String name = motorcycleDataHolder.get(position).getName();
        String type = motorcycleDataHolder.get(position).getType();
        String licensePlate = motorcycleDataHolder.get(position).getLicensePlate();
        String brand = motorcycleDataHolder.get(position).getBrand();
        String fuelType = motorcycleDataHolder.get(position).getFuelType();
        String color = motorcycleDataHolder.get(position).getColor();
        String productionYear = motorcycleDataHolder.get(position).getDate();

        Intent intent =  new Intent(getActivity(), MotorcycleDetailsActivity.class);
        intent.putExtra("name", name.toString());
        intent.putExtra("image", image);
        intent.putExtra("brand", brand.toString());
        intent.putExtra("type", type.toString());
        intent.putExtra("license_plate", licensePlate.toString());
        intent.putExtra("cylinder_capacity", cylinderCapacity);
        intent.putExtra("color", color);
        intent.putExtra("fuel_type", fuelType);
        intent.putExtra("production_year",productionYear);
        startActivity(intent);
    }
}