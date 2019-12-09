package com.example.mp1.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    private List<Shop> allShops = new ArrayList<>();

    public ShopRepositoryFirebase(Application application) {
        this.db = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        this.dr = db.getReference("users/" + user.getUid() + "/shops");

        dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String shopId = dataSnapshot.child("shopId").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String desc = dataSnapshot.child("description").getValue().toString();
                Double radius = ((Long) dataSnapshot.child("radius").getValue()).doubleValue();
                Double latitude = ((Long) dataSnapshot.child("location").child("latitude").getValue()).doubleValue();
                Double longitude = ((Long) dataSnapshot.child("location").child("longitude").getValue()).doubleValue();

                allShops.add(new Shop(shopId, name, desc, radius, new LatLng(latitude, longitude)));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (Shop shop : allShops) {
                    if (shop.getShopId().equals(dataSnapshot.getKey())) {
                        shop.setName(dataSnapshot.child("name").getValue().toString());
                        shop.setDescription(dataSnapshot.child("description").getValue().toString());
                        shop.setRadius(((Long) dataSnapshot.child("radius").getValue()).doubleValue());
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Shop shopToDelete = null;
                for (Shop shop : allShops) {
                    if (shop.getShopId().equals(dataSnapshot.getKey()))
                        shopToDelete = shop;
                }
                allShops.remove(shopToDelete);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public List<Shop> getAllShops() {
        return allShops;
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
