package com.example.mp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mp1.DB.Product;
import com.example.mp1.adapter.ProductAdapter;
import com.example.mp1.viewModel.ProductViewModel;

import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private ProductViewModel productViewModel;
    private final ProductAdapter adapter = new ProductAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        RecyclerView rvProductList = findViewById(R.id.rvProductList);

        setProducts(adapter);
        LinearLayoutManager rlm = new LinearLayoutManager(this);
        rvProductList.setLayoutManager(rlm);
        rvProductList.addItemDecoration(new DividerItemDecoration(rvProductList.getContext(), rlm.getOrientation()));
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
        if(item.getTitle() == "Edit"){
            final Product productToEdit = adapter.getProductAtIndex(item.getGroupId());
            AlertDialog.Builder ad = new AlertDialog.Builder(ProductListActivity.this);
            ad.setTitle("Edit product");
            ad.setMessage("Change existing product");

            LinearLayout layout = new LinearLayout(ProductListActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText name = new EditText(ProductListActivity.this);
            name.setText(productToEdit.getProductName());

            final EditText price = new EditText(ProductListActivity.this);
            price.setText(String.valueOf(productToEdit.getPrice()));
            price.setInputType(InputType.TYPE_CLASS_NUMBER);

            layout.addView(name);
            layout.addView(price);

            ad.setPositiveButton("Edit",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            productToEdit.setProductName(name.getText().toString());
                            productToEdit.setPrice(Integer.parseInt(price.getText().toString()));
                            productViewModel.update(productToEdit);
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
        else if(item.getTitle() == "Delete") {
            productViewModel.delete(adapter.getProductAtIndex(item.getGroupId()));
        }
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

                        String myIntent = "com.example.mp1.intent.action.EVENT1";

                        Intent i = new Intent(myIntent);
                        i.putExtra("name", name.getText().toString());
                        i.putExtra("price", Integer.parseInt(price.getText().toString()));

                        sendBroadcast(i, "com.example.my_permissions.MY_PERMISSION");
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
