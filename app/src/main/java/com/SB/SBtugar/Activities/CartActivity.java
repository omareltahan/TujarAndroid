package com.SB.SBtugar.Activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.SB.SBtugar.Adapters.CartRecyclerViewAdapter;
import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.Product;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AllModels.listener.IUserDataObserver;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.CartData.AppDatabase;
import com.SB.SBtugar.CartData.CartItem;
import com.SB.SBtugar.CartData.UserCart;
import com.SB.SBtugar.R;
import com.SB.SBtugar.SeverData.Constant;
import com.SB.SBtugar.SeverData.ServiceGenerator;
import com.SB.SBtugar.SeverData.StoreAPIInterface;
import com.SB.SBtugar.listener.ICartListListener;
import com.SB.SBtugar.listener.IProductListListener;
import com.SB.SBtugar.listener.MyFirebaseInstanceIDService;
import com.SB.SBtugar.utils.DataExchangeParam;
import com.SB.SBtugar.utils.Network;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import de.hdodenhof.circleimageview.CircleImageView;

public class CartActivity extends AppCompatActivity implements View.OnClickListener, ICartListListener, IUserDataObserver, IProductListListener {

    UserCart carGlobal;
    private CartRecyclerViewAdapter mCartAdapter;
    private TextView totalPrice;
    private int marketId;
    private static final int RC_SIGN_IN = 123;

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
        startActivity(new Intent(this,DrawerPageView.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_page);
        menuImg = findViewById(R.id.menuImg);
        menuImg.setOnClickListener(view -> takeScreenshot());
        mCartAdapter = new CartRecyclerViewAdapter();
        carGlobal = new UserCart(CartActivity.this);
        ImageView back = findViewById(R.id.back);
        if (back!=null) {
            back.setOnClickListener(v -> finish());
        }
        mCartAdapter.setGlideManager(Glide.with(CartActivity.this));
        mCartAdapter.setListListener(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CartActivity.this);

        RecyclerView cartItemList = findViewById(R.id.list);

        cartItemList.setLayoutManager(layoutManager);
        cartItemList.setAdapter(mCartAdapter);

        totalPrice = findViewById(R.id.total_price);
        AppDatabase db = Room.databaseBuilder(CartActivity.this,
                AppDatabase.class, "CartDB").allowMainThreadQueries().build();
        List<CartItem> items= db.TransactionsDao().getAll();
        UserCart cart = new UserCart(CartActivity.this);


        totalPrice.setText(replaceOfArabic(String.format ("%.2f", Float.parseFloat(Double.toString(cart.getTotalPrice())))+getResources().getString(R.string.egp)));
        mCartAdapter.setCartItems(items);

        if (items.size() == 0 || cart.getMarketDeliveryFeesValue().equals("") ){
            ((TextView) findViewById(R.id.total_price)).setText("00.00" + getResources().getString(R.string.egp));
            ((TextView) findViewById(R.id.tv_cart_delivery_price)).setText("00.00" + getResources().getString(R.string.egp));
        }
        else {
            ((TextView) findViewById(R.id.total_price)).setText(replaceOfArabic(String.format("%.2f", Float.parseFloat(Double.toString(cart.getTotalPrice() -
                    Double.parseDouble(cart.getMarketDeliveryFeesValue())))) + getResources().getString(R.string.egp)));
            ((TextView) findViewById(R.id.tv_cart_delivery_price)).setText(replaceOfArabic(String.format("%.2f",
                    Float.parseFloat(cart.getMarketDeliveryFeesValue())) + getResources().getString(R.string.egp)));
        }
        Button checkout = findViewById(R.id.btn_checkout);
        checkout.setOnClickListener(this);




    }

