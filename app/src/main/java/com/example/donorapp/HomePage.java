package com.example.donorapp;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.donorapp.DonationListing.Donation;
import com.example.donorapp.DonationListing.DonationListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class HomePage extends AppCompatActivity implements DonationListFragment.OnListFragmentInteractionListener {

    ImageButton newDonation;
    ImageButton settings;
    ImageButton statistics;
    private FragmentManager fragmentManager;

    AutoCompleteTextView searchBox;
    Spinner items;

    //replace this with actual data
    ArrayList<String> donationExamples = new ArrayList<>();

    private FirebaseAuth mAuth;
    DatabaseReference userDatabase;

    Boolean userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        newDonation = findViewById(R.id.newDonationBtn);
        settings = findViewById(R.id.settingsBtn);
        statistics = findViewById(R.id.statisticsBtn);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");

        getUserType();



        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StatisticsPage.class);
                startActivity(intent);
            }
        });

        searchBox = (AutoCompleteTextView)findViewById(R.id.searchBox);
        items = (Spinner)findViewById(R.id.items);

        //delete these later
        donationExamples.add("iPad");
        donationExamples.add("iPhone 7");
        donationExamples.add("iPhone 8+");
        donationExamples.add("Samsung Galaxy S8");
        donationExamples.add("Nintendo Switch");
        donationExamples.add("Nvidia 1080ti Graphics Card");

        //update donationExamples with actual information
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomePage.this, android.R.layout.simple_spinner_dropdown_item, donationExamples );
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(HomePage.this, android.R.layout.simple_spinner_dropdown_item, donationExamples );

        searchBox.setAdapter(adapter);
        items.setAdapter(adapter1);
    }

    private void setDonationList(){
        int numDonations = 0;
        for(int i = 0; i < numDonations; i++) {
            String donation = "";
            donationExamples.add( donation );
        }
    }

    @Override
    public void onListFragmentInteraction(Donation donation)
    {
        Intent intent = new Intent();
        if (userType) {
            intent.setClass(this, RequestView.class);
        } else {
            intent.setClass(this, DonationView.class);
        }


        Bundle bundle = new Bundle();
        bundle.putString("donationId", donation.id);
        bundle.putString("donorId", donation.donorId);
        bundle.putString("title", donation.title);
        bundle.putString("description", donation.description);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getUserType(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if( currentUser != null ) {
            String userID = currentUser.getUid();
            final DatabaseReference userTypeRef = userDatabase.child(userID).child( "userType" );

            userTypeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String type = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                    if( type.equals( "donor" ) ) {
                        setUserType(true);
                    } else {
                        setUserType(false);
                    }
                    setUserTypeDependentButtons();
                    setUpDonationListFragment();
                    searchBox.setHint(String.format(searchBox.getHint().toString(), userType ? "donation requests" : "donations"));
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

    private void setUserTypeDependentButtons(){
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

    private void setUpDonationListFragment(){
        fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        DonationListFragment fragment = new DonationListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("usrType", userType ? "donor" : "charity");
        fragment.setArguments(bundle);
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}


