package com.SB.SBtugar.dataGrabber;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.SB.SBtugar.AllModels.Billing;
import com.SB.SBtugar.AllModels.LineItem;
import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.Orders.OrderRequest;
import com.SB.SBtugar.AllModels.Orders.OrderResponse;
import com.SB.SBtugar.AllModels.Orders.SmallMetaDatum;
import com.SB.SBtugar.AllModels.Shipping;
import com.SB.SBtugar.AllModels.Shipping_lines;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AllModels.coupon_lines;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.CartData.CartItem;
import com.SB.SBtugar.CartData.UserCart;
import com.SB.SBtugar.SeverData.Constant;
import com.SB.SBtugar.SeverData.ServiceGenerator;
import com.SB.SBtugar.SeverData.StoreAPIInterface;
import com.SB.SBtugar.SeverData.TokenType;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


/**
 * Project ${PROJECT}
 * Created by asamy on 5/6/2018.
 */

public class OrderSubmitter extends AsyncTask<Void, Void, OrderResponse> {

    private String address;
    private String phone;
    private String shipping;
    private List<CartItem> cart;
    private String customer_note;
    private String deliveryTime;
    private UserData user;
    private String userToken;
    Context context;
    public OrderSubmitter(Context context, String address, String phone, List<CartItem> cart, String deliveryTime,
                          UserData user, String userToken , String shipping , String customer_note){
        this.address =address;
        this.context =context;
        this.customer_note =customer_note;
        Log.e("here_is",shipping+"EGP");
        this.shipping = shipping;
        this.phone = phone;
        this.cart =cart;
        this.deliveryTime = deliveryTime;
        this.user = user;
        this.userToken = userToken;
    }

    @Override
    protected OrderResponse doInBackground(Void... voids) {
        StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL,userToken,
                TokenType.WOOCOMMERCETOKEN_TYPE);
        OrderResponse response = null;
        Call<OrderResponse> call = storeServices.placeOrder(createOrder());
        try {
            response = call.execute().body();
            Log.e("requestorder",new Gson().toJson(response));

            return response;

            /////////////
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;

    }

    @Override
    protected void onPostExecute(OrderResponse orderResponse) {

        if(orderResponse == null){
            Log.e("res_val_total","nnull");
            ModelController.getInstance().orderSubmitStatus(false);
            return;
        }
        if(orderResponse.getId()!= null && orderResponse.getId().intValue()>0 &&
                orderResponse.getStatus().equals("processing")){
            ModelController.getInstance().orderSubmitStatus(true);

            //////////
        }else{
            ModelController.getInstance().orderSubmitStatus(false);
        }
    }

    private OrderRequest createOrder(){
        OrderRequest request = new OrderRequest();
        SmallMetaDatum meta = new SmallMetaDatum();
        meta.setKey("locale");
        meta.setValue(AppController.SelectedSuperMarket.getLocale());
        request.getMeta_data().add(meta);

        if (AppController.isAtm == 2){
            request.setPaymentMethod("VISA");
            request.setPaymentMethodTitle("VISA On Delivery");
        }
        else if (AppController.isAtm == 1){
            request.setPaymentMethod("ATM");
            request.setPaymentMethodTitle("Card On Delivery");
        }
        else {
            request.setPaymentMethodTitle("Cash On Delivery");
            request.setPaymentMethod("COD");
        }
        request.setSetPaid(true);
//        request.setPaymentMethod(AppController.string_of_category);
        request.setCustomer_note(this.customer_note);
        //Billing Address
        Billing billingData = new Billing();
        String[] arr = ModelController.getInstance().getModel().getUser().getBilling().getAddress1().split("-");
        if (arr.length > 2)
            billingData.setAddress1(arr[0] + " - المبني: " + arr[1] + " - " + " رقم الدور : " + arr[2] + " - " );
        if (arr.length > 3)
            billingData.setAddress1(arr[0] + " - المبني: " + arr[1] + " - " + " رقم الدور : " + arr[2] + " - " + " رقم الشقة : " + arr[3]);
        else
            billingData.setAddress1(this.address);
        billingData.setAddress2(AppController.SelectedSuperMarket.getMarketAddress().getStreet1());
        billingData.setAddress2((new UserCart(context)).getMarketName());

        if (!user.getEmail().equals(""))
            billingData.setEmail(user.getEmail());
        else
            billingData.setEmail("order@superbekala.com");
        billingData.setFirstName(user.getDisplayName());
        billingData.setPhone(this.phone);
        request.setBilling(billingData);

        //Shipping Address
        Shipping shippingData = new Shipping();
        if (arr.length > 3) {
            shippingData.setAddress1(arr[0] + " - " + arr[1] + " - " + " رقم الدور : " + arr[2] );
        } else
            shippingData.setAddress1(this.address);
        shippingData.setAddress2(AppController.SelectedSuperMarket.getMarketAddress().getStreet1());
        shippingData.setFirstName(user.getDisplayName());
        request.setShipping(shippingData);

        Log.e("here_is",shipping+"EGP");
        List<Shipping_lines> vvv_lines =new ArrayList<>();
        Shipping_lines shippingData_lines = new Shipping_lines();
        shippingData_lines.setMethod_title("store_pickup");
        shippingData_lines.setMethod_id("Store Pickup");
        shippingData_lines.setTotal(this.shipping);
        vvv_lines.add(shippingData_lines);
        request.setShippingLines(vvv_lines);

        if (!AppController.promocode_order.equals("")) {
            List<coupon_lines> items_coupon_lines =new ArrayList<>();
            coupon_lines lineItem_coupon_lines = new coupon_lines();
            lineItem_coupon_lines.setCode(AppController.promocode_order);
            items_coupon_lines.add(lineItem_coupon_lines);
            request.setCoupon_lines(items_coupon_lines);

        }

        //LineItems
        List<LineItem> items =new ArrayList<>();
        for (CartItem item : cart) {
            LineItem lineItem = new LineItem();
            lineItem.setProductId(item.id);
            lineItem.setQuantity(item.quantity);
            if (item.variation != -1)
                lineItem.setVariation(item.variation);
            items.add(lineItem);
        }



        request.setLineItems(items);
        Log.e("requestorder",new Gson().toJson(request));
        return request;
    }
}
