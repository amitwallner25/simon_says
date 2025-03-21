package com.example.saimonsays;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Random;

public class FourthDesign extends Fragment {
    private ImageButton buttonRed, buttonGreen, buttonBlue, buttonYellow;
    private ArrayList<Integer> pattern = new ArrayList<>();
    private ArrayList<Integer> userInput = new ArrayList<>();
    private Handler handler = new Handler();
    private Random random = new Random();
    private int currentStep = 0;
    private int score = 0;
    private FourthDesignListener listener;
    private SharedPreferences preferences;
    private static final String PREF_NAME = "MusicPrefs";
    private static final String KEY_BUTTON_SOUNDS_STATE = "buttonSoundsEnabled";

    public interface FourthDesignListener {
        void onScoreUpdated(int newScore);
        void onGameFailed(int finalScore);
    }

    public FourthDesign() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fourth_design, container, false);

        preferences = requireActivity().getSharedPreferences(PREF_NAME, requireActivity().MODE_PRIVATE);
        buttonRed = view.findViewById(R.id.buttonRed);
        buttonGreen = view.findViewById(R.id.buttonGreen);
        buttonBlue = view.findViewById(R.id.buttonBlue);
        buttonYellow = view.findViewById(R.id.buttonYellow);

        setupButtonListeners();
        startGame();

        return view;
    }

    private void setupButtonListeners() {
        buttonRed.setOnClickListener(v -> handleUserInput(1, R.raw.red));
        buttonGreen.setOnClickListener(v -> handleUserInput(2, R.raw.green));
        buttonBlue.setOnClickListener(v -> handleUserInput(3, R.raw.blue));
        buttonYellow.setOnClickListener(v -> handleUserInput(4, R.raw.yellow));
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
                    handler.postDelayed(this, 1000);
                } else {
                    enableClickOnThe4Colors();
                }
            }
        }, 1000);
    }

    private void disableClickOnThe4Colors() {
        buttonBlue.setEnabled(false);
        buttonRed.setEnabled(false);
        buttonGreen.setEnabled(false);
        buttonYellow.setEnabled(false);
    }

    private void enableClickOnThe4Colors() {
        buttonBlue.setEnabled(true);
        buttonRed.setEnabled(true);
        buttonGreen.setEnabled(true);
        buttonYellow.setEnabled(true);
    }

    private void animateButton(int buttonNumber) {
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
            ObjectAnimator animator = ObjectAnimator.ofFloat(buttonToAnimate, "alpha", 0f, 1f);
            animator.setDuration(500);
            animator.start();
        }
    }

    private void handleUserInput(int buttonNumber, int soundResId) {
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
            if (listener != null) {
                listener.onGameFailed(score);
            }
        }
    }

    private void updateScore() {
        if (listener != null) {
            listener.onScoreUpdated(score);
        }
    }

    private void playSound(int resId) {
        boolean buttonSoundsEnabled = preferences.getBoolean(KEY_BUTTON_SOUNDS_STATE, true);
        if (buttonSoundsEnabled) {
            MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), resId);
            mediaPlayer.setVolume(100, 100);
            mediaPlayer.start();
        }
    }

    public void setFourthDesignListener(FourthDesignListener listener) {
        this.listener = listener;
    }
}

