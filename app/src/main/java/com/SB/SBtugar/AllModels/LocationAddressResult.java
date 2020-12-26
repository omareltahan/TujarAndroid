package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class LocationAddressResult implements Serializable {

    @SerializedName("results")
    @Expose
    private ArrayList<LocationAddressResultItem> results;

    public ArrayList<LocationAddressResultItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<LocationAddressResultItem> results) {
        this.results = results;
    }
}
