package com.example.groceriesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Cart extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerAdapter2 adapter;
    TextView noItms;
    TextView totals;

    ArrayList<String> pricesCloud = new ArrayList<String>();
    ArrayList<String> namesCloud = new ArrayList<String>();
    String productNames[];
    String productPrices[];
    int picIds[];
    int allPicIds[];
    int productQuantities[];
    int allProductQuantities[];
    boolean noItems = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.mainGray));
        }

        SharedPreferences sharedPreferences = getSharedPreferences("store info", MODE_PRIVATE);
        Gson gson = new Gson();

        String json = sharedPreferences.getString("names", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        namesCloud = gson.fromJson(json, type);

        json = sharedPreferences.getString("prices", null);
        type = new TypeToken<ArrayList<String>>() {
        }.getType();
        pricesCloud = gson.fromJson(json, type);

        Bundle bundle = getIntent().getExtras();
        allPicIds = bundle.getIntArray("picIds");
        allProductQuantities = bundle.getIntArray("quantities");

        noItms = findViewById(R.id.no_items);
        totals = findViewById(R.id.total);

        int itemsInCart = 0;
        for (int i : allProductQuantities)
            if (i != 0) {
                noItems = false;
                itemsInCart++;
            }

        productNames = new String[itemsInCart];
        productPrices = new String[itemsInCart];
        productQuantities = new int[itemsInCart];
        picIds = new int[itemsInCart];

        int n = 0;
        for (int i = 0; i < allProductQuantities.length; i++) {
            if (allProductQuantities[i] != 0) {
                productNames[n] = namesCloud.get(i);
                productPrices[n] = pricesCloud.get(i);
                productQuantities[n] = allProductQuantities[i];
                picIds[n] = allPicIds[i];
                n++;
            }
        }

        if (noItems)
            noItms.setText("There is nothing in your cart.");

        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter2(this, productNames, productPrices, picIds, productQuantities, new RecyclerAdapter2.OnQuantityChangeListener() {

            @Override
            public void onQuantityChange(double difference) {
                totals.setText("€" + String.format("%.2f", difference));
            }

        });
        recyclerView.setAdapter(adapter);

        SharedPreferences sp = getSharedPreferences("total", Context.MODE_PRIVATE);
        if (sp.contains("total")) {
            totals.setText("€" + String.format("%.2f", Double.parseDouble(sp.getString("total", "0"))));
        }

    }

    public void goBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void otherStores(View view) {
        Intent intent = new Intent(this, StoresTotals.class);
        startActivity(intent);
    }
}