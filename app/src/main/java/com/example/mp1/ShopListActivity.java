package com.example.mp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.mp1.DB.Shop;
import com.example.mp1.adapter.ShopAdapter;
import com.example.mp1.viewModel.ShopViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

public class ShopListActivity extends AppCompatActivity {

    private ShopViewModel shopViewModel;
    private final ShopAdapter adapter = new ShopAdapter();
    private FusedLocationProviderClient mflc;
    final Double[] latitude = new Double[1];
    final Double[] longitude = new Double[1];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        RecyclerView rvShopList = findViewById(R.id.rvShopList);

        setShops(adapter);

        LinearLayoutManager rlm = new LinearLayoutManager(this);
        rvShopList.setLayoutManager(rlm);
        rvShopList.addItemDecoration(new DividerItemDecoration(rvShopList.getContext(), rlm.getOrientation()));
        rvShopList.setAdapter(adapter);
        registerForContextMenu(rvShopList);

        mflc = LocationServices.getFusedLocationProviderClient(ShopListActivity.this);
        mflc.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        latitude[0] = location.getLatitude();
                        longitude[0] = location.getLongitude();
                    }
                });
    }

    private void setShops(final ShopAdapter adapter){
        shopViewModel = ViewModelProviders.of(this).get(ShopViewModel.class);
        adapter.setShops(shopViewModel.getAllShops());
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle() == "Edit"){

        }
        else if(item.getTitle() == "Delete"){
            shopViewModel.delete(adapter.getShopAtIndex(item.getGroupId()));
        }
        return super.onContextItemSelected(item);
    }

    public void clickAddShop(View view){
        AlertDialog.Builder ad = new AlertDialog.Builder(ShopListActivity.this);
        ad.setTitle("Add shop");
        ad.setMessage("Enter new shop");

        LinearLayout layout = new LinearLayout(ShopListActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText name = new EditText(ShopListActivity.this);
        name.setHint("Shop name");

        final EditText desc = new EditText(ShopListActivity.this);
        desc.setHint("Description");

        final EditText radius = new EditText(ShopListActivity.this);
        radius.setHint("Radius");
        radius.setInputType(InputType.TYPE_CLASS_NUMBER);

        layout.addView(name);
        layout.addView(desc);
        layout.addView(radius);

        ad.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shopViewModel.insert(new Shop("", name.getText().toString(), desc.getText().toString(), Double.valueOf(radius.getText().toString()), new LatLng(latitude[0],longitude[0])));
                        adapter.notifyDataSetChanged();
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
