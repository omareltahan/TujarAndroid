package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Area implements Serializable {

    @SerializedName("city")
    @Expose
    private display_value_item city;
    @SerializedName("area")
    @Expose
    private List<display_value_item> area = null;

    public display_value_item getCity() {
        return city;
    }

    public void setCity(display_value_item city) {
        this.city = city;
    }

    public List<display_value_item> getArea() {
        return area;
    }

    public void setArea(List<display_value_item> area) {
        this.area = area;
    }

}
