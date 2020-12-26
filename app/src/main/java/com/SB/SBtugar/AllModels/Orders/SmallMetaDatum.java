package com.SB.SBtugar.AllModels.Orders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Project ${PROJECT}
 * Created by asamy on 4/28/2018.
 */

public class SmallMetaDatum implements Serializable {


    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("value")
    @Expose
    private Object value;
    private final static long serialVersionUID = 7103709520098126141L;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
