package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Project SuperBekala
 * Created by asamy on 3/11/2018.
 */

public class VariationModelsecond {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("option")
    @Expose
    private String option;

    public String getName() {
        return name;
    }

    public String getOption() {
        return option;
    }






    public void setName(String name) {
        this.name = name;
    }

    public void setOption(String option) {
        this.option = option;
    }

}
