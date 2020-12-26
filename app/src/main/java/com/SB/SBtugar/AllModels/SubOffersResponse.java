package com.SB.SBtugar.AllModels;

import com.SB.SBtugar.AllModels.SuperMarket.Vendor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Project ${PROJECT}
 * Created by asamy on 6/18/2018.
 */
public class SubOffersResponse {

    @SerializedName("store")
    @Expose
    private Vendor store;
    @SerializedName("product")
    @Expose
    private Product product = new Product();
    @SerializedName("images")
    @Expose
    private List<Image> images = new ArrayList<>();

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setStore(Vendor store) {
        this.store = store;
    }

    public Product getProduct() {
        return product;
    }

    public Vendor getStore() {
        return store;
    }
}
