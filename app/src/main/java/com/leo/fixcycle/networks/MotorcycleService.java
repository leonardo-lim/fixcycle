package com.leo.fixcycle.networks;

import com.leo.fixcycle.models.Motorcycle;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MotorcycleService {
    @GET("motorcycles")
    Call<Motorcycle> getMotorcycles(@Header("Authorization") String accessToken);

    @GET("motorcycles/user")
    Call<Motorcycle> getMotorcycle(@Header("Authorization") String accessToken);

    @GET("motorcycles/{id}")
    Call<Motorcycle> getMotorcycle(@Path("id") int motorcycleId, @Header("Authorization") String accessToken);

    @POST("motorcycles")
    Call<Motorcycle> saveMotorcycle(@Body Motorcycle motorcycle, @Header("Authorization") String accessToken);

    @PUT("motorcycles/{id}")
    Call<Motorcycle> updateMotorcycle(@Path("id") int motorcycleId, @Body Motorcycle motorcycle, @Header("Authorization") String accessToken);

    @DELETE("motorcycles/{id}")
    Call<Motorcycle> removeMotorcycle(@Path("id") int motorcycleId, @Header("Authorization") String accessToken);
}