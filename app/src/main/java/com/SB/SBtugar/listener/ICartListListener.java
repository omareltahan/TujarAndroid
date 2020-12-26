package com.SB.SBtugar.listener;


import com.SB.SBtugar.CartData.CartItem;

/**
 * Project ${PROJECT}
 * Created by asamy on 5/6/2018.
 */

public interface ICartListListener {
    void onRemoveItem(CartItem item);
    void onQuantityChange(CartItem product, int value, boolean plus);
}
