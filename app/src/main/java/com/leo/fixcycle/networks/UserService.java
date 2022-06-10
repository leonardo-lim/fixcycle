package com.leo.fixcycle.networks;

import com.leo.fixcycle.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserService {
    @POST("login")
    Call<User> getUser(@Body User user);

    @POST("register")
    Call<User> saveUser(@Body User user);

    @GET("users")
    Call<User> getUser(@Header("Authorization") String accessToken);
}