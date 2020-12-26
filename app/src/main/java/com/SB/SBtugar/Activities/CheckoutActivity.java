package com.SB.SBtugar.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.SB.SBtugar.AllModels.Billing;
import com.SB.SBtugar.AllModels.Model;
import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.RequestCreateCoupon;
import com.SB.SBtugar.AllModels.SuperMarket.SuperMarketsStatusResponse;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AllModels.UserUpdateRequest;
import com.SB.SBtugar.AllModels.listener.IOrderSubmitObserver;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.CartData.AppDatabase;
import com.SB.SBtugar.CartData.UserCart;
import com.SB.SBtugar.R;
import com.SB.SBtugar.SeverData.Constant;
import com.SB.SBtugar.SeverData.ServiceGenerator;
import com.SB.SBtugar.SeverData.StoreAPIInterface;
import com.SB.SBtugar.dataGrabber.UserUpdateSubmitter;
import com.bumptech.glide.Glide;
import com.google.api.client.util.Strings;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.room.Room;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener, IOrderSubmitObserver {
    UserCart cart ;
    String currentDate = "";
    double deliveryMoney = 0f;
    Dialog dialog;
    String note = "";
    String val_address = "";
    EditText customer_note;
    EditText promocode;
    TextView deliveryContact;
    private TextView placeOrder;
    TextView maddress ;
    TextView totalPrice;
    double total_is = 0f;
    ImageView leftImage,CenterImage,RightImage;
    ImageView leftSelected,CenterSelected,RightSelected;
    TextView TitlePay;
    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        // TODO Auto-generated method stub
        final Calendar calendar = Calendar.getInstance();


        currentDate = dayOfMonth+"/"+monthOfYear+"/"+year;

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);
        maddress = findViewById(R.id.maddress);
        leftImage = findViewById(R.id.leftImage);
        TitlePay = findViewById(R.id.TitlePay);
        CenterImage = findViewById(R.id.CenterImage);
        RightImage = findViewById(R.id.RightImage);
        leftSelected = findViewById(R.id.leftSelected);
        CenterSelected = findViewById(R.id.CenterSelected);
        RightSelected = findViewById(R.id.RightSelected);


        leftImage.setOnClickListener(view -> {
            if (AppController.isAtm == 1)
                selectedCash();
            else
                selectedMachine();
        });
        RightImage.setOnClickListener(view -> {
            if (AppController.isAtm == 0)
                selectedMachine();
            else
                selectedVISA();
        });
        leftSelected.setOnClickListener(view -> selectedCash());
        CenterSelected.setOnClickListener(view -> selectedMachine());
        RightSelected.setOnClickListener(view -> selectedVISA());

        cart = new UserCart(getApplicationContext());
         customer_note = findViewById(R.id.customer_note);
        totalPrice = findViewById(R.id.tv_checkout_total_price);
        ImageView imageShop = findViewById(R.id.shop_image);
        TextView delivery_money = findViewById(R.id.delivery_time);
        AppController.isAtm = -1;
        AppController.CartOrderShipping = cart.getMarketDeliveryFeesValue();
        deliveryMoney = Double.valueOf(cart.getMarketDeliveryFeesValue());
        Glide.with(this).load(cart.getMarketLogoValue()).into(imageShop);
        delivery_money.setText(cart.getMarketDeliveryDurationValue());
        UserData user = ModelController.getInstance().getModel().getUser();
        AppController.promocode_order = "";
        promocode = findViewById(R.id.promocode);
