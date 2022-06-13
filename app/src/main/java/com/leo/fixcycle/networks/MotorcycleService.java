package com.leo.fixcycle.networks;

import com.leo.fixcycle.models.Motorcycle;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface MotorcycleService {
    @GET("motorcycles/user")
    Call<Motorcycle> getMotorcycle(@Header("Authorization") String accessToken);

    @POST("motorcycles")
    Call<Motorcycle> saveMotorcycle(@Body Motorcycle motorcycle, @Header("Authorization") String accessToken);
}