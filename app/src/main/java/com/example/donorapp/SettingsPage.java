package com.example.donorapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import java.util.Objects;

public class SettingsPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference userDatabase;

    Button logoutBtn;
    TextView firstName;
    TextView lastName;
    TextView email;

    ImageButton newDonation;
    ImageButton booking;
    ImageButton home;
    ImageButton statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");

        logoutBtn = findViewById(R.id.logoutButton);
        firstName = findViewById(R.id.firstNameTV);
        lastName = findViewById(R.id.lastNameTV);
        email = findViewById(R.id.emailTV);

        newDonation = findViewById(R.id.newDonationBtn);
        booking = findViewById(R.id.bookingBtn);
        home = findViewById(R.id.homeBtn);
        statistics = findViewById(R.id.statisticsBtn);

        setUserFields();

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

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StatisticsPage.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(SettingsPage.this,"Are you sure you wish to log out?", null);
            }
        });

    }

    public void showDialog(Activity activity, String title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogTheme);

        if (title != null) builder.setTitle(title);

        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
            }
        });
        builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void setUserFields(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if( currentUser != null ) {
            String userID = currentUser.getUid();
            DatabaseReference first = userDatabase.child(userID).child( "firstName" );
            DatabaseReference last = userDatabase.child(userID).child( "lastName" );
            DatabaseReference emailAddress = userDatabase.child(userID).child( "email" );

            first.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    firstName.setText( Objects.requireNonNull(dataSnapshot.getValue()).toString() );
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            last.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    lastName.setText( Objects.requireNonNull(dataSnapshot.getValue()).toString() );
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            emailAddress.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    email.setText( Objects.requireNonNull(dataSnapshot.getValue()).toString() );
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

    }

}
