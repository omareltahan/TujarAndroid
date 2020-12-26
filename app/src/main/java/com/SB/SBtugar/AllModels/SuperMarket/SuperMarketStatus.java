package com.SB.SBtugar.AllModels.SuperMarket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Project ${PROJECT}
 * Created by asamy on 12/21/2018.
 */
public class SuperMarketStatus implements Serializable {

    @SerializedName("isOpen")
    @Expose
    private boolean isOpen;

    @SerializedName("openingTime")
    @Expose
    private String openingTime = "";

    @SerializedName("closingTime")
    @Expose
    private String closingTime = "";

    public String getClosingTime() {
        return closingTime;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }
}
