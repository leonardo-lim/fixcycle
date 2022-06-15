package com.leo.fixcycle.networks;

import com.leo.fixcycle.models.Service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ServiceService {
    @GET("services/user")
    Call<Service> getService(@Header("Authorization") String accessToken);
}