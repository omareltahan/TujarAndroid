package com.SB.SBtugar.AllModels;

import java.util.List;

/**
 * Project SuperBekala
 * Created by asamy on 3/11/2018.
 */

public class SubCategory {

    private int subCatID;
    private String subCatName;
    private String subCatImage;
    private List<Product> products;

    public int getSubCatID() {
        return subCatID;
    }

    public void setSubCatID(int subCatID) {
        this.subCatID = subCatID;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public String getSubCatImage() {
        return subCatImage;
    }

    public void setSubCatImage(String subCatImage) {
        this.subCatImage = subCatImage;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
