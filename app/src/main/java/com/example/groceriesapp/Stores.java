package com.example.groceriesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Stores extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter4 adapter1;
    private String stores[] = {"AlphaMega", "Lidl", "Athienitis", "Metro", "Kkolias", "Papantoniou", "Smart"};
    String storesPrices[][] = new String[7][10]; //CHANGE IF AMOUNT OF PRODUCTS CHANGE //AlphaMegaPrices, LidlPrices, AthienitisPrices, MetroPrices, KkoliasPrices, SmartPrices;
    private String urls[] = {"https://www.google.com/search?bih=880&biw=1920&hl=en-GB&tbs=lf:1,lf_ui:4&tbm=lcl&sxsrf=ALeKk01iO8K-FevPHbpSJAVYlkycWuY4CA:1626476414105&q=alphamega&rflfq=1&num=10&ved=2ahUKEwiSnpfn2OjxAhVgBGMBHVcRBDcQtgN6BAgQEAQ#rlfi=hd:;si:;mv:[[35.1677302,33.391467899999995],[35.0832979,33.2783924]];tbs:lrf:!1m4!1u3!2m2!3m1!1e1!2m1!1e3!3sIAE,lf:1,lf_ui:4",
    "https://www.google.com/search?q=lidl&bih=880&biw=1920&hl=en-GB&tbm=lcl&sxsrf=ALeKk01z-g10LytEqDa8Nr5V-iJLczktNA%3A1626476417250&ei=gQ_yYIDMDsrlgwfk0ZfQAw&oq=lidl&gs_l=psy-ab.3..35i39k1j0i433i67k1j0i67k1j0i433k1l3j0i457k1j0i402k1j0l2.25395.28152.0.28766.12.9.2.0.0.0.147.933.1j7.8.0....0...1c.1.64.psy-ab..2.10.943...0i433i131k1j0i10k1j35i305i39k1j0i433i10k1j0i457i10k1j0i273k1.0.1LRKrOOuFow#rlfi=hd:;si:;mv:[[35.38332441892572,34.748192182400935],[34.567515887473235,32.688255658963435],null,[34.976435947829636,33.718223920682185],10]",
    "https://www.google.com/search?q=athienitis+supermarket&bih=880&biw=1920&hl=en-GB&tbm=lcl&sxsrf=ALeKk02qKSy6c0fk11fFGiBOu1e51dw7PQ%3A1626476484183&ei=xA_yYKTHCt2bjLsP3--XkAw&oq=athienitis+s&gs_l=psy-ab.1.0.0l10.5623.5991.0.6669.2.2.0.0.0.0.126.234.0j2.2.0....0...1c.1.64.psy-ab..0.2.234....0.lRa2MAiYeMA#rlfi=hd:;si:;mv:[[35.174392399999995,33.383797099999995],[35.1320933,33.2724507]];tbs:lrf:!1m4!1u3!2m2!3m1!1e1!2m1!1e3!3sIAE,lf:1,lf_ui:4",
    "https://www.google.com/search?q=metro+supermarket&bih=880&biw=1920&hl=en-GB&tbm=lcl&sxsrf=ALeKk02v5IZ-HBjxkJnhtLu7_zkkhILAvw%3A1626476512135&ei=4A_yYPHkB9mGjLsPlIuksAM&oq=metro+&gs_l=psy-ab.1.5.0i67k1j0i457i67k1j0i402k1l2j0i67k1j0i263i433i20k1j0l4.2993.2993.0.4707.1.1.0.0.0.0.126.126.0j1.1.0....0...1c.1.64.psy-ab..0.1.126....0.hrOhgAg8-PY#rlfi=hd:;si:;mv:[[35.1709775,33.6428746],[34.906797100000006,33.3058736]];tbs:lrf:!1m4!1u3!2m2!3m1!1e1!2m1!1e3!3sIAE,lf:1,lf_ui:4",
    "https://www.google.com/search?q=kkolias&bih=880&biw=1920&hl=en-GB&tbm=lcl&sxsrf=ALeKk02yUfqap2xmXm2xRfAM4VbZkLcR8Q%3A1626476517758&ei=5Q_yYOrfLamejLsP8Pmj-Ao&oq=kkolias&gs_l=psy-ab.3..0i67k1j0l2j0i263i20k1j38j0i30k1l2j0i10i30k1j0i5i10i30k1j0i10i30k1.19600.20567.0.20725.7.7.0.0.0.0.134.615.0j5.5.0....0...1c.1.64.psy-ab..2.5.615...0i433i131k1j0i433k1j0i10k1.0.blAQj9OfGTg",
    "https://www.google.com/search?q=papantoniou+supermarket&bih=880&biw=1920&hl=en-GB&tbm=lcl&sxsrf=ALeKk03pq8cizfFAlkeGS6HNMI_fuE7CRg%3A1626476539305&ei=-w_yYJj0EZ-CjLsP9cq7wAM&oq=papa&gs_l=psy-ab.1.0.35i39k1j0i433i131i273k1j0i263i20k1j0i433i131k1j0l6.17643.17957.0.20015.4.4.0.0.0.0.143.265.0j2.2.0....0...1c.1.64.psy-ab..2.2.265...0i433k1.0.nDXppRZ1BX0",
    "https://www.google.com/search?q=smart+supermarket&bih=880&biw=1920&hl=en-GB&tbm=lcl&sxsrf=ALeKk00KKBcr9J4HTThus6_mTa2TI9oqyg%3A1626476559991&ei=DxDyYKuFPITgU6bggeAK&oq=smart&gs_l=psy-ab.3.0.0i433i131i457i67k1j0i402k1l2j0i67k1j0i433i67k1j0l5.16737.17487.0.19629.5.4.0.1.1.0.126.491.0j4.4.0....0...1c.1.64.psy-ab..0.5.496...35i39k1j0i433i131i67k1j0i433i131k1j0i273k1.0.AfA5RXin9CQ#rlfi=hd:;si:;mv:[[35.203733799999995,34.0680853],[34.6423774,32.351642600000005]];tbs:lrf:!1m4!1u3!2m2!3m1!1e1!2m1!1e3!3sIAE,lf:1,lf_ui:4"};
    private int picIds[];
    private DocumentReference store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.mainGray));
        }

        loadPrices();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                savePrices();
            }
        }, 1500);
        picIds = new int[stores.length];

        for (int i = 0; i < stores.length; i++) {
            String str = stores[i].replaceAll("\\s+", "").toLowerCase();
            picIds[i] = getResources().getIdentifier(str, "drawable", getPackageName());
        }

        recyclerView = findViewById(R.id.recyclerView);
        final LinearLayoutManager lm = new LinearLayoutManager(Stores.this);
        recyclerView.setLayoutManager(lm);
        adapter1 = new RecyclerAdapter4(Stores.this, stores, picIds, urls);
        recyclerView.setAdapter(adapter1);
    }

    public void settings(View v) {
//        Intent viewIntent =
//                new Intent("android.intent.action.VIEW",
//                        Uri.parse("https://www.google.com/search?sa=X&bih=937&biw=1920&hl=en-GB&tbs=lf:1,lf_ui:4&tbm=lcl&sxsrf=ALeKk01ywBZDP7mqzG5cXp1ccx0Dg02rRg:1626472848637&q=alfa+mega&rflfq=1&num=10&ved=2ahUKEwitt4TDy-jxAhX3C2MBHTA0Bz4QtgN6BAgTEAQ#rlfi=hd:;si:;mv:[[35.1665056,33.3900059],[35.1049319,33.304221]];tbs:lrf:!1m4!1u3!2m2!3m1!1e1!2m1!1e3!3sIAE,lf:1,lf_ui:4"));
//        startActivity(viewIntent);
    }

    public void loadPrices(){

        for(int i=0;i<stores.length;i++) {
            store = FirebaseFirestore.getInstance().document("supermarkets/" + stores[i]);
            int storePointer = i;
            store.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot doc) {
                    if (doc.exists()) {
                        for (int i = 0; i < 10; i++) { //CHANGE IF AMOUNT OF PRODUCTS CHANGE
                            if (doc.contains(i + "")) {
                                final int j = i;
                                String price = doc.getString(i + "");
                                storesPrices[storePointer][i] = price;
                            }
                        }
                    } else {
                        //Toast.makeText(Stores.this, "Error getting prices", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Stores.this, "Error getting prices", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void savePrices(){
        SharedPreferences sharedPreferences = getSharedPreferences("all prices", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (int i=0;i<stores.length;i++){
            ArrayList<String> temp = new ArrayList<>();

            if (sharedPreferences.contains(stores[i])) {
                editor.remove(stores[i]);
                editor.apply();
            }

            for (int j = 0; j < storesPrices[i].length; j++)
                temp.add(storesPrices[i][j]);

            Gson gson = new Gson();

            String json = gson.toJson(temp);
            editor.putString(stores[i], json);

            editor.apply();
        }
    }
}