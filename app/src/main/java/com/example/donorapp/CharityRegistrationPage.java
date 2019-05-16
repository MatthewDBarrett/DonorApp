package com.example.donorapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CharityRegistrationPage extends AppCompatActivity {
    DatabaseReference charityDatabase;
    FirebaseAuth firebaseAuth;

    ArrayList<String> list = new ArrayList<>();

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

        charityDatabase = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();

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
            submit.setEnabled(false);
            String organisationName = String.valueOf(orgName.getText());
            String streetName = String.valueOf(street.getText());
            String suburbName = String.valueOf(suburb.getText());
            String postcodeNumber = String.valueOf(postcode.getText());
            String emailAddress = String.valueOf(email.getText());
            String passwordDetails = String.valueOf(password.getText());
            String confirmPasswordDetails = String.valueOf(confirmPassword.getText());

            if (organisationName.isEmpty() || streetName.isEmpty() || suburbName.isEmpty() || postcodeNumber.isEmpty() || emailAddress.isEmpty() || passwordDetails.isEmpty() || confirmPasswordDetails.isEmpty()) {
                Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_LONG).show();
                return;
            }
            if (!passwordDetails.equals(confirmPasswordDetails)) {
                Toast.makeText(getApplicationContext(), "Password confirmation does not match!", Toast.LENGTH_LONG).show();
                return;
            }
            registerUser(emailAddress, passwordDetails);
            }
        });
    }

    void registerUser(String email, String password) {
        final String emailTmp = email;
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userID = firebaseAuth.getUid();
                    storeData(userID);
                    firebaseAuth.getCurrentUser().sendEmailVerification()
                        .addOnCompleteListener(CharityRegistrationPage.this, new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    new AlertDialog.Builder(CharityRegistrationPage.this)
                                            .setTitle("Verification Needed")
                                            .setMessage("We've sent a verification email to " + emailTmp + ".")
                                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                                                    startActivity(intent);
                                                }
                                            })
                                            .show();

                                } else {
                                    new AlertDialog.Builder(CharityRegistrationPage.this)
                                            .setTitle("Verification Needed")
                                            .setMessage("Please log in with this account.")
                                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                                                    startActivity(intent);
                                                }
                                            })
                                            .show();
                                }
                            }
                        });

                } else {
                    submit.setEnabled(true);
                    String res = task.getException().getMessage();
                    new AlertDialog.Builder(CharityRegistrationPage.this)
                            .setTitle("Registration Failed")
                            .setMessage(res)
                            .setNegativeButton("OK", null)
                            .show();
                }
                    }
                }
            );
    }

    private void storeData(String uID){
        DatabaseReference currentCharityRef = charityDatabase.child(uID);
        currentCharityRef.child("userType").setValue("charity");
        currentCharityRef.child("orgName").setValue(orgName.getText().toString().trim());
        currentCharityRef.child("email").setValue(email.getText().toString().trim());
        currentCharityRef.child("postcode").setValue(postcode.getText().toString().trim());
        currentCharityRef.child("street").setValue(street.getText().toString().trim());
        currentCharityRef.child("suburb").setValue(suburb.getText().toString().trim());
    }
}


















