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
    private class DataStruct {
        Integer score;
        String name;
        String date;
    }
    Context context;
    ArrayList<DataStruct> mData = new ArrayList<>();
    public CardAdapter(Context context) {
        this.context = context;
        //Read data base
        //Put what was read into an ArrayList/ - initData()
    }

    void initData(){
        /*for each
                DataStruct tmp = new DataStruct();
                tmp.date =

                mData.add(tmp);*/
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

    }

    @Override
       public int getItemCount() {
           // the recycler view just wants to know thw number of items you want displayed
        return 0;
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{
        // grabbing the views from our row layout file
        //kinda like onCreate method


        TextView score,playerName,date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            playerName = itemView.findViewById(R.id.playerName);
            score = itemView.findViewById(R.id.score);
            date = itemView.findViewById(R.id.date);
        }
    }
}
