package com.example.groceriesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductCategories extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerAdapter3 adapter;
    String categories[] = {"All products", "Drinks", "Fruits", "Snacks"};
    int picIds[];
    EditText search;
    TextView store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_categories);

        search = findViewById(R.id.search_bar);
        store = findViewById(R.id.title);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.mainGray));
        }

        SharedPreferences sp1 = getSharedPreferences("catStore", MODE_PRIVATE);
        store.setText(sp1.getString("store name", ""));

        SharedPreferences sp = getSharedPreferences("position", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();

        picIds = new int[categories.length];
        for (int i = 0; i < categories.length; i++) {
            String str = categories[i].replaceAll("\\s+", "").toLowerCase();
            picIds[i] = getResources().getIdentifier(str, "drawable", getPackageName());
        }

        recyclerView = findViewById(R.id.recyclerView3);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mGridLayoutManager);
        adapter = new RecyclerAdapter3(this, categories, picIds);
        recyclerView.setAdapter(adapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        ArrayList<String> filteredCats = new ArrayList<>();
        for (String n : categories) {
            if (n.toLowerCase().contains(text.toLowerCase())) {
                filteredCats.add(n);
            }
        }

        adapter.filterList(filteredCats);
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, Stores.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Stores.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}