package com.SB.SBtugar.AllModels;

/**
 * Project ${PROJECT}
 * Created by asamy on 12/21/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rating implements Serializable
{

    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("count")
    @Expose
    private Integer count;
    private final static long serialVersionUID = -8550698084729663046L;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}