package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Project ${PROJECT}
 * Created by asamy on 5/5/2018.
 */

public class AttributeModel implements Serializable {
    @SerializedName("options")
    @Expose
    private ArrayList<String> options;
    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SerializedName("variations_images")
    @Expose
    private ArrayList<String> variations_images;

    public ArrayList<String> getVariations_images() {
        return variations_images;
    }

    public void setVariations_images(ArrayList<String> variations_images) {
        this.variations_images = variations_images;
    }

    @SerializedName("position")
    @Expose
    private Integer position;

    public ArrayList<String> getOptions() {
        return options;
    }

    public Integer getPosition() {
        return position;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
