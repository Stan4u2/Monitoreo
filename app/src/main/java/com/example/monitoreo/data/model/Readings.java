package com.example.monitoreo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Readings implements Serializable {

    @SerializedName("hora")
    private String Time;

    @SerializedName("id")
    private Integer idReading;

    @SerializedName("usuarioId")
    private Integer UserIDFK;

    public Readings () {

    }

    public Readings(String time, Integer idReading, Integer userIDFK) {
        this.Time = time;
        this.idReading = idReading;
        this.UserIDFK = userIDFK;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public Integer getIdReading() {
        return idReading;
    }

    public void setIdReading(Integer idReading) {
        this.idReading = idReading;
    }

    public Integer getUserIDFK() {
        return UserIDFK;
    }

    public void setUserIDFK(Integer userIDFK) {
        UserIDFK = userIDFK;
    }
}
