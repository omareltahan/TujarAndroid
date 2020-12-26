package com.SB.SBtugar.AllModels.Orders;

/**
 * Project ${PROJECT}
 * Created by asamy on 12/21/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderChangeStatus implements Serializable
{

    @SerializedName("order_id")
    @Expose
    private int order_id;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("time")
    @Expose
    private String time;

    public String getStatus() {
        return status;
    }

    public int getOrder_id() {
        return order_id;
    }

    public String getTime() {
        return time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setTime(String time) {
        this.time = time;
    }
}