package com.SB.SBtugar.AllModels;

import android.content.Context;
import android.util.Log;


import com.SB.SBtugar.AllModels.SuperMarket.SuperMarket;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.CartData.AppDatabase;
import com.SB.SBtugar.CartData.CartItem;
import com.SB.SBtugar.CartData.UserCart;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Room;

public class Model {
    private List<SuperMarket> marketList=new ArrayList<>();
    private List<Product> productList;
    private AreaResponse areas;
    private String wooCommerceUserToken;
    private UserData user;

    public void setUser(UserData user) {
        this.user = user;
    }

    public UserData getUser() {
        return user;
    }

    private static Model model = new Model();

    public static Model getInstance(){
        if(model == null){
            model = new Model();
        }
        return model;
    }

    public void setMarketList(List<SuperMarket> markets){
        this.marketList = markets;
    }
    public void setMarketItem(SuperMarket market){
        this.marketList.add(market);
    }

    public List<SuperMarket> getMarketList(){
        return this.marketList;
    }


    public SuperMarket getMarket(int id){
        if (marketList != null) {
            for (SuperMarket market : marketList) {
                if (market.getId() == id) {
                    return market;
                }
            }
        }
        return new SuperMarket();
    }

    public void updateMarketData(SuperMarket marketData){
        SuperMarket market = getMarket(marketData.getId());
        if(market == null){
            //Add New Market
            marketList.add(marketData);
            return;
        } else {
            //Copy Data to update Market Information
            // TODO Check What to do
            //market.setCategories(marketData.getCategories());
            //market.setMarketPopularProducts(marketData.getMarketPopularProducts());
            //market.setMarketOffers(marketData.getMarketOffers());
            //market.setMarketSlides(marketData.getMarketSlides());
        }


    }

    // TODO Check what to do
    /*public void setMarketCategories(String marketId, List<Category> categories){
        Market market = getMarket(marketId);
        if(market != null){
            market.setCategories(categories);
        }
    }*/

    public void setProductList(List<Product> productList){
        this.productList = productList;
    }

    public List<Product> getProductList(){
        return this.productList;
    }

    /*
    * User Cart Operations
    */
    CartItem ConvertProductToCart(Product product, int count, int variationId,String variationImg){
        CartItem item = new CartItem();
        item.quantity = count;

        item.description = product.getDescription();
        item.id = product.getId();
        if (!variationImg.equals("")){
            item.Image = variationImg;
        }
        else {
            String imageUrl = "";
            if (product.getMainImage() != null && !product.getMainImage().isEmpty()) {
                imageUrl = product.getMainImage();
            } else if (product.getImages() != null) {
                if (product.getImages().size() == 0) {
                    imageUrl = "";
                } else {
                    imageUrl = product.getImages().get(0).getSrc();
                }
            }
            item.Image = imageUrl;
        }
        item.name = product.getName();
        if (product.getPrice().equals("")) {
            item.Price_Is = 0f;
            item.price = "0.0";
            item.regularPrice = "0.0";
            item.salePrice = "0.0";
        }
        else {
            item.Price_Is = Float.valueOf(product.getPrice()) * count;
            item.price = product.getPrice();
            item.regularPrice = product.getRegularPrice();
            item.salePrice = product.getSalePrice();
        }

        item.shortDescription = product.getShortDescription();
        item.type = product.getType();
        item.variation = variationId;
        if (variationId!= 0 && variationId != -1)
            item.Variation_txt = AppController.variation_txt;
        else
            item.Variation_txt = "";
        return item;
    }

