package com.leo.fixcycle.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leo.fixcycle.R;
import com.leo.fixcycle.models.Motorcycle;

import java.util.ArrayList;


public class MotorcycleAdapter extends RecyclerView.Adapter<MotorcycleAdapter.myviewholder>{

    ArrayList<Motorcycle> motorcyclesDataHolder;
    private int limit = 4;

    public MotorcycleAdapter(ArrayList<Motorcycle> motorcyclesDataHolder) {
        this.motorcyclesDataHolder = motorcyclesDataHolder;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_motorcycle,parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MotorcycleAdapter.myviewholder holder, int position) {
        holder.img.setImageResource(motorcyclesDataHolder.get(position).getImage());
        holder.name.setText(motorcyclesDataHolder.get(position).getName());
        holder.brand.setText(motorcyclesDataHolder.get(position).getBrand());
        holder.licensePlate.setText(motorcyclesDataHolder.get(position).getLicensePlate());

    }

    @Override
    public int getItemCount() {
        return motorcyclesDataHolder.size();
    }

    class myviewholder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name,brand,licensePlate;

        public myviewholder(@NonNull View itemView){
            super(itemView);
            img= itemView.findViewById(R.id.motorcycle_img);
            name =itemView.findViewById(R.id.motorcycle_name);
            brand= itemView.findViewById(R.id.motorcycle_brand);
            licensePlate=itemView.findViewById(R.id.motorcycle_license_plate);
        }
    }
}
