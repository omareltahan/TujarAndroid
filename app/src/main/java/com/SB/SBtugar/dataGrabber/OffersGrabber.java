package com.SB.SBtugar.dataGrabber;

import android.os.AsyncTask;
import android.util.Log;


import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.Product;
import com.SB.SBtugar.AllModels.SuperMarket.SuperMarket;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.SeverData.Constant;
import com.SB.SBtugar.SeverData.ServiceGenerator;
import com.SB.SBtugar.SeverData.StoreAPIInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Project ${PROJECT}
 * Created by asamy on 12/30/2018.
 */
public class OffersGrabber extends AsyncTask<Void, Void, List<Product>> {

    SuperMarket market;
    public OffersGrabber(SuperMarket market){
        this.market = market;

    }
    @Override
    protected List<Product> doInBackground(Void... voids) {
        Log.e("ShowDataLog", "voids");

        List<Product> products = new ArrayList<>();
        final StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class,Constant.BASE_URL);
        Log.e("ShowDataLog", Constant.BASE_URL);
        try {
            products = storeServices.getShopOffers(true, String.valueOf(AppController.SelectedSuperMarket.getId())
                    ,"publish").execute().body();
            Log.e("ShowDataLog", "published");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ShowDataLog", e.getMessage());

        }
        return products;
    }

    @Override
    protected void onPostExecute(List<Product> products) {
        if (products==null)
            return;
        super.onPostExecute(products);
        market.setOffers(products);
        ModelController.getInstance().marketInfoReady(market);
    }
}
