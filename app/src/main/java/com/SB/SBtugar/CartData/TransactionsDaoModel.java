package com.SB.SBtugar.CartData;


import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TransactionsDaoModel {
    @Query("SELECT * FROM CartItem")
    List<CartItem> getAll();

    @Query("SELECT * FROM CartItem WHERE id = :ItemId AND variation = :variation")
    CartItem FindCartItemById(int ItemId, int variation);


    @Query("Update CartItem SET quantity = :newquantity WHERE id = :ItemId AND variation = :variationId")
    void UpdateCartQuantity(int newquantity, int ItemId, int variationId);

    @Insert
    void insertAll(CartItem... products);

    @Query("DELETE FROM CartItem WHERE id = :ItemId AND variation = :variationId")
    void delete(int ItemId, int variationId);

    @Query("DELETE FROM CartItem")
    public void deleteAll();



}
