package com.example.donorapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class statisticsPage extends AppCompatActivity {

    TextView statsTitle;

    TextView donationCountLabel;
    EditText donationCountET;
    TextView charitiesCountLabel;
    EditText charitiesCountET;

    Button backButton;

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

        backButton = findViewById(R.id.backButton);
    }
}
