package com.example.mp1.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mp1.DB.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryFirebase {

    private FirebaseDatabase db;
    private DatabaseReference dr;
    private FirebaseUser user;
    private MutableLiveData<List<Product>> allProducts = new MutableLiveData<>();

    public ProductRepositoryFirebase(Application application) {
        this.db = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        this.dr = db.getReference("users/" + user.getUid() + "/products");
        this.allProducts.setValue(downloadAllProducts());

        dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String name = dataSnapshot.child("productName").getValue().toString();
                int price = ((Long)dataSnapshot.child("price").getValue()).intValue();
                boolean bought = (Boolean)dataSnapshot.child("bought").getValue();
                String idProduct = dataSnapshot.child("productId").getValue().toString();
                allProducts.getValue().add(new Product(name, price, bought, idProduct));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                List<Product> tmp = allProducts.getValue();
                for (Product product : tmp){
                    if(product.getProductId().equals(dataSnapshot.getKey())){
                        product.setProductName(dataSnapshot.child("productName").getValue().toString());
                        product.setPrice(((Long)dataSnapshot.child("price").getValue()).intValue());
                        product.setBought((Boolean)dataSnapshot.child("bought").getValue());
                    }
                }
                allProducts.setValue(tmp);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                List<Product> tmp = allProducts.getValue();
                Product productToDelete = null;
                for (Product product : tmp){
                    if(product.getProductId().equals(dataSnapshot.getKey()))
                        productToDelete = product;
                }
                tmp.remove(productToDelete);
                allProducts.setValue(tmp);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public LiveData<List<Product>> getAllProducts(){
        return allProducts ;
    }

    private List<Product> downloadAllProducts(){
        final List<Product> products = new ArrayList<>();

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allProducts.setValue(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return products;
    }

    public void insert(Product product){
        //DatabaseReference drp = dr.push();
        DatabaseReference drp = db.getReference("users/" + user.getUid() + "/products/").push();
        product.setProductId(drp.getKey());
        drp.setValue(product);
    }

    public void update(Product product){
        DatabaseReference drp = dr.child(product.getProductId());
        drp.setValue(product);
    }

    public void delete(int idProduct){
        Product productToDelete = allProducts.getValue().get(idProduct);
        dr.child(productToDelete.getProductId()).removeValue();
    }
}
