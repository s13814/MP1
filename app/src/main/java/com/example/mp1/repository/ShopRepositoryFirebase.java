package com.example.mp1.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mp1.DB.Shop;
import com.example.mp1.adapter.ShopAdapter;
import com.google.android.gms.maps.model.LatLng;
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

public class ShopRepositoryFirebase {

    private FirebaseDatabase db;
    private DatabaseReference dr;
    private FirebaseUser user;
    private MutableLiveData<List<Shop>> allShops = new MutableLiveData<>();

    public ShopRepositoryFirebase(Application application) {
        this.db = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        this.dr = db.getReference("users/" + user.getUid() + "/shops");
        this.allShops.setValue(downloadAllShops());

        dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String shopId = dataSnapshot.child("shopId").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String desc = dataSnapshot.child("description").getValue().toString();
                Long radius = dataSnapshot.child("radius").getValue(Long.class);
                Long latitude = dataSnapshot.child("location").child("latitude").getValue(Long.class);
                Long longitude = dataSnapshot.child("location").child("longitude").getValue(Long.class);

                Log.i("SHOP_REPO","Called onChildAdded" );
                allShops.getValue().add(new Shop(shopId, name, desc, radius.doubleValue(), new LatLng(latitude.doubleValue(), longitude.doubleValue())));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                List<Shop> tmp = allShops.getValue();
                for (Shop shop : tmp) {
                    if (shop.getShopId().equals(dataSnapshot.getKey())) {
                        shop.setName(dataSnapshot.child("name").getValue().toString());
                        shop.setDescription(dataSnapshot.child("description").getValue().toString());
                        shop.setRadius(((Long) dataSnapshot.child("radius").getValue()).doubleValue());
                    }
                }
                allShops.setValue(tmp);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                List<Shop> tmp = allShops.getValue();
                Shop shopToDelete = null;
                for (Shop shop : tmp) {
                    if (shop.getShopId().equals(dataSnapshot.getKey()))
                        shopToDelete = shop;
                }
                tmp.remove(shopToDelete);
                allShops.setValue(tmp);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public LiveData<List<Shop>> getAllShops() {
        return allShops;
    }

    private List<Shop> downloadAllShops(){
        final List<Shop> shops = new ArrayList<>();

        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allShops.setValue(shops);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return shops;
    }

    public void insert(Shop shop) {
        DatabaseReference drp = dr.push();
        shop.setShopId(drp.getKey());
        drp.setValue(shop);
    }

    public void update(Shop shop) {
        DatabaseReference drp = dr.child(shop.getShopId());
        drp.setValue(shop);
    }

    public void delete(Shop shop) {
        dr.child(shop.getShopId()).removeValue();
    }
}
