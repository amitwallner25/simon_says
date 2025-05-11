package com.example.saimonsays;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private  Button buttonRegister, buttonBackToLogin;
    private EditText passwordET, usernameET;
    private GameDatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);


        buttonRegister = findViewById(R.id.registerButton);
        buttonBackToLogin = findViewById(R.id.ButtonBackToLogIn);
        passwordET = findViewById(R.id.passwordEditText);
        usernameET = findViewById(R.id.usernameEditText);
        db = new GameDatabaseHelper(this);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();

                // Check if username already exists
                if (db.doesUserExist(db, username)) {
                    Toast.makeText(RegisterActivity.this, "Username already exists. Please choose a different one.", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }
                if(username == null || username.isEmpty() || username.length() < 3 || username.length() > 15 && password == null || password.isEmpty() || password.length() < 4 || username.length() > 15 )
                {
                    Toast.makeText(RegisterActivity.this, "Invalid username or password. Please try again.", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }
                else if ( username == null || username.isEmpty() || username.length() < 3 || username.length() > 15) {
                    Toast.makeText(RegisterActivity.this, "Name is invalid. Please choose a different one.", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }
                else if ( password == null || password.isEmpty() || password.length() < 4 || username.length() > 15) {
                    Toast.makeText(RegisterActivity.this, "Password is invalid. Please choose a different one.", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                // Add the new player to the database
                db.addPlayer(username, password); // Assuming initial highest score and hsDate are empty

                Toast.makeText(RegisterActivity.this, "Registration successful! You can now log in.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close this activity to prevent returning with the back button
            }
        });

        buttonBackToLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close this activity to prevent returning with the back button
            }
        });









    }


}