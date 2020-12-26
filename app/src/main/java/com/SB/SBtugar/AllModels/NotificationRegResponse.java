package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Project ${PROJECT}
 * Created by asamy on 6/2/2018.
 */

public class NotificationRegResponse {
    @SerializedName("result")
    @Expose
    private Boolean result;

    @SerializedName("token")
    @Expose
    private String token;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
