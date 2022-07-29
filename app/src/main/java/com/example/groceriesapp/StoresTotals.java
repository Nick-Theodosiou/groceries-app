package com.example.groceriesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class StoresTotals extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter5 adapter;
    private String stores[] = {"AlphaMega", "Lidl", "Athienitis", "Metro", "Kkolias", "Papantoniou", "Smart"};
    private int picIds[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores_totals);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.mainGray));
        }

        picIds = new int[stores.length];

        for (int i = 0; i < stores.length; i++) {
            String str = stores[i].replaceAll("\\s+", "").toLowerCase();
            picIds[i] = getResources().getIdentifier(str, "drawable", getPackageName());
        }

        recyclerView = findViewById(R.id.recyclerView);
        final LinearLayoutManager lm = new LinearLayoutManager(StoresTotals.this);
        recyclerView.setLayoutManager(lm);
        adapter = new RecyclerAdapter5(StoresTotals.this, stores, picIds);
        recyclerView.setAdapter(adapter);
    }

    public void goBack(View v){
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
}