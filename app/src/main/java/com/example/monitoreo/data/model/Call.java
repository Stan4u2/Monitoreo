package com.example.monitoreo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Call implements Serializable {
    @SerializedName("done")
    Boolean done;
}
