package com.SB.SBtugar.AllModels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserUpdateRequest implements Serializable
{
    @SerializedName("meta_data")
    @Expose
    private List<MetaDatum> metaData = new ArrayList<>();

    public List<MetaDatum> getMetaData() {
        return metaData;
    }

    public void setMetaData(List<MetaDatum> metaData) {
        this.metaData = metaData;
    }

    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("billing")
    @Expose
    private Billing billing;
    private final static long serialVersionUID = 5699443490763315170L;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

}
