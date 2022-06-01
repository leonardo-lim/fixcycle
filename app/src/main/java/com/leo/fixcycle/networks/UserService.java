package com.leo.fixcycle.networks;

import com.leo.fixcycle.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("users/add")
    Call<User> saveUser(@Body User user);
}