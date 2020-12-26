package com.SB.SBtugar.AllModels;

/**
 * Project ${PROJECT}
 * Created by asamy on 5/6/2018.
 */

public class Shipping_lines
{
    private String method_id = "flat_rate";
    private String method_title = "Flat Rate";
    private String total = "0";

    public void setMethod_id(String method_id) {
        this.method_id = method_id;
    }

    public void setMethod_title(String method_title) {
        this.method_title = method_title;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMethod_id() {
        return method_id;
    }

    public String getMethod_title() {
        return method_title;
    }

    public String getTotal() {
        return total;
    }
}
