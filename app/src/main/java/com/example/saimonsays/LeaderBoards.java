package com.example.saimonsays;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ImageButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LeaderBoards extends MusicActivity {
    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private GameDatabaseHelper dbHelper;
    private ArrayList<CardModel> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_boards);

        recyclerView = findViewById(R.id.recyclerView);
        ImageButton imageButton = findViewById(R.id.imageButton);

        dbHelper = new GameDatabaseHelper(this);
        playerList = dbHelper.getAllPlayers(); // Fetch players from DB

        adapter = new CardAdapter(this, playerList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(LeaderBoards.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        playerList = dbHelper.getAllPlayers(); // Fetch latest data
        adapter.updateData(playerList); // Update RecyclerView
    }
}

