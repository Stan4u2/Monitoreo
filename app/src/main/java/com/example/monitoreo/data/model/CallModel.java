package com.example.monitoreo.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CallModel implements Serializable {
    @SerializedName("done")
    Boolean done;

    @SerializedName("ok")
    Boolean ok;

    public CallModel (){

    }

    public CallModel(Boolean done, Boolean ok) {
        this.done = done;
        this.ok = ok;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }
}
