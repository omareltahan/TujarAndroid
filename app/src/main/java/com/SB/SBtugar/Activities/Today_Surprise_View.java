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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SB.SBtugar.Adapters.MarketsRecycleViewAdapter;
import com.SB.SBtugar.Adapters.ProductRecyclerViewAdapter;
import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.Product;
import com.SB.SBtugar.AllModels.SuperMarket.SuperMarket;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AllModels.UserDataResponse;
import com.SB.SBtugar.AllModels.listener.IProductListObserver;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.R;
import com.SB.SBtugar.SeverData.Constant;
import com.SB.SBtugar.SeverData.ServiceGenerator;
import com.SB.SBtugar.SeverData.StoreAPIInterface;
import com.SB.SBtugar.listener.IMarketListListener;
import com.SB.SBtugar.listener.IProductListListener;
import com.SB.SBtugar.utils.DataExchangeParam;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Today_Surprise_View extends AppCompatActivity implements IProductListListener, IProductListObserver {
    ProductRecyclerViewAdapter Productadapter;
    List<Product> list_products ;
    Dialog dialog;
    RecyclerView top_products;


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ModelController.getInstance().attachToProductListObservers(this);
        try {
            Productadapter.setProductListListener(this);
        } catch (ClassCastException e) {
            throw new ClassCastException( " must implement IProductListListener");
        }
    }
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ModelController.getInstance().detachFromProductListObservers(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        ImageView back = findViewById(R.id.back);
        if (back!=null) {
            findViewById(R.id.backcontainer).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.title)).setText(getResources().getString(R.string.dialy_offers));
            back.setOnClickListener(v -> finish());
        }
        list_products = new ArrayList<>();
        top_products = findViewById(R.id.list_offers);
        Productadapter = new ProductRecyclerViewAdapter(R.layout.product_item);
        Productadapter.setGlideManager(Glide.with(this));
        GridLayoutManager layoutManager = new GridLayoutManager(Today_Surprise_View.this,2);
        top_products.setLayoutManager(layoutManager);
        top_products.setAdapter(Productadapter);
        ShowDialogView(this);
        ModelController.getInstance().GetAllOffersOfApp();
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



    @Override
    public void onProductListReady(List<Product> productList) {
        dialog.dismiss();
        if(productList!= null) {
            list_products.addAll(productList);
            Productadapter.setProductList(list_products);
        }
    }

    @Override
    public void addToCart(final Product product) {
        boolean res = ModelController.getInstance().getModel().addToCart(getApplicationContext(),product,1, Integer.parseInt(product.getStore().getId()),-1,"");
        if(res == true) {
            Toast.makeText(Today_Surprise_View.this, getString(R.string.product_added_to_cart),
                    Toast.LENGTH_SHORT).show();
        }else{
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.alert_to_add_to_cart))
                    .setPositiveButton(R.string.alert_to_add_to_cart_sure, (dialog, which) -> {
                        dialog.dismiss();
                        boolean res1 = ModelController.getInstance().getModel().addToCartReplace(getApplicationContext(),product,1, Integer.parseInt(product.getStore().getId()),-1,"");
                        if(res1 == true) {
                            Toast.makeText(Today_Surprise_View.this, getString(R.string.product_added_to_cart),
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(Today_Surprise_View.this, "حدث خطأ ما ... حاول مرة أخرى",
                                    Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton(R.string.alert_to_add_to_cart_cancel, (dialog, which) -> dialog.dismiss())
                    .setIcon(R.drawable.danger)
                    .show();
        }
    }

    @Override
    public void showProductDetails(Product product) {
        Intent detailsActivty = new Intent(Today_Surprise_View.this, ProductDetailsActivity.class);
        detailsActivty.putExtra(DataExchangeParam.ARG_PRODUCT,product);
        detailsActivty.putExtra(DataExchangeParam.ARG_MARKET_ID,product.getStore().getId());
        startActivity(detailsActivty);
    }
}
