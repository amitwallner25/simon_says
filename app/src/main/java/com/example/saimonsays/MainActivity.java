package com.example.saimonsays;


import android.animation.ObjectAnimator;
import android.content.Intent; // Import for starting a new Activity
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button buttonRed, buttonGreen, buttonBlue, buttonYellow;
    private ImageView leaderBoardImageView, fragmentImageView;
    private TextView scoreTextView,highScoreTextView;
    private ArrayList<Integer> pattern = new ArrayList<>();
    private ArrayList<Integer> userInput = new ArrayList<>();
    private Handler handler = new Handler();
    private Random random = new Random();
    private int currentStep = 0;
    private int score = 0;
    private String mUsername;  // To store the logged-in user's username
    private GameDatabaseHelper db;  // To interact with the SQLite database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        leaderBoardImageView = findViewById(R.id.leaderBoardImageView);
        fragmentImageView = findViewById(R.id.fragmentImageView);
        buttonRed = findViewById(R.id.buttonRed);
        buttonGreen = findViewById(R.id.buttonGreen);
        buttonBlue = findViewById(R.id.buttonBlue);
        buttonYellow = findViewById(R.id.buttonYellow);
        scoreTextView = findViewById(R.id.scoreTextView);
        highScoreTextView = findViewById(R.id.highScoreTextView);

        // Set up button click listeners
        buttonRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserInput(1);
                MediaPlayer rmp = MediaPlayer.create(MainActivity.this, R.raw.red);
                rmp.setVolume(100,100);
                rmp.start();
            }
        });
        buttonGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserInput(2);
                MediaPlayer gmp = MediaPlayer.create(MainActivity.this, R.raw.green);
                gmp.setVolume(100,100);
                gmp.start();

            }
        });
        buttonBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserInput(3);
                MediaPlayer bmp = MediaPlayer.create(MainActivity.this, R.raw.blue);
                bmp.setVolume(1000,1000);
                bmp.start();
            }
        });
        buttonYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserInput(4);
                MediaPlayer ymp = MediaPlayer.create(MainActivity.this, R.raw.yellow);
                ymp.setVolume(100,100);
                ymp.start();
            }
        });
        leaderBoardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LeaderBoards.class);
                startActivity(intent);
                finish(); // Close this activity to prevent returning with the back button
            }
        });
        fragmentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsFragment fragment = new SettingsFragment();

                // Get FragmentManager and start transaction
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                // Replace container with fragment
                fragmentTransaction.replace(R.id.fragmentContainerView, fragment);

                // Add to back stack if you want to go back
                fragmentTransaction.addToBackStack(null);

                // Commit transaction
                fragmentTransaction.commit();
            }
        });


        db = new GameDatabaseHelper(this);  // Initialize the database helper

// Get the username passed from LoginActivity
        mUsername = getIntent().getStringExtra("username");
        if (mUsername == null)
        {
            mUsername = db.getCurrentLoggedIn();
        }
        if (mUsername == null) {
            Log.d("MainActivity","Oh oh - just null-ed username #1");
        }

// Load the previous score from the database for this user
        if (mUsername != null) {
            score = db.getScore(mUsername);  // Get the user's score from the database
            updateScore();  // Display the score on screen
        }

        startGame();
    }

    private void startGame() {
        resetGame();
        addStepToPattern();
        showPattern();
    }

    private void resetGame() {
        pattern.clear();
        userInput.clear();
        updateRecord();
        score = 0;
        updateScore();
    }

    private void addStepToPattern() {
        // Add a random button (1 to 4) to the pattern
        pattern.add(random.nextInt(4) + 1);
    }

    private void showPattern() {
        disableClickOnThe4Colors();
        currentStep = 0;
        userInput.clear();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentStep < pattern.size()) {
                    animateButton(pattern.get(currentStep));
                    currentStep++;
                    handler.postDelayed(this, 1000);  // Delay between showing each step
                }
                else {
                    enableClickOnThe4Colors();
                }

            }
        }, 1000);  // Initial delay before starting the pattern
    }
    private void disableClickOnThe4Colors()
    {
        buttonBlue.setEnabled(false);
        buttonRed.setEnabled(false);
        buttonGreen.setEnabled(false);
        buttonYellow.setEnabled(false);
    }

    private void enableClickOnThe4Colors()
    {
        buttonBlue.setEnabled(true);
        buttonRed.setEnabled(true);
        buttonGreen.setEnabled(true);
        buttonYellow.setEnabled(true);
    }

    private void animateButton(int buttonNumber) {
        Button buttonToAnimate = null;
        switch (buttonNumber) {
            case 1:
                buttonToAnimate = buttonRed;
                MediaPlayer rmp = MediaPlayer.create(MainActivity.this, R.raw.red);
                rmp.setVolume(100,100);
                rmp.start();
                break;
            case 2:
                buttonToAnimate = buttonGreen;
                MediaPlayer gmp = MediaPlayer.create(MainActivity.this, R.raw.green);
                gmp.setVolume(100,100);
                gmp.start();
                break;
            case 3:
                buttonToAnimate = buttonBlue;
                MediaPlayer bmp = MediaPlayer.create(MainActivity.this, R.raw.blue);
                bmp.setVolume(1000,1000);
                bmp.start();
                break;
            case 4:
                buttonToAnimate = buttonYellow;
                MediaPlayer ymp = MediaPlayer.create(MainActivity.this, R.raw.yellow);
                ymp.setVolume(100,100);
                ymp.start();
                break;
        }

        if (buttonToAnimate != null) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(buttonToAnimate, "alpha", 0f, 1f);
            animator.setDuration(500);  // Animation duration
            animator.start();
        }
    }

    private void handleUserInput(int buttonNumber) {
        userInput.add(buttonNumber);

        // Check if user input is correct so far
        if (userInput.get(userInput.size() - 1).equals(pattern.get(userInput.size() - 1))) {
            // If the user completed the current pattern correctly
            if (userInput.size() == pattern.size()) {
                // Increase score and update UI
                score++;
                updateScore();


                // Add a new step and show the new pattern
                addStepToPattern();
                showPattern();
            }
        } else {
            // User made a mistake, transition to FailedActivity
            updateHighScore();
            updateRecord();
            Intent intent = new Intent(MainActivity.this, FailedActivity.class);
            intent.putExtra("score", score); // Send score to FailedActivity
            startActivity(intent);
            finish(); // Close this activity to prevent returning with the back button
        }
    }

    private void updateScore() {
        scoreTextView.setText("Score: " + score);
        if (mUsername != null) {
            db.updateScore(mUsername, score);  // Update the score in the database for the logged-in user
        }
    }

    private void updateHighScore() {
        if (mUsername != null) {
            int currentHighScore = db.getHighScore(mUsername); // Get the correct high score
            String id = db.getIdByUserName(mUsername); // Get the unique ID of the user

            if (id != null) {
                Log.d("MainActivity", "User ID found: " + id);

                if (score > currentHighScore) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String todayDate = dateFormat.format(new Date()); // Get today's date

                    Log.d("MainActivity", "New high score: " + score + " on " + todayDate);

                    db.updatePlayerHighScore(id, score, todayDate); // Send correct parameters
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
            Log.d("MainActivity", "Updated Record TextView: " + currentHighScore);
        }
    }




}
