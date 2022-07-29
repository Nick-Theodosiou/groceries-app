package com.example.groceriesapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.ViewHolder2> {

    String productNames[];
    String productPrices[];
    int picIds[];
    int productQuantities[];
    Context context;
    Double total = 0.0;
    private OnQuantityChangeListener mListener;
    ArrayList<Integer> quantities = new ArrayList<>();
    ArrayList<String> namesCloud = new ArrayList<>();

    public interface OnQuantityChangeListener {
        void onQuantityChange(double change);
    }

    public RecyclerAdapter2(Context context, String names[], String prices[], int ids[], int qnts[], OnQuantityChangeListener listener) {
        this.context = context;
        mListener = listener;

        int n = 0;
        for (int i = 0; i < qnts.length; i++) {
            if (qnts[i] > 0) {
                n++;
            }
        }

        productNames = new String[n];
        productPrices = new String[n];
        picIds = new int[n];
        productQuantities = new int[n];

        n = 0;
        for (int i = 0; i < qnts.length; i++) {
            if (qnts[i] > 0) {
                total += (Double.parseDouble(prices[i]) * qnts[i]);
                productNames[n] = names[i];
                productPrices[n] = prices[i];
                picIds[n] = ids[i];
                productQuantities[n] = qnts[i];
                n++;
            }
        }

        SharedPreferences sp = context.getSharedPreferences("total", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("total", total + "");
        editor.apply();
    }

    @NonNull
    @Override
    public RecyclerAdapter2.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater LayoutInflater = android.view.LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.inflate(R.layout.product2, parent, false);
        RecyclerAdapter2.ViewHolder2 viewHolder = new RecyclerAdapter2.ViewHolder2(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter2.ViewHolder2 holder, int position) {

        holder.productImg.setImageResource(picIds[position]);
        holder.name.setText(productNames[position]);
        holder.price.setText("€" + String.format("%.2f", Double.parseDouble(productPrices[position]) * productQuantities[position]));
        holder.price.setTextColor(Color.BLACK);
        holder.qnt.setText(productQuantities[position] + "");

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productQuantities[position]++;
                holder.qnt.setText(productQuantities[position] + "");
                holder.price.setText("€" + String.format("%.2f", Double.parseDouble(productPrices[position]) * productQuantities[position]));

                SharedPreferences sp = context.getSharedPreferences("updatedQuantity", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(productNames[position], productNames[position]);
                editor.putInt("newQnt" + productNames[position], productQuantities[position]);
                editor.apply();

                total = 0.0;
                for (int i = 0; i < productQuantities.length; i++)
                    if (productQuantities[i] > 0)
                        total += (Double.parseDouble(productPrices[i]) * productQuantities[i]);
                mListener.onQuantityChange(total);

                //change qnt in sp
                SharedPreferences sharedPreferences = context.getSharedPreferences("store info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedPreferences.edit();
                Gson gson = new Gson();

                String json = sharedPreferences.getString("quantities", null);
                Type type = new TypeToken<ArrayList<Integer>>() {
                }.getType();
                quantities = gson.fromJson(json, type);

                json = sharedPreferences.getString("names", null);
                type = new TypeToken<ArrayList<String>>() {
                }.getType();
                namesCloud = gson.fromJson(json, type);

                int temp;
                for (int i = 0; i < namesCloud.size(); i++)
                    if (namesCloud.get(i).equals(productNames[position])) {
                        temp = quantities.get(i);
                        temp = temp + 1;

                        quantities.set(i, temp);
                        json = gson.toJson(quantities);
                        editor2.putString("quantities", json);
                        editor2.apply();
                    }
            }
        });

        holder.minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(holder.qnt.getText() + "") > 0)
                    productQuantities[position]--;
                holder.qnt.setText(productQuantities[position] + "");
                holder.price.setText("€" + String.format("%.2f", Double.parseDouble(productPrices[position]) * productQuantities[position]));

                SharedPreferences sp = context.getSharedPreferences("updatedQuantity", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(productNames[position], productNames[position]);
                editor.putInt("newQnt" + productNames[position], productQuantities[position]);
                editor.apply();

                total = 0.0;
                for (int i = 0; i < productQuantities.length; i++)
                    if (productQuantities[i] > 0)
                        total += (Double.parseDouble(productPrices[i]) * productQuantities[i]);
                mListener.onQuantityChange(total);

                //change qnt in sp
                SharedPreferences sharedPreferences = context.getSharedPreferences("store info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedPreferences.edit();
                Gson gson = new Gson();

                String json = sharedPreferences.getString("quantities", null);
                Type type = new TypeToken<ArrayList<Integer>>() {
                }.getType();
                quantities = gson.fromJson(json, type);

                json = sharedPreferences.getString("names", null);
                type = new TypeToken<ArrayList<String>>() {
                }.getType();
                namesCloud = gson.fromJson(json, type);

                int temp;
                for (int i = 0; i < namesCloud.size(); i++)
                    if (namesCloud.get(i).equals(productNames[position])) {
                        temp = quantities.get(i);
                        temp = temp - 1;

                        quantities.set(i, temp);
                        json = gson.toJson(quantities);
                        editor2.putString("quantities", json);
                        editor2.apply();
                    }
            }
        });

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                productQuantities[position] = 0;

                SharedPreferences sp = context.getSharedPreferences("updatedQuantity", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(productNames[position], productNames[position]);
                editor.putInt("newQnt" + productNames[position], productQuantities[position]);
                editor.apply();

                total = 0.0;
                for (int i = 0; i < productQuantities.length; i++)
                    if (productQuantities[i] > 0)
                        total += (Double.parseDouble(productPrices[i]) * productQuantities[i]);
                mListener.onQuantityChange(total);

                //change qnt in sp
                SharedPreferences sharedPreferences = context.getSharedPreferences("store info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sharedPreferences.edit();
                Gson gson = new Gson();

                String json = sharedPreferences.getString("quantities", null);
                Type type = new TypeToken<ArrayList<Integer>>() {
                }.getType();
                quantities = gson.fromJson(json, type);

                json = sharedPreferences.getString("names", null);
                type = new TypeToken<ArrayList<String>>() {
                }.getType();
                namesCloud = gson.fromJson(json, type);

                int temp;
                for (int i = 0; i < namesCloud.size(); i++)
                    if (namesCloud.get(i).equals(productNames[position])) {
                        temp = quantities.get(i);
                        temp = 0;

                        quantities.set(i, temp);
                        json = gson.toJson(quantities);
                        editor2.putString("quantities", json);
                        editor2.apply();
                    }

                if (productNames.length > 0) {
                    String productNames2[] = new String[productNames.length - 1];
                    String productPrices2[] = new String[productNames.length - 1];
                    int picIds2[] = new int[productNames.length - 1];
                    int productQuantities2[] = new int[productNames.length - 1];

                    int n = 0;
                    for (int i = 0; i < productNames.length; i++) {
                        if (i != position) {
                            productNames2[n] = productNames[i];
                            productPrices2[n] = productPrices[i];
                            picIds2[n] = picIds[i];
                            productQuantities2[n] = productQuantities[i];
                            n++;
                        }
                    }

                    productNames = new String[n];
                    productPrices = new String[n];
                    picIds = new int[n];
                    productQuantities = new int[n];

                    productNames = productNames2;
                    productPrices = productPrices2;
                    picIds = picIds2;
                    productQuantities = productQuantities2;
                }

                notifyItemRemoved(position);
                notifyItemRangeChanged(position, productNames.length);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productNames.length;
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {

        ImageView productImg;
        ImageButton addButton, minusButton;
        TextView name, price, qnt;
        Button removeButton;

        private Context context;

        public ViewHolder2(View itemView) {
            super(itemView);
            context = itemView.getContext();
            productImg = itemView.findViewById(R.id.product_image);
            qnt = itemView.findViewById(R.id.quantity);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.subtotal_money);
            addButton = itemView.findViewById(R.id.plus_btn);
            minusButton = itemView.findViewById(R.id.minus_btn);
            removeButton = itemView.findViewById(R.id.remove);
        }
    }
}
