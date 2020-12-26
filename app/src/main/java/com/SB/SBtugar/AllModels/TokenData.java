package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Project ${PROJECT}
 * Created by asamy on 5/4/2018.
 */

public class TokenData implements Serializable {
    @SerializedName("Token")
    @Expose
    private String token;
    @SerializedName("Details")
    @Expose
    private UserData details;

    private final static long serialVersionUID = 5847891070002141029L;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserData getDetails() {
        return details;
    }

    public void setDetails(UserData details) {
        this.details = details;
    }
}
