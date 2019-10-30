package com.example.mp1.DB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Product {

    @PrimaryKey(autoGenerate = true)
    private int productId;

    @ColumnInfo(name = "productName")
    private String productName;

    @ColumnInfo(name = "price")
    private int price;

    @ColumnInfo(name = "bought")
    private boolean bought;

    public Product(String productName, int price, boolean bought){
        this.productName = productName;
        this.price = price;
        this.bought = bought;
    }

    public void setProductId(int productId) {
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

    public int getProductId(){
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
