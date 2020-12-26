package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Project ${PROJECT}
 * Created by asamy on 5/6/2018.
 */

public class LineItem implements Serializable
{

    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("variation_id")
    @Expose
    private Integer variation;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    private final static long serialVersionUID = -5751729942656761854L;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getVariation() {
        return variation;
    }

    public void setVariation(Integer variation) {
        this.variation = variation;
    }
}
