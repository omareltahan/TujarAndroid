package com.SB.SBtugar.AllModels.listener;


import com.SB.SBtugar.AllModels.Product;

import java.util.List;

/**
 * Project ${PROJECT}
 * Created by asamy on 4/30/2018.
 */

public interface IProductListObserver {

    void onProductListReady(List<Product> productList);
}
