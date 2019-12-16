package com.example.monitoreo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CallModel implements Serializable {
    @SerializedName("done")
    Boolean done;

    public CallModel (){

    }

    public CallModel(Boolean done, Boolean ok) {
        this.done = done;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}
