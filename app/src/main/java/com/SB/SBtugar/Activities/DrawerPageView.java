package com.SB.SBtugar.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AllModels.listener.IUserDataObserver;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.R;
import com.SB.SBtugar.UtilsFirebase.MessagesActivity;
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
import java.util.Arrays;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class DrawerPageView extends AppCompatActivity implements IUserDataObserver {
    private static final int RC_SIGN_IN = 123;

    TextView user_name;
    ImageView user_image;
    TextView login_logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_view);
        findViewById(R.id.searchcontainer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DrawerPageView.this,SearchProducts.class));
            }
        });
        if (AppController.uri != null)
            ((ImageView)findViewById(R.id.screenshot)).setImageURI(AppController.uri);
        user_name = findViewById(R.id.user_name);
        login_logout = findViewById(R.id.login_logout);
        user_image = findViewById(R.id.user_image);

        ImageView back = findViewById(R.id.back);
        if (back!=null) {
            back.setOnClickListener(v -> finish());
        }
        user_image.setOnClickListener(v -> {
            UserData main_user = ModelController.getInstance().getModel().getUser();
            if (main_user != null)
                openActivity(ProfileFragment.class);
            else
                openAuthenticationDialog();
        });
        findViewById(R.id.containerimage).setOnClickListener(v -> finish());

        findViewById(R.id.Guids).setOnClickListener(v -> {
            Intent intent = new Intent(DrawerPageView.this,GuideOne.class);
            intent.putExtra("fromdrawer",true);
            startActivity(intent);
        });
        findViewById(R.id.today_container).setOnClickListener(v -> {
            Intent intent = new Intent(DrawerPageView.this,Today_Surprise_View.class);
            startActivity(intent);
        });
        findViewById(R.id.whatsapp).setOnClickListener(v -> openWhatsApp());
        findViewById(R.id.email).setOnClickListener(v -> openMail(""));


        findViewById(R.id.cart_container).setOnClickListener(v -> startActivity(new Intent(DrawerPageView.this,CartActivity.class)));
        findViewById(R.id.orders_container).setOnClickListener(v -> {
            UserData main_user = ModelController.getInstance().getModel().getUser();
            if (main_user == null){
                Toast.makeText(DrawerPageView.this,getString(R.string.loginfirstly),Toast.LENGTH_LONG).show();
                return;
            }
            startActivity(new Intent(DrawerPageView.this,OrdersActivity.class));}
            );
        findViewById(R.id.profile_container).setOnClickListener(v -> startActivity(new Intent(DrawerPageView.this,ProfileFragment.class)));
        findViewById(R.id.share_container).setOnClickListener(v -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, "\n "+"نزل التطبيق من هنا : "+"http://onelink.to/superbekala");
            startActivity(Intent.createChooser(sharingIntent, "Share Text!"));
        });
        findViewById(R.id.logout_container).setOnClickListener(v -> {
            UserData main_user = ModelController.getInstance().getModel().getUser();
            if (main_user == null){
                openAuthenticationDialog();
                return;
            }
            FirebaseAuth.getInstance().signOut();
            ModelController.getInstance().getModel().setUser(null);
            finish();
        });
        findViewById(R.id.message).setOnClickListener(v -> {
                    UserData main_user = ModelController.getInstance().getModel().getUser();
                    if (main_user == null) {
                        Toast.makeText(DrawerPageView.this, getString(R.string.loginfirstly), Toast.LENGTH_LONG).show();
                        return;
                    }
                    startActivity(new Intent(this, MessagesActivity.class));
                }
            );
        findViewById(R.id.call).setOnClickListener(v -> isPhonePermissionWorksFine());
        findViewById(R.id.fav_container).setOnClickListener(v -> {
            UserData main_user = ModelController.getInstance().getModel().getUser();
            if (main_user == null){
                Toast.makeText(DrawerPageView.this,getString(R.string.loginfirstly),Toast.LENGTH_LONG).show();
                return;
            }
            startActivity(new Intent(DrawerPageView.this,Favourite_View.class));
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        final UserData user = ModelController.getInstance().getModel().getUser();
        if (user == null) {
            login_logout.setText(getString(R.string.login));
            user_image.setImageResource(R.drawable.avatar);
            user_name.setText("تسجيل دخول");
            findViewById(R.id.logout_container).setVisibility(View.GONE);
        }
        else {
            user_name.setText(user.getFirstName() + " " + user.getLastName());
            if (user.getProfilePicture() != null) {
                Glide.with(this).load(user.getProfilePicture()).into(user_image);
                if (user.getProfilePicture().equals("") || user.getProfilePicture().equals("null"))
                    user_image.setImageResource(R.drawable.avatar2);
            }
            else {
                user_image.setImageResource(R.drawable.avatar2);
            }
            login_logout.setText(getString(R.string.logout));
        }
    }

    public void isPhonePermissionWorksFine() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    1011);
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1011);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:+201066672019"));
            if (ActivityCompat.checkSelfPermission(DrawerPageView.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        }
    }

    public void openActivity(Class val){
        startActivity(new Intent(this,val));
    }
    public void openWhatsApp() {
        Uri uri = Uri.parse("smsto:" + "201066672019");
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
        sendIntent.setPackage("com.whatsapp");
        startActivity(sendIntent);
    }
    public void openMail(String url) {
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse("mailto:" + url));
            intent.putExtra(Intent.EXTRA_SUBJECT, "superbekala");
            intent.putExtra(Intent.EXTRA_TEXT, "i have ask for : ");
            startActivity(intent);
        }catch(Exception e){
            //TODO smth
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
            user_name.setText(user.getFirstName() + " " + user.getLastName());
            Glide.with(this).load(user.getAvatarUrl()).into(user_image);
            findViewById(R.id.logout_container).setVisibility(View.VISIBLE);
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
        user_name.setText(user.getFirstName() + " " + user.getLastName());
        findViewById(R.id.logout_container).setVisibility(View.VISIBLE);
        if (user == null){
            user_image.setImageResource(R.drawable.avatar);
        }
        else {
            if (user.getProfilePicture() != null) {
                Glide.with(this).load(user.getProfilePicture()).into(user_image);
                if (user.getProfilePicture().equals("") || user.getProfilePicture().equals("null"))
                    user_image.setImageResource(R.drawable.avatar2);
            }
            else {
                user_image.setImageResource(R.drawable.avatar2);
            }
        }
    }
}
