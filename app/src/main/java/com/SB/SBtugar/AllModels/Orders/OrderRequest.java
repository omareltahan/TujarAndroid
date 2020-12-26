package com.SB.SBtugar.AllModels.Orders;

import com.SB.SBtugar.AllModels.Billing;
import com.SB.SBtugar.AllModels.LineItem;
import com.SB.SBtugar.AllModels.Shipping;
import com.SB.SBtugar.AllModels.Shipping_lines;
import com.SB.SBtugar.AllModels.coupon_lines;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderRequest implements Serializable
{
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;

    @SerializedName("meta_data")
    @Expose
    private List<SmallMetaDatum> meta_data = new ArrayList<>();

    public List<SmallMetaDatum> getMeta_data() {
        return meta_data;
    }

    public void setMeta_data(List<SmallMetaDatum> meta_data) {
        this.meta_data = meta_data;
    }

    @SerializedName("promocode")
    @Expose
    private String promocode;

    @SerializedName("status")
    @Expose
    private String status = "processing";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("customer_note")
    @Expose
    private String customer_note;

    @SerializedName("payment_method_title")
    @Expose
    private String paymentMethodTitle;
    @SerializedName("set_paid")
    @Expose
    private Boolean setPaid;
    @SerializedName("billing")
    @Expose
    private Billing billing;
    @SerializedName("shipping")
    @Expose
    private Shipping shipping;
    @SerializedName("line_items")
    @Expose
    private List<LineItem> lineItems = null;

    @SerializedName("shipping_lines")
    @Expose
    private List<Shipping_lines> shippingLines = null;

    @SerializedName("coupon_lines")
    @Expose
    private List<coupon_lines> coupon_lines = null;

    private final static long serialVersionUID = -2175010161953248925L;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<coupon_lines> getCoupon_lines() {
        return coupon_lines;
    }

    public void setCoupon_lines(List<coupon_lines> coupon_lines) {
        this.coupon_lines = coupon_lines;
    }

    public String getPaymentMethodTitle() {
        return paymentMethodTitle;
    }

    public void setPaymentMethodTitle(String paymentMethodTitle) {
        this.paymentMethodTitle = paymentMethodTitle;
    }

    public Boolean getSetPaid() {
        return setPaid;
    }

    public void setSetPaid(Boolean setPaid) {
        this.setPaid = setPaid;
    }
 public String getCustomer_note() {
        return customer_note;
    }

    public void setCustomer_note(String customer_note) {
        this.customer_note = customer_note;
    }

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public List<Shipping_lines> getShippingLines() {
        return shippingLines;
    }

    public void setShippingLines(List<Shipping_lines> shippingLines) {
        this.shippingLines = shippingLines;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }
}

