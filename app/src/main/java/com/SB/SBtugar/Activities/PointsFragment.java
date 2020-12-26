package com.SB.SBtugar.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.PointsMain;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.R;
import com.SB.SBtugar.SeverData.Constant;
import com.SB.SBtugar.SeverData.ServiceGenerator;
import com.SB.SBtugar.SeverData.StoreAPIInterface;
import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PointsFragment extends AppCompatActivity {
    LinearLayout first;
    TextView total_points;
    LinearLayout second;
    LinearLayout third;
    int value_of_total=0;
    int replaced_points=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.points_tab);
        first=findViewById(R.id.firstrow);
        total_points=findViewById(R.id.total_points);
        second=findViewById(R.id.secondrow);
        third=findViewById(R.id.thirdrow);
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value_of_total >= 1000) {
                    replaced_points = 1000;
                    SetToServerWallet(20);
                }
                else {
                    MakeShowDialog("ليس لديك نقاط كافية للاستبدال","");
                }
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value_of_total >= 2500) {
                    replaced_points = 2500;
                    SetToServerWallet(50);
                }
                else {
                    MakeShowDialog("ليس لديك نقاط كافية للاستبدال","");
                }            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value_of_total >= 5000) {
                    replaced_points = 5000;
                    SetToServerWallet(100);
                }
                else {
                    MakeShowDialog("ليس لديك نقاط كافية للاستبدال","");
                }            }
        });
        getTotal();
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

    void SetToServerWallet(int total){
        dialog.show();
        ModelController.getInstance().getModel().getUser().setWallet(ModelController.getInstance().getModel().getUser().getWallet()+total);
        final StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        storeServices.AddWalletValue(ModelController.getInstance().getModel().getUser().getId(), total)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call,
                                           final @NonNull Response<String> response) {
                        dialog.dismiss();

                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, Throwable t) {
                        dialog.dismiss();
                    }
                });
        MakeShowDialog( "تم استبدال نقاطك باضافة المبلغ الى محفظتك","");
        value_of_total = value_of_total - replaced_points;
        total_points.setText(value_of_total+"");

    }

    public void getTotal() {
        ShowDialogView(this);
        StoreAPIInterface storeServices = ServiceGenerator.createService(
                StoreAPIInterface.class, Constant.BASE_URL_V2);
        UserData user = ModelController.getInstance().getModel().getUser();
        storeServices.getLoyalityPoints(""+user.getId())
                .enqueue(new Callback<PointsMain>() {
                    @Override
                    public void onResponse(@NonNull Call<PointsMain> call,
                                           @NonNull Response<PointsMain> response) {
                        dialog.dismiss();
                        total_points.setText(response.body().getData().get(0).getPoints() + "");
                        value_of_total = response.body().getData().get(0).getPoints();
                    }

                    @Override
                    public void onFailure(@NonNull Call<PointsMain> call, Throwable t) {
                        dialog.dismiss();
                    }
                });
    }
    void MakeShowDialog(String title , String desc){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(desc);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}
