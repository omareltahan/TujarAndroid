package com.SB.SBtugar.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.SB.SBtugar.Adapters.CategoryRecyclerViewAdapter;
import com.SB.SBtugar.Adapters.ProductRecyclerViewAdapter;
import com.SB.SBtugar.Adapters.RatingViewAdapter;
import com.SB.SBtugar.AllModels.Category;
import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.Product;
import com.SB.SBtugar.AllModels.RatingViewMainResponse;
import com.SB.SBtugar.AllModels.Rating_view;
import com.SB.SBtugar.AllModels.SuperMarket.SuperMarket;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AllModels.UserDataResponse;
import com.SB.SBtugar.AllModels.listener.IMarketCategoriesObserver;
import com.SB.SBtugar.AllModels.listener.IMarketDataObserver;
import com.SB.SBtugar.AllModels.listener.IProductListObserver;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.CartData.AppDatabase;
import com.SB.SBtugar.R;
import com.SB.SBtugar.SeverData.Constant;
import com.SB.SBtugar.SeverData.ServiceGenerator;
import com.SB.SBtugar.SeverData.StoreAPIInterface;
import com.SB.SBtugar.listener.IProductListListener;
import com.SB.SBtugar.listener.RecyclerViewItemClickListener;
import com.SB.SBtugar.utils.DataExchangeParam;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketDataView extends AppCompatActivity implements
        View.OnClickListener , IProductListListener, IMarketDataObserver, IMarketCategoriesObserver, RecyclerViewItemClickListener , IProductListObserver {
    private static final String ARG_MARKET_ID = "MARKET_ID";
    private ProductRecyclerViewAdapter productsRecycleViewAdapter;
    boolean isLast = false;
    boolean isRatingsRequestDone = false;
    boolean isOffersRequestDone = false;
    boolean mIsLoading = false;
    int paging_position = 1;
    RecyclerView categoryList;
    int selectedCat = 0;
    RecyclerView recycler2;
    List<Rating_view> list;
    RatingViewAdapter adapter;

    private int id_of_market = 0;
    private ProductRecyclerViewAdapter marketOfferRecyclerViewAdapter;


    private CategoryRecyclerViewAdapter mCategoryRecyclerViewAdapter;

    List<Product> list_products ;
    List<Category> list_categories;
    Dialog dialog;


    TextView address;
    TextView market_name;
    TextView phone;
    TextView openingtimes;
    SuperMarket mMarketData;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supermarket_details);
        findViewById(R.id.mainContainer).setBackgroundColor(Color.parseColor(AppController.selectedCategoyMain.getColor()));
        categoryList = findViewById(R.id.cat_list);
        findViewById(R.id.cart).setOnClickListener(view -> startActivity(new Intent(MarketDataView.this,CartActivity.class)));
        list_categories = new ArrayList<>();
        list_products = new ArrayList<>();
        if (getIntent().getExtras() != null) {
            id_of_market = getIntent().getExtras().getInt(ARG_MARKET_ID);
            ModelController.getInstance().requestMarketInformation(id_of_market);

        }

        productsRecycleViewAdapter = new ProductRecyclerViewAdapter(R.layout.product_item);
        marketOfferRecyclerViewAdapter = new ProductRecyclerViewAdapter(R.layout.product_item);
        mCategoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(this,list_categories,selectedCat);



        mMarketData = ModelController.getInstance().getModel().getMarket(id_of_market);
        if (mMarketData == null){
            mMarketData = new SuperMarket();
        }
        else if (mMarketData.getId() == -1) {
            ModelController.getInstance().getModel().setMarketItem( AppController.SelectedSuperMarket);
            mMarketData =  AppController.SelectedSuperMarket;
        }
        //Market Data
        TextView marketName = findViewById(R.id.tv_cat_market_name);
        marketName.setText(mMarketData.getMarketName());
        marketName.setTextColor(Color.parseColor(AppController.selectedCategoyMain.getColor()));


        TextView marketDesc = findViewById(R.id.desc);
        marketDesc.setText(mMarketData.getDesc());
        marketDesc.setTextColor(Color.parseColor(AppController.selectedCategoyMain.getColor()));

        CircleImageView marketLogo = findViewById(R.id.iv_cat_market_logo);
        Glide.with(MarketDataView.this).load(mMarketData.getMarketLogo()).into(marketLogo);


        TextView deliveryFee = findViewById(R.id.tv_cat_market_delivery_fee);
        String price =MarketDataView.this.getString(R.string.price, mMarketData.getMarketDeliveryFees());
        deliveryFee.setText(price);

        TextView minOrder = findViewById(R.id.tv_cat_market_min_order);
        price = getString(R.string.price, mMarketData.getMarketMinOrder());
        minOrder.setText(price);

        TextView deliveryTime = findViewById(R.id.tv_cat_market_delivery_time);
        String time = getString(R.string.time, mMarketData.getMarketDeliveryDuration());
        deliveryTime.setText(time);

        RatingBar rating = findViewById(R.id.rb_cat_market_rating);
        float ratingVal = 0;
        if(mMarketData.getMarketRating() != null && !mMarketData.getMarketRating().equals("No Ratings found yet") ){
            ratingVal = Float.parseFloat( mMarketData.getMarketRating());
        }
        rating.setRating(ratingVal);

        ImageView closeImage = findViewById(R.id.closeImage);
        if(!mMarketData.isOpen()){
            closeImage.setImageResource(R.drawable.close_image);
            marketLogo.setBorderColor(getResources().getColor(R.color.red_image));
        }
        else {
            closeImage.setImageResource(R.drawable.open_image);
            marketLogo.setBorderColor(getResources().getColor(R.color.green_image));
        }

        findViewById(R.id.rateit).setBackgroundColor(Color.parseColor(AppController.selectedCategoyMain.getColor()));
        findViewById(R.id.rateit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialog(String.valueOf(mMarketData.getId()), ModelController.getInstance().getModel().getUser().getId());
            }
        });
        if (mMarketData.isAccept_coupons())
            findViewById(R.id.coupon).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.coupon).setVisibility(View.INVISIBLE);

