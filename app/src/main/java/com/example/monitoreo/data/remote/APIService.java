package com.example.monitoreo.data.remote;

import com.example.monitoreo.data.model.Area;
import com.example.monitoreo.data.model.Element;
import com.example.monitoreo.data.model.Section;
import com.example.monitoreo.data.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {

    @GET("areas")
    Call<List<Area>> getAllAreas(@Header("Authorization") String access_token);

    @GET("areas/{id}")
    Call<Area> getElementArea(@Header("Authorization") String access_token, @Path("id") long id);

    @GET("secciones")
    Call<List<Section>> getAllSections(@Header("Authorization") String access_token);

    @GET("secciones/{id}")
    Call<Section> getElementSection(@Header("Authorization") String access_token, @Path("id") long id);

    @GET("areas/{id}/secciones")
    Call<List<Section>> getSectionsWithArea(@Header("Authorization") String access_token, @Path("id") long id);

    @GET("elementos")
    Call<List<Element>> getAllElements(@Header("Authorization") String access_token);

    @GET("usuarios")
    Call<List<User>> getAllUsers(@Header("Authorization") String access_token);

    @GET("usuarios/{id}")
    Call<User> getUserData(@Header("Authorization") String access_token, @Path("id") long id);

    @GET("areas/{id}/elementos/count")
    Call<Area> countElementsInArea(@Header("Authorization") String access_token, @Path("id") long id);

    @GET("areas/{id}/secciones/count")
    Call<Area> countSectionsInArea(@Header("Authorization") String access_token, @Path("id") long id);

    @GET("secciones/{id}/elementos/count")
    Call<Section> countElementsInSection(@Header("Authorization") String access_token, @Path("id") long id);

    @POST("secciones")
    Call<Section> createSection(@Header("Authorization") String access_token, @Body Section section);

    @POST("areas")
    Call<Area> createArea(@Header("Authorization") String access_token, @Body Area area);

    @POST("elementos")
    Call<Element> createElement(@Header("Authorization") String access_token, @Body Element element);

    @POST("usuarios/login")
    Call<User> login(@Body User user);

    @POST("usuarios")
    Call<User> createUser(@Body User user);

    @POST("usuarios/logout")
    Call<APIService> logOut(@Header("Authorization") String access_token);

    @FormUrlEncoded
    @POST("usuarios/reset-password")
    Call<ResponseBody> changePassword(@Header("Authorization") String access_token, @Field("newPassword") String password);

    @FormUrlEncoded
    @PUT("elementos/{id}")
    Call<Element> updateElement(@Header("Authorization") String access_token,
                                @Path("id") long id,
                                @Field("identificadorRfId") String RFID,
                                @Field("etiqueta") String lable,
                                @Field("descriptor") String descriptor,
                                @Field("activo") Boolean state,
                                @Field("observaciones") String Observations,
                                @Field("areaId") long areaID,
                                @Field("seccionId") long sectionID);

    @FormUrlEncoded
    @PUT("areas/{id}")
    Call<Area> updateArea(@Header("Authorization") String access_token,
                          @Path("id") long id,
                          @Field("nombre") String name,
                          @Field("activa") Boolean state);

    @FormUrlEncoded
    @PUT("secciones/{id}")
    Call<Section> updateSection(@Header("Authorization") String access_token,
                                @Path("id") long id,
                                @Field("nombre") String name,
                                @Field("activa") Boolean state,
                                @Field("areaId") long areaID);

    @FormUrlEncoded
    @PUT("usuarios/{id}")
    Call<User> updateUserWithPassword(@Header("Authorization") String access_token,
                          @Path("id") long id,
                          @Field("nombre") String name,
                          @Field("isAdmin") Boolean isAdmin,
                          @Field("username") String username,
                          @Field("email") String email,
                          @Field("password") String password);

    @FormUrlEncoded
    @PATCH("usuarios/{id}")
    Call<User> updateUserNoPassword(@Header("Authorization") String access_token,
                                      @Path("id") long id,
                                      @Field("nombre") String name,
                                      @Field("isAdmin") Boolean isAdmin,
                                      @Field("username") String username,
                                      @Field("email") String email);

    @DELETE("elementos/{id}")
    Call<ResponseBody> deleteElement(@Header("Authorization") String access_token, @Path("id") long id);

    @DELETE("areas/{id}")
    Call<ResponseBody> deleteArea(@Header("Authorization") String access_token, @Path("id") long id);

    @DELETE("secciones/{id}")
    Call<ResponseBody> deleteSection(@Header("Authorization") String access_token, @Path("id") long id);

    @DELETE("usuarios/{id}")
    Call<ResponseBody> deleteUser(@Header("Authorization") String access_token, @Path("id") long id);

}
