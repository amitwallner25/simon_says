package com.example.saimonsays;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Settings extends MusicActivity {
    private static final String PREF_NAME = "MusicPrefs";
    private static final String KEY_MUTE_STATE = "isMuted";
    private static final String KEY_USERNAME = "userName";
    private static final String KEY_BUTTON_SOUNDS_STATE = "buttonSoundsEnabled";
    private static final String USER_PREF_NAME = "UserPrefs";
    private static final String KEY_LOGGED_IN_USER = "loggedInUser";
    private static final String KEY_FRAGMENT_STATE = "currentFragment";

    private ImageButton returnButton;
    private Switch musicSwitch;
    private Switch buttonSoundsSwitch;
    private Button logoutButton;
    private ImageButton defaultLayoutButton;
    private ImageButton secondLayoutButton;
    private ImageButton thirdLayoutButton;
    private ImageButton fourthLayoutButton;
    private ToggleButton defaultToggleButton;
    private ToggleButton secondToggleButton;
    private ToggleButton thirdToggleButton;
    private ToggleButton fourthToggleButton;
    private SharedPreferences preferences;
    private SharedPreferences userPreferences;
    private GameDatabaseHelper db;

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

        db = new GameDatabaseHelper(this);
        preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        userPreferences = getSharedPreferences(USER_PREF_NAME, MODE_PRIVATE);
        returnButton = findViewById(R.id.returnButton);
        musicSwitch = findViewById(R.id.musicSwitch);
        buttonSoundsSwitch = findViewById(R.id.ButtonSoundsSwitch);
        logoutButton = findViewById(R.id.logoutButton);
        
        // Initialize layout buttons
        defaultLayoutButton = findViewById(R.id.default_saimonSays_layout);
        secondLayoutButton = findViewById(R.id.scecondDesign_saimonSays_layout);
        thirdLayoutButton = findViewById(R.id.thirdDesign_simonsays_imagebutton);
        fourthLayoutButton = findViewById(R.id.fourthDesign_simonsays_imagebutton);
        
        // Initialize toggle buttons
        defaultToggleButton = findViewById(R.id.default_saimonSays_ToggleButton);
        secondToggleButton = findViewById(R.id.scecondDesign_saimonSays_ToggleButton);
        thirdToggleButton = findViewById(R.id.thirdDesign_simonsays_ToggleButton);
        fourthToggleButton = findViewById(R.id.fourthDesign_simonsays_ToggleButton);

        // Set initial state of the music switch based on SharedPreferences
        boolean isMuted = preferences.getBoolean(KEY_MUTE_STATE, false);
        musicSwitch.setChecked(isMuted);

        // Set initial state of the button sounds switch based on SharedPreferences
        boolean buttonSoundsEnabled = preferences.getBoolean(KEY_BUTTON_SOUNDS_STATE, true);
        buttonSoundsSwitch.setChecked(buttonSoundsEnabled);

        // Set initial state of fragment selection
        String currentFragment = preferences.getString(KEY_FRAGMENT_STATE, "default");
        switch (currentFragment) {
            case "default":
                defaultToggleButton.setChecked(true);
                break;
            case "second":
                secondToggleButton.setChecked(true);
                break;
            case "third":
                thirdToggleButton.setChecked(true);
                break;
            case "fourth":
                fourthToggleButton.setChecked(true);
                break;
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
            // Uncheck all toggle buttons
            defaultToggleButton.setChecked(false);
            secondToggleButton.setChecked(false);
            thirdToggleButton.setChecked(false);
            fourthToggleButton.setChecked(false);

            // Check the corresponding toggle button and update fragment
            if (v == defaultLayoutButton || v == defaultToggleButton) {
                defaultToggleButton.setChecked(true);
                updateFragment("default");
            } else if (v == secondLayoutButton || v == secondToggleButton) {
                secondToggleButton.setChecked(true);
                updateFragment("second");
            } else if (v == thirdLayoutButton || v == thirdToggleButton) {
                thirdToggleButton.setChecked(true);
                updateFragment("third");
            } else if (v == fourthLayoutButton || v == fourthToggleButton) {
                fourthToggleButton.setChecked(true);
                updateFragment("fourth");
            }
        };

        // Set click listeners for all buttons
        defaultLayoutButton.setOnClickListener(fragmentClickListener);
        secondLayoutButton.setOnClickListener(fragmentClickListener);
        thirdLayoutButton.setOnClickListener(fragmentClickListener);
        fourthLayoutButton.setOnClickListener(fragmentClickListener);
        defaultToggleButton.setOnClickListener(fragmentClickListener);
        secondToggleButton.setOnClickListener(fragmentClickListener);
        thirdToggleButton.setOnClickListener(fragmentClickListener);
        fourthToggleButton.setOnClickListener(fragmentClickListener);

        // Handle logout button click
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Settings.this)
                        .setTitle("Confirm Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Clear the logged in user from SharedPreferences
                                userPreferences.edit().remove(KEY_LOGGED_IN_USER).apply();

                                // Navigate to LoginActivity
                                Intent intent = new Intent(Settings.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", null) // Dismisses the dialog
                        .show();
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

    private void updateFragment(String fragmentType) {
        Log.d("Settings", "Updating fragment to: " + fragmentType);
        String currentUserName = db.getCurrentLoggedIn();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_FRAGMENT_STATE, fragmentType);
        editor.putString(KEY_USERNAME,currentUserName);
        Log.d("Settings", "Username: "+currentUserName +", fragmentType: "+fragmentType);
        editor.apply();
        Log.d("Settings", "Fragment state updated and saved");
    }
}