//        if (mMarketData.isAccept_chat())
//            findViewById(R.id.chat).setVisibility(View.VISIBLE);
//        else
//            findViewById(R.id.chat).setVisibility(View.INVISIBLE);
//
//        if (mMarketData.isCan_receive_calls())
//            findViewById(R.id.call).setVisibility(View.VISIBLE);
//        else
//            findViewById(R.id.call).setVisibility(View.INVISIBLE);
        findViewById(R.id.chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserData main_user = ModelController.getInstance().getModel().getUser();
                if (main_user == null){
                    Toast.makeText(MarketDataView.this,"عليك تسجيل الدخول أولا",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(MarketDataView.this, UserVendorChat.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", mMarketData);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        findViewById(R.id.call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] strings = mMarketData.getMarketPhone().split(",");
                if (!mMarketData.getMarketPhone().contains(",")){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+mMarketData.getMarketPhone()));
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + mMarketData.getMarketPhone().split(",")[0]));
                    startActivity(intent);
                }
            }
        });


        final ImageView Favourite = findViewById(R.id.favourite);
        if (mMarketData.isFavourite())
            Favourite.setImageResource(R.drawable.fav_icon);

        Favourite.setOnClickListener(v -> {
            UserData main_user = ModelController.getInstance().getModel().getUser();
            if (main_user == null)
                Toast.makeText(MarketDataView.this, "عليك تسجيل الدخول أولا", Toast.LENGTH_SHORT).show();
            else {
                if (!mMarketData.isFavourite()) {
                    StoreAPIInterface storeServices = ServiceGenerator.createService(
                            StoreAPIInterface.class, Constant.BASE_URL_V2);
                    RequestBody body = RequestBody.create(MediaType.parse("text/plain"), mMarketData.getId() + "");
                    UserData user = ModelController.getInstance().getModel().getUser();

                    storeServices.SetFavouriteToServer(user.getId() + "", body)
                            .enqueue(new Callback<UserDataResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<UserDataResponse> call,
                                                       @NonNull Response<UserDataResponse> response) {
                                    mMarketData.setFavourite(!mMarketData.isFavourite());
                                    MarketDataView.this.getSharedPreferences("superbekala", MODE_PRIVATE).edit().putBoolean(mMarketData.getId() + "", mMarketData.isFavourite()).apply();
                                    Favourite.setImageResource(R.drawable.fav_icon);
                                }
                                @Override
                                public void onFailure(@NonNull Call<UserDataResponse> call, Throwable t) {
                                    mMarketData.setFavourite(!mMarketData.isFavourite());
                                    MarketDataView.this.getSharedPreferences("superbekala", MODE_PRIVATE).edit().putBoolean(mMarketData.getId() + "", mMarketData.isFavourite()).apply();
                                    Favourite.setImageResource(R.drawable.fav_icon);                                    startActivity(new Intent(MarketDataView.this,Favourite_View.class));
                                }
                            });
                } else {
                    StoreAPIInterface storeServices = ServiceGenerator.createService(
                            StoreAPIInterface.class, Constant.BASE_URL_V2);
                    RequestBody body = RequestBody.create(MediaType.parse("text/plain"), mMarketData.getId() + "");
                    UserData user = ModelController.getInstance().getModel().getUser();
                    storeServices.RemoveFavouriteFromServer(user.getId() + "", body)
                            .enqueue(new Callback<UserDataResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<UserDataResponse> call,
                                                       @NonNull Response<UserDataResponse> response) {
                                    mMarketData.setFavourite(!mMarketData.isFavourite());
                                    MarketDataView.this.getSharedPreferences("superbekala", MODE_PRIVATE).edit().putBoolean(mMarketData.getId() + "", mMarketData.isFavourite()).apply();
                                    Favourite.setImageResource(R.drawable.love_white);
                                }

                                @Override
                                public void onFailure(@NonNull Call<UserDataResponse> call, Throwable t) {
                                    mMarketData.setFavourite(!mMarketData.isFavourite());
                                    MarketDataView.this.getSharedPreferences("superbekala", MODE_PRIVATE).edit().putBoolean(mMarketData.getId() + "", mMarketData.isFavourite()).apply();
                                    Favourite.setImageResource(R.drawable.love_white);
                                }
                            });
                }
            }
        });
        ShowDialogView(this);
        ModelController.getInstance().requestMarketCategories(id_of_market);


        findViewById(R.id.like_icon).setOnClickListener(v -> {
            ((ImageView)(findViewById(R.id.like_icon))).setImageResource(R.drawable.like_icon2);
            ((ImageView)(findViewById(R.id.menu_icon))).setImageResource(R.drawable.menu_icon2);
            ((ImageView)(findViewById(R.id.info_icon))).setImageResource(R.drawable.info_icon);
            ((ImageView)(findViewById(R.id.offers_icon))).setImageResource(R.drawable.copoun_icon);

            findViewById(R.id.offers_view).setVisibility(View.GONE);
            findViewById(R.id.rating_view).setVisibility(View.VISIBLE);
            findViewById(R.id.menu_view).setVisibility(View.GONE);
            findViewById(R.id.info_view).setVisibility(View.GONE);
            if (!isRatingsRequestDone) {
                requestMarketList();
            }
        });
        findViewById(R.id.offers_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ImageView)(findViewById(R.id.like_icon))).setImageResource(R.drawable.like_icon);
                ((ImageView)(findViewById(R.id.menu_icon))).setImageResource(R.drawable.menu_icon2);
                ((ImageView)(findViewById(R.id.info_icon))).setImageResource(R.drawable.info_icon);
                ((ImageView)(findViewById(R.id.offers_icon))).setImageResource(R.drawable.offers_icon);

                findViewById(R.id.offers_view).setVisibility(View.VISIBLE);
                findViewById(R.id.rating_view).setVisibility(View.GONE);
                findViewById(R.id.menu_view).setVisibility(View.GONE);
                findViewById(R.id.info_view).setVisibility(View.GONE);
                if (!isOffersRequestDone){
                    ModelController.getInstance().requestMarketInformation(id_of_market);
                }
            }
        });
        findViewById(R.id.menu_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ImageView)(findViewById(R.id.like_icon))).setImageResource(R.drawable.like_icon);
                ((ImageView)(findViewById(R.id.menu_icon))).setImageResource(R.drawable.menu_icon2);
                ((ImageView)(findViewById(R.id.info_icon))).setImageResource(R.drawable.info_icon);
                ((ImageView)(findViewById(R.id.offers_icon))).setImageResource(R.drawable.copoun_icon);                findViewById(R.id.offers_view).setVisibility(View.GONE);
                findViewById(R.id.rating_view).setVisibility(View.GONE);
                findViewById(R.id.menu_view).setVisibility(View.VISIBLE);
                findViewById(R.id.info_view).setVisibility(View.GONE);
            }
        });
        findViewById(R.id.info_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ImageView)(findViewById(R.id.like_icon))).setImageResource(R.drawable.like_icon);
                ((ImageView)(findViewById(R.id.menu_icon))).setImageResource(R.drawable.menu_icon2);
                ((ImageView)(findViewById(R.id.info_icon))).setImageResource(R.drawable.info_icon2);
                ((ImageView)(findViewById(R.id.offers_icon))).setImageResource(R.drawable.copoun_icon);
                findViewById(R.id.offers_view).setVisibility(View.GONE);
                findViewById(R.id.rating_view).setVisibility(View.GONE);
                findViewById(R.id.menu_view).setVisibility(View.GONE);
                findViewById(R.id.info_view).setVisibility(View.VISIBLE);
            }
        });

        ((TextView)findViewById(R.id.phone)).setText(mMarketData.getMarketPhone());
        findViewById(R.id.phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mMarketData.getMarketPhone()));
                startActivity(intent);
            }
        });

        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(MarketDataView.this,
                LinearLayoutManager.HORIZONTAL, false);
        categoryList.setLayoutManager(layoutManager4);
        categoryList.setAdapter(mCategoryRecyclerViewAdapter);
        ViewCompat.setNestedScrollingEnabled(categoryList, false);

        marketOfferRecyclerViewAdapter.setGlideManager(Glide.with(MarketDataView.this));


        // offers RecyclerView
        GridLayoutManager layoutManager = new GridLayoutManager(MarketDataView.this,
                2);
        RecyclerView offersList = findViewById(R.id.list_offers);
        offersList.setLayoutManager(layoutManager);
        offersList.setAdapter(marketOfferRecyclerViewAdapter);

        //Progress Bar
        if (mMarketData == null) {
            mMarketData = new SuperMarket();
        }
        if (mMarketData.isHasOnsaleProducts())
            findViewById(R.id.offers_icon).setVisibility(View.VISIBLE);

        marketOfferRecyclerViewAdapter.setProductList(mMarketData.getOffers());

        market_name = findViewById(R.id.market_name);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        openingtimes = findViewById(R.id.openingTimes);

        if (mMarketData.getMarketAddress()!=null) {
            address.setText(mMarketData.getMarketAddress().getCity() + " , " +
                    mMarketData.getMarketAddress().getStreet1() + " , " + mMarketData.getMarketAddress().getStreet2());
        }
        openingtimes.setText(mMarketData.getOpeningTime() + " - " + mMarketData.getClosingTime());
        market_name.setText(mMarketData.getMarketName());
        phone.setText(mMarketData.getMarketPhone());
        list=new ArrayList<>();
        recycler2 = findViewById(R.id.list);
        recycler2.setNestedScrollingEnabled(true);
        recycler2.setHasFixedSize(true);
        LinearLayoutManager layoutManager222 = new LinearLayoutManager(MarketDataView.this, LinearLayoutManager.VERTICAL, false);
        recycler2.setLayoutManager(layoutManager222);
        adapter = new RatingViewAdapter(MarketDataView.this,list);
        recycler2.setAdapter(adapter);


        final GridLayoutManager layoutManagerpro = new GridLayoutManager(this,2);
        RecyclerView productList = findViewById(R.id.list_list);
        productsRecycleViewAdapter.setGlideManager(Glide.with(this));
        productList.setLayoutManager(layoutManagerpro);
        productList.setAdapter(productsRecycleViewAdapter);


        productList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mIsLoading)
                    return;
                int visibleItemCount = layoutManagerpro.getChildCount();
                int totalItemCount = layoutManagerpro.getItemCount();
                int pastVisibleItems = layoutManagerpro.findFirstVisibleItemPosition();
                if ((pastVisibleItems + visibleItemCount >= totalItemCount) && !isLast && !mIsLoading && (list_products.size() == totalItemCount)) {
                    ModelController.getInstance().requestProductList(list_categories.get(selectedCat).getCatID(),mMarketData.getId(),paging_position);
                    mIsLoading = true;
                    //End of list
                }
            }
        });


