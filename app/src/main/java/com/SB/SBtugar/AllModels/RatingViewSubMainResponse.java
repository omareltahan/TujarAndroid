package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Project ${PROJECT}
 * Created by asamy on 4/28/2018.
 */

public class RatingViewSubMainResponse {
    @SerializedName("reviews")
    List<Rating_view> reviews;

    public List<Rating_view> getReviews() {
        return reviews;
    }

    public void setReviews(List<Rating_view> reviews) {
        this.reviews = reviews;
    }
}