//        try_it = findViewById(R.id.try_it);

        promocode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (findViewById(R.id.try_it).getVisibility()== View.GONE) {
//                    findViewById(R.id.text_verify).setVisibility(View.GONE);
//                    findViewById(R.id.verified_image).setVisibility(View.GONE);
//                    findViewById(R.id.try_it).setVisibility(View.VISIBLE);
//                }

            }
        });

        UserCart cart = new UserCart(getApplicationContext());
        totalPrice.setText(String.format ("%.2f", Float.parseFloat(cart.getTotalPrice()+""))+"");
        total_is = cart.getTotalPrice();
        ((TextView) findViewById(R.id.market_id)).setText(cart.getMarketName());

        ImageView back = findViewById(R.id.back);
        if (back!=null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        DecimalFormat formatter = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.US);
        formatter.applyPattern("###.##");
        deliveryContact = findViewById(R.id.tv_delivery_contact);

        if ( ModelController.getInstance().getModel().getUser() != null &&  ModelController.getInstance().getModel().getUser().getBilling() != null &&  ModelController.getInstance().getModel().getUser().getBilling().getPhone() != null) {
            String mContact = ModelController.getInstance().getModel().getUser().getBilling().getPhone();
            deliveryContact.setText(mContact);
            maddress.setText(ModelController.getInstance().getModel().getUser().getBilling().getAddress1());
        }

        placeOrder = findViewById(R.id.btn_place_Order);
        placeOrder.setOnClickListener(this);

        selectedCash();

    }
    @Override
    public void onClick(View view) {
        if(view.getId()== R.id.btn_place_Order){

            note =  "\"" + customer_note.getText().toString() + "\"";
            Log.e("notenotenote",note);
            val_address = maddress.getText().toString();
            if (val_address.equals("")){
                val_address =  ModelController.getInstance().getModel().getUser().getBilling().getAddress1();
            }
            if(Strings.isNullOrEmpty(val_address) ){
                Toast.makeText(getApplicationContext(),
                        getString(R.string.error_empty_address), Toast.LENGTH_LONG).show();
                return;
            }

            if(Strings.isNullOrEmpty(deliveryContact.getText().toString()) ){
                Toast.makeText(getApplicationContext(),
                        getString(R.string.error_empty_contact), Toast.LENGTH_LONG).show();
                return;
            }
            if(!deliveryContact.getText().toString().startsWith("01") && !deliveryContact.getText().toString().startsWith("٠١")
                    && !deliveryContact.getText().toString().trim().startsWith("٠٦٦") && !deliveryContact.getText().toString().trim().startsWith("066")
                    && !deliveryContact.getText().toString().startsWith("+٢٠١") && !deliveryContact.getText().toString().startsWith("+201")){

                Toast.makeText(getApplicationContext(),
                        "ادخل رقم هاتف صحيح", Toast.LENGTH_LONG).show();
                return;
            }
            if((deliveryContact.getText().toString().trim().startsWith("٠١") || deliveryContact.getText().toString().trim().startsWith("01"))
                    && deliveryContact.getText().toString().trim().length() != 11){
                Toast.makeText(getApplicationContext(),
                        "ادخل رقم صحيح مكون من 11 رقم ", Toast.LENGTH_LONG).show();
                return;
            }
            if((deliveryContact.getText().toString().trim().startsWith("+٢٠١") || deliveryContact.getText().toString().trim().startsWith("+201"))
                    && deliveryContact.getText().toString().trim().length() != 13){
                Toast.makeText(getApplicationContext(),
                        "ادخل رقم صحيح ", Toast.LENGTH_LONG).show();
                return;
            }
            if(deliveryContact.getText().toString().trim().startsWith("٠٦٦") && deliveryContact.getText().toString().trim().length() != 10){
                Toast.makeText(getApplicationContext(),
                        "ادخل رقم منزلي صحيح مكون من 10 ارقام ", Toast.LENGTH_LONG).show();
                return;
            }


            String phonne = ModelController.getInstance().getModel().getUser().getBilling().getPhone();
            if (phonne==null|| phonne.equals("")){
                Log.e("PPPPHONE",phonne+"F");
                UserUpdateRequest updater = new UserUpdateRequest();
                Billing billingAdd = ModelController.getInstance().getModel().getUser().getBilling();
                billingAdd.setPhone(deliveryContact.getText().toString());
                updater.setBilling(billingAdd);
                Model model = Model.getInstance();
                UserUpdateSubmitter submitter = new UserUpdateSubmitter(updater,
                        model.getUser().getId().toString(), model.getWooCommerceUserToken());
                submitter.execute();
            }
            else {
            }

            if (AppController.isAtm == 2 && total_is!=0 ){
//                MakePayButtonRequest();
            }
            else {
                loadData();
            }
        }
    }

    @Override
    public void onOrderSubmitStatus(boolean result) {
        placeOrder.setEnabled(true);
        showCheckoutResult(result);
    }

    private void showCheckoutResult(boolean result){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.place_order));
        if(result){
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "CartDB").allowMainThreadQueries().build();
            db.TransactionsDao().deleteAll();
            UserCart cart = new UserCart(getApplicationContext());

            cart.setTotalPrice(0);

                // Set up the buttons
            builder.setMessage(getString(R.string.order_submitted_successfully)).setCancelable(false);
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                            AppDatabase.class, "CartDB").allowMainThreadQueries().build();
                    db.TransactionsDao().deleteAll();
                    UserCart cart = new UserCart(getApplicationContext());
                    cart.setTotalPrice(0);
                    Intent orderHistory = new Intent(CheckoutActivity.this, OrdersActivity.class);
                    startActivity(orderHistory);
                    finish();
                    String notify_id = String.valueOf((new Random()).nextInt(1000));

                    PendingIntent pendingIntent = PendingIntent.getActivity(CheckoutActivity.this, Integer.parseInt(notify_id), orderHistory,
                            PendingIntent.FLAG_ONE_SHOT);

                    String channelId = String.valueOf((new Random()).nextInt(1000));
                    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(CheckoutActivity.this, channelId)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("تم ارسال طلبك")
                                    .setContentText("تم وصول طلبك الي المتجر و سيتم توصيله وقتما يكون جاهز.")
                                    .setAutoCancel(true)
                                    .setSound(defaultSoundUri)
                                    .setChannelId(channelId)
                                    .setContentIntent(pendingIntent);

                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    // Since android Oreo notification channel is needed.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel(channelId,
                                "Channel human readable title",
                                NotificationManager.IMPORTANCE_HIGH);
                        assert notificationManager != null;
                        notificationManager.createNotificationChannel(channel);
                    }
                    assert notificationManager != null;
                    notificationManager.notify(Integer.parseInt(notify_id), notificationBuilder.build());
