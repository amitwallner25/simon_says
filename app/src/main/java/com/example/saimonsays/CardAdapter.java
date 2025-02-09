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
    private Context context;
    private ArrayList<CardModel> mData;

    public CardAdapter(Context context, ArrayList<CardModel> data) {
        this.context = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public CardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.MyViewHolder holder, int position) {
        // Set data from database
        CardModel currentPlayer = mData.get(position);
        holder.playerName.setText(currentPlayer.getPlayerName());
        holder.score.setText(String.valueOf(currentPlayer.getScore())); // Convert int to String
        holder.date.setText(currentPlayer.getDate());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView score, playerName, date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.playerName);
            score = itemView.findViewById(R.id.score);
            date = itemView.findViewById(R.id.date);
        }
    }

    // Method to update the list dynamically
    public void updateData(ArrayList<CardModel> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged(); // Refresh RecyclerView
    }
}

