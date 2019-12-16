package com.example.monitoreo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReadingsDetails implements Serializable {
    @SerializedName("leido")
    Boolean read;

    @SerializedName("id")
    Integer id;

    @SerializedName("lecturaId")
    Integer readingID;

    @SerializedName("elementoId")
    Integer elementID;

    public ReadingsDetails(Boolean read, Integer id, Integer readingID, Integer elementID) {
        this.read = read;
        this.id = id;
        this.readingID = readingID;
        this.elementID = elementID;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReadingID() {
        return readingID;
    }

    public void setReadingID(Integer readingID) {
        this.readingID = readingID;
    }

    public Integer getElementID() {
        return elementID;
    }

    public void setElementID(Integer elementID) {
        this.elementID = elementID;
    }
}
