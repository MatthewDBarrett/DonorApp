package com.example.donorapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText username;
    EditText password;
    CardView loginBtn;
    CardView registrationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.usernameET);
        password = findViewById(R.id.passwordET);
        loginBtn = findViewById(R.id.loginCV);
        registrationBtn = findViewById(R.id.registrationCV);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = String.valueOf( username.getText() );
                String pass = String.valueOf( password.getText() );
                String both = "Username: " + user + " Password: " + pass;
                Toast.makeText(MainActivity.this, both, Toast.LENGTH_SHORT).show();

                //CODE TO AUTHENTICATE LOGIN DETAILS
                logIn(user, pass);

//                Intent intent = new Intent(MainActivity.this, HomePage.class);
//                startActivity(intent);
            }
        });

        registrationBtn.setOnClickListener(new View.OnClickListener() {
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

    public void logIn(String username, String password)
    {
        if (username.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this, "The username and password fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        final Intent intent = new Intent(this, RegistrationPage.class); // HomePage.class);

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                }
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
