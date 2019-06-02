package com.example.donorapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class CharitySettingsPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference userDatabase;

    Button logoutBtn;
    TextView orgName;
    TextView email;
    TextView street;
    TextView suburb;
    TextView postCode;

    ImageButton newDonation;
    ImageButton home;
    ImageButton statistics;

    Boolean userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_settings_page);

        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");

        logoutBtn = findViewById(R.id.logoutButton);
        email = findViewById(R.id.emailTV);

        orgName = findViewById(R.id.organisationTV);
        street = findViewById(R.id.streetTV);
        suburb = findViewById(R.id.suburbTV);
        postCode = findViewById(R.id.postCodeTV);

        newDonation = findViewById(R.id.newDonationBtn);
        home = findViewById(R.id.homeBtn);
        statistics = findViewById(R.id.statisticsBtn);

        getUserType();
        setUserFields();

        newDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( userType ){
                    Intent intent = new Intent(getApplicationContext(), DonationAd.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), DonationRequest.class);
                    startActivity(intent);
                }
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
                showDialog(CharitySettingsPage.this,"Are you sure you wish to log out?", null);
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
            DatabaseReference name = userDatabase.child(userID).child( "orgName" );
            DatabaseReference emailAddress = userDatabase.child(userID).child( "email" );
            DatabaseReference charityStreet = userDatabase.child(userID).child( "street" );
            DatabaseReference charitySuburb = userDatabase.child(userID).child( "suburb" );
            DatabaseReference charityPostCode = userDatabase.child(userID).child( "postcode" );

            name.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    orgName.setText( Objects.requireNonNull(dataSnapshot.getValue()).toString() );
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

            charityStreet.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    street.setText( Objects.requireNonNull(dataSnapshot.getValue()).toString() );
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            charitySuburb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    suburb.setText( Objects.requireNonNull(dataSnapshot.getValue()).toString() );
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            charityPostCode.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    postCode.setText( Objects.requireNonNull(dataSnapshot.getValue()).toString() );
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }

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
