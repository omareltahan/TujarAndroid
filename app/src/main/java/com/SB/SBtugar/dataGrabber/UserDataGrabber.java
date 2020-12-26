package com.SB.SBtugar.dataGrabber;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AllModels.ValidateTokenResponse;
import com.SB.SBtugar.SeverData.Constant;
import com.SB.SBtugar.SeverData.ServiceGenerator;
import com.SB.SBtugar.SeverData.StoreAPIInterface;
import com.SB.SBtugar.SeverData.TokenType;
import com.SB.SBtugar.utils.ErrorCode;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;

/**
 * Project ${PROJECT}
 * Created by asamy on 5/4/2018.
 */

public class UserDataGrabber extends AsyncTask<Void, Void, ValidateTokenResponse> {

    private String userToken;

    public UserDataGrabber(String userToken){
        this.userToken = userToken;

    }

    @Override
    protected ValidateTokenResponse doInBackground(Void... voids) {

        try {
            Log.e("ValidateTokenResponse",Constant.BASE_URL_V2+"user/validateToken");
            Log.e("ValidateTokenResponse",userToken);
            StoreAPIInterface storeServices = ServiceGenerator.createService(
                    StoreAPIInterface.class, Constant.BASE_URL_V2,userToken,
                    TokenType.FIREBATOKEN_TYPE);
            Call<ValidateTokenResponse> call = storeServices.validateToken();
            ValidateTokenResponse response = call.execute().body();
            return response;

        } catch (Exception e) {
            Log.e("UserDataGrabber", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(ValidateTokenResponse response) {
        Log.e("currentUser","onPostExecute");

        if(response == null) {
            FirebaseAuth.getInstance().signOut();
            ModelController.getInstance().getModel().setUser(null);
            Log.e("currentUser","onPostExecute null");
            return;
        }
        if(response.getStatus() == ErrorCode.SUCCESS){
            Log.e("currentUser","Success");
            Uri image = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
            UserData user = response.getData().getDetails();
            user.setProfilePicture(image);
            ModelController.getInstance().setUserInfo(response.getData().getToken(),
                    user);
        }
        else {
            Log.e("currentUser","failed");
            FirebaseAuth.getInstance().signOut();
            ModelController.getInstance().getModel().setUser(null);
        }

    }
}
