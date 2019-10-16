package com.example.monitoreo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Area implements Serializable {

    @SerializedName("id")
    private Integer id;

    @SerializedName("activa")
    private Boolean activa;

    @SerializedName("nombre")
    private String name;

    public Area (){

    }

    public Area(String name, Boolean activa) {
        this.name = name;
        this.activa = activa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
