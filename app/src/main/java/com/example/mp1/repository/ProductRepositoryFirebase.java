package com.example.mp1.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mp1.DB.Product;
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
    private MutableLiveData<List<Product>> allProducts = new MutableLiveData<>();
    //public LiveData<List<Product>> _allProducts;

    public ProductRepositoryFirebase(Application application) {
        this.db = FirebaseDatabase.getInstance();
        this.dr = db.getReference("Products");
        //this.allProducts.setValue(downloadAllProducts());
        dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                List<Product> temp = allProducts.getValue();
                String name = dataSnapshot.child("productName").getValue().toString();
                int price = ((Long)dataSnapshot.child("price").getValue()).intValue();
                boolean bought = (Boolean)dataSnapshot.child("bought").getValue();

                temp.add(new Product(name, price, bought));
                allProducts.setValue(temp);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

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

  /*  private List<Product> downloadAllProducts(){
        final List<Product> products = new ArrayList<>();

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dbSnap : dataSnapshot.getChildren()) {
                    String name = dbSnap.child("productName").getValue().toString();
                    int price = ((Long)dbSnap.child("price").getValue()).intValue();
                    boolean bought = (Boolean)dbSnap.child("bought").getValue();

                    products.add(new Product(name, price, bought));
                }
               // allProducts.setValue(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return products;
    }
*/
    public void insert(Product product){
        DatabaseReference drp = dr.push();
        drp.setValue(product);
    }

    public void update(Product product){

    }

    public void delete(Product product){

    }
}