//                    setDialog(orderHistory);
                }
            });
        } else {
            builder.setMessage(getString(R.string.order_submitted_failed));
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
        }

        AlertDialog dialog = builder.create();
        dialog.show();

    }
    public void ExecutePromoExecute22(final String code){

        final StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL);
        final StoreAPIInterface storeServices22 = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        storeServices.GetCouponByCode(code)
                .enqueue(new Callback<List<RequestCreateCoupon>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<RequestCreateCoupon>> call,
                                           final @NonNull Response<List<RequestCreateCoupon>> response) {
                        if (response.body()==null){
                            ExecutePromoExecute22(code);
                            return;
                        }

                        final UserData users = ModelController.getInstance().getModel().getUser();
                        if (response.body().size() == 0)
                            ModelController.getInstance().submitOrder(getApplicationContext(),val_address, deliveryContact.getText().toString(), "", AppController.SelectedSuperMarket.getMarketDeliveryFees(), note);
                        else if (response.body().get(0).getUsage_limit() != -1 && response.body().get(0).getUsage_limit_per_user() != -1){
                            ModelController.getInstance().submitOrder(getApplicationContext(),val_address, deliveryContact.getText().toString(), "", AppController.SelectedSuperMarket.getMarketDeliveryFees(), note);
                        }
                        else if (response.body().get(0).getUsage_limit() != -1 && (response.body().get(0).getUsage_limit() <= response.body().get(0).getUsage_count()))
                            ModelController.getInstance().submitOrder(getApplicationContext(),val_address, deliveryContact.getText().toString(), "", AppController.SelectedSuperMarket.getMarketDeliveryFees(), note);
                        else if (response.body().get(0).getUsage_limit_per_user() == 1 && (response.body().get(0).getUsed_by().contains(users.getId())))
                            ModelController.getInstance().submitOrder(getApplicationContext(),val_address, deliveryContact.getText().toString(), "", AppController.SelectedSuperMarket.getMarketDeliveryFees(), note);
                        else {
                            final RequestBody body = RequestBody.create(MediaType.parse("text/plain"), code);
                            final UserData user = ModelController.getInstance().getModel().getUser();
                            storeServices22.RemoveCodeFromUser(user.getId(), body)
                                    .enqueue(new Callback<UserData>() {
                                        @Override
                                        public void onResponse(@NonNull Call<UserData> call,
                                                               @NonNull Response<UserData> responses) {
                                            storeServices.DeleteCopounFromAll(response.body().get(0).getId())
                                                    .enqueue(new Callback<String>() {
                                                        @Override
                                                        public void onResponse(@NonNull Call<String> call,
                                                                               @NonNull Response<String> response) {

                                                        }

                                                        @Override
                                                        public void onFailure(@NonNull Call<String> call, Throwable t) {
                                                            Log.e("ModelController", "Failed to get super Markets Data", t);
                                                        }
                                                    });
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<UserData> call, Throwable t) {
                                            Log.e("ModelController", "Failed to get super Markets Data", t);
                                        }
                                    });
                            AppController.promocode_order = code;
                            if (response.body().get(0).isFree_shipping()) {
                                ModelController.getInstance().submitOrder(getApplicationContext(),val_address, deliveryContact.getText().toString(), "", "0", note);
                            }
                            else {
                                ModelController.getInstance().submitOrder(getApplicationContext(),val_address, deliveryContact.getText().toString(), "", AppController.SelectedSuperMarket.getMarketDeliveryFees(), note);
                            }

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<RequestCreateCoupon>> call, Throwable t) {
                        Log.e("ModelController", "Failed to get super Markets Data", t);
                    }
                });


    }

    public void ExecutePromoExecute(final String code){

        final StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL);
        final StoreAPIInterface storeServices22 = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        storeServices.GetCouponByCode(code)
                .enqueue(new Callback<List<RequestCreateCoupon>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<RequestCreateCoupon>> call,
                                           final @NonNull Response<List<RequestCreateCoupon>> response_is) {
                        final UserData users = ModelController.getInstance().getModel().getUser();
                        if (response_is.body()==null){
                            ExecutePromoExecute(code);
                            return;
                        }

                        if (response_is.body().size() == 0)
                            ModelController.getInstance().submitOrder(getApplicationContext(),val_address, deliveryContact.getText().toString(), "", AppController.CartOrderShipping, note);
                        else if (response_is.body().get(0).getUsage_limit() != -1 && response_is.body().get(0).getUsage_limit_per_user() != -1){
                            ModelController.getInstance().submitOrder(getApplicationContext(),val_address, deliveryContact.getText().toString(), "", AppController.CartOrderShipping, note);
                        }
                        else if (response_is.body().get(0).getUsage_limit() != -1 && (response_is.body().get(0).getUsage_limit() <= response_is.body().get(0).getUsage_count()))
                            ModelController.getInstance().submitOrder(getApplicationContext(),val_address, deliveryContact.getText().toString(), "", AppController.CartOrderShipping, note);
                        else if (response_is.body().get(0).getUsage_limit_per_user() == 1 && (response_is.body().get(0).getUsed_by().contains(users.getId())))
                            ModelController.getInstance().submitOrder(getApplicationContext(),val_address, deliveryContact.getText().toString(), "", AppController.CartOrderShipping, note);
                        else {
                            final RequestBody body = RequestBody.create(MediaType.parse("text/plain"), code);
                            final UserData user = ModelController.getInstance().getModel().getUser();

                            storeServices22.RemoveCodeFromUser(user.getId(), body)
                                    .enqueue(new Callback<UserData>() {
                                        @Override
                                        public void onResponse(@NonNull Call<UserData> call,
                                                               @NonNull Response<UserData> responses) {
//                                            storeServices.DeleteCopounFromAll(response_is.body().get(0).getId())
//                                                    .enqueue(new Callback<String>() {
//                                                        @Override
//                                                        public void onResponse(@NonNull Call<String> call,
//                                                                               @NonNull Response<String> response) {
//
//                                                        }
//
//                                                        @Override
//                                                        public void onFailure(@NonNull Call<String> call, Throwable t) {
//                                                            Log.e("ModelController", "Failed to get super Markets Data", t);
//                                                        }
//                                                    });
                                            AppController.promocode_order = code;
                                            if (response_is.body().get(0).isFree_shipping()) {
                                                ModelController.getInstance().submitOrder(getApplicationContext(),val_address, deliveryContact.getText().toString(), "", "0", note);
                                            }
                                            else {
                                                UserCart cart = new UserCart(getApplicationContext());

                                                cart.setTotalPrice(cart.getTotalPrice() - Double.parseDouble(response_is.body().get(0).getAmount()));
                                                ModelController.getInstance().submitOrder(getApplicationContext(),val_address, deliveryContact.getText().toString(), "", AppController.CartOrderShipping, note);
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<UserData> call, Throwable t) {
                                            Log.e("ModelController", "Failed to get super Markets Data", t);
                                        }
                                    });



                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<RequestCreateCoupon>> call, Throwable t) {
                        Log.e("ModelController", "Failed to get super Markets Data", t);
                    }
                });


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

    private void loadData(){
        placeOrder.setEnabled(false);
        ShowDialogView(this);
        StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        int data_id = 0;
        data_id = (new UserCart(getApplicationContext())).getMarketId();

        storeServices.getSuperMarketStatus(data_id+"")
                .enqueue(new Callback<SuperMarketsStatusResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SuperMarketsStatusResponse> call,
                                           @NonNull Response<SuperMarketsStatusResponse> response) {
                        if (response.body()==null){
                            loadData();
                            return;
                        }
                        if (response.body().getData().isOpen()) {
                            if (getIntent().getExtras().getStringArrayList("customer_note") != null) {
                                getEmptyProductOfMarket();
                            }
                            else {
                                if (promocode.getText().toString().trim().isEmpty())
                                    ModelController.getInstance().submitOrder(getApplicationContext(),val_address, deliveryContact.getText().toString(), "",  AppController.CartOrderShipping, note);
                                else
                                    ExecutePromoExecute(promocode.getText().toString().trim());
                            }
                        }
                        else {
                            if (response.body().getData().getOpeningTime().equals("null"))
                                response.body().getData().setOpeningTime("");
                            new AlertDialog.Builder(CheckoutActivity.this).setCancelable(false)
                                    .setTitle(R.string.title_closed_alert)
                                    .setMessage(getString(R.string.title_closed_alert_now)+" " +  getString(R.string.desc_closed_alert)+response.body().getData().getOpeningTime())
                                    .setPositiveButton(R.string.continue_closed_alert, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (getIntent().getExtras().getStringArrayList("customer_note") != null) {
                                                getEmptyProductOfMarket();

                                            }
                                            else {
                                                if (promocode.getText().toString().trim().isEmpty())
                                                    ModelController.getInstance().submitOrder(getApplicationContext(),val_address, deliveryContact.getText().toString(), "",  AppController.CartOrderShipping, note);
                                                else
                                                    ExecutePromoExecute(promocode.getText().toString().trim());
                                            }
                                            dialog.cancel();
                                        }
                                    })
                                    .setNegativeButton(R.string.cencel_closed_alert, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            CheckoutActivity.this.dialog.dismiss();
                                            placeOrder.setEnabled(true);
                                        }
                                    })
                                    .setIcon(R.drawable.danger)
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SuperMarketsStatusResponse> call, Throwable t) {
                        Log.e("ModelController", "Failed to get super Markets Data", t);
                    }
                });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ModelController.getInstance().attachToOrderSubmitObservers(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ModelController.getInstance().detachFromOrderSubmitObservers(this);
    }
    public void getEmptyProductOfMarket() {
        if (promocode.getText().toString().trim().isEmpty())
            ModelController.getInstance().submitOrder(getApplicationContext(),val_address, deliveryContact.getText().toString(), "", AppController.SelectedSuperMarket.getMarketDeliveryFees(), note);
        else
            ExecutePromoExecute22(promocode.getText().toString().trim());
    }


    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }


