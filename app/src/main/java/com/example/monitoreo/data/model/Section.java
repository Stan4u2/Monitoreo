package com.example.monitoreo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Section implements Serializable {

    @SerializedName("id")
    private Integer id;

    @SerializedName("activa")
    private Boolean state;

    @SerializedName("nombre")
    private String name;

    @SerializedName("areaId")
    private Integer areaId;

    @SerializedName("count")
    private int count;

    public Section(){

    }

    public Section(String name, Boolean state, Integer areaId) {
        this.name = name;
        this.state = state;
        this.areaId = areaId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
