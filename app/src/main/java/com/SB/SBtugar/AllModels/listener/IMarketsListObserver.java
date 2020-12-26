package com.SB.SBtugar.AllModels.listener;


import com.SB.SBtugar.AllModels.SuperMarket.SuperMarket;

import java.util.List;

/**
 *
 * Project ${PROJECT}
 * Created by asamy on 4/5/2018.
 */

public interface IMarketsListObserver {
    void onMarketListChanged();
    void onMarketListReady(List<SuperMarket> marketList);
}
