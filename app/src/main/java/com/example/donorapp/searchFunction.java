package com.example.donorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import java.util.ArrayList;

public class searchFunction extends AppCompatActivity {

    AutoCompleteTextView searchBox;
    Spinner items;

    //replace this with actual data
    ArrayList<String> donationExamples = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(searchFunction.this, android.R.layout.simple_spinner_dropdown_item, donationExamples );
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(searchFunction.this, android.R.layout.simple_spinner_dropdown_item, donationExamples );

        searchBox.setAdapter(adapter);
        items.setAdapter(adapter1);
    }
}