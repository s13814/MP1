package com.example.mp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import        android.view.Menu;
import        android.view.MenuItem;
import        android.view.MenuInflater;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mp1.DB.Product;
import com.example.mp1.DB.ProductDB;
import com.example.mp1.adapter.ProductAdapter;
import com.example.mp1.viewModel.ProductViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        RecyclerView rvProductList = findViewById(R.id.rvProductList);
        final ProductAdapter adapter = new ProductAdapter(this);
        setProducts(adapter);
        rvProductList.setLayoutManager(new LinearLayoutManager(this));
        rvProductList.setAdapter(adapter);
        registerForContextMenu(rvProductList);

        adapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                if(product.getBought())
                    product.setBought(false);
                else {
                    product.setBought(true);
                    Toast.makeText(ProductListActivity.this, product.getProductName()+ " is bought!", Toast.LENGTH_LONG).show();
                }
                productViewModel.update(product);
            }
        });

        /*adapter.setOnItemLongClickListener(new ProductAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick() {
                Toast.makeText(ProductListActivity.this, "Long click", Toast.LENGTH_LONG).show();
                return true;
            }
        });*/
    }

    private void setProducts(final ProductAdapter adapter){
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                adapter.setProducts(products);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getTitle() == "Delete")
            Toast.makeText(ProductListActivity.this, Long.toString(info.id), Toast.LENGTH_LONG).show();

        return super.onContextItemSelected(item);
    }

    public void clickAdd(View view){
        AlertDialog.Builder ad = new AlertDialog.Builder(ProductListActivity.this);
        ad.setTitle("Add product");
        ad.setMessage("Enter new product");

        LinearLayout layout = new LinearLayout(ProductListActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText name = new EditText(ProductListActivity.this);
        name.setHint("Name");

        final EditText price = new EditText(ProductListActivity.this);
        price.setHint("Price");
        price.setInputType(InputType.TYPE_CLASS_NUMBER);

        layout.addView(name);
        layout.addView(price);

        ad.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        productViewModel.insert(new Product(name.getText().toString(), Integer.parseInt(price.getText().toString()), false));
                        Log.i("ProductListActivity", name.getText().toString());
                    }
                });

        ad.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        ad.setView(layout, 50, 0, 50, 0);
        ad.show();
    }
}
