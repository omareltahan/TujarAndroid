package com.SB.SBtugar.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AllModels.listener.IUserDataObserver;
import com.SB.SBtugar.R;
import com.SB.SBtugar.listener.MyFirebaseInstanceIDService;
import com.SB.SBtugar.utils.Network;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class GuideOne extends AppCompatActivity implements IUserDataObserver {
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_guide);
    if (getIntent().getExtras()!= null) {
        findViewById(R.id.back).setVisibility(View.VISIBLE);
        findViewById(R.id.container).setVisibility(View.GONE);
        findViewById(R.id.back).setOnClickListener(view -> {
            finish();
        });
    }
        findViewById(R.id.login).setOnClickListener(view -> openAuthenticationDialog());
        findViewById(R.id.skip).setOnClickListener(view -> {
            startActivity(new Intent(GuideOne.this,AreaSelectorActivity.class));
            finish();
        });
        findViewById(R.id.next).setOnClickListener(view -> {
            Intent intent = new Intent(GuideOne.this,GuideSecond.class);
            if (getIntent().getExtras()!= null) {
                intent.putExtra("fromdrawer",true);
            }
            startActivity(intent);
            finish();
        });
    }

    private void openAuthenticationDialog(){
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()
        );
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.tugar_logo)      // Set logo drawable
                        .build(),
                RC_SIGN_IN);


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult","onActivityResult");
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == -1) {
                getTokenAndAuthenticate();
            } else {
                Log.e("onActivityResult","onActivityResult" + response);
            }
        }
    }
    private void getTokenAndAuthenticate(){

        //Check Internet Connection
        if(!Network.isConnected(this)){
            //TODO show Error Message
            return;
        }

        //Check if User Already Authenticated at Server
        UserData user = ModelController.getInstance().getModel().getUser();
        if(user != null){
            showCheckoutActvity();
            return;
        }
        Log.e("onActivityResult","onActivityResultt");

        //Authenticate User on Server
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            //TODO show Error Message
            return;
        }
        currentUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            Log.e("onActivityResult","onActivityResult"+idToken);
                            authenticateUser(idToken);
                        } else {
                        }
                    }
                });
    }
    private void authenticateUser(String token){
        ModelController.getInstance().requestUserData(token);
        Log.e("onActivityResult","onActivityResulttt");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ModelController.getInstance().detachFromUserDataObservers(this);
    }
    @Override
    public void onUserDataReady() {
        ModelController.getInstance().detachFromUserDataObservers(this);
        final MyFirebaseInstanceIDService service=new MyFirebaseInstanceIDService();
        service.onTokenRefresh();
        showCheckoutActvity();
    }
    private void showCheckoutActvity(){
        Intent checkOutActivity = new Intent(this, AreaSelectorActivity.class);
        startActivity(checkOutActivity);
        finish();
    }


}
