package com.SB.SBtugar.Activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.PointsMain;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.R;
import com.SB.SBtugar.SeverData.Constant;
import com.SB.SBtugar.SeverData.ServiceGenerator;
import com.SB.SBtugar.SeverData.StoreAPIInterface;
import com.bumptech.glide.Glide;
import com.google.api.client.util.Strings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends AppCompatActivity {
    UserData mUser;
    ImageView userImage;
    TextView Name;
    TextView mobileNumber;
    TextView Address;
    TextView MarketAddress;
    TextView MarketName;
    TextView market_phone;


    LinearLayout containerProfile;
    View bottomLineProfile;

    LinearLayout containerPoint;
    View bottomLinePoint;

    LinearLayout containerMarket;
    View bottomLineMarket;


    LinearLayout containerViewProfile;
    LinearLayout containerViewPoint;
    LinearLayout containerViewMarket;

    TextView wallet;
    LinearLayout first;
    TextView total_points;
    LinearLayout second;
    LinearLayout third;
    int value_of_total=0;
    int replaced_points=0;

    boolean gotBefore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view);
        wallet = findViewById(R.id.wallet);
        MarketAddress = findViewById(R.id.MarketAddress);
        MarketName = findViewById(R.id.MarketName);
        market_phone = findViewById(R.id.market_phone);

        containerPoint = findViewById(R.id.containerPoint);
        bottomLinePoint = findViewById(R.id.bottomLinePoint);
        containerViewProfile = findViewById(R.id.containerViewProfile);
        containerViewMarket = findViewById(R.id.containerViewMarket);
        containerProfile = findViewById(R.id.containerProfile);
        bottomLineProfile = findViewById(R.id.bottomLineProfile);
        containerMarket = findViewById(R.id.containerMarket);
        bottomLineMarket = findViewById(R.id.bottomLineMarket);
        containerViewPoint = findViewById(R.id.containerViewPoint);
        ImageView back = findViewById(R.id.back);
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        containerMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                containerViewMarket.setVisibility(View.VISIBLE);
                containerViewProfile.setVisibility(View.GONE);
                containerViewPoint.setVisibility(View.GONE);
                bottomLineMarket.setVisibility(View.VISIBLE);
                bottomLineProfile.setVisibility(View.GONE);
                bottomLinePoint.setVisibility(View.GONE);
            }
        });
        containerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                containerViewMarket.setVisibility(View.GONE);
                containerViewProfile.setVisibility(View.VISIBLE);
                containerViewPoint.setVisibility(View.GONE);
                bottomLineMarket.setVisibility(View.GONE);
                bottomLineProfile.setVisibility(View.VISIBLE);
                bottomLinePoint.setVisibility(View.GONE);
            }
        });
        containerPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                containerViewMarket.setVisibility(View.GONE);
                containerViewProfile.setVisibility(View.GONE);
                containerViewPoint.setVisibility(View.VISIBLE);
                bottomLineMarket.setVisibility(View.GONE);
                bottomLineProfile.setVisibility(View.GONE);
                bottomLinePoint.setVisibility(View.VISIBLE);
                if (!gotBefore)
                    getTotal();
                gotBefore = true;

            }
        });

        first=findViewById(R.id.firstrow);
        total_points=findViewById(R.id.total_points);
        second=findViewById(R.id.secondrow);
        third=findViewById(R.id.thirdrow);
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value_of_total >= 1000) {
                    replaced_points = 1000;
                    SetToServerWallet(20);
                }
                else {
                    Toast.makeText(getApplicationContext(),"ليس لديك نقاط كافية للاستبدال", Toast.LENGTH_LONG).show();
                }
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value_of_total >= 2500) {
                    replaced_points = 2500;
                    SetToServerWallet(50);
                }
                else {
                    Toast.makeText(getApplicationContext(),"ليس لديك نقاط كافية للاستبدال", Toast.LENGTH_LONG).show();
                }            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value_of_total >= 5000) {
                    replaced_points = 5000;
                    SetToServerWallet(100);
                }
                else {
                    Toast.makeText(getApplicationContext(),"ليس لديك نقاط كافية للاستبدال", Toast.LENGTH_LONG).show();
                }            }
        });
    }
    Dialog dialog;
    public void ShowDialogView(Context context){
        dialog = new Dialog(context,R.style.Theme_Dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_item);
        Glide.with(context).asGif().load(R.drawable.progress_image).into((ImageView) dialog.findViewById(R.id.image));
        final Window dialogWindow = dialog.getWindow();
        final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogWindowAttributes);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity =  Gravity.CENTER;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    void SetToServerWallet(int total){
        dialog.show();
        ModelController.getInstance().getModel().getUser().setWallet(ModelController.getInstance().getModel().getUser().getWallet()+total);
        final StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        storeServices.AddWalletValue(ModelController.getInstance().getModel().getUser().getId(), total)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call,
                                           final @NonNull Response<String> response) {
                        dialog.dismiss();

                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, Throwable t) {
                        dialog.dismiss();
                    }
                });
        Toast.makeText(getApplicationContext(),"تم استبدال نقاطك باضافة المبلغ الى محفظتك", Toast.LENGTH_LONG).show();
        value_of_total = value_of_total - replaced_points;
        total_points.setText(value_of_total+"");

    }

    public void getTotal() {
        ShowDialogView(this);
        StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        UserData user = ModelController.getInstance().getModel().getUser();
        Log.e("onResponsedd",Constant.BASE_URL_V2 + "loyalty/" +  user.getId());
        storeServices.getLoyalityPoints(""+user.getId())
                .enqueue(new Callback<PointsMain>() {
                    @Override
                    public void onResponse(@NonNull Call<PointsMain> call,
                                           @NonNull Response<PointsMain> response) {
                        Log.e("onResponsedd","here");
                        dialog.dismiss();
                        total_points.setText(response.body().getData().get(0).getPoints() + "");
                        value_of_total = response.body().getData().get(0).getPoints();
                    }

                    @Override
                    public void onFailure(@NonNull Call<PointsMain> call, Throwable t) {
                        Log.e("onResponsedd","nooo");
                        dialog.dismiss();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUser = ModelController.getInstance().getModel().getUser();
        if (mUser != null) {
            Name = findViewById(R.id.username);
            Address = findViewById(R.id.address);
            userImage = findViewById(R.id.userimage);
            Name.setText(mUser.getFirstName() + " " + mUser.getLastName());
            mobileNumber = findViewById(R.id.phone);
            String phone = mUser.getBilling().getPhone();
            Glide.with(this).load(mUser.getAvatarUrl()).into(userImage);
            wallet.setText(mUser.getWallet()+"ج.م");
            if (!Strings.isNullOrEmpty(phone)) {
                mobileNumber.setText(phone);
            } else {
                mobileNumber.setText("");
            }
            String mAddress = ModelController.getInstance().getModel().getUser().getBilling().getAddress1();
            Address.setText(mAddress);
            for (int i = 0 ; i<mUser.getMeta_data().size();i++){
                if (mUser.getMeta_data().get(i).getKey().toLowerCase().equals("store_name".toLowerCase()))
                    MarketName.setText(String.valueOf(mUser.getMeta_data().get(i).getValue()));
                else if (mUser.getMeta_data().get(i).getKey().toLowerCase().equals("store_address".toLowerCase()))
                    MarketAddress.setText(String.valueOf(mUser.getMeta_data().get(i).getValue()));
                else if (mUser.getMeta_data().get(i).getKey().toLowerCase().equals("store_phone".toLowerCase()))
                    market_phone.setText(String.valueOf(mUser.getMeta_data().get(i).getValue()));
            }
        }
    }
}
