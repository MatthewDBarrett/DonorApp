package com.example.donorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Random;

public class DonationRequest extends AppCompatActivity {

    Button post;
    EditText title;
    EditText description;

    ImageButton home;
    ImageButton settings;
    ImageButton statistics;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference requestDatabase;

    private FirebaseAuth mAuth;

    int requestNumber = -1;

    DatabaseReference userDatabase;

    Boolean userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_request);

        home = findViewById(R.id.homeBtn);

        settings = findViewById(R.id.settingsBtn);
        statistics = findViewById(R.id.statisticsBtn);
        post = findViewById(R.id.postBtn);
        title = findViewById(R.id.titleET);
        description = findViewById(R.id.descriptionET);

        firebaseDatabase = FirebaseDatabase.getInstance();
        requestDatabase = firebaseDatabase.getReference("Requests");
        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");

        getUserType();
        clearSharedPrefs();

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( userType ){
                    Intent intent = new Intent(getApplicationContext(), SettingsPage.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), CharitySettingsPage.class);
                    startActivity(intent);
                }
            }
        });

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StatisticsPage.class);
                startActivity(intent);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeDonation();
                post.setEnabled( false );
            }
        });
    }

    private void storeDonation(){
        if( getCurrentUser() != null ) {
            if(requestNumber == -1)
                generateRequestNum();

            DatabaseReference currentUserRef = requestDatabase.child(String.valueOf( requestNumber ));
            currentUserRef.child("userID").setValue( getCurrentUser() );
            currentUserRef.child("status").setValue( "available" );
            currentUserRef.child("title").setValue(title.getText().toString().trim());
            currentUserRef.child("description").setValue(description.getText().toString().trim());

            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            startActivity(intent);
        }
    }

    private void generateRequestNum(){
        final int min = 111111;
        final int max = 999999;
        requestNumber = new Random().nextInt((max - min) + 1) + min;
    }

    private String getCurrentUser(){
        return mAuth.getUid();
    }

    private void clearSharedPrefs(){
        SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.donationPrefsString), Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    private void getUserType(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if( currentUser != null ) {
            String userID = currentUser.getUid();
            DatabaseReference userType = userDatabase.child(userID).child( "userType" );

            userType.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String type = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                    if( type.equals( "donor" ) ) {
                        setUserType(true);
                    } else {
                        setUserType(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    private void setUserType(Boolean donor){
        userType = donor;
    }

}
