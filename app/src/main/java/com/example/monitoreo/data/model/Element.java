package com.example.monitoreo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Element implements Serializable {

    @SerializedName("identificadorRfId")
    private String RFID;

    @SerializedName("etiqueta")
    private String Lable;

    @SerializedName("descriptor")
    private String Descriptor;

    @SerializedName("activo")
    private Boolean State;

    @SerializedName("observaciones")
    private String Observations;

    @SerializedName("id")
    private Integer id;

    @SerializedName("areaId")
    private Integer AreaID;

    @SerializedName("seccionId")
    private Integer SectionID;

    public Element(){

    }

    public Element(String RFID, String lable, String descriptor, Boolean state, String observations, Integer areaID, Integer sectionID) {
        this.RFID = RFID;
        this.Lable = lable;
        this.Descriptor = descriptor;
        this.State = state;
        this.Observations = observations;
        this.AreaID = areaID;
        this.SectionID = sectionID;
    }

    public String getRFID() {
        return RFID;
    }

    public void setRFID(String RFID) {
        this.RFID = RFID;
    }

    public String getLable() {
        return Lable;
    }

    public void setLable(String lable) {
        Lable = lable;
    }

    public String getDescriptor() {
        return Descriptor;
    }

    public void setDescriptor(String descriptor) {
        Descriptor = descriptor;
    }

    public Boolean getState() {
        return State;
    }

    public void setState(Boolean state) {
        State = state;
    }

    public String getObservations() {
        return Observations;
    }

    public void setObservations(String observations) {
        Observations = observations;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAreaID() {
        return AreaID;
    }

    public void setAreaID(Integer areaID) {
        AreaID = areaID;
    }

    public Integer getSectionID() {
        return SectionID;
    }

    public void setSectionID(Integer sectionID) {
        SectionID = sectionID;
    }
}
