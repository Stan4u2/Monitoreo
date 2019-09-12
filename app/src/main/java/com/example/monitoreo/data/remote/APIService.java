package com.example.monitoreo.data.remote;

import com.example.monitoreo.data.model.Area;
import com.example.monitoreo.data.model.Section;
import com.example.monitoreo.data.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface APIService {

    @GET("areas")
    Call<List<Area>> getAllAreas(@Header("access_token") String access_token);

    @GET("secciones")
    Call<List<Section>> getAllSections(@Header("access_token") String access_token);

    @POST("secciones")
    Call<Section> createSection(@Header("access_token") String access_token, @Body Section section);

    @POST("areas")
    Call<Area> createArea(@Header("access_token") String access_token, @Body Area area);

    @POST("usuarios/login")
    Call<User> login(@Body User user);

    @POST("usuarios")
    Call<User> createUser(@Body User user);
}
