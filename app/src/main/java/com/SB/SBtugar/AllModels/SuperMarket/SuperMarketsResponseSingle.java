package com.SB.SBtugar.AllModels.SuperMarket;

/**
 * Project ${PROJECT}
 * Created by asamy on 12/30/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SuperMarketsResponseSingle {

    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("Data")
    @Expose
    private SuperMarket data = null;
    
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public SuperMarket getData() {
        return data;
    }

    public void setData(SuperMarket data) {
        this.data = data;
    }

}
