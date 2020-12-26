package com.SB.SBtugar.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AllModels.listener.IAreaListObserver;
import com.SB.SBtugar.AllModels.listener.IUserDataObserver;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.R;
import com.SB.SBtugar.listener.MyFirebaseInstanceIDService;
import com.SB.SBtugar.utils.Network;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FullscreenActivity extends AppCompatActivity implements IUserDataObserver, IAreaListObserver {
    ModelController controller;
    boolean value_loaded = false;
    String type = "";
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = ModelController.getInstance();
        setContentView(R.layout.splash_screen);
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.SB.SBtugar", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("dataresult", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("dataresult", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("dataresult", e.toString());
        } catch (Exception e) {
            Log.e("dataresult", e.toString());
        }
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            value_loaded = true;
            if (!type.equals("")){
                if (type.equals("second")){
                    Intent areaSelector = new Intent(getApplicationContext(), AreaSelectorActivity.class);
                    startActivity(areaSelector);
                }
                else {
                    Intent areaSelector = new Intent(getApplicationContext(), GuideOne.class);
                    startActivity(areaSelector);
                    getSharedPreferences("tujar",MODE_PRIVATE).edit().putBoolean("guide",true).apply();
                }
                finish();

            }
        }, 5000);

        FirebaseAnalytics.getInstance(this);
        if (!isNetworkAvailable(this)){
            new AlertDialog.Builder(this)
                    .setTitle("لا يوجد اتصال بالانترنت")
                    .setMessage("لا يوجد اتصال بالانترنت ... عليك التاكد من الاتصال بالانترنت و معاودة المحاولة مرة اخرى")
                    .setNegativeButton("خروج", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    }).setPositiveButton("إعادة المحاولة", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            recreate();
                        }
                    })
                    .setIcon(R.drawable.danger)
                    .show();
        }
        ModelController.getInstance().attachToUserDataObservers(this);
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(FullscreenActivity.this)
                        .setTitle("تأكد من اتصالك بالانترنت")
                        .setMessage("عليك التاكد من الاتصال بالانترنت و معاودة المحاولة مرة اخرى")
                        .setNegativeButton("خروج", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (dialog!=null)
                                    dialog.cancel();
                                finish();
                            }
                        }).setPositiveButton("إعادة المحاولة", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                recreate();
                            }
                        });
                if (dialog!=null) {
                    try {
                        dialog.show();
                    }
                    catch (WindowManager.BadTokenException e) {
                        //use a log message
                    }
                }
            }
        }, 15000);


        Glide.with(this).asGif().load(R.drawable.animation_image).into(((ImageView)findViewById(R.id.image)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ModelController.getInstance().attachToAreaListObservers(FullscreenActivity.this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ModelController.getInstance().detachFromUserDataObservers(this);
        ModelController.getInstance().detachFromAreaListObservers(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null)
            getTokenAndAuthenticate();
        else
            getAreaList();
    }

    @Override
    public void onUserDataReady() {
        getAreaList();
        final MyFirebaseInstanceIDService service=new MyFirebaseInstanceIDService();
        service.onTokenRefresh();
    }

    private void getTokenAndAuthenticate(){
        if(!Network.isConnected(this)){
            //TODO show Error Message
            return;
        }
        UserData user = ModelController.getInstance().getModel().getUser();
        if(user != null){
            getAreaList();
            return;
        }
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            return;
        }
        currentUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            authenticateUser(idToken);
                        }
                    }
                });


    }

    private void authenticateUser(String token){
        ModelController.getInstance().requestUserData(token);
    }

    private void getAreaList(){
        ModelController.getInstance().requestSupportedAreas();
    }

    @Override
    public void onAreaListReady() {
        if (ModelController.getInstance().getSupportedAreas()==null || ModelController.getInstance().getSupportedAreas().getData() == null ) {
            getAreaList();
        }
        else if (getSharedPreferences("tujar",MODE_PRIVATE).getBoolean("guide",false)) {
            if (value_loaded){
                Intent areaSelector = new Intent(getApplicationContext(), GuideOne.class);
                startActivity(areaSelector);
                finish();
                getSharedPreferences("tujar",MODE_PRIVATE).edit().putBoolean("guide",true).apply();
            }
            else {
                type = "first";
            }
        }
        else {
            if (value_loaded){
                Intent areaSelector = new Intent(getApplicationContext(), AreaSelectorActivity.class);
                startActivity(areaSelector);
                finish();
            }
            else  {
                type = "second";
            }
        }
    }
}
