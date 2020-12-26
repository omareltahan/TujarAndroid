package com.SB.SBtugar.AllModels.listener;


import com.SB.SBtugar.AllModels.Orders.OrderResponse;

import java.util.List;

/**
 * Project ${PROJECT}
 * Created by asamy on 5/9/2018.
 */

public interface IOrderListObserver {
    void onOrderListReady(List<OrderResponse> orders);
}
