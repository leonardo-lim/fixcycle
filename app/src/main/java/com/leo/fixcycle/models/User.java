package com.leo.fixcycle.models;

public class User {
    private String name;
    private String email;
    private String password;
    private UserData data;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserData getData() {
        return data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setData(UserData data) {
        this.data = data;
    }
}