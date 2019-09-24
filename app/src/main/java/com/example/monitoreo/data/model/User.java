package com.example.monitoreo.data.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("nombre")
    private String name;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("ttl")
    private Integer ttl;

    @SerializedName("userId")
    private Integer userId;

    //This is the token
    @SerializedName("id")
    private String id;

    public User(String name, String username, String email, String password, Integer ttl) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.ttl = ttl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getTtl() {
        return ttl;
    }

    public void setTtl(Integer ttl) {
        this.ttl = ttl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
