package com.example.monitoreo.data.model;

import com.google.gson.annotations.SerializedName;

public class Section {

    @SerializedName("id")
    private Integer id;

    @SerializedName("nombre")
    private String name;

    public Section(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
