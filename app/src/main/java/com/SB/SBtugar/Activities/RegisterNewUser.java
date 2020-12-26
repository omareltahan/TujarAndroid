package com.SB.SBtugar.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.R;

public class RegisterNewUser extends AppCompatActivity {
    TextView marketaddress;
    TextView specialist;
    TextView username;
    TextView marketname;
    TextView marketphone;
    UserData mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        mUser = ModelController.getInstance().getModel().getUser();
        marketaddress = findViewById(R.id.marketaddress);
        specialist = findViewById(R.id.specialist);
        username = findViewById(R.id.username);
        marketname = findViewById(R.id.marketname);
        marketphone = findViewById(R.id.phone_number);


        for (int i = 0; i < mUser.getMeta_data().size(); i++) {
            if (mUser.getMeta_data().get(i).getKey().toLowerCase().equals("store_name".toLowerCase()))
                marketname.setText(String.valueOf(mUser.getMeta_data().get(i).getValue()));
            else if (mUser.getMeta_data().get(i).getKey().toLowerCase().equals("store_address".toLowerCase()))
                marketaddress.setText(String.valueOf(mUser.getMeta_data().get(i).getValue()));
            else if (mUser.getMeta_data().get(i).getKey().toLowerCase().equals("store_phone".toLowerCase()))
                marketphone.setText(String.valueOf(mUser.getMeta_data().get(i).getValue()));
            else if (mUser.getMeta_data().get(i).getKey().toLowerCase().equals("store_catogry".toLowerCase()))
                specialist.setText(String.valueOf(mUser.getMeta_data().get(i).getValue()));
        }
        username.setText(mUser.getFirstName() + " " + mUser.getLastName());


        findViewById(R.id.btn).setOnClickListener(view -> {
            ModelController.getInstance().updateUserData(username.getText().toString(),
                    marketphone.getText().toString(),marketname.getText().toString(),marketaddress.getText().toString(),specialist.getText().toString(),marketphone.getText().toString());
            finish();
        });


    }


}
