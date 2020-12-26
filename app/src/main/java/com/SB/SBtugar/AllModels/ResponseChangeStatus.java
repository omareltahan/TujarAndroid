package com.SB.SBtugar.AllModels;

/**
 * Project ${PROJECT}
 * Created by asamy on 12/21/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseChangeStatus implements Serializable
{

    @SerializedName("pending")
    @Expose
    private String pending;

    @SerializedName("rejected")
    @Expose
    private String rejected;

    @SerializedName("completed")
    @Expose
    private String completed;

    @SerializedName("out-for-delivery")
    @Expose
    private String out_for_delivery;

    @SerializedName("cancelled")
    @Expose
    private String cancelled;

    @SerializedName("processing")
    @Expose
    private String processing;


    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getRejected() {
        return rejected;
    }

    public void setRejected(String rejected) {
        this.rejected = rejected;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getOut_for_delivery() {
        return out_for_delivery;
    }

    public void setOut_for_delivery(String out_for_delivery) {
        this.out_for_delivery = out_for_delivery;
    }

    public String getCancelled() {
        return cancelled;
    }

    public void setCancelled(String cancelled) {
        this.cancelled = cancelled;
    }

    public String getProcessing() {
        return processing;
    }

    public void setProcessing(String processing) {
        this.processing = processing;
    }
}