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

import java.util.List;


public class ChooseMotorcycleAdapter extends RecyclerView.Adapter<ChooseMotorcycleAdapter.myviewholder>{

    List<MotorcycleDataMotorcycle> motorcyclesDataHolder;
    OnClickShowListener mOnClickShowListener;

    public ChooseMotorcycleAdapter(List<MotorcycleDataMotorcycle> motorcyclesDataHolder, OnClickShowListener onClickShowListener) {
        this.motorcyclesDataHolder = motorcyclesDataHolder;
        this.mOnClickShowListener = onClickShowListener;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_choose_motorcycle,parent, false);
        return new myviewholder(view, mOnClickShowListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseMotorcycleAdapter.myviewholder holder, int position) {
        MotorcycleDataMotorcycle res = motorcyclesDataHolder.get(position);
        holder.img.setImageResource(R.drawable.start);
        holder.name.setText(res.getName());
        holder.brand.setText(res.getBrand());
        holder.licensePlate.setText(res.getLicensePlate());

        holder.itemView.setOnClickListener(view -> {
            mOnClickShowListener.onClickShowListener(res);
        });
    }

    @Override
    public int getItemCount() {
        return motorcyclesDataHolder.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name,brand,licensePlate;
        OnClickShowListener onClickShowListener;
        public myviewholder(@NonNull View itemView, OnClickShowListener onClickShowListener){
            super(itemView);
            img= itemView.findViewById(R.id.motorcycle_img);
            name =itemView.findViewById(R.id.motorcycle_name);
            brand= itemView.findViewById(R.id.motorcycle_brand);
            licensePlate=itemView.findViewById(R.id.motorcycle_license_plate);
        }

    }

    public interface OnClickShowListener{
        void onClickShowListener(MotorcycleDataMotorcycle motorcycleDataMotorcycle);
    }

    public void setData(List<MotorcycleDataMotorcycle> motorcyclesDataHolder){
        this.motorcyclesDataHolder = motorcyclesDataHolder;
        notifyDataSetChanged();
    }

}
