package com.example.donorapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DonorRegistrationPage extends AppCompatActivity {

    Button submit;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText confirmPassword;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference donorDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_registration_page);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        donorDatabase = firebaseDatabase.getReference("Donors");

        submit = findViewById(R.id.submitBtn);
        firstName = findViewById(R.id.firstNameET);
        lastName = findViewById(R.id.lastNameET);
        email = findViewById(R.id.emailET);
        password = findViewById(R.id.passwordET);
        confirmPassword = findViewById(R.id.confirmPasswordET);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit.setEnabled(false);
                String firstNameDetails = String.valueOf(firstName.getText());
                String lastNameDetails = String.valueOf(lastName.getText());
                String emailAddress = String.valueOf(email.getText());
                String passwordDetails = String.valueOf(password.getText());
                String confirmPasswordDetails = String.valueOf(confirmPassword.getText());

                if (firstNameDetails.isEmpty() || lastNameDetails.isEmpty() || emailAddress.isEmpty() || passwordDetails.isEmpty() || confirmPasswordDetails.isEmpty()) {
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
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userID = firebaseAuth.getUid();
                            uploadData(userID);
                            new AlertDialog.Builder(DonorRegistrationPage.this)
                                    .setTitle("Registration Successful")
                                    .setMessage("You may now log in.")
                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .show();
                        } else {
                            submit.setEnabled(true);
                            String res = task.getException().getMessage();
                            new AlertDialog.Builder(DonorRegistrationPage.this)
                                    .setTitle("Registration Failed")
                                    .setMessage(res)
                                    .setNegativeButton("OK", null)
                                    .show();
                        }
                    }
                }
            );
    }

    void uploadData(String uID) {
        DatabaseReference currentUserRef = donorDatabase.child(uID);
        currentUserRef.child("firstName").setValue(firstName.getText().toString().trim());
        currentUserRef.child("lastName").setValue(lastName.getText().toString().trim());
        currentUserRef.child("email").setValue(email.getText().toString().trim());

    }
}
