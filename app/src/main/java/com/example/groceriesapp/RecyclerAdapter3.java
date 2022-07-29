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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter3 extends RecyclerView.Adapter<RecyclerAdapter3.ViewHolder3> {

    String categories[];
    int picIds[];
    String categories2[];
    int picIds2[];
    Context context;

    public RecyclerAdapter3(Context context, String categories[], int picIds[]) {
        this.context = context;

        this.categories = categories;
        this.picIds = picIds;

        categories2=categories;
        picIds2=picIds;
    }

    @NonNull
    @Override
    public RecyclerAdapter3.ViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater LayoutInflater = android.view.LayoutInflater.from(parent.getContext());
        View view = LayoutInflater.inflate(R.layout.category, parent, false);
        RecyclerAdapter3.ViewHolder3 viewHolder = new RecyclerAdapter3.ViewHolder3(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter3.ViewHolder3 holder, int position) {

        holder.image.setImageResource(picIds[position]);
        holder.title.setText(categories[position]);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = context.getSharedPreferences("newQuantities", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("new", true);
                editor.apply();

                SharedPreferences sp2 = context.getSharedPreferences("catStore", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = sp2.edit();
                editor2.putString("category", categories[position]);
                editor2.apply();

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    public void filterList(ArrayList<String> filteredCats) {
        String categories3[] = new String[filteredCats.size()];
        int picIds3[] = new int[filteredCats.size()];

        int n = 0;
        for (String name : filteredCats) {
            for (int i = 0; i < categories2.length; i++) {
                if (categories2[i].equals(name)) {
                    categories3[n] = categories2[i];
                    picIds3[n] = picIds2[i];
                    n++;
                }
            }
        }

        categories = categories3;
        picIds = picIds3;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    public class ViewHolder3 extends RecyclerView.ViewHolder {

        ImageView image = itemView.findViewById(R.id.categoryImg);
        TextView title = itemView.findViewById(R.id.categoryTxt);
        CardView cardView = itemView.findViewById(R.id.cardView);

        public ViewHolder3(View itemView) {
            super(itemView);

        }
    }
}
