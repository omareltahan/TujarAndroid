package com.SB.SBtugar.dataGrabber;

import android.os.AsyncTask;

import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.Orders.OrderResponse;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.SeverData.Constant;
import com.SB.SBtugar.SeverData.ServiceGenerator;
import com.SB.SBtugar.SeverData.StoreAPIInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Project ${PROJECT}
 * Created by asamy on 5/8/2018.
 */

public class OrdersGrabber extends AsyncTask<Void, Void, List<OrderResponse>> {

    private String userToken;
    int page;
    public OrdersGrabber(String userToken, int page){
        this.userToken = userToken;
        this.page = page;
    }

    @Override
    protected List<OrderResponse> doInBackground(Void... voids) {
        final StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL);
        UserData user = ModelController.getInstance().getModel().getUser();
        if (user != null) {
            Call<List<OrderResponse>> call = storeServices.getOrders(user.getId(), page);
            List<OrderResponse> responses = null;
            try {
                responses = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responses;
        }
        else {
            return new ArrayList<>();
        }

    }

    @Override
    protected void onPostExecute(List<OrderResponse> orderResponses) {
        ModelController.getInstance().setOrdersList(orderResponses);
    }
}