//        checkIfHas();
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

    @Override
    public void addToCart(final Product product) {
        boolean res = ModelController.getInstance().getModel().addToCart(MarketDataView.this,product,1,mMarketData.getId(),-1,"");
        if(res == true) {
            Toast.makeText(MarketDataView.this, getString(R.string.product_added_to_cart),
                    Toast.LENGTH_SHORT).show();
        }else{
            new AlertDialog.Builder(MarketDataView.this)
                    .setMessage(getString(R.string.alert_to_add_to_cart))
                    .setPositiveButton(R.string.alert_to_add_to_cart_sure, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            boolean res = ModelController.getInstance().getModel().addToCartReplace(MarketDataView.this,product,1,mMarketData.getId(),-1,"");
                            if(res == true) {
                                Toast.makeText(MarketDataView.this, getString(R.string.product_added_to_cart),
                                        Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(MarketDataView.this, "حدث خطأ ما ... حاول مرة أخرى",
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

    @Override
    public void showProductDetails(Product product) {
        Intent detailsActivty = new Intent(MarketDataView.this, ProductDetailsActivity.class);
        detailsActivty.putExtra(DataExchangeParam.ARG_PRODUCT,product);
        detailsActivty.putExtra(DataExchangeParam.ARG_MARKET_ID,mMarketData.getId());
        startActivity(detailsActivty);
    }

    @Override
    public void onMarketDataReady() {
        dialog.dismiss();
        isOffersRequestDone = true;
        mMarketData = ModelController.getInstance().getModel().getMarket(id_of_market);
        marketOfferRecyclerViewAdapter.setProductList(mMarketData.getOffers());

    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        ModelController.getInstance().attachToMarketCategoriesObservers(this);
        ModelController.getInstance().attachToMarketDataObservers(this);
        try{
            marketOfferRecyclerViewAdapter.setProductListListener(this);
        }catch (ClassCastException e) {
            throw new ClassCastException(" must implement IProductListListener");
        }
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ModelController.getInstance().detachFromMarketDataObservers(this);
        ModelController.getInstance().detachFromMarketCategoriesObservers(this);
    }

    @Override
    public void onCategoriesListChanged() {
        SuperMarket market = ModelController.getInstance().getModel().getMarket(id_of_market);
        List<Category> categories = market.getCategories();
        List<Category> newcategories = market.getCategories();
        if (categories==null){
            ModelController.getInstance().requestMarketCategories(id_of_market);
            return;
        }
        for (int i=0;i<categories.size();i++){
            if(categories.get(i).getCatName().contains("uncategorized")){
                newcategories.remove(i);
                break;
            }
        }
        Log.e("currentUser","requestUserData"+categories.size());
        showCategories(newcategories);
    }

    private void showCategories(List<Category> categories) {
        if (categories.size() == 1 && categories.get(0).getSubCategories().size() > 0) {
            categories = categories.get(0).getSubCategories();
        }
        list_categories = categories;
        mCategoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(this,list_categories,selectedCat);
        mCategoryRecyclerViewAdapter.onItemClickListener(this);
        categoryList.setAdapter(mCategoryRecyclerViewAdapter);
        Log.e("valueRes: " , "here , " + list_categories.size());
        dialog.show();
        ModelController.getInstance().requestProductList(list_categories.get(selectedCat).getCatID(),id_of_market,paging_position);
    }

    @Override
    public void onItemClick(View view, int position) {
        paging_position = 1;
        selectedCat = position;
        mCategoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(this,list_categories,selectedCat);
        mCategoryRecyclerViewAdapter.onItemClickListener(this);
        categoryList.setAdapter(mCategoryRecyclerViewAdapter);

        ModelController.getInstance().requestProductList(list_categories.get(selectedCat).getCatID(),id_of_market,paging_position);
    }
    public void requestMarketList() {
        StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        storeServices.getRatingsOfShop(mMarketData.getId())
                .enqueue(new Callback<RatingViewMainResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RatingViewMainResponse> call,
                                           @NonNull Response<RatingViewMainResponse> response) {
                        if (response.body()==null || response.body().getData()==null ) {
                            requestMarketList();
                            return;
                        }
                        dialog.dismiss();
                        list = response.body().getData().getReviews();
                        adapter = new RatingViewAdapter(MarketDataView.this,list);
                        recycler2.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<RatingViewMainResponse> call, Throwable t) {
                        dialog.dismiss();
                        Log.e("ModelController", "Failed to get super Markets Data", t);
                    }
                });
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        ModelController.getInstance().attachToProductListObservers(this);
        try {
            productsRecycleViewAdapter.setProductListListener(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(" must implement IProductListListener");
        }
    }

    @Override
    public void onProductListReady(List<Product> productList) {
        dialog.dismiss();
        if (paging_position == 1)
            list_products.clear();
        paging_position += 1;
        if(productList!= null) {
            if (productList.size() == 0){
                isLast = true;
                mIsLoading = false;
                return;
            }
            list_products.addAll(productList);
            for (int vv = 0 ; vv<productList.size();vv++){
                if (productList.get(vv).getSlug().equals(mMarketData+"-empty")){
                    list_products.remove(productList.get(vv));
                    break;
                }

            }
            productsRecycleViewAdapter.setProductList(list_products);
        }
        mIsLoading = false;
    }
    public void checkIfHas() {
        final UserData user = ModelController.getInstance().getModel().getUser();

        StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        storeServices.getRatingsOfShopOfUser(mMarketData.getId(),user.getId())
                .enqueue(new Callback<RatingViewMainResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RatingViewMainResponse> call,
                                           @NonNull Response<RatingViewMainResponse> response) {
                        Log.e("valueRes: " , "here , is");
                        if (response.body()!= null && response.body().getData() != null && response.body().getData().getReviews()!=null) {
                            if (response.body().getData().getReviews().size() == 0)
                                findViewById(R.id.rateit).setVisibility(View.VISIBLE);
                        }
                        else {
                            findViewById(R.id.rateit).setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<RatingViewMainResponse> call, Throwable t) {
                        Log.e("ModelController", "Failed to get super Markets Data", t);
                    }
                });
    }

    private void setDialog(final String shop_id, final int user_id) {
        final Dialog dialog = new Dialog(this,R.style.Theme_Dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_rate);

        final RatingBar ratingBar = dialog.findViewById(R.id.rate);
        ratingBar.setRating(5);
        final TextView text = dialog.findViewById(R.id.text);
        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.findViewById(R.id.add_rate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((int)ratingBar.getRating()<4){
                    Toast.makeText(MarketDataView.this,"من فضلك اكتب تقييمك",Toast.LENGTH_LONG).show();
                    return;
                }


                dialog.show();

                Calendar mcurrentTime = Calendar.getInstance();

                final String date = mcurrentTime.get(Calendar.YEAR)+"/"+mcurrentTime.get(Calendar.MONTH)+"/"+mcurrentTime.get(Calendar.DAY_OF_MONTH);
                final String time = mcurrentTime.get(Calendar.HOUR_OF_DAY)+":"+mcurrentTime.get(Calendar.MINUTE);
                dialog.cancel();
                StoreAPIInterface storeServices = ServiceGenerator.createService(
                        StoreAPIInterface.class, Constant.BASE_URL_V2);
                storeServices.SetRatingsOfShopOfUser(Integer.parseInt(shop_id),(int)ratingBar.getRating(),
                        time,text.getText().toString(),date,user_id)
                        .enqueue(new Callback<RatingViewMainResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<RatingViewMainResponse> call,
                                                   @NonNull Response<RatingViewMainResponse> response) {
                                dialog.dismiss();
                                Rating_view item = new Rating_view();
                                item.setName(ModelController.getInstance().getModel().getUser().getFirstName());
                                item.setRate((int)ratingBar.getRating());
                                item.setText(text.getText().toString());
                                item.setTime(time);
                                item.setDate(date);
                                list.add(item);
                                adapter = new RatingViewAdapter(MarketDataView.this,list);
                                recycler2.setAdapter(adapter);
                                Toast.makeText(MarketDataView.this,"thanx for your review",Toast.LENGTH_LONG).show();
                                findViewById(R.id.rateit).setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(@NonNull Call<RatingViewMainResponse> call, Throwable t) {
                                dialog.dismiss();
                            }
                        });
            }
        });


        final Window dialogWindow = dialog.getWindow();
        final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();
        // Set fixed width (280dp) and WRAP_CONTENT height
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogWindowAttributes);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity =  Gravity.CENTER;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
        if (dialog!=null)
            dialog.show();
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
    public void onClick(View view) {
        finish();
    }
}