package com.example.mp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private Button btList;
    private Button btOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        btList = findViewById(R.id.listButton);
        btOptions = findViewById(R.id.optionsButton);
    }

    @Override
    protected void onStart() {
        super.onStart();
        btList.setTextColor(sp.getInt("colorTheme", 0x000000));
        btList.setTextSize(sp.getInt("size", 14));
        btOptions.setTextColor(sp.getInt("colorTheme", 0x000000));
        btOptions.setTextSize(sp.getInt("size", 14));
    }

    public void clickOptions(View view){
        Intent i = new Intent(this, OptionsActivity.class);
        startActivity(i);
    }

    public void clickList(View view){
        Intent i = new Intent(this, ProductListActivity.class);
        startActivity(i);
    }
}
