package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Project ${PROJECT}
 * Created by asamy on 5/6/2018.
 */

public class coupon_lines
{
    @SerializedName("code")
    @Expose
    private String code="";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
