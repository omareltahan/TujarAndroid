package com.SB.SBtugar;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.SB.SBtugar.AllModels.Orders.OrderResponse;
import com.SB.SBtugar.AllModels.SuperMarket.SuperMarket;
import com.SB.SBtugar.AllModels.display_value_item;

import androidx.multidex.MultiDexApplication;

public class AppController extends MultiDexApplication {
    public static SuperMarket SelectedSuperMarket = new SuperMarket();
    public static display_value_item selectedCategoyMain = new display_value_item();
    public static OrderResponse order_global ;
    public static int isAtm=0;
    public static Context contextValue;
    public static String CartOrderShipping = "0";
    public static String promocode_order = "";
    public static float product_price_variation = 0;
    public static String string_of_category="";
    public static String variation_txt = "";
    public static Uri uri;
    public static Bitmap image;

}
