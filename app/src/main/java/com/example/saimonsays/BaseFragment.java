package com.example.saimonsays;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Random;

/**
 * Base fragment class that contains common functionality shared across all Simon Says game fragments.
 * This class handles basic game mechanics, sound management, and UI interactions.
 */
public abstract class BaseFragment extends Fragment {
    protected ImageButton buttonRed, buttonGreen, buttonBlue, buttonYellow;
    protected ArrayList<Integer> pattern = new ArrayList<>();
    protected ArrayList<Integer> userInput = new ArrayList<>();
    protected Handler handler = new Handler();
    protected Random random = new Random();
    protected int currentStep = 0;
    protected int score = 0;
    protected SharedPreferences preferences;
    protected static final String PREF_NAME = "MusicPrefs";
    protected static final String KEY_BUTTON_SOUNDS_STATE = "buttonSoundsEnabled";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = requireActivity().getSharedPreferences(PREF_NAME, requireActivity().MODE_PRIVATE);
    }

    /**
     * Initializes the game by resetting state and starting a new pattern
     */
    protected void startGame() {
        resetGame();
        addStepToPattern();
        showPattern();
    }

    /**
     * Resets the game state to initial values
     */
    protected void resetGame() {
        pattern.clear();
        userInput.clear();
        score = 0;
        updateScore();
    }

    /**
     * Adds a new random step to the pattern
     */
    protected void addStepToPattern() {
        pattern.add(random.nextInt(4) + 1);
    }

    /**
     * Displays the current pattern to the user
     */
    protected void showPattern() {
        disableClickOnThe4Colors();
        currentStep = 0;
        userInput.clear();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentStep < pattern.size()) {
                    animateButton(pattern.get(currentStep));
                    currentStep++;
                    handler.postDelayed(this, 1000);
                } else {
                    enableClickOnThe4Colors();
                }
            }
        }, 1000);
    }

    /**
     * Disables all color buttons
     */
    protected void disableClickOnThe4Colors() {
        buttonBlue.setEnabled(false);
        buttonRed.setEnabled(false);
        buttonGreen.setEnabled(false);
        buttonYellow.setEnabled(false);
    }

    /**
     * Enables all color buttons
     */
    protected void enableClickOnThe4Colors() {
        buttonBlue.setEnabled(true);
        buttonRed.setEnabled(true);
        buttonGreen.setEnabled(true);
        buttonYellow.setEnabled(true);
    }

    /**
     * Animates a button based on its number
     * @param buttonNumber The number of the button to animate (1-4)
     */
    protected void animateButton(int buttonNumber) {
        ImageButton buttonToAnimate = null;
        int soundResId = 0;

        switch (buttonNumber) {
            case 1:
                buttonToAnimate = buttonRed;
                soundResId = R.raw.red;
                break;
            case 2:
                buttonToAnimate = buttonGreen;
                soundResId = R.raw.green;
                break;
            case 3:
                buttonToAnimate = buttonBlue;
                soundResId = R.raw.blue;
                break;
            case 4:
                buttonToAnimate = buttonYellow;
                soundResId = R.raw.yellow;
                break;
        }

        if (buttonToAnimate != null) {
            buttonToAnimate.animate()
                    .alpha(1f) // Target alpha value
                    .setDuration(500)
                    .start();
        }
    }

    /**
     * Handles user input for a button press
     * @param buttonNumber The number of the button pressed (1-4)
     * @param soundResId The sound resource ID to play
     */
    protected void handleUserInput(int buttonNumber, int soundResId) {
        userInput.add(buttonNumber);
        playSound(soundResId);

        if (userInput.get(userInput.size() - 1).equals(pattern.get(userInput.size() - 1))) {
            if (userInput.size() == pattern.size()) {
                score++;
                updateScore();
                addStepToPattern();
                showPattern();
            }
        } else {
            onGameFailed(score);
        }
    }

    /**
     * Updates the score and notifies listeners
     */
    protected void updateScore() {
        onScoreUpdated(score);
    }

    /**
     * Plays a sound if sound is enabled in preferences
     * @param resId The sound resource ID to play
     */
    protected void playSound(int resId) {
        boolean buttonSoundsEnabled = preferences.getBoolean(KEY_BUTTON_SOUNDS_STATE, true);
        if (buttonSoundsEnabled) {
            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), resId);
            mediaPlayer.setVolume(100, 100);
            mediaPlayer.start();
        }
    }

    /**
     * Called when the score is updated
     * @param newScore The new score value
     */
    protected abstract void onScoreUpdated(int newScore);

    /**
     * Called when the game is failed
     * @param finalScore The final score when the game ended
     */
    protected abstract void onGameFailed(int finalScore);
} 