package com.SB.SBtugar.dataGrabber;

import android.os.AsyncTask;
import android.util.Log;


import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AllModels.UserUpdateRequest;
import com.SB.SBtugar.SeverData.Constant;
import com.SB.SBtugar.SeverData.ServiceGenerator;
import com.SB.SBtugar.SeverData.StoreAPIInterface;
import com.SB.SBtugar.SeverData.TokenType;

import retrofit2.Call;

/**
 * Project ${PROJECT}
 * Created by asamy on 6/1/2018.
 */

public class UserUpdateSubmitter extends AsyncTask<Void, Void, UserData> {

    UserUpdateRequest request;
    String userId;
    String userToken;

    public UserUpdateSubmitter(UserUpdateRequest userUpdateRequest, String userId, String userToken){
        Log.e("res_val",userUpdateRequest.toString());
        this.request = userUpdateRequest;
        this.userId = userId;
        this.userToken = userToken;
    }


    @Override
    protected UserData doInBackground(Void... voids) {

        try {

            StoreAPIInterface storeServices = ServiceGenerator.createService(
                    StoreAPIInterface.class, Constant.BASE_URL,userToken,
                    TokenType.WOOCOMMERCETOKEN_TYPE);
            Call<UserData> call = storeServices.updateUserData(this.userId, this.request);
            UserData response = call.execute().body();
            return response;

        } catch (Exception e) {
            Log.e("UserUpdateSubmitter", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(UserData userData) {
        ModelController.getInstance().updateUserInfo(userData);
    }
}
