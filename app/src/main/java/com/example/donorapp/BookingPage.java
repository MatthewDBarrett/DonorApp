package com.example.donorapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.util.Calendar;

public class BookingPage extends AppCompatActivity {

    ImageButton newDonation;
    ImageButton booking;
    ImageButton home;

    Button book;
    EditText comments;
    EditText location;
    EditText date;
    EditText time;

    int startMinute, startHour, endMinute, endHour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page);

        newDonation = findViewById(R.id.newDonationBtn);
        booking = findViewById(R.id.bookingBtn);
        home = findViewById(R.id.homeBtn);

        book = findViewById(R.id.bookButtonET);
        comments = findViewById(R.id.commentsET);
        location = findViewById(R.id.locationET);
        date = findViewById(R.id.dateET);
        time = findViewById(R.id.timeET);

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

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog( BookingPage.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                    startMinute = minutes;
                    startHour = hourOfDay;
                    time.setText( hourOfDay + ":" + minutes );
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

                Button nbutton = timePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                Button pbutton = timePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                nbutton.setTextColor(Color.parseColor("#00897B"));
                pbutton.setTextColor(Color.parseColor("#00897B"));

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dayOfMonth;
                int month;
                int year;

                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(BookingPage.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                date.setText( getString( R.string.dateFormat, day, "/", month + 1, "/", year ) );
                            }
                        }, year, month, dayOfMonth);

                datePickerDialog.show();

                Button nbutton = datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                Button pbutton = datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                nbutton.setTextColor(Color.parseColor("#00897B"));
                pbutton.setTextColor(Color.parseColor("#00897B"));

            }
        });

    }
}