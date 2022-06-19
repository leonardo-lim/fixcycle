package com.leo.fixcycle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.leo.fixcycle.R;
import com.leo.fixcycle.models.MotorcycleDataMotorcycle;
import com.leo.fixcycle.models.ServiceDataService;

public class ServiceDetailsActivity extends AppCompatActivity {
    MotorcycleDataMotorcycle motorcycle;
    ServiceDataService service;
    Bundle extras;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        button = findViewById(R.id.see_motorcycle_btn);

        getServiceDetails();

        button.setOnClickListener(v->{
            motorcycle = (MotorcycleDataMotorcycle) getIntent().getSerializableExtra("motorcycleData");
            Intent intent = new Intent(ServiceDetailsActivity.this,MotorcycleDetailsActivity.class).putExtra("data",motorcycle);
            startActivity(intent);
        });
    }

    public void getServiceDetails(){
        ImageView imageView = findViewById(R.id.service_details_img);
        TextView statusContent = findViewById(R.id.status_content);
        TextView typeContent = findViewById(R.id.type_content);
        TextView dateContent = findViewById(R.id.date_content);
        TextView timeContent = findViewById(R.id.time_content);
        TextView requestContent = findViewById(R.id.request_content);

        extras = getIntent().getExtras();

        if (extras != null) {
            motorcycle = (MotorcycleDataMotorcycle) getIntent().getSerializableExtra("motorcycleData");
            service = (ServiceDataService) getIntent().getSerializableExtra("serviceData");

            String status;
            int stat =service.getServiceStatus();
            if(stat==1){
                status="Pending";
            }else if(stat==2){
                status="Ongoing";
            }else if(stat==3){
                status="Finished";
            }else{
                status="Canceled";
            }
            String typeService;
            int type = service.getType();
            if(type==1){
                typeService="Light Service";
            }else{
                typeService="Full Service";
            }

            String date = service.getServiceTime();

            String request = service.getRequest();

            imageView.setImageResource(R.drawable.start);
            statusContent.setText(status);
            typeContent.setText(typeService);
            dateContent.setText(date.substring(0,10));
            timeContent.setText(date.substring(11,16));
            requestContent.setText(request);
        }
    }
}