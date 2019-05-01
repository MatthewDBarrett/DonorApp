package com.example.donorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CharityRegistrationPage extends AppCompatActivity {


   // private FirebaseDatabase database =  FirebaseDatabase.getInstance();
    private DatabaseReference database;

    ArrayList<String> list = new ArrayList<>();

    Button submit;
    EditText orgName;
    EditText street;
    EditText suburb;
    EditText postcode;
    EditText email;
    EditText password;
    EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_registration_page);

        submit = findViewById(R.id.submitBtn);
        orgName = findViewById(R.id.organisationNameET);
        street = findViewById(R.id.streetET);
        suburb = findViewById(R.id.suburbET);
        postcode = findViewById(R.id.postcodeET);
        email = findViewById(R.id.emailET);
        password = findViewById(R.id.passwordET);
        confirmPassword = findViewById(R.id.confirmPasswordET);

        database = FirebaseDatabase.getInstance().getReference("Charities");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            String organisationName = String.valueOf( orgName.getText() );
            String streetName = String.valueOf( street.getText());
            String suburbName = String.valueOf(suburb.getText());
            String postcodeNumber = String.valueOf(postcode.getText());
            String emailAddress = String.valueOf(email.getText());
            String passwordDetails = String.valueOf(password.getText());
            String confirmPasswordDetails = String.valueOf(confirmPassword.getText());






            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);



            storeData();




            }
        });

       /* submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


            }

        });
        */


    }

//    public boolean checkRequiredField() {
//        String organisationName = String.valueOf( orgName.getText() );
//        String emailText = String.valueOf(email.getText());


//           if (!organisationName.equals("") &&
//            !emailText.equals("") &&
//            !organisationName.equals("") &&
//        ) return true;
//        return false;
//    }


/*
    @Override
    protected void onStart(){
        super.onStart();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String details = dataSnapshot.getValue(String.class);
                // Log.d(TAG, "Value is: " + details);
                // rootReference.setValue("Charity Data");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                // Log.w(TAG,"Failed to read value", databaseError.toException());

            }

        });

    }  */



    private void storeData(){

        String id = database.push().getKey();

        DatabaseReference currentCharityId = database.child(id);
        currentCharityId.child("orgName").setValue(orgName.getText().toString().trim());
        currentCharityId.child("email").setValue(email.getText().toString().trim());
        currentCharityId.child("postcode").setValue(postcode.getText().toString().trim());
        currentCharityId.child("street").setValue(street.getText().toString().trim());
        currentCharityId.child("password").setValue(password.getText().toString().trim());
        currentCharityId.child("suburb").setValue(suburb.getText().toString().trim());



            // SharedPreferences preferences = getSharedPreferences("preferences", );


        }



  /*  private void addCharity(){




        if(!TextUtils.isEmpty("")){


          }

        else{

            Toast.makeText(this, "Data not added to databsae", Toast.LENGTH_LONG).show();

        }
    }
            */

}



