    @Override
    public void onResume() {
        super.onResume();
        //Market Information
        AppDatabase db = Room.databaseBuilder(CartActivity.this,
                AppDatabase.class, "CartDB").allowMainThreadQueries().build();
        List<CartItem> items= db.TransactionsDao().getAll();
        UserCart cart = new UserCart(CartActivity.this);
        totalPrice.setText(replaceOfArabic(String.format ("%.2f", Float.parseFloat(Double.toString(cart.getTotalPrice())))+getResources().getString(R.string.egp)));
        mCartAdapter.setCartItems(items);
        if (items.size() == 0 ){
            ((TextView) findViewById(R.id.total_price)).setText("00.00" + getResources().getString(R.string.egp));
            ((TextView) findViewById(R.id.tv_cart_delivery_price)).setText("00.00" + getResources().getString(R.string.egp));
        }
        else {
            ((TextView) findViewById(R.id.total_price)).setText(replaceOfArabic(String.format("%.2f", Float.parseFloat(Double.toString(cart.getTotalPrice() -
                    Double.parseDouble(cart.getMarketDeliveryFeesValue())))) + getResources().getString(R.string.egp)));
            ((TextView) findViewById(R.id.tv_cart_delivery_price)).setText(replaceOfArabic(String.format("%.2f",
                    Float.parseFloat(cart.getMarketDeliveryFeesValue())) + getResources().getString(R.string.egp)));
        }
        marketId = cart.getMarketId();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_checkout) {
            AppDatabase db = Room.databaseBuilder(CartActivity.this,
                    AppDatabase.class, "CartDB").allowMainThreadQueries().build();
            List<CartItem> items= db.TransactionsDao().getAll();
            UserCart cart = new UserCart(CartActivity.this);

            if (items.size() == 0) {
                return;
            }
            double minOrder = Double.parseDouble(cart.getMarketMinOrderValue());
            if (cart.getTotalPrice() < minOrder) {

                Toast.makeText(CartActivity.this, R.string.min_order_error,
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (items.size() > 0) {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = auth.getCurrentUser();

                ModelController.getInstance().attachToUserDataObservers(this);

                if (currentUser != null) {
                    getTokenAndAuthenticate();

                } else {
                    //check Logged in User
                    openAuthenticationDialog();
                }


            } else {
                Toast.makeText(CartActivity.this, getString(R.string.cart_empty),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRemoveItem(CartItem item) {
        AppDatabase db = Room.databaseBuilder(CartActivity.this,
                AppDatabase.class, "CartDB").allowMainThreadQueries().build();
        db.TransactionsDao().delete(item.id,item.variation);
        UserCart cart = new UserCart(CartActivity.this);
        cart.setTotalPrice(cart.getTotalPrice() - item.Price_Is);
        List<CartItem> items= db.TransactionsDao().getAll();
        if (items.size()==0){
            cart.setTotalPrice(0);
        }
        mCartAdapter.setCartItems(items);
        totalPrice.setText(replaceOfArabic(String.format ("%.2f", Float.parseFloat(Double.toString(cart.getTotalPrice())))+getResources().getString(R.string.egp)));
        if (items.size() == 0 ){
            ((TextView) findViewById(R.id.total_price)).setText("00.00" + getResources().getString(R.string.egp));
            ((TextView) findViewById(R.id.tv_cart_delivery_price)).setText("00.00" + getResources().getString(R.string.egp));
        }
        else {
            ((TextView) findViewById(R.id.total_price)).setText(replaceOfArabic(String.format("%.2f", Float.parseFloat(Double.toString(cart.getTotalPrice() -
                    Double.parseDouble(cart.getMarketDeliveryFeesValue())))) + getResources().getString(R.string.egp)));
            ((TextView) findViewById(R.id.tv_cart_delivery_price)).setText(replaceOfArabic(String.format("%.2f",
                    Float.parseFloat(cart.getMarketDeliveryFeesValue())) + getResources().getString(R.string.egp)));
        }
    }

    @Override
    public void onQuantityChange(CartItem item, int value,boolean isPlus) {
        AppDatabase db = Room.databaseBuilder(CartActivity.this,
                AppDatabase.class, "CartDB").allowMainThreadQueries().build();
        db.TransactionsDao().UpdateCartQuantity(value,item.id,item.variation);
        UserCart cart = new UserCart(CartActivity.this);
        if (isPlus)
            cart.setTotalPrice(cart.getTotalPrice() + item.Price_Is);
        else
            cart.setTotalPrice(cart.getTotalPrice() - item.Price_Is);
        totalPrice.setText(replaceOfArabic(String.format ("%.2f", Float.parseFloat(Double.toString(cart.getTotalPrice())))+getResources().getString(R.string.egp)));
        ((TextView) findViewById(R.id.total_price)).setText(replaceOfArabic(String.format("%.2f", Float.parseFloat(Double.toString(cart.getTotalPrice() -
                Double.parseDouble(cart.getMarketDeliveryFeesValue())))) + getResources().getString(R.string.egp)));
        ((TextView) findViewById(R.id.tv_cart_delivery_price)).setText(replaceOfArabic(String.format("%.2f",
                Float.parseFloat(cart.getMarketDeliveryFeesValue())) + getResources().getString(R.string.egp)));

    }









    @Override
    public void onDestroy() {
        super.onDestroy();
        ModelController.getInstance().detachFromUserDataObservers(this);
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
        if (requestCode == RC_SIGN_IN) {


            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == -1) {
                Button checkout = findViewById(R.id.btn_checkout);
                checkout.setEnabled(false);
                // Successfully signed in
                getTokenAndAuthenticate();
                // ...
            } else {
                // Sign in failed, check response for error code
                // ...
            }
        }
    }

    private void getTokenAndAuthenticate(){

        //Check Internet Connection
        if(!Network.isConnected(CartActivity.this)){
            //TODO show Error Message
            return;
        }

        //Check if User Already Authenticated at Server
        UserData user = ModelController.getInstance().getModel().getUser();
        if(user != null){
            showCheckoutActvity();
            return;
        }

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
                            // Send token to your backend via HTTPS
                            authenticateUser(idToken);
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });
    }
    private void authenticateUser(String token){
        ModelController.getInstance().requestUserData(token);

    }

    @Override
    public void onUserDataReady() {
        ModelController.getInstance().detachFromUserDataObservers(this);
        UserData user = ModelController.getInstance().getModel().getUser();
        final MyFirebaseInstanceIDService service=new MyFirebaseInstanceIDService();
        service.onTokenRefresh();
        showCheckoutActvity();
    }

    private void showCheckoutActvity(){
        Intent checkOutActivity = new Intent(CartActivity.this, CheckoutActivity.class);
        checkOutActivity.putExtra("shipping", carGlobal.getMarketDeliveryFeesValue());
        startActivity(checkOutActivity);
        finish();
        Button checkout = findViewById(R.id.btn_checkout);
        checkout.setEnabled(true);

    }
    public class OffersGrabber extends AsyncTask<Void, Void, List<Product>> {

        int marketId;
        public OffersGrabber(int marketId){
            this.marketId = marketId;

        }
        @Override
        protected List<Product> doInBackground(Void... voids) {
            List<Product> products = new ArrayList<>();
            final StoreAPIInterface storeServices = ServiceGenerator.createService(
                    StoreAPIInterface.class, Constant.BASE_URL);
            try {
                products = storeServices.getFeaturedProducts(String.valueOf(marketId),true,100,"publish").execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return products;
        }

        @Override
        protected void onPostExecute(List<Product> products) {
            super.onPostExecute(products);
        }
    }
    CartItem ConvertProductToCart(Product product,int count,int variationId){
        CartItem item = new CartItem();
        item.quantity = count;
        item.Price_Is = Float.valueOf(product.getPrice());
        item.description = product.getDescription();
        item.id = product.getId();
        String imageUrl = AppController.SelectedSuperMarket.getMarketLogo();
        if(product.getMainImage() != null && !product.getMainImage().isEmpty()){
            imageUrl = product.getMainImage();
        }else if(product.getImages()!=null) {
            if (product.getImages().size()==0) {
                imageUrl  = AppController.SelectedSuperMarket.getMarketLogo();
            }
            else{
                imageUrl = product.getImages().get(0).getSrc();
            }
        }
        item.Image = imageUrl;
        item.name = product.getName();
        item.price = product.getPrice();
        item.regularPrice = product.getRegularPrice();
        item.salePrice = product.getSalePrice();
        item.shortDescription = product.getShortDescription();
        item.type = product.getType();
        item.variation = variationId;
        item.Variation_txt = "";
    return item;
    }
    @Override
    public void addToCart(Product product) {
        if (product.getType().equals("variable")){
            Intent detailsActivty = new Intent(CartActivity.this, ProductDetailsActivity.class);
            detailsActivty.putExtra(DataExchangeParam.ARG_PRODUCT,product);
            detailsActivty.putExtra(DataExchangeParam.ARG_MARKET_ID,marketId);
            startActivity(detailsActivty);
        }
        else {
             ModelController.getInstance().getModel().addToCart(CartActivity.this,product,1,marketId,-1,"");
            CartItem itemCart = ConvertProductToCart(product, 1, -1);
            AppDatabase db = Room.databaseBuilder(CartActivity.this,
                    AppDatabase.class, "CartDB").allowMainThreadQueries().build();
            UserCart cart = new UserCart(CartActivity.this);
            totalPrice.setText(replaceOfArabic(String.format("%.2f", Float.parseFloat(Double.toString(cart.getTotalPrice())))+getResources().getString(R.string.egp)));

            List<CartItem> items = db.TransactionsDao().getAll();
            mCartAdapter.setCartItems(items);
            ((TextView) findViewById(R.id.total_price)).setText(replaceOfArabic(String.format("%.2f", Float.parseFloat(Double.toString(cart.getTotalPrice() -
                    Double.parseDouble(cart.getMarketDeliveryFeesValue())))) + getResources().getString(R.string.egp)));
            ((TextView) findViewById(R.id.tv_cart_delivery_price)).setText(replaceOfArabic(String.format("%.2f",
                    Float.parseFloat(cart.getMarketDeliveryFeesValue())) + getResources().getString(R.string.egp)));

        }

    }

    @Override
    public void showProductDetails(Product product) {
        if (product.getType().equals("variable")){
            Intent detailsActivty = new Intent(CartActivity.this, ProductDetailsActivity.class);
            detailsActivty.putExtra(DataExchangeParam.ARG_PRODUCT,product);
            detailsActivty.putExtra(DataExchangeParam.ARG_MARKET_ID,marketId);
            startActivity(detailsActivty);
        }
        else {
            ModelController.getInstance().getModel().addToCart(CartActivity.this,product,1,marketId,-1,"");
            AppDatabase db = Room.databaseBuilder(CartActivity.this,
                    AppDatabase.class, "CartDB").allowMainThreadQueries().build();
            UserCart cart = new UserCart(CartActivity.this);
            totalPrice.setText(replaceOfArabic(String.format("%.2f", Float.parseFloat(Double.toString(cart.getTotalPrice())))+getResources().getString(R.string.egp)));
            List<CartItem> items = db.TransactionsDao().getAll();
            mCartAdapter.setCartItems(items);
            ((TextView) findViewById(R.id.total_price)).setText(replaceOfArabic(String.format("%.2f", Float.parseFloat(Double.toString(cart.getTotalPrice() -
                    Double.parseDouble(cart.getMarketDeliveryFeesValue())))) + getResources().getString(R.string.egp)));
            ((TextView) findViewById(R.id.tv_cart_delivery_price)).setText(replaceOfArabic(String.format("%.2f",
                    Float.parseFloat(cart.getMarketDeliveryFeesValue())) + getResources().getString(R.string.egp)));

        }

    }
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";


    String replaceOfArabic (String str){
        return str.replace("٠", "0").replace(",", ".")
                .replace("١", "1").replace("٢", "2").replace("٣", "3")
                .replace("٤", "4").replace("٥", "5").replace("٦", "6")
                .replace("٧", "7").replace("٨", "8").replace("٩", "9");
    }
}
