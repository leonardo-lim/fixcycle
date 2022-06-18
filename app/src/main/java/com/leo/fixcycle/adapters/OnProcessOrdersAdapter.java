package com.leo.fixcycle.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leo.fixcycle.R;
import com.leo.fixcycle.models.MotorcycleDataMotorcycle;
import com.leo.fixcycle.models.ServiceDataService;

import java.util.List;

public class OnProcessOrdersAdapter extends RecyclerView.Adapter<OnProcessOrdersAdapter.OnProcessOrdersViewHolder> {
    List<MotorcycleDataMotorcycle> motorcyclesDataHolder;
    List<ServiceDataService> onProcessServicesDataHolder;
    OnProcessOrdersListener onProcessOrdersListener;

    public OnProcessOrdersAdapter(List<MotorcycleDataMotorcycle> motorcyclesDataHolder, List<ServiceDataService> onProcessServicesDataHolder, OnProcessOrdersListener onProcessOrdersListener) {
        this.motorcyclesDataHolder = motorcyclesDataHolder;
        this.onProcessServicesDataHolder = onProcessServicesDataHolder;
        this.onProcessOrdersListener = onProcessOrdersListener;
    }

    @NonNull
    @Override
    public OnProcessOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_row_on_process_orders, parent, false);
        return new OnProcessOrdersViewHolder(view, onProcessOrdersListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OnProcessOrdersAdapter.OnProcessOrdersViewHolder holder, int position) {
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

    class OnProcessOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        TextView name, brand, licensePlate;
        Button finishServiceButton;
        OnProcessOrdersListener onProcessOrdersListener;

        public OnProcessOrdersViewHolder(@NonNull View itemView, OnProcessOrdersListener onProcessOrdersListener){
            super(itemView);
            this.onProcessOrdersListener = onProcessOrdersListener;

            img= itemView.findViewById(R.id.motorcycle_img);
            name =itemView.findViewById(R.id.motorcycle_name);
            brand= itemView.findViewById(R.id.motorcycle_brand);
            licensePlate=itemView.findViewById(R.id.motorcycle_license_plate);
            finishServiceButton = itemView.findViewById(R.id.finish_service_btn);

            finishServiceButton.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if (view.getId() == R.id.finish_service_btn) {
                onProcessOrdersListener.onFinishServiceButtonClick(onProcessServicesDataHolder.get(position).getId());
            } else {
                onProcessOrdersListener.onClickShowListener(motorcyclesDataHolder.get(position), onProcessServicesDataHolder.get(position));
            }
        }
    }

    public interface OnProcessOrdersListener {
        void onFinishServiceButtonClick(int onProcessServiceId);
        void onClickShowListener(MotorcycleDataMotorcycle motorcycleDataMotorcycle, ServiceDataService serviceDataService);
    }
}