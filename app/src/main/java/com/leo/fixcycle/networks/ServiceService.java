package com.leo.fixcycle.networks;

import com.leo.fixcycle.models.Service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ServiceService {
    @GET("services/user")
    Call<Service> getService(@Header("Authorization") String accessToken);

    @POST("services")
    Call<Service> saveService(@Body Service service, @Header("Authorization") String accessToken);
}