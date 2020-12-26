package com.SB.SBtugar.CartData;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Project ${PROJECT}
 * Created by asamy on 5/2/2018.
 */
@Entity
public class CartItem implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "NewId")
    int NewId;
    @ColumnInfo(name = "Image")
    public String Image;
    @ColumnInfo(name = "Variation_txt")
    public String Variation_txt;
    @ColumnInfo(name = "quantity")
    public int quantity;
    @ColumnInfo(name = "variation")
    public int variation;
    @ColumnInfo(name = "Price_Is")
    public Float Price_Is;
    @ColumnInfo(name = "id")
    @Expose
    public Integer id;
    @ColumnInfo(name = "name")
    @Expose
    public String name;
    @ColumnInfo(name = "type")
    @Expose
    public String type;
    @ColumnInfo(name = "description")
    @Expose
    public String description;
    @ColumnInfo(name = "short_description")
    @Expose
    public String shortDescription;
    @ColumnInfo(name = "price")
    @Expose
    public String price;
    @ColumnInfo(name = "regular_price")
    @Expose
    public String regularPrice;
    @ColumnInfo(name = "sale_price")
    @Expose
    public String salePrice;

}
