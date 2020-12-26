package com.SB.SBtugar.listener;


import com.SB.SBtugar.AllModels.Product;

/**
 * Project ${PROJECT}
 * Created by asamy on 5/2/2018.
 */

public interface IProductListListener {

    void addToCart(Product product);
    void showProductDetails(Product product);
}
