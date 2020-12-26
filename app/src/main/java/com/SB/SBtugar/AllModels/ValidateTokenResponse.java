package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Project ${PROJECT}
 * Created by asamy on 5/4/2018.
 */

public class ValidateTokenResponse implements Serializable
{

    @SerializedName("Status")
    @Expose
    private Integer status;
    @SerializedName("Data")
    @Expose
    private TokenData data;
    private final static long serialVersionUID = -1726994114014283447L;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public TokenData getData() {
        return data;
    }

    public void setData(TokenData data) {
        this.data = data;
    }

}
