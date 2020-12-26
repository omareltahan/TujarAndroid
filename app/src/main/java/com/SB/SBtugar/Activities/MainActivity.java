package com.SB.SBtugar.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.SB.SBtugar.Adapters.MarketsRecycleViewAdapter;
import com.SB.SBtugar.Adapters.ProductRecyclerViewAdapter;
import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.Product;
import com.SB.SBtugar.AllModels.SuperMarket.SuperMarket;
import com.SB.SBtugar.AllModels.display_value_item;
import com.SB.SBtugar.AllModels.listener.IMarketsListObserver;
import com.SB.SBtugar.AllModels.listener.IProductListObserver;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.R;
import com.SB.SBtugar.listener.IMarketListListener;
import com.SB.SBtugar.listener.IProductListListener;
import com.SB.SBtugar.utils.DataExchangeParam;
import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import me.leolin.shortcutbadger.ShortcutBadger;
import static com.SB.SBtugar.utils.DataExchangeParam.ARG_MAIN_CAT;
import static com.SB.SBtugar.utils.DataExchangeParam.ARG_MAIN_CATALL;

public class MainActivity extends AppCompatActivity implements IMarketListListener, IMarketsListObserver,
        SwipeRefreshLayout.OnRefreshListener  , IProductListListener, IProductListObserver  {
    private static final int RC_SIGN_IN = 123;
    List<SuperMarket> marketList_global;
    Dialog dialog;
    private ModelController controller;
    private MarketsRecycleViewAdapter marketsRecycleViewAdapter;
    private SwipeRefreshLayout swipeLayout;
    int modelCat = 0;
    ArrayList<display_value_item> listCat = new ArrayList<>();
    ImageView menuImg;


    void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            Log.e("eeeeeee",e.getMessage());
        }
    }
    void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        AppController.uri = Uri.fromFile(imageFile);
        startActivity(new Intent(MainActivity.this,DrawerPageView.class));
    }
    ProductRecyclerViewAdapter Productadapter;
    RecyclerView top_products;
    List<Product> list_products ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_list_view);
        findViewById(R.id.back).setOnClickListener(view -> finish());

        menuImg = findViewById(R.id.menuImg);
        menuImg.setOnClickListener(view -> takeScreenshot());
        ShortcutBadger.applyCount(this, 0); //for 1.1.4+
        FirebaseAnalytics.getInstance(this);

        modelCat = getIntent().getExtras().getInt(ARG_MAIN_CAT);
        listCat = (ArrayList<display_value_item>) getIntent().getExtras().getSerializable(ARG_MAIN_CATALL);
        marketList_global = new ArrayList<>();

        list_products = new ArrayList<>();
        top_products = findViewById(R.id.list);
        Productadapter = new ProductRecyclerViewAdapter(R.layout.product_item2);
        Productadapter.setGlideManager(Glide.with(this));
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(MainActivity.this,RecyclerView.HORIZONTAL,false);
        top_products.setLayoutManager(layoutManager2);
        top_products.setAdapter(Productadapter);

        marketsRecycleViewAdapter = new MarketsRecycleViewAdapter(MainActivity.this);
        marketsRecycleViewAdapter.setGlideManager(Glide.with(MainActivity.this));
        swipeLayout = findViewById(R.id.swipe_layout_main);
        swipeLayout.setOnRefreshListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        RecyclerView marketList = findViewById(R.id.list2);
        marketList.setLayoutManager(layoutManager);
        marketList.setAdapter(marketsRecycleViewAdapter);
        controller = ModelController.getInstance();
        controller.requestMarketList(listCat.get(modelCat).getValue(),this);
        ShowDialogView(this);
