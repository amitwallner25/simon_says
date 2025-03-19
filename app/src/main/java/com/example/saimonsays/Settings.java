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

    private ImageButton returnButton;
    private Switch musicSwitch;
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

        // Set initial state of the switch based on SharedPreferences
        boolean isMuted = preferences.getBoolean(KEY_MUTE_STATE, false);
        musicSwitch.setChecked(isMuted);

        // Handle switch state changes
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (bound && musicService != null) {
                musicService.setMute(isChecked);
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