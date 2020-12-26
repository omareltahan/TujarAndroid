package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Project SuperBekala
 * Created by asamy on 3/11/2018.
 */

public class UserDataRequestModel {
    @SerializedName("cityCode")
    @Expose
    private String cityCode;
    @SerializedName("cityName")
    @Expose
    private String cityName;
    private final static long serialVersionUID = -5496262752514945657L;

}
