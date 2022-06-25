package com.leo.fixcycle.models;

public class UserData {
    private String accessToken;
    private UserDataUser user;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserDataUser getUser() {
        return user;
    }

    public void setUser(UserDataUser user) {
        this.user = user;
    }
}