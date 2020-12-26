package com.SB.SBtugar.AllModels.SuperMarket;

/**
 * Project ${PROJECT}
 * Created by asamy on 12/30/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SuperMarketsResponseMain {

    @SerializedName("stores")
    @Expose
    private List<SuperMarket> allstores = new ArrayList<>();

    public List<SuperMarket> getAllstores() {
        return allstores;
    }

    public void setAllstores(List<SuperMarket> allstores) {
        this.allstores = allstores;
    }
}
