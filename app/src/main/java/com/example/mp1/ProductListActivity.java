package com.example.mp1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

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
        //List<Product> lp = getProducts();

        final ProductAdapter adapter = new ProductAdapter(this);
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

    public void clickAdd(View view){
        AlertDialog.Builder ad = new AlertDialog.Builder(ProductListActivity.this);
        ad.setTitle("Add product");
        ad.setMessage("Enter new product");

        ad.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        ad.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        LinearLayout layout = new LinearLayout(ProductListActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        EditText name = new EditText(ProductListActivity.this);
        name.setHint("Name");

        EditText price = new EditText(ProductListActivity.this);
        price.setHint("Price");

        layout.addView(name);
        layout.addView(price);

        ad.setView(layout, 50, 0, 50, 0);
        ad.show();
    }
    /*private List<Product> getProducts(){
        LiveData<List<Product>> lp = new LiveData<List<Product>>();
        lp = db.productDao().getAllProducts();
        return lp;
    }*/
}
