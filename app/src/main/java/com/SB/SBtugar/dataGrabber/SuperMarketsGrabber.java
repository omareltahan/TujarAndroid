package com.SB.SBtugar.dataGrabber;

import android.os.AsyncTask;


import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.SuperMarket.SuperMarket;

import java.util.List;

/**
 * Project ${PROJECT}
 * Created by asamy on 4/5/2018.
 */

public class SuperMarketsGrabber extends AsyncTask<Void, Void, List<SuperMarket>> {
    private String cityCode;

    public SuperMarketsGrabber(String cityCode){
        this.cityCode = cityCode;
    }

    @Override
    protected List<SuperMarket> doInBackground(Void... voids) {
        List<SuperMarket> allSuperMarkets = ModelController.getInstance().getModel()
                .getMarketList();
//        //Add Super Markets that are in the same Area
//        for (SuperMarket superMarket:allSuperMarkets) {
//            if(superMarket.getMarketAddress().getZip().equals(cityCode) && superMarket.isOpen()){
//                result.add(superMarket);
//            }
//        }
//        //Add Super Markets that are not in the same Area
//        for (SuperMarket superMarket:allSuperMarkets) {
//            if(!superMarket.getMarketAddress().getZip().equals(cityCode) && superMarket.isOpen()){
//                result.add(superMarket);
//            }
//        }
//
//        //Add closed SuperMarkets at end of the list
//        for (SuperMarket superMarket:allSuperMarkets) {
//            if(!superMarket.isOpen()){
//                result.add(superMarket);
//            }
//        }



        return allSuperMarkets;
    }

    @Override
    protected void onPostExecute(List<SuperMarket> markets) {
        ModelController.getInstance().notifyMarketListObservers(markets);
    }
}