//    void MakePayButtonRequest() {
//        PayButton payButton = new PayButton(this);
//        payButton.setMerchantId("10253847133"); // Merchant id
//        payButton.setTerminalId("57547386"); // Terminal  id
//        Double valueIs = total_is;
//        if (open_wallet.isChecked()&& !wallet.getText().toString().isEmpty()){
//            valueIs = valueIs - (int)(Float.parseFloat(wallet.getText().toString()));
//        }
//        payButton.setAmount(valueIs); // Amount
//        payButton.setCurrencyCode(818); // Currency Code
//        payButton.setMerchantSecureHash("37353137326166332D326561322D343665652D383461612D353630383335653231396638");
//        payButton.setTransactionReferenceNumber(AppUtils.generateRandomNumber());
//        payButton.setProductionStatus(AllURLsStatus.PRODUCTION);
//        payButton.createTransaction(new PayButton.PaymentTransactionCallback() {
//            @Override
//            public void onCardTransactionSuccess(SuccessfulCardTransaction cardTransaction) {
//                loadData();
//                Toast.makeText(getApplicationContext(), R.string.transactiondone, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onWalletTransactionSuccess(SuccessfulWalletTransaction walletTransaction) {
//                loadData();
//                Toast.makeText(getApplicationContext(), R.string.transactiondone, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onError(TransactionException error) {
//                Toast.makeText(getApplicationContext(), R.string.failed_trans, Toast.LENGTH_LONG).show();
//            }
//        });
//    }

    void selectedCash() {
        AppController.isAtm = 0;
        RightImage.setVisibility(View.VISIBLE);
        leftImage.setVisibility(View.INVISIBLE);
        CenterImage.setImageResource(R.drawable.cash_icon);
        RightImage.setImageResource(R.drawable.visadelivery);

        leftSelected.setImageResource(R.drawable.green_icon);
        CenterSelected.setImageResource(R.drawable.gray_icon);
        RightSelected.setImageResource(R.drawable.gray_icon);

        TitlePay.setText("كاش");

    }
    void selectedMachine() {
        AppController.isAtm = 1;
        RightImage.setVisibility(View.VISIBLE);
        leftImage.setVisibility(View.VISIBLE);
        leftImage.setImageResource(R.drawable.cash_icon);
        CenterImage.setImageResource(R.drawable.visadelivery);
        RightImage.setImageResource(R.drawable.visa_icon);

        leftSelected.setImageResource(R.drawable.gray_icon);
        CenterSelected.setImageResource(R.drawable.green_icon);
        RightSelected.setImageResource(R.drawable.gray_icon);

        TitlePay.setText("بالمكينة عند الاستلام");

    }
    void selectedVISA() {
        RightImage.setVisibility(View.INVISIBLE);
        leftImage.setVisibility(View.VISIBLE);
        leftImage.setImageResource(R.drawable.visadelivery);
        CenterImage.setImageResource(R.drawable.visa_icon);

        leftSelected.setImageResource(R.drawable.gray_icon);
        CenterSelected.setImageResource(R.drawable.gray_icon);
        RightSelected.setImageResource(R.drawable.green_icon);

        TitlePay.setText("فيزا");
        AppController.isAtm = 2;

    }

}
