package com.SB.SBtugar.Activities;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.TextView;

import com.SB.SBtugar.Adapters.OrderRecyclerViewAdapter;
import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.Orders.OrderResponse;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AllModels.listener.IOrderListObserver;
import com.SB.SBtugar.AllModels.listener.IUserDataObserver;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.R;
import com.SB.SBtugar.listener.MyFirebaseInstanceIDService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;

public class OrdersActivity extends AppCompatActivity implements IOrderListObserver,
        SwipeRefreshLayout.OnRefreshListener , IUserDataObserver {
    boolean isLast = false;
    private static final int RC_SIGN_IN = 123;
    boolean mIsLoading = false;
    int paging_position = 1;
    private OrderRecyclerViewAdapter mOrdersAdapter;
    ArrayList list_orders ;

    ImageView menuImg;
    CircleImageView userImg;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        findViewById(R.id.backcontainer).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.title)).setText(getResources().getString(R.string.oldorders));
        findViewById(R.id.back).setOnClickListener(view -> finish());
        menuImg = findViewById(R.id.menuImg);
        userImg = findViewById(R.id.userImg);
        menuImg.setOnClickListener(view -> takeScreenshot());
        userImg.setOnClickListener(view -> {
            UserData main_user = ModelController.getInstance().getModel().getUser();
            if (main_user != null)
                startActivity(new Intent(OrdersActivity.this,ProfileFragment.class));
            else
                openAuthenticationDialog();
        });
        list_orders = new ArrayList();
        mOrdersAdapter = new OrderRecyclerViewAdapter(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView orderList = findViewById(R.id.list_offers);
        orderList.setLayoutManager(layoutManager);
        orderList.setAdapter(mOrdersAdapter);

        orderList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mIsLoading)
                    return;
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if ((pastVisibleItems + visibleItemCount >= totalItemCount) && !isLast && !mIsLoading ) {
                    dialog.show();
                    ModelController.getInstance().requestOrderList(paging_position);
                    mIsLoading = true;
                    //End of list
                }
            }
        });
        ShowDialogView(this);
        ModelController.getInstance().requestOrderList(paging_position);
    }
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ModelController.getInstance().attachToOrderListObservers(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ModelController.getInstance().detachFromOrderListObservers(this);
    }
    @Override
    public void onOrderListReady(List<OrderResponse> orders) {
        dialog.dismiss();
        paging_position += 1;

        if(orders!= null) {
            if (orders.size() == 0){
                isLast = true;
                return;
            }
            list_orders.addAll(orders);
            mOrdersAdapter.setOrderList(list_orders);
        }
        else {
            paging_position--;
            ModelController.getInstance().requestOrderList(paging_position);
        }
        mIsLoading = false;

    }

    @Override
    public void onRefresh() {
        paging_position=1;
        list_orders.clear();
        dialog.show();
        ModelController.getInstance().requestOrderList(paging_position);
    }
    @Override
    protected void onResume() {
        super.onResume();
        UserData main_user = ModelController.getInstance().getModel().getUser();
        if (main_user == null){
            userImg.setImageResource(R.drawable.avatar);
        }
        else {
            if (main_user.getProfilePicture() != null) {
                Glide.with(this).load(main_user.getProfilePicture()).into(userImg);
                if (main_user.getProfilePicture().equals("") || main_user.getProfilePicture().equals("null"))
                    userImg.setImageResource(R.drawable.avatar2);
            }
            else {
                userImg.setImageResource(R.drawable.avatar2);
            }
        }
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
                getTokenAndAuthenticate();
            } else {
            }
        }
    }
    private void getTokenAndAuthenticate(){

        //Check Internet Connection
        if(!Network.isConnected(this)){
            //TODO show Error Message
            return;
        }

        //Check if User Already Authenticated at Server
        UserData user = ModelController.getInstance().getModel().getUser();
        if(user != null){
            Glide.with(this).load(user.getAvatarUrl()).into(userImg);
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
    public void onDestroy() {
        super.onDestroy();
        ModelController.getInstance().detachFromUserDataObservers(this);
    }
    @Override
    public void onUserDataReady() {
        ModelController.getInstance().detachFromUserDataObservers(this);
        final MyFirebaseInstanceIDService service=new MyFirebaseInstanceIDService();
        service.onTokenRefresh();
        UserData user = ModelController.getInstance().getModel().getUser();
        if (user == null){
            userImg.setImageResource(R.drawable.avatar);
        }
        else {
            if (user.getProfilePicture() != null) {
                Glide.with(this).load(user.getProfilePicture()).into(userImg);
                if (user.getProfilePicture().equals("") || user.getProfilePicture().equals("null"))
                    userImg.setImageResource(R.drawable.avatar2);
            }
            else {
                userImg.setImageResource(R.drawable.avatar2);
            }
        }
    }
}
