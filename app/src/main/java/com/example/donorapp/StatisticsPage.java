package com.example.donorapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Objects;

public class StatisticsPage extends AppCompatActivity {

    TextView statsTitle;

    TextView donationCountLabel;
    EditText donationCountET;
    TextView charitiesCountLabel;
    EditText charitiesCountET;

    ImageButton newDonation;
    ImageButton booking;
    ImageButton settings;
    ImageButton home;

    private FirebaseAuth mAuth;
    DatabaseReference userDatabase;

    Boolean userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_page);


        statsTitle = findViewById(R.id.statsTitle);
        statsTitle.setTextColor(Color.WHITE);

        donationCountLabel = findViewById(R.id.donationCountLabel);
        donationCountET = findViewById(R.id.donationCountET);
        charitiesCountLabel = findViewById(R.id.charitiesCountLabel);
        charitiesCountET = findViewById(R.id.charitiesCountET);

        newDonation = findViewById(R.id.newDonationBtn);
        booking = findViewById(R.id.bookingBtn);
        home = findViewById(R.id.homeBtn);
        settings = findViewById(R.id.settingsBtn);

        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");

        getUserType();

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookingPage.class);
                startActivity(intent);
            }
        });

        newDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DonationAd.class);
                startActivity(intent);
            }
        });

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
