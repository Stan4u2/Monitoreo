package com.example.monitoreo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Section implements Serializable {

    @SerializedName("id")
    private Integer id;

    @SerializedName("activa")
    private Boolean activa;

    @SerializedName("nombre")
    private String name;

    @SerializedName("areaId")
    private Integer areaId;

    public Section(){

    }

    public Section(String name, Boolean activa, Integer areaId) {
        this.name = name;
        this.activa = activa;
        this.areaId = areaId;
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

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }
}
