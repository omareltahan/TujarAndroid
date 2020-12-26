package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Project ${PROJECT}
 * Created by asamy on 5/12/2018.
 */

public class ShippingLine implements Serializable {

    @SerializedName("method_id")
    @Expose
    private String methodId;
    @SerializedName("method_title")
    @Expose
    private String methodTitle;
    @SerializedName("total")
    @Expose
    private String total;
    private final static long serialVersionUID = -6119065727569702853L;

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public String getMethodTitle() {
        return methodTitle;
    }

    public void setMethodTitle(String methodTitle) {
        this.methodTitle = methodTitle;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
