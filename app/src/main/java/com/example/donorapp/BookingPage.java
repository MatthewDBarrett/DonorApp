package com.example.donorapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class BookingPage extends AppCompatActivity {

    ImageButton newDonation;
    ImageButton home;
    ImageButton settings;
    ImageButton statistics;

    Button book;
    EditText comments;
    EditText location;
    EditText date;
    EditText time;
    TextView bookingTitle;

    private String mDonorId;
    private String mDonationTitle;

    String firstName;
    String lastName;
    String name;
    String userId;
    private FirebaseAuth mAuth;
    DatabaseReference userDatabase;

    Boolean userType;

    int startMinute, startHour, endMinute, endHour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_booking_page);

        Bundle bundle = getIntent().getExtras();
        mDonationTitle = bundle.getString("title");
        bookingTitle = findViewById(R.id.booking_title);
        bookingTitle.setText(String.format(((String)bookingTitle.getText()), mDonationTitle));
        mDonorId = bundle.getString("donorId");

        newDonation = findViewById(R.id.newDonationBtn);
        home = findViewById(R.id.homeBtn);
        settings = findViewById(R.id.settingsBtn);
        statistics = findViewById(R.id.statisticsBtn);

        book = findViewById(R.id.bookButtonET);
        comments = findViewById(R.id.commentsET);
        location = findViewById(R.id.locationET);
        date = findViewById(R.id.dateET);
        time = findViewById(R.id.timeET);

        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");

        getUserType();

        SharedPreferences prefs = getApplication().getSharedPreferences(getResources().getString(R.string.locationPrefs), Context.MODE_PRIVATE);

        location.setText( prefs.getString(getResources().getString(R.string.latLongPrefs), ""));

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

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });


//        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if( currentUser != null ) {
            userId = currentUser.getUid();
//            try {
//                userDatabase.child(userId).child("firstName").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        firstName += Objects.requireNonNull(dataSnapshot.getValue()).toString();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                    }
//                });
//                ;
//                userDatabase.child(userId).child("lastName").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        lastName = Objects.requireNonNull(dataSnapshot.getValue()).toString();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                    }
//                });
//                name = firstName + " " + lastName;
//            } catch (NullPointerException npe) {
//                userDatabase.child(userId).child("orgName").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        name = Objects.requireNonNull(dataSnapshot.getValue()).toString();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                    }
//                });
//            }
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(intent);
        }

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Notifications").child(mDonorId);
                databaseRef.child("title").setValue(mDonationTitle);
//                databaseRef.child("fromName").setValue(name);
                databaseRef.child("fromId").setValue(userId);
                Toast.makeText(BookingPage.this, "A message has been sent to user " + mDonorId + " about " + mDonationTitle, Toast.LENGTH_LONG).show();
                finish();
            }
        });
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
