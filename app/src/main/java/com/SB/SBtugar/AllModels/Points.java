package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Project SuperBekala
 * Created by asamy on 3/11/2018.
 */

public class Points {
    @SerializedName("points")
    @Expose
    private int Points;

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        Points = points;
    }
}
