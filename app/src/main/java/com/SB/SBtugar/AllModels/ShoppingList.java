package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Project SuperBekala
 * Created by asamy on 3/11/2018.
 */

public class ShoppingList {
    @SerializedName("vendor_name")
    @Expose
    private String vendor_name;
    @SerializedName("is_hold")
    @Expose
    private boolean is_hold = false;

    public boolean isIs_hold() {
        return is_hold;
    }

    public void setIs_hold(boolean is_hold) {
        this.is_hold = is_hold;
    }

    @SerializedName("vendor_id")
    @Expose
    private int vendor_id;
    @SerializedName("items")
    @Expose
    private List<Product> items;

    public List<Product> getItems() {
        return items;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setItems(List<Product> items) {
        this.items = items;
    }

    public void setVendor_id(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }
}
