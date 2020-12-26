package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class display_value_item implements Serializable {
    @SerializedName("color")
    @Expose
    private String color = "#014669";
    @SerializedName("number_of_vendors")
    @Expose
    private int number_of_vendors = 0;
    @SerializedName("number_of_recent_orders")
    @Expose
    private int number_of_recent_orders = 0;

    public int getNumber_of_recent_orders() {
        return number_of_recent_orders;
    }

    public int getNumber_of_vendors() {
        return number_of_vendors;
    }

    public void setNumber_of_recent_orders(int number_of_recent_orders) {
        this.number_of_recent_orders = number_of_recent_orders;
    }

    public void setNumber_of_vendors(int number_of_vendors) {
        this.number_of_vendors = number_of_vendors;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("description")
    @Expose
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @SerializedName("value")
    @Expose
    private String value ;

    @SerializedName("icon")
    @Expose
    private String icon ;

    public String getValue() {
        return value;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDisplay() {
        return display;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
