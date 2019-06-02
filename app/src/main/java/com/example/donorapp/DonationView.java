package com.example.donorapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class DonationView extends AppCompatActivity {

    Button book;
    TextView title;
    TextView description;
    LinearLayout photosLL;
    ImageView img;

    ImageButton home;
    ImageButton settings;
    ImageButton statistics;

    private static int RESULT_LOAD_IMAGE = 1;
    public static final int REQUEST_IMAGE = 100;
    String imageFilePath = "";

    ArrayList<Button> buttons = new ArrayList<>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference donationDatabase;
    StorageReference imagesDatabase;

    private String mDonationId;
    private String mDonorId;
    private String mTitle;
    private String mDescription;


    private FirebaseAuth mAuth;

    DatabaseReference userDatabase;

    Boolean userType;

    int numStoredImages = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_view);

        home = findViewById(R.id.homeBtn);
        settings = findViewById(R.id.settingsBtn);
        statistics = findViewById(R.id.statisticsBtn);
        book = findViewById(R.id.bookBtn);
        title = findViewById(R.id.titleTV);
        description = findViewById(R.id.descriptionTV);
        photosLL = findViewById(R.id.photosLL);
        img = findViewById(R.id.imageView);

        firebaseDatabase = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");

        getUserType();

        clearSharedPrefs();

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
        imagesDatabase = FirebaseStorage.getInstance().getReference().child("donation images").child(mDonationId);
        title.setText(mTitle);
        description.setText(mDescription);

        getNumStoredImages();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                img.setImageURI(Uri.parse(imageFilePath));
                newImage( imageFilePath, false );
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You cancelled the operation", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {




            imagesDatabase.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri downloadUrl)
                    {

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        if (downloadUrl != null) {
                            Cursor cursor = getContentResolver().query(downloadUrl, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                cursor.close();


                                img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                newImage(picturePath, false);
                            }
                        }

                    }
                }
                );


        }

    }

    private void newImage(final String imageFilePath, boolean storedImages ) {
        final Button newBtn = new Button( this );               //Create the new button
        newBtn.setId( buttons.size() );

        newBtn.setLayoutParams(new LinearLayout.LayoutParams(1,1));

        newBtn.setBackground( null );

        photosLL.addView( newBtn );                                //new button is added to the listView

        final ViewGroup.LayoutParams params = newBtn.getLayoutParams();
        photosLL.post(new Runnable() {
            public void run() {
                params.width = photosLL.getHeight();
                params.height = photosLL.getHeight();
                newBtn.setLayoutParams(params);
            }
        });

        setButtonListener( newBtn );

        if ( imageFilePath != null ) {
            final Button lastBtn = findViewById(buttons.size() - 1);

            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);

            if (bitmap != null) {
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                BitmapDrawable ob = new BitmapDrawable(getResources(), resizedBitmap);

                if (buttons.size() > 1) {
                    ViewGroup.MarginLayoutParams params2 = (ViewGroup.MarginLayoutParams) lastBtn.getLayoutParams();
                    params2.leftMargin = 20;
                }
//                if (lastBtn != null) {
//                    lastBtn.setBackground(ob);
//                    setButtonListener(lastBtn);
//                }
//                img.setImageDrawable( ob );
//                newBtn.setBackground( null );
                lastBtn.setBackground( ob );
                newBtn.setBackground( null );
                setButtonListener( lastBtn );
            }

            if ( !storedImages ) {
                storeImage(imageFilePath);

            }
        }

        buttons.add( newBtn );
    }

    private void storeImage( String filePath){
        SharedPreferences prefs;
        prefs = getSharedPreferences(getResources().getString(R.string.donationPrefsString), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int index = getNumImages();

        editor.putString(getResources().getString(R.string.imagePrefsString) + index, filePath).apply();

        incrementNumImages();
    }

    private int getNumImages(){
        SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.donationPrefsString), Context.MODE_PRIVATE);
        return prefs.getInt(getResources().getString(R.string.numImagesString), 0);
    }

    private void incrementNumImages() {
        SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.donationPrefsString), Context.MODE_PRIVATE);

        int numJobs = prefs.getInt(getResources().getString(R.string.numImagesString), 0);

        SharedPreferences.Editor editor2 = prefs.edit();
        editor2.putInt(getResources().getString(R.string.numImagesString), numJobs + 1).apply();
    }

    private void setButtonListener(final Button button) {

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                int index = 0;

                switch (view.getId()) {
                    case 0:
                        index = 0;
                        break;
                    case 1:
                        index = 1;
                        break;
                    case 2:
                        index = 2;
                        break;
                    case 3:
                        index = 3;
                        break;
                    case 4:
                        index = 4;
                        break;
                    case 5:
                        index = 5;
                        break;
                    case 6:
                        index = 6;
                        break;
                    case 7:
                        index = 7;
                        break;
                    case 8:
                        index = 8;
                        break;
                    case 9:
                        index = 9;
                        break;
                    default:
                        break;
                }

                if (index != (buttons.size() - 1)) {
                    SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.donationPrefsString), Context.MODE_PRIVATE);
                    img.setImageURI(Uri.parse(prefs.getString(getResources().getString(R.string.imagePrefsString) + index, null)));
                } else {

//                    showDialog(DonationView.this, null, null);

                }

            }
        });

    }

    private void clearSharedPrefs(){
        SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.donationPrefsString), Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    private void loadImages() throws IOException {

        for(int i = 0; i < numStoredImages + 1; i++){
            final File localFile = File.createTempFile("images", "jpg");
            StorageReference image = imagesDatabase.child( String.valueOf( i + 1 ) );

            image.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            newImage( localFile.getAbsolutePath(), false );
                            setDisplayImage();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }
    }

    private void getUserType(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if( currentUser != null ) {
            String userID = currentUser.getUid();
            DatabaseReference userTypeRef = userDatabase.child(userID).child( "userType" );

            userTypeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String type = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                    if( type.equals( "donor" ) ) {
                        setUserType(true);
                    } else {
                        setUserType(false);
                    }
//                    if (userType) { //donor
//                        findViewById(R.id.imageView).setVisibility(View.GONE);
//                        findViewById(R.id.horizontalScrollView).setVisibility(View.GONE);
//                    }
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

    private void setDisplayImage() {
        SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.donationPrefsString), Context.MODE_PRIVATE);
        img.setImageURI(Uri.parse(prefs.getString(getResources().getString(R.string.imagePrefsString) + 0, null)));
    }

    private void getNumStoredImages(){
        DatabaseReference numImages = donationDatabase.child("numImages");

        numImages.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    int num = Integer.valueOf(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    setNumStoredImages(num);
                } catch(Exception e) {}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void setNumStoredImages(int num){
        numStoredImages = num;

        newImage(null, false);

        try {
            loadImages();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

