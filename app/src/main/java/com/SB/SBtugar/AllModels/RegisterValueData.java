package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Project ${PROJECT}
 * Created by asamy on 5/5/2018.
 */

public class RegisterValueData implements Serializable {
    @SerializedName("user_id")
    @Expose
    private int user_id;
    @SerializedName("token")
    @Expose
    private String token;


    public int getUser_id() {
        return user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
