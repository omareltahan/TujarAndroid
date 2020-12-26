package com.SB.SBtugar.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.SB.SBtugar.Adapters.OrderDetailsAdapter;
import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.Orders.OrderDetailsModel;
import com.SB.SBtugar.AllModels.Orders.OrderResponse;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.R;
import com.SB.SBtugar.UtilsFirebase.MessagesActivity;
import com.bumptech.glide.Glide;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderDetailsView extends AppCompatActivity {
    String ORDER_ON_DELIVERY = "out-for-delivery";
    String ORDER_COMPLETED = "completed";
    String ORDER_PROCESSING = "processing";
    String ORDER_REJECTED = "rejected";
    String ORDER_CANCELLED = "cancelled";
    String ORDER_FAILED = "failed";

    RecyclerView recycler;
    private Runnable mTickExecutor = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(mTickExecutor, 100);
        }
    };
    private Handler mHandler = new Handler();
    List<OrderDetailsModel> list_items;
    OrderDetailsAdapter adapter;
    TextView marketname;

    TextView total;
    TextView order_id;
    TextView time;
    OrderResponse model;
    CircleImageView marketLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_view);
        marketname= findViewById(R.id.market_name);
        marketLogo = findViewById(R.id.logo);
        order_id= findViewById(R.id.order_id);
        total= findViewById(R.id.total);
        time= findViewById(R.id.date);
        ImageView back = findViewById(R.id.back);
        if (back!=null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        findViewById(R.id.call).setOnClickListener(v -> isPhonePermissionWorksFine());
        findViewById(R.id.chat).setOnClickListener(v -> startActivity(new Intent(this, MessagesActivity.class)));

        model = AppController.order_global;
        Glide.with(this).load(model.getStore().getUrl()).into(marketLogo);
//        marketLogo.setBorderColor(Color.parseColor(model.getColor()));
        marketname.setText(model.getStore().getShop_name());
        order_id.setText(model.getId()+"");
        total.setText(replaceOfArabic (String.format ("%.2f", Float.parseFloat(model.getTotal()))));
        time.setText(model.getDateCreated().replace("T"," "));
        list_items = model.getLineItems();
        recycler = findViewById(R.id.list);
        recycler.setNestedScrollingEnabled(true);
        recycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(layoutManager2);
        adapter = new OrderDetailsAdapter(this,list_items);
        recycler.setAdapter(adapter);

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
            if (ActivityCompat.checkSelfPermission(OrderDetailsView.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        }
    }

    String replaceOfArabic (String str){
        return str.replace("٠", "0").replace(",", ".")
                .replace("١", "1").replace("٢", "2").replace("٣", "3")
                .replace("٤", "4").replace("٥", "5").replace("٦", "6")
                .replace("٧", "7").replace("٨", "8").replace("٩", "9");
    }
}
