package com.example.saimonsays;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class MusicActivity extends AppCompatActivity {
    protected MusicService musicService;
    protected boolean bound = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("BaseActivity", "Service connected");
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            bound = true;
            if (musicService != null) {
                Log.d("BaseActivity", "Starting music");
                musicService.playMusic();
            } else {
                Log.e("BaseActivity", "MusicService is null after binding");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("BaseActivity", "Service disconnected");
            bound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Start and bind to the music service
        Log.d("BaseActivity", "Starting music service");
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bound && musicService != null) {
            musicService.playMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bound && musicService != null) {
            musicService.pauseMusic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bound) {
            unbindService(connection);
            bound = false;
        }
    }
} 