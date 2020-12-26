package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Project SuperBekala
 * Created by asamy on 3/11/2018.
 */

public class SliderImageModel {
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("store_cat")
    @Expose
    private String category_id;
    @SerializedName("action_type")
    @Expose
    private String type;
    @SerializedName("action_link")
    @Expose
    private String action;

    public String getAction() {
        return action;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getType() {
        return type;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setType(String type) {
        this.type = type;
    }
}
