package com.SB.SBtugar.AllModels.SuperMarket;

import com.SB.SBtugar.AllModels.Address;
import com.SB.SBtugar.AllModels.Category;
import com.SB.SBtugar.AllModels.Product;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Project ${PROJECT}
 * Created by asamy on 12/21/2018.
 */
public class SuperMarket implements Serializable {
private boolean isFavourite;

    @SerializedName("locale")
    private String locale;

    @SerializedName("desc")
    private String desc = "";
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @SerializedName("provide_online_payment")
    private boolean provide_online_payment = true;

    public boolean isProvide_online_payment() {
        return provide_online_payment;
    }

    public void setProvide_online_payment(boolean provide_online_payment) {
        this.provide_online_payment = provide_online_payment;
    }

    @SerializedName("onHold")
    private boolean onHold;

    @SerializedName("HasBranches")
    private boolean isHasBranches;

    public boolean isHasBranches() {
        return isHasBranches;
    }

    public void setHasBranches(boolean hasBranches) {
        isHasBranches = hasBranches;
    }

    public boolean isOnHold() {
        return onHold;
    }

    public void setOnHold(boolean onHold) {
        this.onHold = onHold;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    @SerializedName("marketCat")
    private String marketCat;

    @SerializedName("provide_atm")
    private boolean provide_atm;

    public boolean isProvide_atm() {
        return provide_atm;
    }

    public void setProvide_atm(boolean provide_atm) {
        this.provide_atm = provide_atm;
    }

    @SerializedName("accept_coupons")
    private boolean accept_coupons;

    @SerializedName("accept_chat")
    private boolean accept_chat;

    public boolean isAccept_chat() {
        return accept_chat;
    }

    public void setAccept_chat(boolean accept_chat) {
        this.accept_chat = accept_chat;
    }

    public boolean isAccept_coupons() {
        return accept_coupons;
    }

    public void setAccept_coupons(boolean accept_coupons) {
        this.accept_coupons = accept_coupons;
    }

    public String getMarketCat() {
        return marketCat;
    }

    public void setMarketCat(String marketCat) {
        this.marketCat = marketCat;
    }

    @SerializedName("isFeatured")
    private boolean isFeatured;

    @SerializedName("quick_order_feature")
    private boolean CanQuickOrder;

    @SerializedName("quick_voice_feature")
    private boolean CanQuickOrderVoice = true;


    public boolean isCanQuickOrderVoice() {
        return CanQuickOrderVoice;
    }

    public void setCanQuickOrderVoice(boolean canQuickOrderVoice) {
        CanQuickOrderVoice = canQuickOrderVoice;
    }

    @SerializedName("marketID")
    @Expose
    private Integer id = -1;
    @Expose
    private String marketName;
    @SerializedName("marketLogo")
    @Expose
    private String marketLogo;
    @SerializedName("marketAddress")
    @Expose
    private Address marketAddress = new Address();
    @SerializedName("numberOfProducts")
    @Expose
    private Integer numberOfProducts;
    @SerializedName("marketAuthor")
    @Expose
    private String marketAuthor;
    @SerializedName("marketPhone")
    @Expose
    private String marketPhone;
    @SerializedName("marketRating")
    @Expose
    private String marketRating;
    @SerializedName("reviewCount")
    @Expose
    private int reviewCount;
    @SerializedName("marketMinOrder")
    @Expose
    private String marketMinOrder;
    @SerializedName("marketDeliveryDuration")
    @Expose
    private String marketDeliveryDuration;
    @SerializedName("marketDeliveryFees")
    @Expose
    private String marketDeliveryFees;

    @SerializedName("isOpen")
    @Expose
    private boolean isOpen;

    @SerializedName("can_receive_calls")
    @Expose
    private boolean can_receive_calls;

    public boolean isCan_receive_calls() {
        return can_receive_calls;
    }

    public void setCan_receive_calls(boolean can_receive_calls) {
        this.can_receive_calls = can_receive_calls;
    }

    @SerializedName("isBusy")
    @Expose
    private boolean isBusy = false;

    @SerializedName("closedMessage")
    @Expose
    private String closedMessage;

    @SerializedName("marketNote")
    @Expose
    private String marketNote;


    @SerializedName("openingTime")
    @Expose
    private String openingTime;

    @SerializedName("deliveryStartTime")
    @Expose
    private String deliveryStartTime = "";

    @SerializedName("deliveryEndTime")
    @Expose
    private String deliveryEndTime;

    @SerializedName("deliveryAreas")
    @Expose
    private List<String> deliveryAreas;

    public List<String> getDeliveryAreas() {
        return deliveryAreas;
    }

    public String getDeliveryEndTime() {
        return deliveryEndTime;
    }

    public String getDeliveryStartTime() {
        return deliveryStartTime;
    }

    public void setDeliveryAreas(List<String> deliveryAreas) {
        this.deliveryAreas = deliveryAreas;
    }

    public void setDeliveryEndTime(String deliveryEndTime) {
        this.deliveryEndTime = deliveryEndTime;
    }

    public void setDeliveryStartTime(String deliveryStartTime) {
        this.deliveryStartTime = deliveryStartTime;
    }

    @SerializedName("closingTime")
    @Expose
    private String closingTime;

    @SerializedName("hasOnsaleProducts")
    @Expose
    private boolean hasOnsaleProducts;

    @SerializedName("hasFeaturedProducts")
    @Expose
    private boolean hasFeaturedProducts;

    public boolean isHasFeaturedProducts() {
        return hasFeaturedProducts;
    }

    public boolean isHasOnsaleProducts() {
        return hasOnsaleProducts;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setHasFeaturedProducts(boolean hasFeaturedProducts) {
        this.hasFeaturedProducts = hasFeaturedProducts;
    }

    public void setHasOnsaleProducts(boolean hasOnsaleProducts) {
        this.hasOnsaleProducts = hasOnsaleProducts;
    }

    private List<Product> offers;
    private List<Product> featuredProducts;
    private List<Category> categories;

    public Integer getId() {
        return id;
    }

    public void setID(Integer marketID) {
        this.id = marketID;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getMarketLogo() {
        return marketLogo;
    }

    public void setMarketLogo(String marketLogo) {
        this.marketLogo = marketLogo;
    }

    public Address getMarketAddress() {
        return marketAddress;
    }

    public void setMarketAddress(Address marketAddress) {
        this.marketAddress = marketAddress;
    }

    public Integer getNumberOfProducts() {
        return numberOfProducts;
    }

    public void setNumberOfProducts(Integer numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }

    public String getMarketAuthor() {
        return marketAuthor;
    }

    public void setMarketAuthor(String marketAuthor) {
        this.marketAuthor = marketAuthor;
    }

    public String getMarketPhone() {
        return marketPhone;
    }

    public void setMarketPhone(String marketPhone) {
        this.marketPhone = marketPhone;
    }

    public String getMarketRating() {
        return marketRating;
    }

    public void setMarketRating(String marketRating) {
        this.marketRating = marketRating;
    }

    public String getMarketMinOrder() {
        return marketMinOrder;
    }

    public void setMarketMinOrder(String marketMinOrder) {
        this.marketMinOrder = marketMinOrder;
    }

    public String getMarketDeliveryDuration() {
        return marketDeliveryDuration;
    }

    public void setMarketDeliveryDuration(String marketDeliveryDuration) {
        this.marketDeliveryDuration = marketDeliveryDuration;
    }

    public String getMarketDeliveryFees() {
        return marketDeliveryFees;
    }

    public void setMarketDeliveryFees(String marketDeliveryFees) {
        this.marketDeliveryFees = marketDeliveryFees;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getClosedMessage() {
        return closedMessage;
    }

    public void setClosedMessage(String closedMessage) {
        this.closedMessage = closedMessage;
    }


    public String getMarketNote() {
        return marketNote;
    }

    public void setMarketNote(String marketNote) {
        this.marketNote = marketNote;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public List<Product> getOffers() {
        return offers;
    }

    public void setOffers(List<Product> offers) {
        this.offers = offers;
    }

    public List<Product> getFeaturedProducts() {
        return featuredProducts;
    }

    public void setFeaturedProducts(List<Product> featuredProducts) {
        this.featuredProducts = featuredProducts;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void setCanQuickOrder(boolean canQuickOrder) {
        CanQuickOrder = canQuickOrder;
    }

    public boolean isCanQuickOrder() {
        return CanQuickOrder;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }
}
