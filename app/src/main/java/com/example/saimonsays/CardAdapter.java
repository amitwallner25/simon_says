package com.example.saimonsays;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {
    Context context;
    ArrayList  <CardModel> pictures;
    public CardAdapter(Context context, ArrayList<CardModel> pictures) {
        this.context = context;
        this.pictures = pictures;
    }

    @NonNull
    @Override
    public CardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // this is where you inflate the layout (giving a look to our rows)
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row,parent,false);
        return new CardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.MyViewHolder holder, int position) {
        // assigning values to the views we Created in the recycler_view_row layout file
        // based on the position of the recycler view

        holder.tvn.setText(pictures.get(position).getName());
        holder.imageView.setImageResource(pictures.get(position).getImage());
        holder.tvl.setText(pictures.get(position).getLatter());

    }

    @Override
    public int getItemCount() {
        // the recycler view just wants to know thw number of items you want displayed
        return pictures.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{
        // grabbing the views from our row layout file
        //kinda like onCreate method

        ImageView imageView;
        TextView tvn,tvl;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            tvn = itemView.findViewById(R.id.textView);
            tvl = itemView.findViewById(R.id.tvl);
        }
    }
}
