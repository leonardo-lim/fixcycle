package com.leo.fixcycle.networks;

import com.leo.fixcycle.models.Motorcycle;
import com.leo.fixcycle.models.Service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceService {
    @GET("services")
    Call<Service> getServices(@Header("Authorization") String accessToken);

    @GET("services/user")
    Call<Service> getService(@Header("Authorization") String accessToken);

    @POST("services")
    Call<Service> saveService(@Body Service service, @Header("Authorization") String accessToken);

    @PUT("services/{id}")
    Call<Service> updateService(@Path("id") int serviceId, @Body Service service, @Header("Authorization") String accessToken);

    @DELETE("services/{id}")
    Call<Motorcycle> removeService(@Path("id") int serviceId, @Header("Authorization") String accessToken);
}