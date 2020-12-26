package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Location_address_components implements Serializable {

    @SerializedName("short_name")
    @Expose
    private String short_name;

    @SerializedName("long_name")
    @Expose
    private String long_name;

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getShort_name() {
        return short_name;
    }
}
