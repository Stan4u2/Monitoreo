package com.example.monitoreo.data.remote;

import com.example.monitoreo.data.model.Area;
import com.example.monitoreo.data.model.Section;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {

    @GET("areas")
    Call<List<Area>> getAllAreas();

    @GET("secciones")
    Call<List<Section>> getAllSections();

    @POST("secciones")
    Call<Section> createSection (@Body Section section);

    @POST("areas")
    Call<Area> createArea (@Body Area area);
}
