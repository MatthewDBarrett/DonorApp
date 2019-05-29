package com.example.donorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

public class preferencesPage extends AppCompatActivity {

    Button backButton;
    Button logoutButton;
    TextView emailLabel;
    TextView nameLabel;
    EditText nameET;
    EditText emailET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences_page);

    backButton = findViewById(R.id.backButton);
    logoutButton = findViewById(R.id.logoutButton);
    emailLabel = findViewById(R.id.emailLabel);
    nameLabel = findViewById(R.id.nameLabel);
    nameET = findViewById(R.id.nameET);
    emailET = findViewById(R.id.emailET);

    }
}
