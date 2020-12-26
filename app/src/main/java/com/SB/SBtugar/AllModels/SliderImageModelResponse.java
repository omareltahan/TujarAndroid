package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Project SuperBekala
 * Created by asamy on 3/11/2018.
 */

public class SliderImageModelResponse {
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("Data")
    @Expose
    private SliderImageModelResponsesSlideShow Data;




    public SliderImageModelResponsesSlideShow getData() {
        return Data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(SliderImageModelResponsesSlideShow data) {
        Data = data;
    }
}
