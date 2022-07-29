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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RecyclerAdapter5 extends RecyclerView.Adapter<RecyclerAdapter5.ViewHolder5> {

    String stores[];
    int picIds[];
    Context context;
    int chosen = -1;

    ArrayList<Integer> quantities = new ArrayList<Integer>();

    public RecyclerAdapter5(Context context, String stores[], int picIds[]) {
        this.context = context;
        this.stores = stores;
        this.picIds = picIds;

        SharedPreferences sharedPreferences = context.getSharedPreferences("store info", context.MODE_PRIVATE);
        Gson gson = new Gson();

        String json = sharedPreferences.getString("quantities", null);
        Type type = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        quantities = gson.fromJson(json, type);

        SharedPreferences sp = context.getSharedPreferences("catStore", Context.MODE_PRIVATE);
        String storeName = sp.getString("store name", "");

        for (int i = 0; i < stores.length; i++)
            if (stores[i].equals(storeName))
                chosen = i;
    }

    @NonNull
    @Override
    public RecyclerAdapter5.ViewHolder5 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater LayoutInflater = android.view.LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.inflate(R.layout.stores2, parent, false);
        RecyclerAdapter5.ViewHolder5 viewHolder = new RecyclerAdapter5.ViewHolder5(view);
        return viewHolder;
    }

    public void onBindViewHolder(@NonNull RecyclerAdapter5.ViewHolder5 holder, int position) {

        holder.image.setImageResource(picIds[position]);
        holder.name.setText(stores[position]);
        holder.total.setText("€" + String.format("%.2f", calculateTotal(position)));
        holder.selected.setChecked(position == chosen);
    }

    @Override
    public int getItemCount() {
        return stores.length;
    }

    public class ViewHolder5 extends RecyclerView.ViewHolder {

        ImageView image = itemView.findViewById(R.id.storeImage);
        TextView name = itemView.findViewById(R.id.storeName);
        TextView total = itemView.findViewById(R.id.total);
        TextView button = itemView.findViewById(R.id.button);
        RadioButton selected = itemView.findViewById(R.id.selected);

        public ViewHolder5(View itemView) {

            super(itemView);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chosen = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });

            selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chosen = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }

    private double calculateTotal(int position) {
        double total = 0;
        SharedPreferences sharedPreferences = context.getSharedPreferences("all prices", context.MODE_PRIVATE);
        Gson gson = new Gson();

        ArrayList<String> storePrices = new ArrayList<>();
        String json = sharedPreferences.getString(stores[position], null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        storePrices = gson.fromJson(json, type);

        for (int i = 0; i < quantities.size(); i++)
            if (storePrices.get(i) != null)
                total += (double) quantities.get(i) * Double.parseDouble(storePrices.get(i));

        return total;
    }
}
