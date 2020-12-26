package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.SerializedName;

/**
 * Project ${PROJECT}
 * Created by asamy on 4/28/2018.
 */

public class RatingViewMainResponse {
    @SerializedName("Data")
    RatingViewSubMainResponse Data;

    public RatingViewSubMainResponse getData() {
        return Data;
    }

    public void setData(RatingViewSubMainResponse data) {
        Data = data;
    }
}
