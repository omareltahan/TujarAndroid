package com.SB.SBtugar.AllModels.Orders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Project SuperBekala
 * Created by asamy on 3/11/2018.
 */

public class StoreModel {
    @SerializedName("shop_name")
    @Expose
    private String shop_name;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("id")
    @Expose
    private String id;

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
