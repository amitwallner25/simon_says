package com.example.saimonsays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends BaseActivity implements DefaultFragment.SimonSaysListener, SecondDesign.SimonSaysListener {

    private TextView scoreTextView, highScoreTextView;
    private ImageView leaderBoardImageView, settingsImageView;
    private String mUsername;
    private GameDatabaseHelper db;
    private static final String PREF_NAME = "MusicPrefs";
    private static final String KEY_FRAGMENT_STATE = "currentFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreTextView = findViewById(R.id.scoreTextView);
        highScoreTextView = findViewById(R.id.highScoreTextView);
        leaderBoardImageView = findViewById(R.id.leaderBoardImageView);
        settingsImageView = findViewById(R.id.settingsImageView);

        db = new GameDatabaseHelper(this);

        // Get the username passed from LoginActivity or fetch the current logged-in user
        mUsername = getIntent().getStringExtra("username");
        if (mUsername == null) {
            mUsername = db.getCurrentLoggedIn();
        }

        if (mUsername == null) {
            Log.d("MainActivity", "Oh oh - username is null");
        } else {
            updateRecord(); // Update high score display
        }

        // Load the appropriate fragment based on saved preference
        loadFragment();

        leaderBoardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LeaderBoards.class);
                startActivity(intent);
                finish(); // Close this activity to prevent returning with the back button
            }
        });

        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                finish(); // Close this activity to prevent returning with the back button
            }
        });
    }

    private void loadFragment() {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String currentFragment = preferences.getString(KEY_FRAGMENT_STATE, "default");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (currentFragment.equals("default")) {
            DefaultFragment simonSaysFragment = new DefaultFragment();
            simonSaysFragment.setSimonSaysListener(this);
            fragmentTransaction.replace(R.id.fragmentContainerView, simonSaysFragment);
        } else {
            SecondDesign secondDesignFragment = new SecondDesign();
            secondDesignFragment.setSimonSaysListener(this);
            fragmentTransaction.replace(R.id.fragmentContainerView, secondDesignFragment);
        }

        fragmentTransaction.commit();
    }

    @Override
    public void onScoreUpdated(int newScore) {
        scoreTextView.setText("Score: " + newScore);
        if (mUsername != null) {
            db.updateScore(mUsername, newScore);
        }
    }

    @Override
    public void onGameFailed(int finalScore) {
        updateHighScore(finalScore);
        updateRecord();

        Intent intent = new Intent(MainActivity.this, FailedActivity.class);
        intent.putExtra("score", finalScore);
        startActivity(intent);
        finish(); // Close MainActivity to prevent returning with the back button
    }

    private void updateHighScore(int finalScore) {
        if (mUsername != null) {
            int currentHighScore = db.getHighScore(mUsername); // Get current high score
            String id = db.getIdByUserName(mUsername); // Get user ID

            if (id != null) {
                Log.d("MainActivity", "User ID found: " + id);

                if (finalScore > currentHighScore) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String todayDate = dateFormat.format(new Date()); // Get today's date

                    Log.d("MainActivity", "New high score: " + finalScore + " on " + todayDate);

                    db.updatePlayerHighScore(id, finalScore, todayDate); // Update database with new high score
                } else {
                    Log.d("MainActivity", "Score not higher than current high score. No update needed.");
                }
            } else {
                Log.e("MainActivity", "Failed to get user ID for username: " + mUsername);
            }
        } else {
            Log.e("MainActivity", "Username is null. Cannot update high score.");
        }
    }

    private void updateRecord() {
        if (mUsername != null) {
            int currentHighScore = db.getHighScore(mUsername);
            highScoreTextView.setText("Record: " + currentHighScore);
            Log.d("MainActivity", "Updated High Score Display: " + currentHighScore);
        }
    }
}




