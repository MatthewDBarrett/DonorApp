package com.example.donorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText username;
    EditText password;
    CardView loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.usernameET);
        password = findViewById(R.id.passwordET);
        loginBtn = findViewById(R.id.loginCV);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = String.valueOf( username.getText() );
                String pass = String.valueOf( password.getText() );
                String both = "Username: " + user + " Password: " + pass;
                Toast.makeText(MainActivity.this, both, Toast.LENGTH_SHORT).show();

                //CODE TO AUTHENTICATE LOGIN DETAILS

                Intent intent = new Intent(MainActivity.this, RegistrationPage.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
}
