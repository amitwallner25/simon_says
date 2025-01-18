package com.example.saimonsays;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;
    private GameDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new GameDatabaseHelper(this);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Handle Login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (db.checkUser(username, password)) {
                    db.setCurrentLoggedInUser(username); // Update the current logged-in user
                    // Successful login, move to MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username", username); // Pass username
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Check if username already exists
                if (db.doesUserExist(db, username)) {
                    Toast.makeText(LoginActivity.this, "Username already exists. Please choose a different one.", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                // Add the new player to the database
                db.addPlayer(username, password); // Assuming initial highest score and hsDate are empty

                Toast.makeText(LoginActivity.this, "Registration successful! You can now log in.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
