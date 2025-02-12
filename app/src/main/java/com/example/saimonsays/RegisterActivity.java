package com.example.saimonsays;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private  Button buttonRegister;
    private TextView passwordET, usernameET;
    private GameDatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);


        buttonRegister.findViewById(R.id.registerButton);
        passwordET.findViewById(R.id.passwordEditText);
        usernameET.findViewById(R.id.usernameEditText);
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

                // Add the new player to the database
                db.addPlayer(username, password); // Assuming initial highest score and hsDate are empty

                Toast.makeText(RegisterActivity.this, "Registration successful! You can now log in.", Toast.LENGTH_SHORT).show();
            }
        });







    }


}