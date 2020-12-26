package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AllOffersResponse {

    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("Data")
    @Expose
    ArrayList<SubOffersResponse> dataproduct = new ArrayList<SubOffersResponse>();

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ArrayList<SubOffersResponse> getDataproduct() {
        return dataproduct;
    }

    public void setDataproduct(ArrayList<SubOffersResponse> dataproduct) {
        this.dataproduct = dataproduct;
    }
}
