package com.example.saimonsays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Settings extends BaseActivity {
    private static final String PREF_NAME = "MusicPrefs";
    private static final String KEY_MUTE_STATE = "isMuted";
    private static final String KEY_BUTTON_SOUNDS_STATE = "buttonSoundsEnabled";
    private static final String USER_PREF_NAME = "UserPrefs";
    private static final String KEY_LOGGED_IN_USER = "loggedInUser";

    private ImageButton returnButton;
    private Switch musicSwitch;
    private Switch buttonSoundsSwitch;
    private Button logoutButton;
    private SharedPreferences preferences;
    private SharedPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        userPreferences = getSharedPreferences(USER_PREF_NAME, MODE_PRIVATE);
        returnButton = findViewById(R.id.returnButton);
        musicSwitch = findViewById(R.id.musicSwitch);
        buttonSoundsSwitch = findViewById(R.id.ButtonSoundsSwitch);
        logoutButton = findViewById(R.id.logoutButton);

        // Set initial state of the music switch based on SharedPreferences
        boolean isMuted = preferences.getBoolean(KEY_MUTE_STATE, false);
        musicSwitch.setChecked(isMuted);

        // Set initial state of the button sounds switch based on SharedPreferences
        boolean buttonSoundsEnabled = preferences.getBoolean(KEY_BUTTON_SOUNDS_STATE, true);
        buttonSoundsSwitch.setChecked(buttonSoundsEnabled);

        // Handle music switch state changes
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (bound && musicService != null) {
                musicService.setMute(isChecked);
                preferences.edit().putBoolean(KEY_MUTE_STATE, isChecked).apply();
            }
        });

        // Handle button sounds switch state changes
        buttonSoundsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit().putBoolean(KEY_BUTTON_SOUNDS_STATE, isChecked).apply();
        });

        // Handle logout button click
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the logged in user from SharedPreferences
                userPreferences.edit().remove(KEY_LOGGED_IN_USER).apply();
                
                // Navigate to LoginActivity
                Intent intent = new Intent(Settings.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}