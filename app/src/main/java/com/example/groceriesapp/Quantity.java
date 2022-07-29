package com.example.groceriesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Quantity extends AppCompatActivity {

    ImageView pic;
    TextView name, subtotal, quantityString;
    double price;
    String productName;
    int quantity,position;
    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantity);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.mainGray));
        }

        pic = findViewById(R.id.productImage);
        name = findViewById(R.id.name);
        subtotal = findViewById(R.id.subtotal_price);
        quantityString = findViewById(R.id.quantity);
        quantity = Integer.parseInt((String) quantityString.getText());

        Bundle bundle = getIntent().getExtras();
        b = bundle;
        productName = b.getString("name");
        pic.setImageResource(b.getInt("pictureId"));
        name.setText(b.getString("name"));
        price = Double.parseDouble(b.getString("price"));
        position=b.getInt("position");
    }

    public void add_to_cart(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        SharedPreferences sp = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("quantity",quantity);
        editor.putString("name",productName);
        editor.apply();

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void plus(View view) {
        quantity++;
        quantityString.setText(quantity + "");
        subtotal.setText(String.format("%.2f", price * quantity));
    }

    public void minus(View view) {
        if (quantity > 0)
            quantity--;
        quantityString.setText(quantity + "");
        subtotal.setText(String.format("%.2f", price * quantity));
    }

    public void goBack(View view){
        Intent intent= new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent= new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}