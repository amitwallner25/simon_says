package com.example.saimonsays;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class LeaderBoards extends AppCompatActivity {
    ArrayList  <Pictures> pictures = new ArrayList<>();

    int [] pictureArr = {R.drawable.bathman, R.drawable.baseline_attribution_24,R.drawable.bathman,
            R.drawable.bathman,R.drawable.bathman,R.drawable.bathman,R.drawable.bathman,
            R.drawable.bathman,R.drawable.bathman,R.drawable.bathman,R.drawable.bathman,
            R.drawable.bathman,R.drawable.bathman,R.drawable.bathman,R.drawable.bathman,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.myrecycleview);

        setUpPictures();
        Recyclerviewadapter adapter = new Recyclerviewadapter( this,pictures);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void  setUpPictures(){
        String [] pName = getResources().getStringArray(R.array.pName);
        String [] latarr = getResources().getStringArray(R.array.RandomLatter);

        for (int i = 0; i < pName.length; i++){
            pictures.add(new Pictures(pName[i],pictureArr[i],latarr[i]));
        }
    }
}