//        ModelController.getInstance().GetAllProductsOfApp();

    }
    private void setDialog() {
        final Dialog dialog = new Dialog(MainActivity.this,R.style.Theme_Dialog);
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
            return;
        }
        else if(!market.isOpen()){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.title_closed_alert)
                    .setMessage(getString(R.string.title_closed_alert_now)+ " " + market.getMarketName() + " " + getString(R.string.desc_closed_alert)+market.getDeliveryStartTime())
                    .setPositiveButton(R.string.continue_closed_alert, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent MarketDataView = new Intent(MainActivity.this,
                                    MarketDataView.class);
                            MarketDataView.putExtra(DataExchangeParam.ARG_MARKET_ID,market.getId());
                            AppController.SelectedSuperMarket = market;
                            startActivity(MarketDataView);
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
        else if(market.isBusy()){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("المتجر مشغول")
                    .setMessage(market.getMarketName() + " مشغول الأن .. سيتم استكمال الطلب و مضاعفة وقت التوصيل . " )
                    .setPositiveButton(R.string.continue_closed_alert, (dialog, which) -> {
                        Intent MarketDataView = new Intent(MainActivity.this,
                                MarketDataView.class);
                        MarketDataView.putExtra(DataExchangeParam.ARG_MARKET_ID,market.getId());
                        AppController.SelectedSuperMarket = market;
                        startActivity(MarketDataView);
                        dialog.cancel();
                    })
                    .setNegativeButton(R.string.cencel_closed_alert, (dialog, which) -> dialog.dismiss())
                    .setIcon(R.drawable.danger)
                    .show();

            return;
        }
        Intent MarketDataView = new Intent(MainActivity.this,
                MarketDataView.class);
        MarketDataView.putExtra(DataExchangeParam.ARG_MARKET_ID,market.getId());
        this.startActivity(MarketDataView);
    }

    @Override
    public void onMarketListChanged() {

    }

    @Override
    public void onMarketListReady(List<SuperMarket> marketList) {
        dialog.dismiss();
        if(marketList != null){
            marketList_global = marketList;
            marketsRecycleViewAdapter.setMarketList(marketList);
            if (marketList.size()==0)
                findViewById(R.id.nomarketscontainer).setVisibility(View.VISIBLE);
            else {
                ModelController.getInstance().GetAllProductsOfAppById(marketList.get(0).getId());
                findViewById(R.id.nomarketscontainer).setVisibility(View.GONE);
            }
        }
        else {
            marketList_global = new ArrayList<>();
            marketsRecycleViewAdapter.setMarketList(new ArrayList<>());
            findViewById(R.id.nomarketscontainer).setVisibility(View.VISIBLE);
        }
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ModelController.getInstance().attachToMarketListObservers(this);
        try {
            IMarketListListener mCallback = (IMarketListListener) this;
            marketsRecycleViewAdapter.setOnItemClickListener(mCallback);
        } catch (ClassCastException e) {
            throw new ClassCastException(
                     " must implement IMarketListListener");
        }
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
        ModelController.getInstance().detachFromMarketListObservers(this);

    }

    @Override
    public void onRefresh() {
        dialog.show();
        controller.requestMarketList(listCat.get(modelCat).getValue(),this);
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
        if(productList!= null) {
            list_products.addAll(productList);
            Productadapter.setProductList(list_products);
        }
    }

    @Override
    public void addToCart(final Product product) {
        boolean res = ModelController.getInstance().getModel().addToCart(getApplicationContext(),product,1, Integer.parseInt(product.getStore().getId()),-1,"");
        if(res == true) {
            Toast.makeText(MainActivity.this, getString(R.string.product_added_to_cart),
                    Toast.LENGTH_SHORT).show();
        }else{
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.alert_to_add_to_cart))
                    .setPositiveButton(R.string.alert_to_add_to_cart_sure, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            boolean res = ModelController.getInstance().getModel().addToCartReplace(getApplicationContext(),product,1, Integer.parseInt(product.getStore().getId()),-1,"");
                            if(res == true) {
                                Toast.makeText(MainActivity.this, getString(R.string.product_added_to_cart),
                                        Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(MainActivity.this, "حدث خطأ ما ... حاول مرة أخرى",
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
        Intent detailsActivty = new Intent(MainActivity.this, ProductDetailsActivity.class);
        detailsActivty.putExtra(DataExchangeParam.ARG_PRODUCT,product);
        detailsActivty.putExtra(DataExchangeParam.ARG_MARKET_ID,product.getStore().getId());
        startActivity(detailsActivty);
    }
}
