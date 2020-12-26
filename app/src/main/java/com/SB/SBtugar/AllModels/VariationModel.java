package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Project SuperBekala
 * Created by asamy on 3/11/2018.
 */

public class VariationModel {
    @SerializedName("attributes")
    @Expose
    private List<VariationModelsecond> attributes;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("color")
    @Expose
    private String color;

    public String getColor() {
        if (color.contains("#"))
            return color;
        else
            return "#" + color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @SerializedName("stock_status")
    @Expose
    private String stock_status;
    @SerializedName("id")
    @Expose
    private int id;

    public String getStock_status() {
        return stock_status;
    }

    public void setStock_status(String stock_status) {
        this.stock_status = stock_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<VariationModelsecond> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<VariationModelsecond> attributes) {
        this.attributes = attributes;
    }

    private boolean isChecked = false;

    public String getPrice() {
        return price;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
