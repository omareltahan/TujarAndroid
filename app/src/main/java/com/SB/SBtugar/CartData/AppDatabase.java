package com.SB.SBtugar.CartData;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CartItem.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TransactionsDaoModel TransactionsDao();

}
