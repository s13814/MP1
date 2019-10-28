package com.example.mp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

public class OptionsActivity extends AppCompatActivity {

    private EditText etSize;
    private Button btColor;
    private TextView tvSize;
    private TextView tvColor;
    private SharedPreferences sp;
    private Button btSave;
    private TextView tvOptions;
    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        etSize = findViewById(R.id.sizeEditText);
        btColor = findViewById(R.id.colorButton);
        tvSize = findViewById(R.id.tvSize);
        tvColor = findViewById(R.id.tvColor);
        btSave = findViewById(R.id.saveButton);
        tvOptions = findViewById(R.id.textView);
        sp = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        btColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorClick();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        etSize.setTextColor(sp.getInt("colorTheme", 0xFFFFFF));
        tvSize.setTextColor(sp.getInt("colorTheme", 0xFFFFFF));
        tvColor.setTextColor(sp.getInt("colorTheme", 0xFFFFFF));
        btColor.setTextColor(sp.getInt("colorTheme", 0xFFFFFF));
        btSave.setTextColor(sp.getInt("colorTheme", 0xFFFFFF));
        tvOptions.setTextColor(sp.getInt("colorTheme", 0xFFFFFF));
    }

    public void colorClick(){
        ColorPickerDialogBuilder
                .with(this, R.style.ColorPickerDialogTheme)
                .setTitle("Choose color")
                .initialColor(0xFFFFFFFF)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(10)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        Toast.makeText(OptionsActivity.this ,"Chosen color: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        etSize.setTextColor(selectedColor);
                        tvSize.setTextColor(selectedColor);
                        tvColor.setTextColor(selectedColor);
                        btColor.setTextColor(selectedColor);
                        btSave.setTextColor(selectedColor);
                        tvOptions.setTextColor(selectedColor);
                        color = selectedColor;

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    public void saveClick(View view){
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("colorTheme", color);
        editor.apply();
        finish();
    }
}
