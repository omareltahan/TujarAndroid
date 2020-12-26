package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Project SuperBekala
 * Created by asamy on 3/11/2018.
 */

public class PointsMain {
    @SerializedName("Data")
    @Expose
    private ArrayList<Points> Data;

    public ArrayList<Points> getData() {
        return Data;
    }

    public void setData(ArrayList<Points> data) {
        Data = data;
    }
}
