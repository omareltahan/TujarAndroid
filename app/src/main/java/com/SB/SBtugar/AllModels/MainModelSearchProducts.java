package com.SB.SBtugar.AllModels;

/**
 * Project ${PROJECT}
 * Created by asamy on 12/21/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MainModelSearchProducts implements Serializable
{

    @SerializedName("Data")
    ArrayList<Product> Data = new ArrayList<>();

    public ArrayList<Product> getData() {
        return Data;
    }

    public void setData(ArrayList<Product> data) {
        Data = data;
    }
}