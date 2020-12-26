package com.SB.SBtugar.AllModels.Orders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailsModel {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("product_id")
    @Expose
    private int product_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
}
