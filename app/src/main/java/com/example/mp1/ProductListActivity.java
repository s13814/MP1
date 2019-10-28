package com.example.mp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mp1.DB.Product;
import com.example.mp1.DB.ProductDB;
import com.example.mp1.adapter.ProductAdapter;
import com.example.mp1.viewModel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private ProductDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        RecyclerView rvProductList = findViewById(R.id.rvProductList);
        db = ProductDB.getDatabase(this);
        //db.initDB();
        List<Product> lp = getProducts();

        List<Product> a = new ArrayList<Product>();
        a.add(new Product(1,"a", 5, false));

        final ProductAdapter adapter = new ProductAdapter(this, a);
        setProducts(adapter);
        rvProductList.setLayoutManager(new LinearLayoutManager(this));
        rvProductList.setAdapter(adapter);
    }

    private void setProducts(final ProductAdapter adapter){
        ProductViewModel productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.setProducts(products);
            }
        });
    }

    private List<Product> getProducts(){
        List<Product> lp = new ArrayList<>();
        //lp = db.productDao().getAllProducts();
        return lp;
    }
}
