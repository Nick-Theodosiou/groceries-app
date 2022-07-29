package com.example.groceriesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class loadingScreen extends AppCompatActivity {

    private boolean ended;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.mainGray));
        }

        ended = false;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (ended == false) {
                    Intent intent = new Intent(loadingScreen.this, ProductCategories.class);
                    startActivity(intent);
                }
            }
        }, 1600);

    }

    public void onBackPressed() {
        Intent intent = new Intent(this, Stores.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        ended = true;
        startActivity(intent);
        finish();
    }
}