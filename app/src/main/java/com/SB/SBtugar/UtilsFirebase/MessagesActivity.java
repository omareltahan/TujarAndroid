package com.SB.SBtugar.UtilsFirebase;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AllModels.UserData;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MessagesActivity extends AppCompatActivity {
    EditText Comment_Text;
    UserData main_user = ModelController.getInstance().getModel().getUser();
    ImageView imageSend;
    RecyclerView recyclerView;
    MessagesRecycler adapter;
    ArrayList<MessageModel> List_Data;
    ImageView back;
    Uri uri;

    String ChatId = "";
    String StartTime = "";
    String LastText = "";
    String NameOfEmployee = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_chat_inside);

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlertDialog();
            }
        });

        final getDataFromServer data = new getDataFromServer(this, AllUrls.ListenToCurrentChat(String.valueOf(main_user.getId())));
        try {
            data.Data(new DataFromFirebaseOnGet() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue()!=null){
                        ChatId =  dataSnapshot.child("ChatId").getValue(String.class);
                        StartTime =  dataSnapshot.child("StartTime").getValue(String.class);
                        LastText =  dataSnapshot.child("LastText").getValue(String.class);
                        NameOfEmployee =  dataSnapshot.child("NameOfEmployee").getValue(String.class);
                        if (getIntent().getExtras()==null)
                            setDialog(false);
                        else {
                            GetChatMessages();
                            adapter = new MessagesRecycler(MessagesActivity.this,List_Data);
                            recyclerView.setAdapter(adapter);
                            findViewById(R.id.sec_lay).setVisibility(View.VISIBLE);
                            findViewById(R.id.close).setVisibility(View.VISIBLE);
                            findViewById(R.id.chat_close).setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        setDialog(true);
                    }
                }

                @Override
                public void onCancel(DatabaseError databaseError) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


        List_Data = new ArrayList<>();

        MessageModel model1 = new MessageModel();
        model1.setMessage_text("أهلا "+main_user.getFirstName()+", شكرا لتواصلك مع سوبر بقالة. أقدر اساعدك ازاي؟");

        MessageModel model2 = new MessageModel();
        model2.setMessage_text("مرحبا كيف حالك اليوم ؟");

        MessageModel model3 = new MessageModel();
        model3.setMessage_text("كيف اقدر اساعدك ؟");
        List_Data.add(model1);
        List_Data.add(model2);
        List_Data.add(model3);

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

        imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Comment_Text.getText().toString().trim().isEmpty()) {
                    HashMap<String, String> hash = new HashMap<>();
                    hash.put("Message_Text", Comment_Text.getText().toString());
                    Calendar mcurrentTime = Calendar.getInstance();
                    hash.put("Message_Time", mcurrentTime.get(Calendar.HOUR_OF_DAY) + ":" + mcurrentTime.get(Calendar.MINUTE));
                    hash.put("isImage", String.valueOf(false));
                    hash.put("Sender_Id", String.valueOf(main_user.getId()));
                    final String valueV = FirebaseDatabase.getInstance().getReference().push().getKey();
                    Comment_Text.setText("");//empty field
                    setDataToServer data = new setDataToServer(MessagesActivity.this, AllUrls.GetMessageUrlFromMessageId( valueV), hash);
                    try {
                        data.Data(new DataFromFirebaseOnAdded() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                setDataToServer data_inside = new setDataToServer(MessagesActivity.this, AllUrls.GetChatAllMessagesUrlById(ChatId) + "/" + valueV, "");
                                try {
                                    data_inside.Data(new DataFromFirebaseOnAdded() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            setDataToServer data_inside = new setDataToServer(MessagesActivity.this, AllUrls.ListenToChatLastMessage(String.valueOf(main_user.getId())), valueV);
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
                    startService(new Intent(MessagesActivity.this, FloatingWidgetShowService.class));

                } else if (Settings.canDrawOverlays(MessagesActivity.this)) {

                    finish();
                    startService(new Intent(MessagesActivity.this, FloatingWidgetShowService.class));


                } else {
                    RuntimePermissionForUser();

                    Toast.makeText(MessagesActivity.this, "System Alert Window Permission Is Required For Floating Widget.", Toast.LENGTH_LONG).show();
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
                        GetNewMessageAdded(String.valueOf(main_user.getId()));
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
                                            GetNewMessageAdded(String.valueOf(main_user.getId()));
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
    public void GetNewMessageAdded(String chat_id) {
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
            Intent intent = new Intent(MessagesActivity.this, SendImageActivity.class);
            intent.putExtra("chat_id",ChatId);
            startActivity(intent);

        }
    }

    private void setDialog(boolean status) {
        final Dialog dialog = new Dialog(this,R.style.Theme_Dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_to_start_chat);
        if (status)
            dialog.findViewById(R.id.start_previous).setVisibility(View.GONE);

        dialog.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog.findViewById(R.id.start_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child(AllUrls.ListenToCurrentChat(String.valueOf(main_user.getId())))
                        .removeValue();
                DatabaseReference value = FirebaseDatabase.getInstance().getReference().child(AllUrls.ListenToPreviousChat(String.valueOf(main_user.getId())))
                        .child(ChatId);
                value.child("ChatId").setValue(ChatId);
                value.child("NameOfEmployee").setValue(NameOfEmployee);
                value.child("StartTime").setValue(StartTime);
                value.child("EndTime").setValue(StartTime);
                value.child("LastText").setValue(LastText);


                DatabaseReference valuedata = FirebaseDatabase.getInstance().getReference().child(AllUrls.ListenToCurrentChat(String.valueOf(main_user.getId())));
                ChatId = valuedata.push().getKey();
                valuedata.child("ChatId").setValue(ChatId);
                valuedata.child("Status").setValue("New");
                valuedata.child("StartTime").setValue("now");
                valuedata.child("NameOfEmployee").setValue(NameOfEmployee);

                adapter = new MessagesRecycler(MessagesActivity.this,List_Data);
                recyclerView.setAdapter(adapter);
                findViewById(R.id.sec_lay).setVisibility(View.VISIBLE);
                findViewById(R.id.close).setVisibility(View.VISIBLE);
                findViewById(R.id.chat_close).setVisibility(View.VISIBLE);
                GetChatMessages();
                dialog.cancel();
            }
        });
        dialog.findViewById(R.id.start_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetChatMessages();
                adapter = new MessagesRecycler(MessagesActivity.this,List_Data);
                recyclerView.setAdapter(adapter);
                findViewById(R.id.sec_lay).setVisibility(View.VISIBLE);
                findViewById(R.id.close).setVisibility(View.VISIBLE);
                findViewById(R.id.chat_close).setVisibility(View.VISIBLE);
                dialog.cancel();
            }
        });

        final Window dialogWindow = dialog.getWindow();
        final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();
        final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogWindowAttributes);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity =  Gravity.CENTER;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    void ShowAlertDialog(){
        new AlertDialog.Builder(this)
                .setTitle("إغلاق المحادثة الحالية")
                .setMessage("سيتم الموافقة على إغلاق المحادثة الحالية انه قد تم حل المشكلة التي تواجهك و عدم الرجوع لها مرة اخرى")
                .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child(AllUrls.ListenToCurrentChat(String.valueOf(main_user.getId())))
                                .removeValue();
                        DatabaseReference value = FirebaseDatabase.getInstance().getReference().child(AllUrls.ListenToPreviousChat(String.valueOf(main_user.getId())))
                                .child(ChatId);
                        value.child("ChatId").setValue(ChatId);
                        value.child("NameOfEmployee").setValue(NameOfEmployee);
                        value.child("StartTime").setValue(StartTime);
                        value.child("EndTime").setValue(StartTime);
                        value.child("LastText").setValue(LastText);

                        finish();
                    }
                })
                .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.danger)
                .show();

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
                startService(new Intent(MessagesActivity.this, FloatingWidgetShowService.class));

            } else {
                finish();
                startService(new Intent(MessagesActivity.this, FloatingWidgetShowService.class));


            }
        }
    }
}
