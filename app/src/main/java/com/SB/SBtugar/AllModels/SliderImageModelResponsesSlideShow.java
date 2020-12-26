package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Project SuperBekala
 * Created by asamy on 3/11/2018.
 */

public class SliderImageModelResponsesSlideShow {

    @SerializedName("slideShow")
    @Expose
    private ArrayList<SliderImageModel> Data;




    public ArrayList<SliderImageModel> getData() {
        return Data;
    }


    public void setData(ArrayList<SliderImageModel> data) {
        Data = data;
    }
}
