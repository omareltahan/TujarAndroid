package com.SB.SBtugar.UtilsFirebase;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.SB.SBtugar.AllModels.ModelController;
import com.SB.SBtugar.AppController;
import com.SB.SBtugar.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class SendImageActivity extends AppCompatActivity {
    ImageView imageSend;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_full);

        imageSend = findViewById(R.id.ClickToSend);
        imageSend.setImageBitmap(AppController.image);
        ((ImageView) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((ImageView) findViewById(R.id.send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                AppController.image.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                byte[] datax = baos.toByteArray();
                final String valueV = FirebaseDatabase.getInstance().getReference().push().getKey();
                UploadTask uploadTask = FirebaseStorage.getInstance().getReference()
                        .child(AllUrls.GetMessageUrlFromMessageId("") + "/" + valueV + ".jpg").putBytes(datax);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        HashMap<String, String> hash = new HashMap<>();
                        hash.put("Message_Text", "image");
                        Calendar mcurrentTime = Calendar.getInstance();
                        hash.put("Message_Time", mcurrentTime.get(Calendar.HOUR_OF_DAY) + ":" + mcurrentTime.get(Calendar.MINUTE));
                        hash.put("isImage", "true");
                        hash.put("Sender_Id", String.valueOf(ModelController.getInstance().getModel().getUser().getId()));
                        setDataToServer data = new setDataToServer(SendImageActivity.this, AllUrls.GetMessageUrlFromMessageId("") + "/" + valueV, hash);
                        try {
                            data.Data(new DataFromFirebaseOnAdded() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    setDataToServer data_inside = new setDataToServer(SendImageActivity.this, AllUrls.GetChatAllMessagesUrlById(getIntent().getExtras().getString("chat_id"))
                                            + "/" + valueV, "");
                                    try {
                                        data_inside.Data(new DataFromFirebaseOnAdded() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                setDataToServer data_inside = new setDataToServer(SendImageActivity.this, AllUrls.ListenToChatLastMessage(String.valueOf(ModelController.getInstance().getModel().getUser().getId()))
                                                        , valueV);
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
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("res", e.toString());
                    }
                });
                finish();
            }
        });

    }
}
