
package com.SB.SBtugar.Activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.SB.SBtugar.Adapters.BottomImagesProductDetails;
import com.SB.SBtugar.Adapters.ImagesProductDetails;
import com.SB.SBtugar.Adapters.Variation2Adapter;
import com.SB.SBtugar.Adapters.VariationAdapter;
import com.SB.SBtugar.AllModels.Image;
import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.Product;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AllModels.VariationModel;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.CartData.AppDatabase;
import com.SB.SBtugar.R;
import com.SB.SBtugar.SeverData.Constant;
import com.SB.SBtugar.SeverData.ServiceGenerator;
import com.SB.SBtugar.SeverData.StoreAPIInterface;
import com.SB.SBtugar.listener.listen_to_variation;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener , listen_to_variation {
    String UrlOfImage = "";
    TextView productPrice;
    Dialog dialog;
    private static final String ARG_PRODUCT = "Product";
    private static final String ARG_MARKET_ID = "MARKET_ID";
    private Product product;
    private int marketId;
    private ImageView Minus;
    private ImageView Plus;
    private TextView quantity;
    ArrayList<String> titles;
    private Variation2Adapter adapter;
    private ImagesProductDetails adapterImages;
    private BottomImagesProductDetails adapterImagesBottom;
    private RecyclerView recycler_variation;
    int variationselected = -1;
    int variationselectedPosition = -1;
    private List<VariationModel> list, list2;
    private TextView productOldPrice;
    private TextView percent;
    private RelativeLayout offer_container;
    ArrayList<Integer> allSelected;

    public ProductDetailsActivity() {
    }

    TextView code,market;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_inside);
        findViewById(R.id.cart).setOnClickListener(view -> startActivity(new Intent(ProductDetailsActivity.this,CartActivity.class)));

        market = findViewById(R.id.market);
        code = findViewById(R.id.code);
        offer_container = findViewById(R.id.offer_container);
        productOldPrice = findViewById(R.id.tv_product_price_old);
        percent = findViewById(R.id.percent);

        product = (Product) getIntent().getExtras().getSerializable(ARG_PRODUCT);

        Log.e("productproduct",new Gson().toJson(product));
        findViewById(R.id.chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserData main_user = ModelController.getInstance().getModel().getUser();
                if (main_user == null){
                    Toast.makeText(ProductDetailsActivity.this,"عليك تسجيل الدخول أولا",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(ProductDetailsActivity.this, UserVendorChat.class);
                    Bundle bundle = new Bundle();
                    if (product.getStore().getId().equals("-1"))
                        bundle.putSerializable("ID", Integer.parseInt(product.getStore().getId()));
                    else
                        bundle.putSerializable("ID", AppController.SelectedSuperMarket.getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        if (product.getStore().getId().equals("-1")){
            market.setText(AppController.SelectedSuperMarket.getMarketName());
        }
        else {
            market.setText(product.getStore().getStoreName());
        }
        findViewById(R.id.call).setOnClickListener(v -> {
            String phoneVal = "";
            if (product.getStore().getId().equals("-1")){
                phoneVal = AppController.SelectedSuperMarket.getMarketPhone();
            }
            else {
                phoneVal =  product.getStore().getPhone();
            }
            if (phoneVal == null || phoneVal.equals(""))
                Toast.makeText(ProductDetailsActivity.this,"هذا المتجر ليس لديه رقم مسجل داخل التطبيق ",Toast.LENGTH_LONG).show();
            else if (!phoneVal.contains(",")){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phoneVal));

                startActivity(intent);
            }
            else {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneVal.split(",")[0]));
                startActivity(intent);
            }
        });

        code.setText(product.getCode());
        marketId = getIntent().getExtras().getInt(ARG_MARKET_ID);
        if (marketId == 0)
            marketId = Integer.parseInt(product.getStore().getId());

        allSelected = new ArrayList<>();
        ImageView back = findViewById(R.id.back);
        if (back != null) {
            back.setOnClickListener(v -> finish());
        }
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        if (product.getPrice().isEmpty())
            AppController.product_price_variation = 0;
        else
            AppController.product_price_variation = Float.parseFloat(product.getPrice());
        if (product.getType() != null) {
            if (product.getType().equals("variable")) {
                for (int ii = 0; ii < product.getAttributes().size(); ii++) {
                    allSelected.add(0);
                }
                requestVariation(product.getId() + "");
            }
        }
        List<Image> newImagesList = product.getImages();
        for (int i = 0 ; i<product.getMeta_data().size();i++){
            if (product.getMeta_data().get(i).getKey().equals("_custom_product_images")){
                Log.e("metameta",product.getMeta_data().get(i).getValue().toString());
                ArrayList<String> listmodel = (ArrayList<String>)product.getMeta_data().get(i).getValue();
                for (int val = 0 ; val<listmodel.size();val++) {
                    Image image = new Image();
                    image.setSrc(listmodel.get(val));
                    newImagesList.add(image);
                }
            }
        }

        RecyclerView productImage = findViewById(R.id.list_images);
        productImage.setNestedScrollingEnabled(true);
        productImage.setHasFixedSize(true);
        final LinearLayoutManager layoutManager2 = new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        productImage.setLayoutManager(layoutManager2);
        adapterImages = new ImagesProductDetails(ProductDetailsActivity.this, newImagesList,product);
        productImage.setAdapter(adapterImages);


        final RecyclerView productImageBottom = findViewById(R.id.list_images_bottom);
        productImageBottom.setNestedScrollingEnabled(true);
        productImageBottom.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        productImageBottom.setLayoutManager(layoutManager);
        adapterImagesBottom = new BottomImagesProductDetails(ProductDetailsActivity.this, newImagesList.size(),0);
        productImageBottom.setAdapter(adapterImagesBottom);

        productImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                int visibleItemCount = layoutManager2.getChildCount();
