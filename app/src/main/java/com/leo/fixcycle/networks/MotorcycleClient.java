package com.leo.fixcycle.networks;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MotorcycleClient {
    private static final String BASE_URL = "http://10.0.2.2:5000/api/v1/";

    public MotorcycleService getApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(MotorcycleService.class);
    }
}