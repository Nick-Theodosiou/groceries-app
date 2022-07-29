package com.example.groceriesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;

public class RecyclerAdapter4 extends RecyclerView.Adapter<RecyclerAdapter4.ViewHolder4> {

    String stores[];
    int picIds[];
    String urls[];
    Context context;

    private DocumentReference storePrices;
    ArrayList<String> pricesCloud = new ArrayList<String>();
    ArrayList<String> namesCloud = new ArrayList<String>();
    ArrayList<String> categoriesCloud = new ArrayList<String>();
    ArrayList<Integer> quantities = new ArrayList<Integer>();
    private DocumentReference prodNames = FirebaseFirestore.getInstance().document("supermarkets/0Products");
    private DocumentReference prodCategories = FirebaseFirestore.getInstance().document("supermarkets/1Categories");

    public RecyclerAdapter4(Context context, String stores[], int picIds[], String urls[]) {
        this.context = context;
        this.stores = stores;
        this.picIds = picIds;
        this.urls = urls;
    }

    @NonNull
    @Override
    public RecyclerAdapter4.ViewHolder4 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater LayoutInflater = android.view.LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.inflate(R.layout.stores, parent, false);
        RecyclerAdapter4.ViewHolder4 viewHolder = new RecyclerAdapter4.ViewHolder4(view);
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull RecyclerAdapter4.ViewHolder4 holder, int position) {

        holder.image.setImageResource(picIds[position]);
        holder.name.setText(stores[position]);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storePrices = FirebaseFirestore.getInstance().document("supermarkets/" + stores[position]);
                loadData();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        saveData();
                    }
                }, 1500);

                SharedPreferences sp = context.getSharedPreferences("catStore", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sp.edit();
                editor2.putString("store name", stores[position]);
                editor2.apply();

                Intent intent = new Intent(v.getContext(), loadingScreen.class);
                context.startActivity(intent);
            }
        });

        holder.locations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent = new Intent("android.intent.action.VIEW", Uri.parse(urls[position]));
                context.startActivity(viewIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stores.length;
    }

    public class ViewHolder4 extends RecyclerView.ViewHolder {

        ImageView image = itemView.findViewById(R.id.storeImage);
        TextView name = itemView.findViewById(R.id.storeName);
        ImageButton locations = itemView.findViewById(R.id.locations);
        TextView button = itemView.findViewById(R.id.button);

        public ViewHolder4(View itemView) {
            super(itemView);
        }
    }

    private void loadData() {
        storePrices.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                if (doc.exists()) {
                    for (int i = 0; i < 10; i++) { //CHANGE IF AMOUNT OF PRODUCTS CHANGE
                        if (doc.contains(i + "")) {
                            final int j = i;
                            String price = doc.getString(i + "");
                            pricesCloud.add(price);
                            prodNames.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot doc) {

                                    if (doc.exists()) {
                                        if (doc.contains(j + "")) {
                                            String name = doc.getString(j + "");
                                            namesCloud.add(name);

                                            prodCategories.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot doc) {

                                                    if (doc.exists()) {
                                                        if (doc.contains(j + "")) {
                                                            String cat = doc.getString(j + "");
                                                            categoriesCloud.add(cat);
                                                        }
                                                    } else {
                                                        Toast.makeText(context.getApplicationContext(), "No Category document exists", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context.getApplicationContext(), "Error getting data!", Toast.LENGTH_LONG).show();
                                                }
                                            });

                                        }
                                    } else {
                                        Toast.makeText(context.getApplicationContext(), "No Category document exists", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context.getApplicationContext(), "Error getting data!", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                } else {
                    Toast.makeText(context.getApplicationContext(), "Store not yet implemented", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context.getApplicationContext(), "Error getting data!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("store info", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (sharedPreferences.contains("names")) {
            editor.remove("names");
            editor.remove("prices");
            editor.remove("categories");
            editor.remove("quantities");
            editor.apply();
        }

        for (int i = 0; i < namesCloud.size(); i++)
            quantities.add(i, 0);

        Gson gson = new Gson();

        String json = gson.toJson(namesCloud);
        editor.putString("names", json);

        json = gson.toJson(pricesCloud);
        editor.putString("prices", json);

        json = gson.toJson(categoriesCloud);
        editor.putString("categories", json);

        json = gson.toJson(quantities);
        editor.putString("quantities", json);

        editor.apply();
    }
}