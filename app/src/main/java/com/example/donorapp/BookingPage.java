package com.example.donorapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BookingPage extends AppCompatActivity {

    Button book;
    EditText comments;
    EditText location;
    EditText date;
    EditText time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page);

        book = findViewById(R.id.bookButtonET);
        comments = findViewById(R.id.commentsET);
        location = findViewById(R.id.locationET);
        date = findViewById(R.id.dateET);
        time = findViewById(R.id.timeET);





    }
}