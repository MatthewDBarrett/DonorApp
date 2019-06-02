package com.example.donorapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class RequestView extends AppCompatActivity {

    Button book;
    TextView title;
    TextView description;

    ImageButton home;
    ImageButton settings;
    ImageButton statistics;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference donationDatabase;

    private String mDonationId;
    private String mDonorId;
    private String mTitle;
    private String mDescription;

    private FirebaseAuth mAuth;

    DatabaseReference userDatabase;

    Boolean userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_view);

        home = findViewById(R.id.homeBtn);
        settings = findViewById(R.id.settingsBtn);
        statistics = findViewById(R.id.statisticsBtn);
        book = findViewById(R.id.bookBtn);
        title = findViewById(R.id.titleTV);
        description = findViewById(R.id.descriptionTV);

        firebaseDatabase = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");

        getUserType();

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


        final Bundle bundle = getIntent().getExtras();
        mDonationId = bundle.getString("donationId");
        mDonorId = bundle.getString("donorId");
        mTitle = bundle.getString("title");
        mDescription = bundle.getString("description");

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookingPage.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        donationDatabase = firebaseDatabase.getReference("Donations").child( mDonationId );
        title.setText(mTitle);
        description.setText(mDescription);
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

