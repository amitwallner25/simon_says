package com.example.saimonsays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.ToggleButton;

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
    private static final String KEY_FRAGMENT_STATE = "currentFragment";

    private ImageButton returnButton;
    private Switch musicSwitch;
    private Switch buttonSoundsSwitch;
    private Button logoutButton;
    private ImageButton imageButton2;
    private ImageButton imageButton3;
    private ToggleButton toggleButton2;
    private ToggleButton toggleButton3;
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
        imageButton2 = findViewById(R.id.imageButton2);
        imageButton3 = findViewById(R.id.imageButton3);
        toggleButton2 = findViewById(R.id.toggleButton2);
        toggleButton3 = findViewById(R.id.toggleButton3);

        // Set initial state of the music switch based on SharedPreferences
        boolean isMuted = preferences.getBoolean(KEY_MUTE_STATE, false);
        musicSwitch.setChecked(isMuted);

        // Set initial state of the button sounds switch based on SharedPreferences
        boolean buttonSoundsEnabled = preferences.getBoolean(KEY_BUTTON_SOUNDS_STATE, true);
        buttonSoundsSwitch.setChecked(buttonSoundsEnabled);

        // Set initial state of fragment selection
        String currentFragment = preferences.getString(KEY_FRAGMENT_STATE, "default");
        if (currentFragment.equals("default")) {
            toggleButton2.setChecked(true);
            toggleButton3.setChecked(false);
        } else {
            toggleButton2.setChecked(false);
            toggleButton3.setChecked(true);
        }

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

        // Handle fragment selection
        View.OnClickListener fragmentClickListener = v -> {
            if (v == imageButton2 || v == toggleButton2) {
                preferences.edit().putString(KEY_FRAGMENT_STATE, "default").apply();
                toggleButton2.setChecked(true);
                toggleButton3.setChecked(false);
            } else if (v == imageButton3 || v == toggleButton3) {
                preferences.edit().putString(KEY_FRAGMENT_STATE, "second").apply();
                toggleButton2.setChecked(false);
                toggleButton3.setChecked(true);
            }
        };

        imageButton2.setOnClickListener(fragmentClickListener);
        imageButton3.setOnClickListener(fragmentClickListener);
        toggleButton2.setOnClickListener(fragmentClickListener);
        toggleButton3.setOnClickListener(fragmentClickListener);

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