//                int totalItemCount = layoutManager2.getItemCount();
                int pastVisibleItems = layoutManager2.findFirstVisibleItemPosition();
                adapterImagesBottom = new BottomImagesProductDetails(ProductDetailsActivity.this, newImagesList.size(),pastVisibleItems);
                productImageBottom.setAdapter(adapterImagesBottom);

            }
        });



        TextView productName = findViewById(R.id.tv_product_details_name);
        if (Build.VERSION.SDK_INT >= 24) {
            if (product.getName().contains("-")) {
                if (Locale.getDefault().getLanguage().equals("ar"))
                    productName.setText(Html.fromHtml(product.getName().split("-")[1], 0));
                else
                    productName.setText(Html.fromHtml(product.getName().split("-")[0], 0));
            } else {
                productName.setText(Html.fromHtml(product.getName(), 0));

            }
        } else {
            if (product.getName().contains("-")) {
                if (Locale.getDefault().getLanguage().equals("ar"))
                    productName.setText(HtmlCompat.fromHtml(product.getName().split("-")[1], HtmlCompat.FROM_HTML_MODE_LEGACY));
                else
                    productName.setText(HtmlCompat.fromHtml(product.getName().split("-")[1], HtmlCompat.FROM_HTML_MODE_LEGACY));
            } else {
                productName.setText(HtmlCompat.fromHtml(product.getName(), HtmlCompat.FROM_HTML_MODE_LEGACY));
            }
        }

        productPrice = findViewById(R.id.tv_product_details_price);
        String price = getString(R.string.price, product.getPrice());
        productPrice.setText(price);

        TextView productDetails = findViewById(R.id.tv_product_details);
        if (Build.VERSION.SDK_INT >= 24) {
            productDetails.setText(Html.fromHtml(product.getDescription(), 0));
        } else {
            productDetails.setText(HtmlCompat.fromHtml(product.getDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        }

        Button addToCart = findViewById(R.id.btn_add_to_cart);
        addToCart.setOnClickListener(this);


        quantity = findViewById(R.id.quantity);
        Minus = findViewById(R.id.minus);
        Plus = findViewById(R.id.plus);

        Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.parseInt(quantity.getText().toString()) - 1;
                if (value > 0) {
                    quantity.setText(value + "");
                    double prodQuantity = value * Double.parseDouble(product.getPrice());
                    String price = getString(R.string.price, String.valueOf(prodQuantity));
                    productPrice.setText(price);
                }
            }
        });

        Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.parseInt(quantity.getText().toString()) + 1;
                if (value < 20) {
                    quantity.setText(value + "");
                    double prodQuantity = value * Double.parseDouble(product.getPrice());
                    String price = getString(R.string.price, String.valueOf(prodQuantity));
                    productPrice.setText(price);
                }
            }
        });

        if(!product.getOnSale()){
            productOldPrice.setVisibility(View.GONE);
            offer_container.setVisibility(View.GONE);
        }else{
            String oldPrice = productOldPrice.getContext().getString(R.string.price,
                    product.getRegularPrice());
            productOldPrice.setText(oldPrice);
            productOldPrice.setVisibility(View.VISIBLE);
            offer_container.setVisibility(View.VISIBLE);
            int valOf = ((int) (Float.parseFloat(product.getSalePrice()) / Float.parseFloat(product.getPrice())))  * 100;
            percent.setText(valOf+"%");
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_add_to_cart) {
            if (list.size() > 0 && list.get(variationselectedPosition).getStock_status().equals("outofstock")) {
                Toast.makeText(ProductDetailsActivity.this, "اختر منتج متاح لاستكمال اضافه المنتج",
                        Toast.LENGTH_LONG).show();
                return;
            }

            //Get Quantity
            final int prodQuantity = Integer.parseInt(quantity.getText().toString());
            boolean res = ModelController.getInstance().getModel().addToCart(ProductDetailsActivity.this,product, prodQuantity, marketId, variationselected,UrlOfImage);
            if (res) {
                product = (Product) getIntent().getExtras().getSerializable(ARG_PRODUCT);
                Toast.makeText(ProductDetailsActivity.this, R.string.product_added_to_cart,
                        Toast.LENGTH_LONG).show();
                AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "CartDB").allowMainThreadQueries().build();
                ((TextView)findViewById(R.id.cart_count)).setText(db.TransactionsDao().getAll().size()+"");
                findViewById(R.id.cart_count).setVisibility(View.VISIBLE);
            } else {
                new AlertDialog.Builder(ProductDetailsActivity.this)
                        .setMessage(getString(R.string.alert_to_add_to_cart))
                        .setPositiveButton(R.string.alert_to_add_to_cart_sure, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                boolean res = ModelController.getInstance().getModel().addToCartReplace(ProductDetailsActivity.this,product, prodQuantity, marketId, variationselected,UrlOfImage);
                                if (res == true) {
                                    Toast.makeText(ProductDetailsActivity.this, getString(R.string.product_added_to_cart),
                                            Toast.LENGTH_LONG).show();
                                    AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                                            AppDatabase.class, "CartDB").allowMainThreadQueries().build();
                                    ((TextView)findViewById(R.id.cart_count)).setText(db.TransactionsDao().getAll().size()+"");
                                    findViewById(R.id.cart_count).setVisibility(View.VISIBLE);
                                } else {
                                    Toast.makeText(ProductDetailsActivity.this, "حدث خطأ ما ... حاول مرة أخرى",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.alert_to_add_to_cart_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(R.drawable.danger)
                        .show();
            }
        }
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
    Dialog newdialog;
    public void ShowImagesRecycler(int position){
        newdialog = new Dialog(ProductDetailsActivity.this,R.style.Theme_Dialog);
        newdialog.setCancelable(false);
        newdialog.setCanceledOnTouchOutside(false);
        newdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        newdialog.setContentView(R.layout.images_variation_list);
        RecyclerView recyclerView = newdialog.findViewById(R.id.recycler);
        ImageView close = newdialog.findViewById(R.id.close);
        close.setOnClickListener(view -> {
            newdialog.dismiss();
        });
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager2);
        VariationAdapter adapter = new VariationAdapter(ProductDetailsActivity.this, product.getAttributes().get(position).getVariations_images() , product.getAttributes().get(position).getOptions(), allSelected.get(position), ProductDetailsActivity.this, position);
        recyclerView.setAdapter(adapter);
        final Window dialogWindow = newdialog.getWindow();
        final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogWindowAttributes);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity =  Gravity.BOTTOM;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        newdialog.show();
    }

    @Override
    public void onVariationSelected(int position, int type,boolean fromRecycler) {
        if (newdialog!=null)
            newdialog.dismiss();
        allSelected.set(type, position);
        if (fromRecycler) {
            adapter = new Variation2Adapter(ProductDetailsActivity.this, titles, product.getAttributes(), allSelected, ProductDetailsActivity.this);
            recycler_variation.setAdapter(adapter);
        }
        selectedVariation();
    }

    @Override
    public void onImageSelected(int position) {
        ShowImagesRecycler(position);
    }

    int pages = 0;

    public void requestVariation(final String product_id) {
        pages++;
        ShowDialogView(this);
        StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL);
        storeServices.GetProductVariations(product_id, pages, 100)
                .enqueue(new Callback<List<VariationModel>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<VariationModel>> call,
                                           @NonNull Response<List<VariationModel>> response) {
                        dialog.dismiss();
                        if (response.body()==null) {
                            pages--;
                            requestVariation(product_id);
                            return;
                        }
                        list.addAll(response.body());
                        assert list != null;
                        list2.addAll(response.body());
                        if ((response.body().size() < 100 && response.body().size() > 0) || (response.body().size() == 0 && list.size() > 0)) {
                            titles = new ArrayList<>();
                            for (int i = 0; i < list.get(0).getAttributes().size(); i++) {
                                titles.add(list.get(0).getAttributes().get(i).getName());
                            }
                            recycler_variation = findViewById(R.id.list);
//                            recycler_variation.setNestedScrollingEnabled(true);
//                            recycler_variation.setHasFixedSize(true);
                            LinearLayoutManager layoutManager2 = new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                            recycler_variation.setLayoutManager(layoutManager2);
                            adapter = new Variation2Adapter(ProductDetailsActivity.this, titles, product.getAttributes(), allSelected, ProductDetailsActivity.this);
                            recycler_variation.setAdapter(adapter);
                            recycler_variation.setVisibility(View.VISIBLE);
                            selectedVariation();
                        } else if (response.body().size() == 100) {
                            requestVariation(product_id);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<VariationModel>> call, @NonNull Throwable t) {
                        dialog.dismiss();
                    }
                });
    }
    void selectedVariation() {
        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            int value = 0;
            String valueVariation = "";
            for (int j = 0; j < allSelected.size(); j++) {
                if (list.get(i).getAttributes().get(j).getOption().equals(product.getAttributes().get(j).getOptions().get(allSelected.get(j)))) {
                    if (product.getAttributes().get(j).getName().contains("model")) {
                        UrlOfImage = product.getAttributes().get(j).getVariations_images().get(allSelected.get(j));
                    }
                    if (!valueVariation.equals("")) {
                        valueVariation = valueVariation + ",";
                    }

                    String txt = list.get(i).getAttributes().get(j).getOption();
                    if (list.get(i).getAttributes().get(j).getOption().contains("-")) {
                        if (Locale.getDefault().getLanguage().equals("ar"))
                            txt = list.get(i).getAttributes().get(j).getOption().split("-")[1];
                        else
                            txt = list.get(i).getAttributes().get(j).getOption().split("-")[0];
                    }

                    valueVariation = valueVariation + txt;
                    value++;
                }
            }
            if (value == allSelected.size()) {
                AppController.variation_txt = valueVariation;
                position = i;
                break;
            }
        }

        if (position == -1)
            return;

        variationselected = list.get(position).getId();
        variationselectedPosition = position;
        list.clear();
        list.addAll(list2);
        list.get(position).setChecked(true);

        if (!list.get(position).getPrice().equals("")) {
            AppController.product_price_variation = Float.parseFloat(list.get(position).getPrice());
        } else {
            AppController.product_price_variation = 0;
        }
        product.setPrice(String.valueOf(AppController.product_price_variation));
        double prodQuantity = Integer.parseInt(quantity.getText().toString()) * Double.parseDouble(product.getPrice());
        String price = prodQuantity + " EGP";
            price = prodQuantity + " " + getResources().getString(R.string.pricess);
        productPrice.setText(price);
    }
    @Override
    protected void onResume() {
        super.onResume();
        AppDatabase db = Room.databaseBuilder(this,
                AppDatabase.class, "CartDB").allowMainThreadQueries().build();
        if (db.TransactionsDao().getAll().size()>0){
            ((TextView)findViewById(R.id.cart_count)).setText(db.TransactionsDao().getAll().size()+"");
            findViewById(R.id.cart_count).setVisibility(View.VISIBLE);
        }
        else {
            findViewById(R.id.cart_count).setVisibility(View.GONE);
        }
    }
}
