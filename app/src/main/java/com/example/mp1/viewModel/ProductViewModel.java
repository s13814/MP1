package com.example.mp1.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mp1.DB.Product;
import com.example.mp1.repository.ProductRepository;
import com.example.mp1.repository.ProductRepositoryFirebase;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private ProductRepositoryFirebase repository;
    private LiveData<List<Product>> allProducts;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        repository = new ProductRepositoryFirebase(application);
        allProducts = repository.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts(){
        return allProducts;
    }

    public void insert (Product product){
        repository.insert(product);
    }

    public void update (Product product) {
        repository.update(product);
    }

    public void delete (int idProduct){
        repository.delete(idProduct);
    }
}
