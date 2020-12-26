package com.SB.SBtugar.AllModels.SuperMarket;

import com.SB.SBtugar.AllModels.Address;
import com.SB.SBtugar.AllModels.Rating;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Project ${PROJECT}
 * Created by asamy on 12/21/2018.
 */
public class Vendor implements Serializable {
    @SerializedName("street_address")
    @Expose
    private String street_address;
    @SerializedName("deliveryFee")
    @Expose
    private String deliveryFee;
    @SerializedName("accept_coupons")
    @Expose
    private int accept_coupons;
    @SerializedName("provide_atm")
    @Expose
    private int provide_atm;
    @SerializedName("provide_online_payment")
    @Expose
    private int provide_online_payment;

    public int getProvide_atm() {
        return provide_atm;
    }

    public int getProvide_online_payment() {
        return provide_online_payment;
    }

    @SerializedName("deliveryDuration")
    @Expose
    private String deliveryDuration;
    @SerializedName("marketMinOrder")
    @Expose
    private int marketMinOrder;
    @SerializedName("marketLogo")
    @Expose
    private String marketLogo;

    public int getAccept_coupons() {
        return accept_coupons;
    }

    public int getMarketMinOrder() {
        return marketMinOrder;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }


    public String getMarketLogo() {
        return marketLogo;
    }

    public String getStreet_address() {
        return street_address;
    }

    public void setAccept_coupons(int accept_coupons) {
        this.accept_coupons = accept_coupons;
    }

    public void setDeliveryFee(String deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getDeliveryDuration() {
        return deliveryDuration;
    }

    public void setDeliveryDuration(String deliveryDuration) {
        this.deliveryDuration = deliveryDuration;
    }

    public void setMarketLogo(String marketLogo) {
        this.marketLogo = marketLogo;
    }

    public void setMarketMinOrder(int marketMinOrder) {
        this.marketMinOrder = marketMinOrder;
    }

    public void setStreet_address(String street_address) {
        this.street_address = street_address;
    }

    public void setProvide_atm(int provide_atm) {
        this.provide_atm = provide_atm;
    }

    public void setProvide_online_payment(int provide_online_payment) {
        this.provide_online_payment = provide_online_payment;
    }

    @SerializedName("store_id")
    @Expose
    private String id = "-1";
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone = "";
    @SerializedName("show_email")
    @Expose
    private Boolean showEmail;
//    @SerializedName("address")
//    @Expose
//    private Address address;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("banner")
    @Expose
    private String banner;
    @SerializedName("gravatar")
    @Expose
    private String gravatar;
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;
    @SerializedName("products_per_page")
    @Expose
    private Integer productsPerPage;
    @SerializedName("show_more_product_tab")
    @Expose
    private Boolean showMoreProductTab;
    @SerializedName("toc_enabled")
    @Expose
    private Boolean tocEnabled;
    @SerializedName("store_toc")
    @Expose
    private String storeToc;
    @SerializedName("featured")
    @Expose
    private Boolean featured;
    @SerializedName("rating")
    @Expose
    private Rating rating;
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;
    @SerializedName("registered")
    @Expose
    private String registered;
    @SerializedName("trusted")
    @Expose
    private Boolean trusted;

    private final static long serialVersionUID = 8058201630215477664L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getShowEmail() {
        return showEmail;
    }

    public void setShowEmail(Boolean showEmail) {
        this.showEmail = showEmail;
    }

//    public Address getAddress() {
//        return address;
//    }
//
//    public void setAddress(Address address) {
//        this.address = address;
//    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getGravatar() {
        return gravatar;
    }

    public void setGravatar(String gravatar) {
        this.gravatar = gravatar;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public Integer getProductsPerPage() {
        return productsPerPage;
    }

    public void setProductsPerPage(Integer productsPerPage) {
        this.productsPerPage = productsPerPage;
    }

    public Boolean getShowMoreProductTab() {
        return showMoreProductTab;
    }

    public void setShowMoreProductTab(Boolean showMoreProductTab) {
        this.showMoreProductTab = showMoreProductTab;
    }

    public Boolean getTocEnabled() {
        return tocEnabled;
    }

    public void setTocEnabled(Boolean tocEnabled) {
        this.tocEnabled = tocEnabled;
    }

    public String getStoreToc() {
        return storeToc;
    }

    public void setStoreToc(String storeToc) {
        this.storeToc = storeToc;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public Boolean getTrusted() {
        return trusted;
    }

    public void setTrusted(Boolean trusted) {
        this.trusted = trusted;
    }

}
