package com.example.donorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DonorRegistrationPage extends AppCompatActivity {

    Button submit;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_registration_page);

        submit = findViewById(R.id.submitBtn);
        firstName = findViewById(R.id.firstNameET);
        lastName = findViewById(R.id.lastNameET);
        email = findViewById(R.id.emailET);
        password = findViewById(R.id.passwordET);
        confirmPassword = findViewById(R.id.confirmPasswordET);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                String firstNameDetails = String.valueOf(firstName.getText());
                String lastNameDetails = String.valueOf(lastName.getText());
                String emailAddress = String.valueOf(email.getText());
                String passwordDetails = String.valueOf(password.getText());
                String confirmPasswordDetails = String.valueOf(confirmPassword.getText());


                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);


            }
        });
    }
}