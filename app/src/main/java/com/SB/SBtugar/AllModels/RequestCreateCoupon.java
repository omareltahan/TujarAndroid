package com.SB.SBtugar.AllModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Project SuperBekala
 * Created by asamy on 3/11/2018.
 */

public class RequestCreateCoupon {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("date_expires")
    @Expose
    private String date_expires = "";

    @SerializedName("free_shipping")
    @Expose
    private boolean free_shipping;

    public boolean isFree_shipping() {
        return free_shipping;
    }

    public void setFree_shipping(boolean free_shipping) {
        this.free_shipping = free_shipping;
    }

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("discount_type")
    @Expose
    private String discount_type;
    @SerializedName("minimum_amount")
    @Expose
    private String minimum_amount;
    @SerializedName("individual_use")
    @Expose
    private boolean individual_use;
    @SerializedName("exclude_sale_items")
    @Expose
    private boolean exclude_sale_items;
    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("usage_limit")
    @Expose
    private int usage_limit = -1;
    @SerializedName("usage_limit_per_user")
    @Expose
    private int usage_limit_per_user=-1;

    @SerializedName("usage_count")
    @Expose
    private int usage_count;
    @SerializedName("used_by")
    @Expose
    private List<String> used_by;

    public int getUsage_limit_per_user() {
        return usage_limit_per_user;
    }

    public List<String> getUsed_by() {
        return used_by;
    }

    public void setUsage_limit_per_user(int usage_limit_per_user) {
        this.usage_limit_per_user = usage_limit_per_user;
    }

    public void setUsed_by(List<String> used_by) {
        this.used_by = used_by;
    }

    public int getUsage_count() {
        return usage_count;
    }

    public int getUsage_limit() {
        return usage_limit;
    }

    public void setUsage_limit(int usage_limit) {
        this.usage_limit = usage_limit;
    }

    public void setUsage_count(int usage_count) {
        this.usage_count = usage_count;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getMinimum_amount() {
        return minimum_amount;
    }

    public void setMinimum_amount(String minimum_amount) {
        this.minimum_amount = minimum_amount;
    }

    public boolean isIndividual_use() {
        return individual_use;
    }

    public void setIndividual_use(boolean individual_use) {
        this.individual_use = individual_use;
    }

    public boolean isExclude_sale_items() {
        return exclude_sale_items;
    }

    public void setExclude_sale_items(boolean exclude_sale_items) {
        this.exclude_sale_items = exclude_sale_items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate_expires() {
        return date_expires;
    }

    public void setDate_expires(String date_expires) {
        this.date_expires = date_expires;
    }
}
