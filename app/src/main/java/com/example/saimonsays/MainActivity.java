package com.example.saimonsays;


import android.animation.ObjectAnimator;
import android.content.Intent; // Import for starting a new Activity
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button button1, button2, button3, button4;
    private TextView scoreTextView;
    private ArrayList<Integer> pattern = new ArrayList<>();
    private ArrayList<Integer> userInput = new ArrayList<>();
    private Handler handler = new Handler();
    private Random random = new Random();
    private int currentStep = 0;
    private int score = 0;
    private String username;  // To store the logged-in user's username
    private DatabaseHelper db;  // To interact with the SQLite database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        scoreTextView = findViewById(R.id.scoreTextView);

        // Set up button click listeners
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserInput(1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserInput(2);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserInput(3);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserInput(4);
            }
        });
        db = new DatabaseHelper(this);  // Initialize the database helper

// Get the username passed from LoginActivity
        username = getIntent().getStringExtra("username");

// Load the previous score from the database for this user
        if (username != null) {
            score = db.getScore(username);  // Get the user's score from the database
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
        score = 0;
        updateScore();
    }

    private void addStepToPattern() {
        // Add a random button (1 to 4) to the pattern
        pattern.add(random.nextInt(4) + 1);
    }

    private void showPattern() {
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
            }
        }, 1000);  // Initial delay before starting the pattern
    }

    private void animateButton(int buttonNumber) {
        Button buttonToAnimate = null;
        switch (buttonNumber) {
            case 1:
                buttonToAnimate = button1;
                break;
            case 2:
                buttonToAnimate = button2;
                break;
            case 3:
                buttonToAnimate = button3;
                break;
            case 4:
                buttonToAnimate = button4;
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
            Intent intent = new Intent(MainActivity.this, FailedActivity.class);
            intent.putExtra("score", score); // Send score to FailedActivity
            startActivity(intent);
            finish(); // Close this activity to prevent returning with the back button
        }
    }

    private void updateScore() {
        scoreTextView.setText("Score: " + score);
        if (username != null) {
            db.updateScore(username, score);  // Update the score in the database for the logged-in user
        }
    }
}
