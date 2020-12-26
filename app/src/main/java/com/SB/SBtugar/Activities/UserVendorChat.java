package com.SB.SBtugar.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.SuperMarket.SuperMarket;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.R;
import com.SB.SBtugar.UtilsFirebase.AllUrls;
import com.SB.SBtugar.UtilsFirebase.DataFromFirebaseOnAdded;
import com.SB.SBtugar.UtilsFirebase.DataFromFirebaseOnGet;
import com.SB.SBtugar.UtilsFirebase.FloatingWidgetShowService;
import com.SB.SBtugar.UtilsFirebase.MessageModel;
import com.SB.SBtugar.UtilsFirebase.MessagesRecycler;
import com.SB.SBtugar.UtilsFirebase.SendImageActivity;
import com.SB.SBtugar.UtilsFirebase.getDataFromServer;
import com.SB.SBtugar.UtilsFirebase.setDataToServer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class UserVendorChat extends AppCompatActivity {
    EditText Comment_Text;
    UserData main_user = ModelController.getInstance().getModel().getUser();
    ImageView imageSend;
    RecyclerView recyclerView;
    MessagesRecycler adapter;
    ArrayList<MessageModel> List_Data;
    ImageView back;

    int market;
    String ChatId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_chat_inside);
        findViewById(R.id.sec_lay).setVisibility(View.VISIBLE);
        market =  getIntent().getExtras().getInt("ID");
        ChatId = main_user.getId()+"CHAT"+market;
        List_Data = new ArrayList<>();

        MessageModel model1 = new MessageModel();
        model1.setMessage_text("نشكرك للتواصل معنا .. نقدر نساعدك إزاي؟");
        List_Data.add(model1);

        findViewById(R.id.image_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent localIntent = new Intent("android.intent.action.GET_CONTENT");
                localIntent.setType("image/*");
                startActivityForResult(localIntent, 1);
            }
        });

        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Comment_Text=findViewById(R.id.Comment_Text);
        imageSend=findViewById(R.id.imageSend);
        recyclerView= findViewById(R.id.recycler);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager2.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager2);


        GetChatMessages();
        adapter = new MessagesRecycler(UserVendorChat.this,List_Data);
        recyclerView.setAdapter(adapter);

        imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Comment_Text.getText().toString().trim().isEmpty()) {
                    final HashMap<String, String> hash = new HashMap<>();
                    hash.put("Message_Text", Comment_Text.getText().toString());
                    Calendar mcurrentTime = Calendar.getInstance();
                    hash.put("Message_Time", mcurrentTime.get(Calendar.HOUR_OF_DAY) + ":" + mcurrentTime.get(Calendar.MINUTE));
                    hash.put("isImage", String.valueOf(false));
                    hash.put("Sender_Id", String.valueOf(main_user.getId()));
                    final String valueV = FirebaseDatabase.getInstance().getReference().push().getKey();
                    Comment_Text.setText("");//empty field
                    setDataToServer data = new setDataToServer(UserVendorChat.this, AllUrls.GetMessageUrlFromMessageId( valueV), hash);
                    try {
                        data.Data(new DataFromFirebaseOnAdded() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                setDataToServer data_inside = new setDataToServer(UserVendorChat.this, AllUrls.GetChatAllMessagesUrlById(ChatId) + "/" + valueV, "");
                                try {
                                    data_inside.Data(new DataFromFirebaseOnAdded() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if (List_Data.size()>1){
                                                setDataToServer data_inside = new setDataToServer(UserVendorChat.this, AllUrls.ListenToVendorChatLastMessage(String.valueOf(market),ChatId) + "/last_message", valueV);
                                                try {
                                                    data_inside.Data(new DataFromFirebaseOnAdded() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                        }

                                                        @Override
                                                        public void onFailure(Exception e) {

                                                        }
                                                    });
                                                } catch (Exception e) {

                                                }
                                            }
                                            else {
                                                final HashMap<String, String> newUser = new HashMap<>();
                                                newUser.put("UserName", main_user.getFirstName());
                                                if (main_user.getProfilePicture() != null)
                                                    newUser.put("UserImage", main_user.getProfilePicture().toString());
                                                else
                                                    newUser.put("UserImage", "");
                                                newUser.put("UserPhone", main_user.getBilling().getPhone());
                                                newUser.put("UserId", String.valueOf(main_user.getId()));
                                                newUser.put("last_message", valueV);
                                                setDataToServer data_inside = new setDataToServer(UserVendorChat.this, AllUrls.ListenToVendorChatLastMessage(String.valueOf(market),ChatId),newUser);
                                                try {
                                                    data_inside.Data(new DataFromFirebaseOnAdded() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                        }

                                                        @Override
                                                        public void onFailure(Exception e) {

                                                        }
                                                    });
                                                } catch (Exception e) {

                                                }
                                            }

                                        }

                                        @Override
                                        public void onFailure(Exception e) {

                                        }
                                    });
                                } catch (Exception e) {

                                }

                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });

                    } catch (Exception e) {

                    }
                }
            }
        });
        findViewById(R.id.chat_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    finish();
                    startService(new Intent(UserVendorChat.this, FloatingWidgetShowService.class));

                } else if (Settings.canDrawOverlays(UserVendorChat.this)) {

                    finish();
                    startService(new Intent(UserVendorChat.this, FloatingWidgetShowService.class));


                } else {
                    RuntimePermissionForUser();

                    Toast.makeText(UserVendorChat.this, "System Alert Window Permission Is Required For Floating Widget.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void GetChatMessages() {
        final ArrayList<MessageModel> list_arr = new ArrayList<>();
        final getDataFromServer data = new getDataFromServer(this, AllUrls.GetChatAllMessagesUrlById(String.valueOf(ChatId)));
        try {
            data.Data(new DataFromFirebaseOnGet() {
                @Override
                public void onSuccess(final DataSnapshot dataSnapshott) {
                    final int[] total_be = {0};
                    if (dataSnapshott.getChildrenCount()==0){
                        GetNewMessageAdded();
                    }
                    else {
                        for (DataSnapshot ds : dataSnapshott.getChildren()) {
                            Log.e("RESData", ds.toString() + "Data");
                            getDataFromServer data_inside = new getDataFromServer(getApplicationContext(), AllUrls.GetMessageUrlFromMessageId(ds.getKey()));
                            try {
                                data_inside.Data(new DataFromFirebaseOnGet() {
                                    @Override
                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                        Log.e("RESData", dataSnapshot.toString() + "Data");
                                        MessageModel item = new MessageModel(dataSnapshot);
                                        list_arr.add(item);
                                        total_be[0]++;
                                        if (total_be[0] == dataSnapshott.getChildrenCount()) {
                                            Log.e("TTTOTAL", list_arr.size() + "Size");
                                            GetNewChatAdded(list_arr);
                                            GetNewMessageAdded();
                                        }
                                    }

                                    @Override
                                    public void onCancel(DatabaseError databaseError) {

                                    }
                                });
                            } catch (Exception e) {

                            }
                        }
                    }

                }

                @Override
                public void onCancel(DatabaseError databaseError) {
                    Log.e("RESData",databaseError.toString()+"Data");

                }
            });
        } catch (Exception e) {
            Log.e("RESData",e.toString()+"Data");

        }

    }
    public void GetNewMessageAdded() {
        getDataFromServer data = new getDataFromServer(this, AllUrls.GetChatAllMessagesUrlById(ChatId));
        try {
            data.DataChildLastOne(new DataFromFirebaseOnGet() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    getDataFromServer data_inside = new getDataFromServer(getApplicationContext(), AllUrls.GetMessageUrlFromMessageId(dataSnapshot.getKey()));
                    try {
                        data_inside.Data(new DataFromFirebaseOnGet() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                ArrayList<MessageModel> list_arr = new ArrayList<>();
                                Log.e("RESData",dataSnapshot.toString()+"Data");
                                MessageModel item = new MessageModel(dataSnapshot);
                                list_arr.add(item);
                                GetNewChatAdded(list_arr);

                            }

                            @Override
                            public void onCancel(DatabaseError databaseError) {

                            }
                        });
                    } catch (Exception e) {

                    }

                }

                @Override
                public void onCancel(DatabaseError databaseError) {
                    Log.e("RESData",databaseError.toString()+"Data");

                }
            });
        } catch (Exception e) {
            Log.e("RESData",e.toString()+"Data");

        }

    }


    public void GetNewChatAdded(ArrayList<MessageModel> object) {
        Log.e("Done",object.size()+"Size");
        List_Data.addAll(object);
        adapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
        super.onActivityResult(paramInt1, paramInt2, paramIntent);
        if ((paramInt1 == 1) && (paramInt2 == -1)) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), paramIntent.getData());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            AppController.image = bitmap;
            Intent intent = new Intent(UserVendorChat.this, SendImageActivity.class);
            intent.putExtra("chat_id",ChatId);
            startActivity(intent);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void RuntimePermissionForUser() {

        Intent PermissionIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));

        startActivityForResult(PermissionIntent, 200);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                finish();
                startService(new Intent(UserVendorChat.this, FloatingWidgetShowService.class));

            } else {
                finish();
                startService(new Intent(UserVendorChat.this, FloatingWidgetShowService.class));


            }
        }
    }
}
