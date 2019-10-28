package com.example.mp1.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.mp1.DB.Product;
import com.example.mp1.DB.ProductDAO;
import com.example.mp1.DB.ProductDB;

import java.util.List;

public class ProductRepository{
    private ProductDAO productDao;
    private LiveData<List<Product>> allProducts;

    public ProductRepository(Application application){
        ProductDB db = ProductDB.getDatabase(application);
        productDao = db.productDao();
        allProducts = productDao.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts(){
        return allProducts;
    }

    public void insert(Product product){
        new insertAsyncTask(productDao).execute(product);
    }

    private static class insertAsyncTask extends AsyncTask<Product, Void, Void> {

        private ProductDAO asyncTaskDao;

        insertAsyncTask(ProductDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Product... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
