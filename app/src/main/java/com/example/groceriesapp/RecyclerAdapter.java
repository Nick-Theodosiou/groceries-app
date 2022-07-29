package com.example.groceriesapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    String productNames[];
    String productPrices[];
    int picIds[];
    String productNames2[];
    String productPrices2[];
    int picIds2[];
    Context context;

    public RecyclerAdapter(Context context, String names[], String prices[], int ids[]) {

        this.context = context;
        productNames = names;
        productPrices = prices;
        picIds = ids;
        productNames2 = productNames;
        productPrices2 = productPrices;
        picIds2 = picIds;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater LayoutInflater = android.view.LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.inflate(R.layout.product, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {

        holder.productImg.setImageResource(picIds[position]);
        holder.name.setText(productNames[position]);
        holder.name.setTextColor(Color.BLACK);
        holder.price.setText("€" + String.format("%.2f", Double.parseDouble(productPrices[position])));
        holder.price.setTextColor(Color.BLACK);

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Quantity.class);
                intent.putExtra("pictureId", picIds[position]);
                intent.putExtra("name", productNames[position]);
                intent.putExtra("price", productPrices[position]);
                intent.putExtra("position", position);
                v.getContext().startActivity(intent);
            }
        });
    }

    public void filterList(ArrayList<String> filteredNames) {
        String productNames3[] = new String[filteredNames.size()];
        String productPrices3[] = new String[filteredNames.size()];
        int picIds3[] = new int[filteredNames.size()];

        int n = 0;
        for (String name : filteredNames) {
            for (int i = 0; i < productNames2.length; i++) {
                if (productNames2[i].equals(name)) {
                    productNames3[n] = productNames2[i];
                    productPrices3[n] = productPrices2[i];
                    picIds3[n] = picIds2[i];
                    n++;
                }
            }
        }

        productNames = productNames3;
        productPrices = productPrices3;
        picIds = picIds3;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productNames.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImg, addButton;
        TextView name, price;

        public ViewHolder(View itemView) {
            super(itemView);
            productImg = itemView.findViewById(R.id.product_image);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            addButton = itemView.findViewById(R.id.add_button);
        }
    }
}
