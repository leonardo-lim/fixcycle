package com.leo.fixcycle.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leo.fixcycle.R;
import com.leo.fixcycle.models.MotorcycleDataMotorcycle;
import com.leo.fixcycle.models.ServiceDataService;

import java.util.List;

public class UserOrdersAdapter extends RecyclerView.Adapter<UserOrdersAdapter.UserOrdersViewHolder> {
    List<MotorcycleDataMotorcycle> motorcyclesDataHolder;
    List<ServiceDataService> servicesDataHolder;
    OnClickOrdersListener onClickOrdersListener;

    public UserOrdersAdapter(List<MotorcycleDataMotorcycle> motorcyclesDataHolder, List<ServiceDataService> servicesDataHolder, OnClickOrdersListener onClickOrdersListener) {
        this.motorcyclesDataHolder = motorcyclesDataHolder;
        this.servicesDataHolder = servicesDataHolder;
        this.onClickOrdersListener = onClickOrdersListener;
    }

    @NonNull
    @Override
    public UserOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_row_motorcycle, parent, false);
        return new UserOrdersViewHolder(view, onClickOrdersListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserOrdersAdapter.UserOrdersViewHolder holder, int position) {
        MotorcycleDataMotorcycle res = motorcyclesDataHolder.get(position);
        holder.img.setImageResource(R.drawable.start);
        holder.name.setText(res.getName());
        holder.brand.setText(res.getBrand());
        holder.licensePlate.setText(res.getLicensePlate());
    }

    @Override
    public int getItemCount() {
        return motorcyclesDataHolder.size();
    }

    class UserOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        TextView name, brand, licensePlate;
        OnClickOrdersListener onClickOrdersListener;

        public UserOrdersViewHolder(@NonNull View itemView, OnClickOrdersListener onClickOrdersListener) {
            super(itemView);
            this.onClickOrdersListener = onClickOrdersListener;

            img = itemView.findViewById(R.id.motorcycle_img);
            name = itemView.findViewById(R.id.motorcycle_name);
            brand = itemView.findViewById(R.id.motorcycle_brand);
            licensePlate = itemView.findViewById(R.id.motorcycle_license_plate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            onClickOrdersListener.onClickShowListener(motorcyclesDataHolder.get(position), servicesDataHolder.get(position));
        }
    }

    public interface OnClickOrdersListener {
        void onClickShowListener(MotorcycleDataMotorcycle motorcycleDataMotorcycle, ServiceDataService serviceDataService);
    }

    public void setData(List<MotorcycleDataMotorcycle> motorcyclesDataHolder, List<ServiceDataService> servicesDataHolder) {
        this.motorcyclesDataHolder = motorcyclesDataHolder;
        this.servicesDataHolder = servicesDataHolder;
        notifyDataSetChanged();
    }
}