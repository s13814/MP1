package com.example.mp1.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.mp1.DB.Shop;
import com.example.mp1.repository.ShopRepositoryFirebase;

import java.util.List;

public class ShopViewModel extends AndroidViewModel {

    private ShopRepositoryFirebase repository;
    private List<Shop> allShops;

    public ShopViewModel(@NonNull Application application) {
        super(application);
        repository = new ShopRepositoryFirebase(application);
        refreshAllShops();
    }

    private void refreshAllShops(){
        allShops = repository.getAllShops();
    }

    public List<Shop> getAllShops(){
        return  allShops;
    }

    public void insert(Shop shop){
        repository.insert(shop);
    }

    public void update(Shop shop){
        repository.update(shop);
    }

    public void delete(Shop shop){
        repository.delete(shop);
    }
}
