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

public class IncomingOrdersAdapter extends RecyclerView.Adapter<IncomingOrdersAdapter.IncomingOrdersViewHolder> {
    List<MotorcycleDataMotorcycle> motorcyclesDataHolder;
    List<ServiceDataService> incomingServicesDataHolder;
    OnIncomingOrdersListener onIncomingOrdersListener;

    public IncomingOrdersAdapter(List<MotorcycleDataMotorcycle> motorcyclesDataHolder, List<ServiceDataService> incomingServicesDataHolder, OnIncomingOrdersListener onIncomingOrdersListener) {
        this.motorcyclesDataHolder = motorcyclesDataHolder;
        this.incomingServicesDataHolder = incomingServicesDataHolder;
        this.onIncomingOrdersListener = onIncomingOrdersListener;
    }

    @NonNull
    @Override
    public IncomingOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_row_incoming_orders, parent, false);
        return new IncomingOrdersViewHolder(view, onIncomingOrdersListener);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomingOrdersAdapter.IncomingOrdersViewHolder holder, int position) {
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

    class IncomingOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img;
        TextView name, brand, licensePlate;
        Button rejectButton, acceptButton;
        OnIncomingOrdersListener onIncomingOrdersListener;

        public IncomingOrdersViewHolder(@NonNull View itemView, OnIncomingOrdersListener onIncomingOrdersListener){
            super(itemView);
            this.onIncomingOrdersListener = onIncomingOrdersListener;

            img = itemView.findViewById(R.id.motorcycle_img);
            name = itemView.findViewById(R.id.motorcycle_name);
            brand = itemView.findViewById(R.id.motorcycle_brand);
            licensePlate = itemView.findViewById(R.id.motorcycle_license_plate);
            rejectButton = itemView.findViewById(R.id.reject_btn);
            acceptButton = itemView.findViewById(R.id.accept_btn);

            rejectButton.setOnClickListener(this);
            acceptButton.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if (view.getId() == R.id.reject_btn) {
                onIncomingOrdersListener.onRejectButtonClick(incomingServicesDataHolder.get(position).getId());
            } else if (view.getId() == R.id.accept_btn) {
                onIncomingOrdersListener.onAcceptButtonClick(incomingServicesDataHolder.get(position).getId());
            } else {
                onIncomingOrdersListener.onClickShowListener(motorcyclesDataHolder.get(position), incomingServicesDataHolder.get(position));
            }
        }
    }

    public interface OnIncomingOrdersListener {
        void onRejectButtonClick(int incomingServiceId);
        void onAcceptButtonClick(int incomingServiceId);
        void onClickShowListener(MotorcycleDataMotorcycle motorcycleDataMotorcycle, ServiceDataService serviceDataService);
    }
}