package com.leo.fixcycle.networks;

import com.leo.fixcycle.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserService {
    @POST("login")
    Call<User> getUser(@Body User user);

    @POST("register")
    Call<User> saveUser(@Body User user);

    @DELETE("logout")
    Call<User> removeUser(@Header("Authorization") String accessToken);

    @GET("users")
    Call<User> getUser(@Header("Authorization") String accessToken);

    @PUT("users")
    Call<User> updateUser(@Body User user, @Header("Authorization") String accessToken);
}