package com.SB.SBtugar.CartData;

import android.content.Context;

import java.io.Serializable;

import androidx.room.Entity;

@Entity
public class UserCart implements Serializable {
    private Context context;
    private String SharedDBName = "SharedDB";
    private String SharedMarketName = "SharedMarketName";
    private String SharedTotalPrice = "SharedTotalPrice";
    private String SharedMarketId = "SharedMarketId";

    private String isProvide_online_payment = "isProvide_online_payment";
    private String getMarketLogo = "getMarketLogo";
    private String getMarketAddress = "getMarketAddress";
    private String getMarketDeliveryDuration = "getMarketDeliveryDuration";
    private String isProvide_atm = "isProvide_atm";
    private String isAccept_coupons = "isAccept_coupons";

    private String getMarketMinOrder = "getMarketMinOrder";
    private String getMarketDeliveryFees = "getMarketDeliveryFees";
    private String isHasFeaturedProducts = "isHasFeaturedProducts";

    public String getMarketDeliveryFeesValue() {
        return context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).getString(getMarketDeliveryFees,"");
    }

    public void setMarketDeliveryFeesValue(String value) {
        context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).edit().putString(getMarketDeliveryFees,value).apply();
    }

      public String getMarketMinOrderValue() {
        return context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).getString(getMarketMinOrder,"");
    }

    public void SetMarketMinOrderValue(String value) {
        context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).edit().putString(getMarketMinOrder,value).apply();
    }



    public String getMarketDeliveryDurationValue() {
        return context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).getString(getMarketDeliveryDuration,"");
    }

    public void SetMarketDeliveryDuration(String value) {
        context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).edit().putString(getMarketDeliveryDuration,value).apply();
    }


    public String getMarketAddressValue() {
        return context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).getString(getMarketAddress,"");
    }

    public void SetMarketAddressValue(String value) {
        context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).edit().putString(getMarketAddress,value).apply();
    }


    public String getMarketLogoValue() {
        return context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).getString(getMarketLogo,"");
    }

    public void SetMarketLogoValue(String value) {
        context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).edit().putString(getMarketLogo,value).apply();
    }



    public boolean isAccept_couponsBool() {
        return context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).getBoolean(isAccept_coupons,false);
    }

    public void SetAccept_couponsBool(boolean value) {
        context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).edit().putBoolean(isAccept_coupons,value).apply();
    }

    public boolean isHasFeaturedProductsBool() {
        return context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).getBoolean(isHasFeaturedProducts,false);
    }

    public void SetHasFeaturedProductsBool(boolean value) {
        context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).edit().putBoolean(isHasFeaturedProducts,value).apply();
    }



    public boolean isProvide_online_paymentBool() {
        return context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).getBoolean(isProvide_online_payment,false);
    }

    public void SetProvide_online_paymentBool(boolean value) {
        context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).edit().putBoolean(isProvide_online_payment,value).apply();
    }


    public boolean isProvide_atmBool() {
        return context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).getBoolean(isProvide_atm,false);
    }

    public void SetProvide_atmBool(boolean value) {
        context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).edit().putBoolean(isProvide_atm,value).apply();
    }



    public UserCart(Context context){
        this.context = context;
    }

    public double getTotalPrice() {
        return context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).getFloat(SharedTotalPrice,0);
    }

    public void setMarketId(int marketId) {
        context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).edit().putInt(SharedMarketId,marketId).apply();
    }

    public int getMarketId() {
        return context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).getInt(SharedMarketId,0);
    }

    public String getMarketName() {
        return context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).getString(SharedMarketName,"");
    }

    public void setMarketName(String marketName) {
        context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).edit().putString(SharedMarketName,marketName).apply();
    }

    public void setTotalPrice(double totalPrice) {
        context.getSharedPreferences(SharedDBName, Context.MODE_PRIVATE).edit().putFloat(SharedTotalPrice, (float) totalPrice).apply();
    }
}
