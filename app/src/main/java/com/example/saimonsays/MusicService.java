package com.example.saimonsays;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private static final String PREF_NAME = "MusicPrefs";
    private static final String KEY_MUTE_STATE = "isMuted";
    
    private MediaPlayer mediaPlayer;
    private final IBinder binder = new MusicBinder();
    private boolean isMuted = false;
    private SharedPreferences preferences;

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        isMuted = preferences.getBoolean(KEY_MUTE_STATE, false);
        
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusic);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true);
                Log.d(TAG, "MediaPlayer created successfully");
                if (!isMuted) {
                    mediaPlayer.start();
                }
            } else {
                Log.e(TAG, "Failed to create MediaPlayer");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error creating MediaPlayer: " + e.getMessage());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer != null && !mediaPlayer.isPlaying() && !isMuted) {
            try {
                mediaPlayer.start();
                Log.d(TAG, "Music started playing");
            } catch (Exception e) {
                Log.e(TAG, "Error starting music: " + e.getMessage());
            }
        }
        return START_STICKY;
    }

    public void playMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying() && !isMuted) {
            try {
                mediaPlayer.start();
                Log.d(TAG, "Music started playing");
            } catch (Exception e) {
                Log.e(TAG, "Error starting music: " + e.getMessage());
            }
        }
    }

    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            try {
                mediaPlayer.pause();
                Log.d(TAG, "Music paused");
            } catch (Exception e) {
                Log.e(TAG, "Error pausing music: " + e.getMessage());
            }
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                Log.d(TAG, "Music stopped");
            } catch (Exception e) {
                Log.e(TAG, "Error stopping music: " + e.getMessage());
            }
        }
    }

    public void setMute(boolean mute) {
        isMuted = mute;
        preferences.edit().putBoolean(KEY_MUTE_STATE, mute).apply();
        if (mute) {
            pauseMusic();
        } else {
            playMusic();
        }
    }

    public boolean isMuted() {
        return isMuted;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
                Log.d(TAG, "MediaPlayer released");
            } catch (Exception e) {
                Log.e(TAG, "Error releasing MediaPlayer: " + e.getMessage());
            }
        }
    }
} 