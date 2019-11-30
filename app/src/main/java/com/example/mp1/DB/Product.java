package com.example.mp1.DB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Product {

    @PrimaryKey
   private @NonNull String productId;

    @ColumnInfo(name = "productName")
    private String productName;

    @ColumnInfo(name = "price")
    private int price;

    @ColumnInfo(name = "bought")
    private boolean bought;

    public Product(String productName, int price, boolean bought, String productId){
        this.productName = productName;
        this.price = price;
        this.bought = bought;
        this.productId = productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setProductName(String productName){
        this.productName = productName;
    }

    public void setPrice(int price){
        this.price = price;
    }

    public void setBought(boolean bought){
        this.bought = bought;
    }

    public String getProductId(){
        return this.productId;
    }

    public String getProductName(){
        return this.productName;
    }

    public int getPrice(){
        return this.price;
    }

    public boolean getBought(){
        return this.bought;
    }
}
