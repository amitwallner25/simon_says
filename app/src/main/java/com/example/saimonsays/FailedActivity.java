package com.example.saimonsays;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class FailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failed);

        TextView failedTextView = findViewById(R.id.failedTextView);
        Button tryAgainButton = findViewById(R.id.tryAgainButton);

        // Get the score passed from MainActivity
        int score = getIntent().getIntExtra("score", 0);

        // Display the score in the failure message
        failedTextView.setText("YOU FAILED\nScore: " + score);

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restart the game by going back to MainActivity
                Intent intent = new Intent(FailedActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear previous activities
                startActivity(intent);
                finish(); // Close this activity
            }
        });
    }
}