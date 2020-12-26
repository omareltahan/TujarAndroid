package com.SB.SBtugar.Activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.SB.SBtugar.Adapters.MarketsRecycleViewAdapter;
import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.SuperMarket.SuperMarket;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AllModels.UserDataResponse;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.R;
import com.SB.SBtugar.SeverData.Constant;
import com.SB.SBtugar.SeverData.ServiceGenerator;
import com.SB.SBtugar.SeverData.StoreAPIInterface;
import com.SB.SBtugar.listener.IMarketListListener;
import com.SB.SBtugar.utils.DataExchangeParam;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Favourite_View extends AppCompatActivity implements IMarketListListener {
    private MarketsRecycleViewAdapter marketsRecycleViewAdapter;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        ImageView back = findViewById(R.id.back);
        if (back!=null) {
            findViewById(R.id.backcontainer).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.title)).setText(getResources().getString(R.string.fav_title));
            back.setOnClickListener(v -> finish());
        }
        marketsRecycleViewAdapter = new MarketsRecycleViewAdapter(this);
        marketsRecycleViewAdapter.setGlideManager(Glide.with(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        RecyclerView marketList = findViewById(R.id.list_offers);
        marketList.setLayoutManager(layoutManager);
        marketList.setAdapter(marketsRecycleViewAdapter);
        if (ModelController.getInstance().getModel().getUser() != null)
            requestMarketListFavourite();
    }
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

    public void requestMarketListFavourite() {
        ShowDialogView(this);
        StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        UserData user = ModelController.getInstance().getModel().getUser();
        Log.e("favfav",""+user.getId());
        storeServices.getFav_Markets(""+user.getId())
                .enqueue(new Callback<UserDataResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UserDataResponse> call,
                                           @NonNull Response<UserDataResponse> response) {
                        dialog.dismiss();
                        List<SuperMarket> superMarketList = response.body().getData().getFav_markets();
                        marketsRecycleViewAdapter.setMarketList(superMarketList);
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserDataResponse> call, Throwable t) {
                        dialog.dismiss();
                        Log.e("ModelController", "Failed to get super Markets Data", t);
                    }
                });
    }
    private void setDialog() {
        final Dialog dialog = new Dialog(this,R.style.Theme_Dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image);
        dialog.findViewById(R.id.container_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
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

    @Override
    public void onItemClick(final SuperMarket market) {
        if(market.isOnHold()){
           setDialog();
        }

        else if(!market.isOpen()){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title_closed_alert)
                    .setMessage(getString(R.string.title_closed_alert_now)+market.getMarketName() + getString(R.string.desc_closed_alert)+market.getOpeningTime())
                    .setPositiveButton(R.string.continue_closed_alert, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent marketDataActivity = new Intent(getApplicationContext(),
                                    MarketDataView.class);
                            marketDataActivity.putExtra(DataExchangeParam.ARG_MARKET_ID,market.getId());

                            AppController.SelectedSuperMarket = market;
                            startActivity(marketDataActivity);
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(R.string.cencel_closed_alert, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setIcon(R.drawable.danger)
                    .show();

            return;
        }
        Intent marketDataActivity = new Intent(this,
                MarketDataView.class);
        marketDataActivity.putExtra(DataExchangeParam.ARG_MARKET_ID,market.getId());
        AppController.SelectedSuperMarket = market;
        Log.e("SelectedSuperMarket",market.getMarketName());
        Log.e("SelectedSuperMarket",market.getId()+"");
        startActivity(marketDataActivity);
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            IMarketListListener mCallback = (IMarketListListener) this;
            marketsRecycleViewAdapter.setOnItemClickListener(mCallback);
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    " must implement IMarketListListener");
        }
    }
}
