package com.example.mp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private Button btList;
    private Button btOptions;
    private Button btMap;
    private Button btShopList;
    private FirebaseAuth fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        btList = findViewById(R.id.listButton);
        btOptions = findViewById(R.id.optionsButton);
        btMap = findViewById(R.id.mapButton);
        btShopList = findViewById(R.id.shopListButton);
        fa = FirebaseAuth.getInstance();
        loginOrRegister();
    }

    @Override
    protected void onStart() {
        super.onStart();
        btList.setTextColor(sp.getInt("colorTheme", 0xFF000000));
        btList.setTextSize(sp.getInt("size", 14));
        btOptions.setTextColor(sp.getInt("colorTheme", 0xFF000000));
        btOptions.setTextSize(sp.getInt("size", 14));
        btMap.setTextColor(sp.getInt("colorTheme", 0xFF000000));
        btMap.setTextSize(sp.getInt("size", 14));
        btShopList.setTextColor(sp.getInt("colorTheme", 0xFF000000));
        btShopList.setTextSize(sp.getInt("size", 14));
    }

    public void clickOptions(View view){
        Intent i = new Intent(this, OptionsActivity.class);
        startActivity(i);
    }

    public void clickList(View view){
        Intent i = new Intent(this, ProductListActivity.class);
        startActivity(i);
    }

    public void clickMap(View view){
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }

    public void clickShopList(View view){
        Intent i = new Intent(this, ShopListActivity.class);
        startActivity(i);
    }

    private void loginOrRegister(){
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setTitle("Login/Register");
        ad.setMessage("Enter your data");

        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText email = new EditText(MainActivity.this);
        email.setHint("Email");

        final EditText password = new EditText(MainActivity.this);
        password.setHint("Password");
        password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        layout.addView(email);
        layout.addView(password);

        ad.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fa.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = fa.getCurrentUser();
                                }else{
                                    fa.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                FirebaseUser user = fa.getCurrentUser();
                                            }else{
                                                Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
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
