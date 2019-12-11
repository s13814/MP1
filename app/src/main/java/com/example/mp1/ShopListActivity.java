package com.example.mp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.mp1.DB.Shop;
import com.example.mp1.adapter.ShopAdapter;
import com.example.mp1.viewModel.ShopViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class ShopListActivity extends AppCompatActivity {

    private ShopViewModel shopViewModel;
    private ShopAdapter adapter = new ShopAdapter();
    private double latitude;
    private double longitude;
    private String PROX_ALERT_INTENT = "com.example.mp1.intent.action.PROX_ALERT";
    private LocationManager lm;

    @SuppressLint("MissingPermission")
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


        int minCzas = 0;
        int minDystans = 0;

        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener ll = new LocationListener() {

            public void onLocationChanged(Location location) {
                //Log.i("location",location.getLatitude()+" "+location.getLongitude());
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {

            }

            public void onProviderDisabled(String provider) {

            }
        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minCzas, minDystans, ll);
    }

    private void setShops(final ShopAdapter adapter) {
        shopViewModel = ViewModelProviders.of(this).get(ShopViewModel.class);
        shopViewModel.getAllShops().observe(this, new Observer<List<Shop>>() {
            @Override
            public void onChanged(List<Shop> shops) {
                adapter.setShops(shops);
            }
        });

        Log.i("SHOP_ACTIVITY", "Called setShops();");
    }



    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle() == "Edit") {

        } else if (item.getTitle() == "Delete") {
            shopViewModel.delete(adapter.getShopAtIndex(item.getGroupId()));
        }
        return super.onContextItemSelected(item);
    }

    public void clickAddShop(View view) {
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
                        shopViewModel.insert(new Shop("", name.getText().toString(), desc.getText().toString(), Double.valueOf(radius.getText().toString()), new LatLng(latitude, longitude)));
                        addProximityAlert(latitude, longitude, Double.valueOf(radius.getText().toString()));
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

    private void addProximityAlert(double latitude, double longitude, double radius){
        Intent i = new Intent(PROX_ALERT_INTENT);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0, i, 0);

        int requestcode = 0;
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION}, requestcode);
        }


        lm.addProximityAlert(latitude, longitude, (float)radius, -1, proximityIntent);

        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
        registerReceiver(new ProxAlertBrodcastReceiver(), filter);
    }
}
