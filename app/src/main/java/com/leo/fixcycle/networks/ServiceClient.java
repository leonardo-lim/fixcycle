package com.leo.fixcycle.networks;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceClient {
    private static final String BASE_URL = "https://fixcycle.herokuapp.com/api/v1/";

    public ServiceService getApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ServiceService.class);
    }
}