package com.SB.SBtugar.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import com.SB.SBtugar.Adapters.AllCategoriesOutsideAdapter;
import com.SB.SBtugar.Adapters.ProductRecyclerViewAdapter;
import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.Product;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AllModels.listener.IProductListObserver;
import com.SB.SBtugar.AllModels.listener.IUserDataObserver;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.R;
import com.SB.SBtugar.listener.IProductListListener;
import com.SB.SBtugar.listener.MyFirebaseInstanceIDService;
import com.SB.SBtugar.listener.RecyclerViewItemClickListener;
import com.SB.SBtugar.listener.listen_outside_category;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import static com.SB.SBtugar.utils.DataExchangeParam.ARG_MAIN_CAT;
import static com.SB.SBtugar.utils.DataExchangeParam.ARG_MAIN_CATALL;

public class AreaSelectorActivity extends AppCompatActivity implements listen_outside_category , IUserDataObserver  {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    ImageView menuImg;
    private static final int RC_SIGN_IN = 123;
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
            Log.e("menuImg","no message");
        } catch (Throwable e) {
            Log.e("menuImg",e.getMessage());
        }
    }
    void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        AppController.uri = Uri.fromFile(imageFile);
        startActivity(new Intent(AreaSelectorActivity.this,DrawerPageView.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_area);
        findViewById(R.id.topbar).setVisibility(View.VISIBLE);
        verifyStoragePermissions(this);

        menuImg = findViewById(R.id.menuImg);
        userImg = findViewById(R.id.userImg);
        menuImg.setOnClickListener(view -> takeScreenshot());
        userImg.setOnClickListener(view -> {
            UserData main_user = ModelController.getInstance().getModel().getUser();
            if (main_user != null)
                startActivity(new Intent(AreaSelectorActivity.this,ProfileFragment.class));
            else
                openAuthenticationDialog();
        });

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)) {
            // permissions already given
        } else {
            // request permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }


        final RecyclerView list = findViewById(R.id.list_offers);
        ViewTreeObserver vto = list.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                list.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                AllCategoriesOutsideAdapter mAdapter = new AllCategoriesOutsideAdapter(AreaSelectorActivity.this,ModelController.getInstance().getSupportedAreas().getData().getCategories(),
                        AreaSelectorActivity.this);
                LinearLayoutManager layoutManager = new LinearLayoutManager(AreaSelectorActivity.this,RecyclerView.VERTICAL,false);
                list.setLayoutManager(layoutManager);
                list.setAdapter(mAdapter);
            }
        });

    }

    @Override
    public void listen_outside_category(int pos) {
        Intent mainAct = new Intent(this, MainActivity.class);
        mainAct.putExtra(ARG_MAIN_CAT,pos);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_MAIN_CATALL,
                (Serializable) ModelController.getInstance().getSupportedAreas().getData().getCategories());
        mainAct.putExtras(bundle);
        startActivity(mainAct);
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
