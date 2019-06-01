package com.example.donorapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class DonationAd extends AppCompatActivity {

    Button post;
    EditText title;
    EditText description;
    LinearLayout photosLL;
    ImageView img;

    ImageButton booking;
    ImageButton home;
    ImageButton settings;
    ImageButton statistics;

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private static int RESULT_LOAD_IMAGE = 1;
    public static final int REQUEST_IMAGE = 100;
    String imageFilePath = "";

    ArrayList<Button> buttons = new ArrayList<>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference donationDatabase;
    StorageReference imagesDatabase;

    private FirebaseAuth mAuth;

    int donationNumber = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_ad);

        booking = findViewById(R.id.bookingBtn);
        home = findViewById(R.id.homeBtn);
        settings = findViewById(R.id.settingsBtn);
        statistics = findViewById(R.id.statisticsBtn);
        post = findViewById(R.id.postBtn);
        title = findViewById(R.id.titleET);
        description = findViewById(R.id.descriptionET);
        photosLL = findViewById(R.id.photosLL);
        img = findViewById(R.id.imageView);

        firebaseDatabase = FirebaseDatabase.getInstance();
        donationDatabase = firebaseDatabase.getReference("Donations");
        imagesDatabase = FirebaseStorage.getInstance().getReference().child("donation images");
        mAuth = FirebaseAuth.getInstance();

        clearSharedPrefs();

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookingPage.class);
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

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StatisticsPage.class);
                startActivity(intent);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeDonation();
                post.setEnabled( false );
            }
        });

//        if ( getNumImages() > 0 ) {
//            newImage(null, false);
//            setStoredImages();
//        } else {
            newImage(null, false);
//        }

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
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            if (selectedImage != null) {
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
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

    public void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile;
            try {
                photoFile = createImageFile();
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
            Uri photoUri;
            if ( photoFile != null ) {
                photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(pictureIntent, REQUEST_IMAGE);
            }
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();

        return image;
    }

    private void newImage(final String imageFilePath, boolean storedImages ) {
        final Button newBtn = new Button( this );               //Create the new button
        newBtn.setId( buttons.size() );

        newBtn.setLayoutParams(new LinearLayout.LayoutParams(1,1));

        newBtn.setBackgroundResource(R.drawable.ic_add_photo);

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

                lastBtn.setBackground( ob );
                newBtn.setBackgroundResource(R.drawable.ic_add_photo);
                setButtonListener( lastBtn );
            }

            if ( !storedImages ) {
                storeImage(imageFilePath);

            }

        }

        buttons.add( newBtn );
    }

    public void showDialog(Activity activity, String title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.DialogTheme);

        if (title != null) builder.setTitle(title);

        builder.setMessage(message);
        builder.setPositiveButton("Take Photo", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if ( checkCameraPermissions() )
                    takePhoto();
            }
        });
        builder.setNeutralButton("Choose from Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if ( checkStoragePermissions() )
                    chooseGallery();
            }
        });
        builder.show();
    }

    private void takePhoto() {
        openCameraIntent();
    }

    private Boolean checkCameraPermissions(){
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<>();
        if (addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
        if (addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write External Storage");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message;
                StringBuilder builder = new StringBuilder();
                builder.append( "To use the Camera Feature you need to grant access to " );
                builder.append( permissionsNeeded.get( 0 ) );
                message = builder.toString();
                for (int i = 1; i < permissionsNeeded.size(); i++) {
                    builder.append( " and " );
                    builder.append( permissionsNeeded.get( i ) );
                    message = builder.toString();
                }
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return false;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private Boolean checkStoragePermissions(){
        List<String> permissionsNeeded = new ArrayList<>();

        final List<String> permissionsList = new ArrayList<>();
        if (addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write External Storage");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message;
                StringBuilder builder = new StringBuilder();
                builder.append( "To use the Gallery Feature you need to grant access to " );
                builder.append( permissionsNeeded.get( 0 ) );
                message = builder.toString();
                for (int i = 1; i < permissionsNeeded.size(); i++) {
                    builder.append( " and " );
                    builder.append( permissionsNeeded.get( i ) );
                    message = builder.toString();
                }
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return false;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            return !shouldShowRequestPermissionRationale(permission);
        }
        return false;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder( this );
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setPositiveButton("OK", okListener);
        builder.setNegativeButton("Cancel", null);
        AlertDialog alert = builder.create();
        alert.show();

        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        nbutton.setTextColor(Color.parseColor("#00897B"));
        pbutton.setTextColor(Color.parseColor("#00897B"));
    }

    private void chooseGallery() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    private void storeImage( String filePath){
        SharedPreferences prefs;
        prefs = getSharedPreferences(getResources().getString(R.string.donationPrefsString), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int index = getNumImages();

        editor.putString(getResources().getString(R.string.imagePrefsString) + index, filePath).apply();

        incrementNumImages();
    }

//    private void setStoredImages() {
//        int numImages = getNumImages();
//        SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.donationPrefsString), Context.MODE_PRIVATE);
//
//        for (int i = 0; i < numImages; i++) {
//            String filePath = prefs.getString(getResources().getString(R.string.imagePrefsString) + i, null);
//            newImage(filePath, true);
//        }
//    }

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

                    showDialog(DonationAd.this, null, null);

                }

            }
        });

    }

    private int getDonationNumber(){
//        SharedPreferences prefs;
//        prefs = getSharedPreferences(getResources().getString(R.string.donationPrefsString), Context.MODE_PRIVATE);
//        return prefs.getInt(getResources().getString(R.string.currentDonationString), 0);
        return donationNumber;
    }

    private void storeDonation(){
        if( getCurrentUser() != null ) {
            if(donationNumber == -1)
                generateDonationNum();

            DatabaseReference currentUserRef = donationDatabase.child(String.valueOf( donationNumber ));
            currentUserRef.child("userID").setValue( getCurrentUser() );
            currentUserRef.child("title").setValue(title.getText().toString().trim());
            currentUserRef.child("description").setValue(description.getText().toString().trim());

            storeImages();
        }
    }

    private void storeImages(){
        int numImages = getNumImages();
        SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.donationPrefsString), Context.MODE_PRIVATE);

        for (int i = 0; i < numImages; i++) {
            String filePath = prefs.getString(getResources().getString(R.string.imagePrefsString) + i, null);
            uploadImage( filePath, i + 1 );
        }

//        if ( numImages > 1 ){
//            Toast.makeText(DonationAd.this, numImages + " images uploaded", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(DonationAd.this, numImages + " image uploaded", Toast.LENGTH_SHORT).show();
//        }

        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
    }

    private void uploadImage(String filepath, int number){
        if(filepath != null) {
            Uri uri = Uri.fromFile(new File(filepath));

            StorageReference filePath = imagesDatabase.child( String.valueOf( getDonationNumber() ) ).child( String.valueOf( number ) );

            filePath.putFile( uri )
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(DonationAd.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            String message = exception.toString();
                            Toast.makeText(DonationAd.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void generateDonationNum(){
        final int min = 111111;
        final int max = 999999;
        donationNumber = new Random().nextInt((max - min) + 1) + min;
    }

    private String getCurrentUser(){
        return mAuth.getUid();
    }

    private void clearSharedPrefs(){
        SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.donationPrefsString), Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType( uri ));
    }

}
