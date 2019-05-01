package com.example.donorapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CharityRegistrationPage extends AppCompatActivity {

    Button submit;
    EditText orgName;
    EditText street;
    EditText suburb;
    EditText postcode;
    EditText email;
    EditText password;
    EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_registration_page);

        submit = findViewById(R.id.submitBtn);
        orgName = findViewById(R.id.organisationNameET);
        street = findViewById(R.id.streetET);
        suburb = findViewById(R.id.suburbET);
        postcode = findViewById(R.id.postcodeET);
        email = findViewById(R.id.emailET);
        password = findViewById(R.id.passwordET);
        confirmPassword = findViewById(R.id.confirmPasswordET);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            String organisationName = String.valueOf( orgName.getText() );
            String streetName = String.valueOf( street.getText());
            String suburbName = String.valueOf(suburb.getText());
            String postcodeNumber = String.valueOf(postcode.getText());
            String emailAddress = String.valueOf(email.getText());
            String passwordDetails = String.valueOf(password.getText());
            String confirmPasswordDetails = String.valueOf(confirmPassword.getText());



            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

            }
        });

    }

//    public boolean checkRequiredField() {
//        String organisationName = String.valueOf( orgName.getText() );
//
//        if (!organisationName.equals("") &&
//            !organisationName.equals("") &&
//            !organisationName.equals("") &&
//        ) return true;
//        return false;
//    }

}