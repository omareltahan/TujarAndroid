package com.SB.SBtugar.AllModels;

/**
 * Project ${PROJECT}
 * Created by asamy on 4/27/2019.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {

    @SerializedName("android_version")
    @Expose
    private String android_version;
    @SerializedName("areas_2")
    @Expose
    private List<Area> areas = null;
    @SerializedName("categories_2")
    @Expose
    private List<display_value_item> categories = null;

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public List<display_value_item> getCategories() {
        return categories;
    }

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }

    public void setCategories(List<display_value_item> categories) {
        this.categories = categories;
    }

}