    public boolean addToCart(Context context, Product product, int quantity, int marketId , int variation, String VariationImageUrl){
        Log.e("VariationImageUrl","Value:" + VariationImageUrl);
        UserCart cart = new UserCart(context);
        AppDatabase db = Room.databaseBuilder(context,
                AppDatabase.class, "CartDB").allowMainThreadQueries().build();
        if (marketId != cart.getMarketId() && db.TransactionsDao().getAll().size()>0){
            return false;
        }
        else if (marketId != cart.getMarketId()){
            cart.setTotalPrice(0);
            cart.setMarketId(marketId);
            SuperMarket mMarketData = ModelController.getInstance().getModel().getMarket(marketId);
            if (mMarketData == null || mMarketData.getId() == -1){
                cart.SetMarketDeliveryDuration(product.getStore().getDeliveryDuration());
                cart.SetMarketAddressValue(product.getStore().getStreet_address());
                cart.SetMarketLogoValue(product.getStore().getMarketLogo());
                cart.SetAccept_couponsBool(product.getStore().getAccept_coupons() == 1);
                cart.SetProvide_atmBool(product.getStore().getProvide_atm() == 1);
                cart.SetProvide_online_paymentBool(product.getStore().getProvide_online_payment() == 1);
                cart.setMarketName(product.getStore().getStoreName());

                cart.setMarketDeliveryFeesValue(product.getStore().getDeliveryFee());
                cart.SetMarketMinOrderValue(String.valueOf(product.getStore().getMarketMinOrder()));
                cart.SetHasFeaturedProductsBool(false);
            }
            else {
                cart.SetMarketDeliveryDuration(mMarketData.getMarketDeliveryDuration());
                cart.SetMarketAddressValue(mMarketData.getMarketAddress().getStreet1());
                cart.SetMarketLogoValue(mMarketData.getMarketLogo());
                cart.SetAccept_couponsBool(mMarketData.isAccept_coupons());
                cart.SetProvide_atmBool(mMarketData.isProvide_atm());
                cart.SetProvide_online_paymentBool(mMarketData.isProvide_online_payment());
                cart.setMarketName(mMarketData.getMarketName());

                cart.setMarketDeliveryFeesValue(mMarketData.getMarketDeliveryFees());
                cart.SetMarketMinOrderValue(mMarketData.getMarketMinOrder());
                cart.SetHasFeaturedProductsBool(mMarketData.isHasFeaturedProducts());
            }
        }

        CartItem fromDB = db.TransactionsDao().FindCartItemById(product.getId(),variation);
        if (fromDB == null) {
            CartItem itemCart = ConvertProductToCart(product,quantity,variation,VariationImageUrl);
            db.TransactionsDao().insertAll(itemCart);
            if (cart.getTotalPrice()==0)
                cart.setTotalPrice(Double.parseDouble(cart.getMarketDeliveryFeesValue()));
            cart.setTotalPrice(cart.getTotalPrice() + Float.parseFloat(product.getPrice())*quantity);
        }
        else {
            int newQuantity = fromDB.quantity + quantity;
            db.TransactionsDao().UpdateCartQuantity(newQuantity,product.getId(),variation);
            cart.setTotalPrice(cart.getTotalPrice() + Float.parseFloat(product.getPrice())*quantity);
        }
        return true;
    }
    public boolean addToCartReplace(Context context, Product product, int quantity, int marketId , int variation, String VariationImageUrl){
        CartItem itemCart = ConvertProductToCart(product,quantity,variation,VariationImageUrl);
        AppDatabase db = Room.databaseBuilder(context,
                AppDatabase.class, "CartDB").allowMainThreadQueries().build();
        db.TransactionsDao().deleteAll();
        SuperMarket mMarketData = ModelController.getInstance().getModel().getMarket(marketId);
        db.TransactionsDao().insertAll(itemCart);
        UserCart cart = new UserCart(context);
        if (mMarketData.getId() == -1){
            cart.setTotalPrice(itemCart.Price_Is+ Integer.parseInt(product.getStore().getDeliveryFee()));
            cart.setMarketId(Integer.parseInt(product.getStore().getId()));
            cart.SetMarketDeliveryDuration(product.getStore().getDeliveryDuration());
            cart.SetMarketAddressValue(product.getStore().getStreet_address());
            cart.SetMarketLogoValue(product.getStore().getMarketLogo());
            cart.SetAccept_couponsBool(product.getStore().getAccept_coupons() == 1);
            cart.SetProvide_atmBool(product.getStore().getProvide_atm() == 1);
            cart.SetProvide_online_paymentBool(product.getStore().getProvide_online_payment() == 1);
            cart.setMarketName(product.getStore().getStoreName());

            cart.setMarketDeliveryFeesValue(product.getStore().getDeliveryFee());
            cart.SetMarketMinOrderValue(String.valueOf(product.getStore().getMarketMinOrder()));
            cart.SetHasFeaturedProductsBool(false);
        }
        else {
            cart.setTotalPrice(itemCart.Price_Is+ Integer.parseInt(mMarketData.getMarketDeliveryFees()));
            cart.setMarketId(marketId);
            cart.SetMarketDeliveryDuration(mMarketData.getMarketDeliveryDuration());
            cart.SetMarketAddressValue(mMarketData.getMarketAddress().getStreet1());
            cart.SetMarketLogoValue(mMarketData.getMarketLogo());
            cart.SetAccept_couponsBool(mMarketData.isAccept_coupons());
            cart.SetProvide_atmBool(mMarketData.isProvide_atm());
            cart.SetProvide_online_paymentBool(mMarketData.isProvide_online_payment());
            cart.setMarketName(mMarketData.getMarketName());

            cart.setMarketDeliveryFeesValue(mMarketData.getMarketDeliveryFees());
            cart.SetMarketMinOrderValue(mMarketData.getMarketMinOrder());
            cart.SetHasFeaturedProductsBool(mMarketData.isHasFeaturedProducts());
        }
        return true;
    }

    public String getWooCommerceUserToken() {
        return wooCommerceUserToken;
    }

    public void setWooCommerceUserToken(String wooCommerceUserToken) {
        this.wooCommerceUserToken = wooCommerceUserToken;
    }

    public AreaResponse getAreas() {
        return areas;
    }

    public void setAreas(AreaResponse areas) {
        this.areas = areas;
    }

}
