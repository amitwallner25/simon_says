package com.example.saimonsays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Settings extends BaseActivity {
    private static final String PREF_NAME = "MusicPrefs";
    private static final String KEY_MUTE_STATE = "isMuted";
    private static final String KEY_BUTTON_SOUNDS_STATE = "buttonSoundsEnabled";

    private ImageButton returnButton;
    private Switch musicSwitch;
    private Switch buttonSoundsSwitch;
    private SharedPreferences preferences;

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
        returnButton = findViewById(R.id.returnButton);
        musicSwitch = findViewById(R.id.musicSwitch);
        buttonSoundsSwitch = findViewById(R.id.ButtonSoundsSwitch);

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