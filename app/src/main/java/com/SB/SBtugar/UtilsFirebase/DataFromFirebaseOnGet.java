package com.SB.SBtugar.UtilsFirebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface DataFromFirebaseOnGet {
    void onSuccess(DataSnapshot dataSnapshot);
    void onCancel(DatabaseError databaseError);
}