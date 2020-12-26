package com.SB.SBtugar.Activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.SB.SBtugar.Adapters.ProductRecyclerViewAdapter;
import com.SB.SBtugar.AllModels.Image;
import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.Product;
import com.SB.SBtugar.AllModels.listener.IProductListObserver;
import com.SB.SBtugar.R;
import com.SB.SBtugar.listener.IProductListListener;
import com.SB.SBtugar.utils.DataExchangeParam;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchProducts extends AppCompatActivity implements IProductListObserver, IProductListListener {
    Dialog dialog;
    private ProductRecyclerViewAdapter productsRecycleViewAdapter;
    List<Product> list_products ;
    ImageView clear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_products);
        list_products=new ArrayList<>();
        productsRecycleViewAdapter = new ProductRecyclerViewAdapter(R.layout.product_item);
        final EditText searchView = findViewById(R.id.searchView);
        searchView.requestFocus();
        InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        clear = findViewById(R.id.clear);

        clear.setOnClickListener(view -> {
            searchView.setText("");
            clear.setVisibility(View.GONE);
        });
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (searchView.getText().toString().isEmpty())
                    clear.setVisibility(View.GONE);
                else
                    clear.setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogView(SearchProducts.this);
                ModelController.getInstance().searchProductsOnAllApp(searchView.getText().toString());
            }
        });
        ImageView back =findViewById(R.id.back);
        if (back!=null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   finish();
                }
            });
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(SearchProducts.this,2);

        RecyclerView productList =findViewById(R.id.list);
        productsRecycleViewAdapter.setGlideManager(Glide.with(this));
        productList.setLayoutManager(layoutManager);
        productList.setAdapter(productsRecycleViewAdapter);
        }

    @Override
    public void onProductListReady(List<Product>productList) {
        if(productList!= null) {
            list_products = productList;
            productsRecycleViewAdapter.setProductList(list_products);
            if (productList.size()==0)
                findViewById(R.id.nomarketscontainer).setVisibility(View.VISIBLE);
            else
                findViewById(R.id.nomarketscontainer).setVisibility(View.GONE);
        }
        else {
            list_products = new ArrayList<>();
            productsRecycleViewAdapter.setProductList(new ArrayList<>());
            findViewById(R.id.nomarketscontainer).setVisibility(View.VISIBLE);
        }
        dialog.dismiss();
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
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ModelController.getInstance().attachToProductListObservers(this);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
           productsRecycleViewAdapter.setProductListListener(this);
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
    public void addToCart(final Product product) {
        boolean res = ModelController.getInstance().getModel().addToCart(getApplicationContext(),product,1, Integer.parseInt(product.getStore().getId()),-1,"");
        if(res == true) {
            Toast.makeText(SearchProducts.this, getString(R.string.product_added_to_cart),
                    Toast.LENGTH_SHORT).show();
        }else{
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.alert_to_add_to_cart))
                    .setPositiveButton(R.string.alert_to_add_to_cart_sure, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            boolean res = ModelController.getInstance().getModel().addToCartReplace(getApplicationContext(),product,1, Integer.parseInt(product.getStore().getId()),-1,"");
                            if(res == true) {
                                Toast.makeText(SearchProducts.this, getString(R.string.product_added_to_cart),
                                        Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(SearchProducts.this, "حدث خطأ ما ... حاول مرة أخرى",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton(R.string.alert_to_add_to_cart_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(R.drawable.danger)
                    .show();
        }
    }

    @Override
    public void showProductDetails(Product product) {
        Intent detailsActivty = new Intent(SearchProducts.this, ProductDetailsActivity.class);
        detailsActivty.putExtra(DataExchangeParam.ARG_PRODUCT,product);
        detailsActivty.putExtra(DataExchangeParam.ARG_MARKET_ID,product.getStore().getId());
        startActivity(detailsActivty);
    }
}
