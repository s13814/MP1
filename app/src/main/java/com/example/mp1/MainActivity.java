package com.example.mp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("preferences", Context.MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //et.setText(sp.getString("string1", ""));
        //cb.setChecked(sp.getBoolean("bool1", false));
    }

    public void clickOptions(View view){
        //SharedPreferences.Editor editor = sp.edit();
        //editor.putString("string1", et.getText().toString());
        //editor.putBoolean("bool1", cb.isChecked());
        //editor.apply();
        Intent i = new Intent(this, OptionsActivity.class);
        startActivity(i);
    }

    public void clickList(View view){
        Intent i = new Intent(this, ProductListActivity.class);
        startActivity(i);
    }
}
