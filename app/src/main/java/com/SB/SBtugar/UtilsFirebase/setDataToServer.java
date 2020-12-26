package com.SB.SBtugar.UtilsFirebase;

import android.content.Context;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;

import androidx.annotation.NonNull;


public class setDataToServer {

    private Context context;
    private String url;
    private Object values;
    public setDataToServer(Context context, String url, Object values) {
        this.context = context;
        this.values = values;
        this.url = url;
    }

    public void Data(final DataFromFirebaseOnAdded dataReceived) throws JSONException {
        FirebaseDatabase.getInstance().getReference().child(url).setValue(values)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataReceived.onSuccess(aVoid);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dataReceived.onFailure(e);
            }
        });

    }
}
