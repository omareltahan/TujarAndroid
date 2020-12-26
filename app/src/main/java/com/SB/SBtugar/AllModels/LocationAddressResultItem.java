package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class LocationAddressResultItem implements Serializable {

    @SerializedName("formatted_address")
    @Expose
    private String formatted_address;


  @SerializedName("address_components")
    @Expose
    private ArrayList<Location_address_components> address_components;

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public ArrayList<Location_address_components> getAddress_components() {
        return address_components;
    }

    public void setAddress_components(ArrayList<Location_address_components> address_components) {
        this.address_components = address_components;
    }
}
