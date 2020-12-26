package com.SB.SBtugar.AllModels.SuperMarket;

/**
 * Project ${PROJECT}
 * Created by asamy on 12/30/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SuperMarketsResponseMain2 {

    @SerializedName("stores")
    @Expose
    private List<SuperMarket> data = new ArrayList<>();

    public List<SuperMarket> getData() {
        return data;
    }

    public void setData(List<SuperMarket> data) {
        this.data = data;
    }

}
