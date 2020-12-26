package com.SB.SBtugar.listener;

import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.NotificationRegResponse;
import com.SB.SBtugar.AllModels.RegisterValueData;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.SeverData.Constant;
import com.SB.SBtugar.SeverData.ServiceGenerator;
import com.SB.SBtugar.SeverData.StoreAPIInterface;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseInstanceIDService  extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        if (FirebaseInstanceId.getInstance().getToken()!=null)
            sendRegistrationToServer(FirebaseInstanceId.getInstance().getToken());
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]

    private void sendRegistrationToServer(String token) {
        if (AppController.contextValue == null || token==null)
            return;
        AppController.contextValue.getSharedPreferences("vendorapp", MODE_PRIVATE).edit().putString("token", token).apply();
        StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V1);


        if (ModelController.getInstance().getModel().getUser() == null)
            return;
        int id_is = ModelController.getInstance().getModel().getUser().getId();
        RegisterValueData valueData = new RegisterValueData();
        valueData.setUser_id(id_is);
        valueData.setToken(token);

        storeServices.SetRegisterValue(id_is,token)
                .enqueue(new Callback<NotificationRegResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<NotificationRegResponse> call,
                                           @NonNull Response<NotificationRegResponse> value) {

                    }

                    @Override
                    public void onFailure(@NonNull Call<NotificationRegResponse> call, Throwable t) {

                    }
                });
    }

}