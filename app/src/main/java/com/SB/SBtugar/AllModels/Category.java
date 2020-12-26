package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Project ${PROJECT}
 * Created by asamy on 3/11/2018.
 */

public class Category implements Serializable {

    @SerializedName("catID")
    @Expose
    private Integer catID;
    @SerializedName("catName")
    @Expose
    private String catName;
    @SerializedName("catImage")
    @Expose
    private String catImage;
    @SerializedName("subCategories")
    @Expose
    private List<Category> subCategories = null;
    @SerializedName("catCount")
    @Expose
    private Integer noOfProducts;

    public Integer getCatID() {
        return catID;
    }

    public void setCatID(Integer catID) {
        this.catID = catID;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatImage() {
        return catImage;
    }

    public void setCatImage(String catImage) {
        this.catImage = catImage;
    }


    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }

    public Integer getNoOfProducts() {
        return noOfProducts;
    }

    public void setNoOfProducts(Integer noOfProducts) {
        this.noOfProducts = noOfProducts;
    }
}
