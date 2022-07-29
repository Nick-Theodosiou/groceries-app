package com.example.groceriesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    EditText search;
    private int lastPosition;
    ArrayList<String> pricesCloud = new ArrayList<String>();

    ArrayList<String> namesCloud = new ArrayList<String>();
    ArrayList<String> allNamesCloud = new ArrayList<String>();

    ArrayList<String> categoriesCloud = new ArrayList<String>();
    ArrayList<Integer> quantities = new ArrayList<>();
    String category;

    String productNames[] = {""};
    String productPrices[] = {""};
    int picIds[] = {};
    int allPicIds[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.mainGray));
        }

        SharedPreferences sp4 = getSharedPreferences("catStore", Context.MODE_PRIVATE);
        category = sp4.getString("category", "");

        loadData();
        productNames = new String[pricesCloud.size()];
        productPrices = new String[pricesCloud.size()];
        picIds = new int[pricesCloud.size()];

        int i = 0;
        for (String n : namesCloud) {
            productNames[i] = n;
            String str = productNames[i].replaceAll("\\s+", "").toLowerCase();
            picIds[i] = getResources().getIdentifier(str, "drawable", getPackageName());
            i++;
        }

        i = 0;
        for (String n : allNamesCloud) {
            String str = n.replaceAll("\\s+", "").toLowerCase();
            allPicIds[i] = getResources().getIdentifier(str, "drawable", getPackageName());
            i++;
        }

        i = 0;
        for (String p : pricesCloud) {
            productPrices[i] = p;
            i++;
        }

        search = findViewById(R.id.search_bar);
        recyclerView = findViewById(R.id.recyclerView);
        final LinearLayoutManager lm = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(lm);
        adapter = new RecyclerAdapter(MainActivity.this, productNames, productPrices, picIds);
        recyclerView.setAdapter(adapter);

        SharedPreferences sp = getSharedPreferences("position", Context.MODE_PRIVATE);
        lastPosition = sp.getInt("position", 0);
        recyclerView.scrollToPosition(lastPosition);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                lastPosition = lm.findFirstVisibleItemPosition();
            }
        });

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
        ArrayList<String> filteredNames = new ArrayList<>();
        for (String n : productNames) {
            if (n.toLowerCase().contains(text.toLowerCase())) {
                filteredNames.add(n);
            }
        }

        adapter.filterList(filteredNames);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("store info", MODE_PRIVATE);
        Gson gson = new Gson();

        String json = sharedPreferences.getString("names", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        namesCloud = gson.fromJson(json, type);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        SharedPreferences sp = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        if (sp.contains("quantity") & sp.contains("name")) {

            String name = sp.getString("name", "");
            int q = sp.getInt("quantity", 0);

            int temp;
            for (int i = 0; i < allNamesCloud.size(); i++) {
                if (allNamesCloud.get(i).equals(name)) {
                    temp = quantities.get(i);
                    temp = temp + q;

                    quantities.set(i, temp);
                    json = gson.toJson(quantities);
                    editor.putString("quantities", json);
                    editor.apply();
                }
            }

            int position = 0;
            for (int i = 0; i < productNames.length; i++)
                if (productNames[i].equals(name))
                    position = i;

            if (q != 0)
                Toast.makeText(MainActivity.this, productNames[position] + " added to cart", Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor2 = sp.edit();
            editor2.clear();
            editor2.apply();
        }
    }

    public void cart(View view) {
        Intent intent = new Intent(MainActivity.this, Cart.class);
        intent.putExtra("picIds", allPicIds);

        int productQuantities[] = new int[quantities.size()];
        for (int i = 0; i < quantities.size(); i++)
            productQuantities[i] = quantities.get(i);
        intent.putExtra("quantities", productQuantities);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sp2 = getSharedPreferences("position", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp2.edit();
        editor.putInt("position", lastPosition);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("store info", MODE_PRIVATE);
        Gson gson = new Gson();

        String json = sharedPreferences.getString("names", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        namesCloud = gson.fromJson(json, type);
        allNamesCloud = namesCloud;
        allPicIds = new int[allNamesCloud.size()];

        json = sharedPreferences.getString("prices", null);
        type = new TypeToken<ArrayList<String>>() {
        }.getType();
        pricesCloud = gson.fromJson(json, type);

        json = sharedPreferences.getString("categories", null);
        type = new TypeToken<ArrayList<String>>() {
        }.getType();
        categoriesCloud = gson.fromJson(json, type);

        json = sharedPreferences.getString("quantities", null);
        type = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        quantities = gson.fromJson(json, type);

        ArrayList<String> t1 = new ArrayList<String>();
        ArrayList<String> t2 = new ArrayList<String>();
        ArrayList<String> t3 = new ArrayList<String>();
        for (int i = 0; i < categoriesCloud.size(); i++) {
            if (categoriesCloud.get(i).equals(category) || category.equals("All products")) {
                t1.add(namesCloud.get(i));
                t2.add(pricesCloud.get(i));
                t3.add(categoriesCloud.get(i));
            }
        }
        namesCloud = t1;
        pricesCloud = t2;
        categoriesCloud = t3;
    }
}