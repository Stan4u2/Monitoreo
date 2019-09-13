package com.example.monitoreo.data.remote;

import com.example.monitoreo.data.model.Area;
import com.example.monitoreo.data.model.Section;
import com.example.monitoreo.data.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {

    @GET("areas")
    Call<List<Area>> getAllAreas(@Header("Authorization") String access_token);

    @GET("secciones")
    Call<List<Section>> getAllSections(@Header("Authorization") String access_token);

    @POST("secciones")
    Call<Section> createSection(@Header("Authorization") String access_token, @Body Section section);

    @POST("areas")
    Call<Area> createArea(@Header("Authorization") String access_token, @Body Area area);

    @POST("usuarios/login")
    Call<User> login(@Body User user);

    @POST("usuarios")
    Call<User> createUser(@Body User user);

    @GET("usuarios/{id}")
    Call<User> check(@Header("Authorization") String access_token, @Path("id") long id);


}
