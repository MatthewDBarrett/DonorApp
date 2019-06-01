package com.example.donorapp;

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

import java.util.ArrayList;

public class HomePage extends AppCompatActivity implements DonationListFragment.OnListFragmentInteractionListener {

    ImageButton newDonation;
    ImageButton booking;
    ImageButton settings;
    ImageButton statistics;
    private FragmentManager fragmentManager;

    AutoCompleteTextView searchBox;
    Spinner items;

    //replace this with actual data
    ArrayList<String> donationExamples = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        newDonation = findViewById(R.id.newDonationBtn);
        booking = findViewById(R.id.bookingBtn);
        settings = findViewById(R.id.settingsBtn);
        statistics = findViewById(R.id.statisticsBtn);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsPage.class);
                startActivity(intent);
            }
        });

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DonationView.class);
                Bundle b = new Bundle();
                b.putInt("donationNum", 329704);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });

        fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, new DonationListFragment());
        transaction.addToBackStack(null);
        transaction.commit();


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
        intent.setClass(this, DonationView.class);
        Bundle bundle = new Bundle();
        bundle.putString("donationId", donation.id);
        bundle.putString("donorId", donation.donorId);
        bundle.putString("title", donation.title);
        bundle.putString("description", donation.description);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}


