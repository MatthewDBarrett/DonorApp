package com.example.donorapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

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
                Intent intent = new Intent(getApplicationContext(), SettingsPage.class);
                startActivity(intent);
            }
        });

    }
}
