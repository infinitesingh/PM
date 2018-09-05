package com.info.inet.pm;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pub.devrel.easypermissions.EasyPermissions;

public class add_new extends AppCompatActivity {


    //int REQUEST_TAKE_PHOTO = 1;
    public static final int REQUEST_TAKE_PHOTO_DRIVER = 4, REQUEST_TAKE_PHOTO_VEHICLE =5, REQUEST_TAKE_PHOTO_OTHER =6;

    private static final int SELECT_PHOTO_DRIVER = 1, SELECT_PHOTO_VEHICLE = 2, SELECT_PHOTO_OTHER =3;
    int flag =1, imageuploadflag = 0, imageselecetionflag = 0;
    int categoryFlag = 1;  // 1 for driver, 2 for vehicle, 3 for others
    String mCurrentPhotoPath, timeStamp, realpath;

    Uri selectimage, downloadUrl;

    ProgressDialog uploadprogressdialog;
    UploadTask uploadpic;
    String imagename, imageurl;



    String selectedcategory ;


    Button driver, vehicle, other;
    ConstraintLayout driverLayout, vehicleLayout, otherLayout;

    EditText driver_name,driver_aadhar_no,driver_license,driver_city_prefer,driver_phone_no;
    EditText license_plate_no, other_licesnse_plate_no;
    ImageView driver_image, vehicle_image, otherVehicleImage;
    Button driver_save, driver_cancel;
    Button vehicle_save, vehicle_cancel, other_save,other_cancel,driver_capture,driver_select, driver_upload;
    Button vehicle_capture, vehicle_select, vehicle_upload, other_capture, other_select, other_upload;
    Spinner vehicle_type_spin, vehicle_avail_spin,other_vehicle_spin;

    RadioGroup tourTypeRadioGroup;
    RadioButton pilgrimage,sightseen;

    Color selectedColor, unselectedColor;

    //Spinner Values
    String vehicleType, vehicleAvail, otherVehicleType;

    String selectedTourType;

    FirebaseStorage imagestore;
    FirebaseAuth firebaseAuth;
    StorageReference imagestoreref, imageref;
    DatabaseReference databaseReference;
    GoogleSignInAccount account;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.help:
                Intent intent = new Intent(this,helpActivity.class);
                startActivity(intent);
                break;
            case R.id.action_logout:
                //Logout
                break;
            case R.id.user:
                //TODO: Check if already Logged In
                Intent intent1 = new Intent(this, logInActivity.class);
                startActivity(intent1);
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);


        StrictMode.VmPolicy.Builder newbuilder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(newbuilder.build());

        driver = findViewById(R.id.addDriverButton);
        vehicle = findViewById(R.id.addVehicleButton);
        other = findViewById(R.id.addOtherButton);


        driver.setTextColor(getResources().getColor(R.color.selected));


        account = GoogleSignIn.getLastSignedInAccount(this);

        driverLayout = findViewById(R.id.driver_detail);
        vehicleLayout = findViewById(R.id.vehicle_detail);
        otherLayout = findViewById(R.id.others_detail);

        //driver.setTextColor();
        vehicleLayout.setVisibility(View.GONE);
        otherLayout.setVisibility(View.GONE);


        driver_name = findViewById(R.id.driver_name_text);
        driver_license = findViewById(R.id.driver_license);
        driver_aadhar_no = findViewById(R.id.driver_aadhar_no);
        driver_city_prefer = findViewById(R.id.driver_city_prefer);;
        driver_phone_no = findViewById(R.id.driver_phone_no);
        driver_image = findViewById(R.id.driver_image);

        driver_save = findViewById(R.id.save);
        driver_cancel = findViewById(R.id.cancel);
        driver_capture = findViewById(R.id.captureButton);
        driver_select = findViewById(R.id.selectImage);
        driver_upload = findViewById(R.id.driverUpoadButton);



        vehicle_save = findViewById(R.id.vehicle_save);
        vehicle_cancel = findViewById(R.id.vehicle_cancel);
        vehicle_capture = findViewById(R.id.vehicleCaptureButton);
        vehicle_select = findViewById(R.id.vehicleGallaryButton);
        vehicle_upload = findViewById(R.id.vehicleUploadButton);


        license_plate_no = findViewById(R.id.license_plate_no);


        vehicle_type_spin = findViewById(R.id.vehicle_type_spinner);
        vehicle_avail_spin = findViewById(R.id.vehicle_avail_spinner);


        vehicle_image = findViewById(R.id.vehicle_image);
        otherVehicleImage = findViewById(R.id.other_vehicle_image);
        other_licesnse_plate_no = findViewById(R.id.other_license_plate_no);
        other_save = findViewById(R.id.other_save);
        other_cancel = findViewById(R.id.other_cancel);
        other_vehicle_spin = findViewById(R.id.other_vehicle_type_spin);
        other_capture = findViewById(R.id.otherCaptureButton);
        other_select = findViewById(R.id.otherGallerylButton);
        other_upload = findViewById(R.id.otherUpload);


        tourTypeRadioGroup = findViewById(R.id.OtherRadioGroup);
        pilgrimage = findViewById(R.id.pilgrim_radio);
        sightseen = findViewById(R.id.sightseen_radio);


        vehicle_type_spin.setOnItemSelectedListener(new vehicleTypeSelectionListener());
        other_vehicle_spin.setOnItemSelectedListener(new otherVehicleTypeSelectionListener());
        vehicle_avail_spin.setOnItemSelectedListener(new vehicleAvailSelectionListener());



        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherLayout.setVisibility(View.GONE);
                vehicleLayout.setVisibility(View.GONE);
                driverLayout.setVisibility(View.VISIBLE);
                //driver_image.setImageResource(android.R.color.transparent);
                categoryFlag = 1;
                driver.setTextColor(getResources().getColor(R.color.selected));
                vehicle.setTextColor(getResources().getColor(R.color.notSelected));
                other.setTextColor(getResources().getColor(R.color.notSelected));
                imageselecetionflag = 0;

            }
        });

        vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherLayout.setVisibility(View.GONE);
                driverLayout.setVisibility(View.GONE);
                vehicleLayout.setVisibility(View.VISIBLE);
                categoryFlag = 2;
                driver.setTextColor(getResources().getColor(R.color.notSelected));
                vehicle.setTextColor(getResources().getColor(R.color.selected));
                other.setTextColor(getResources().getColor(R.color.notSelected));
                imageselecetionflag = 0;

            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicleLayout.setVisibility(View.GONE);
                driverLayout.setVisibility(View.GONE);
                otherLayout.setVisibility(View.VISIBLE);
                categoryFlag = 3;
                driver.setTextColor(getResources().getColor(R.color.notSelected));
                vehicle.setTextColor(getResources().getColor(R.color.notSelected));
                other.setTextColor(getResources().getColor(R.color.selected));
                imageselecetionflag = 0;
            }
        });



        //Capture Image
        driver_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                                         /*
                                            Uri photoURI = FileProvider.getUriForFile(this,
                                            "com.example.android.fileprovider",
                                            photoFile);
                                         */
                        //TODO: flag =1;
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", photoFile));
                        flag = 1;
                        //imageselecetionflag = 1;
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO_DRIVER);
                    }
                }
            }
        });

        vehicle_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                                         /*
                                            Uri photoURI = FileProvider.getUriForFile(this,
                                            "com.example.android.fileprovider",
                                            photoFile);
                                         */
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", photoFile));
                        flag = 1;
                        //imageselecetionflag = 1;
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO_VEHICLE);
                    }
                }

            }
        });

        other_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                                         /*
                                            Uri photoURI = FileProvider.getUriForFile(this,
                                            "com.example.android.fileprovider",
                                            photoFile);
                                         */
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileprovider", photoFile));
                        flag = 1;
                        //imageselecetionflag = 1;
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO_OTHER);
                    }
                }
            }
        });

        driver_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] galleryPermissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

                if (EasyPermissions.hasPermissions(add_new.this, galleryPermissions)) {

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    //imageselecetionflag = 1;
                    flag = 0;
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO_DRIVER);

                }
                else {
                    ActivityCompat.requestPermissions(add_new.this,galleryPermissions,101);
                    //EasyPermissions.requestPermissions(this, "Access for storage",101, galleryPermissions);
                }


            }
        });

        vehicle_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] galleryPermissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

                if (EasyPermissions.hasPermissions(add_new.this, galleryPermissions)) {

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    //imageselecetionflag = 1;
                    flag = 0;
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO_VEHICLE);

                }
                else {
                    ActivityCompat.requestPermissions(add_new.this,galleryPermissions,101);
                    //EasyPermissions.requestPermissions(this, "Access for storage",101, galleryPermissions);
                }

            }
        });

        other_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] galleryPermissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

                if (EasyPermissions.hasPermissions(add_new.this, galleryPermissions)) {

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    //imageselecetionflag = 1;
                    flag = 0;
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO_OTHER);

                }
                else {
                    ActivityCompat.requestPermissions(add_new.this,galleryPermissions,101);
                    //EasyPermissions.requestPermissions(this, "Access for storage",101, galleryPermissions);
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        vehicle_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              uploadImage("Vehicle");
            }
        });

        other_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage("Other");
            }
        });

        driver_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage("Driver");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (flag){

            case 1: {

                if (requestCode != RESULT_CANCELED) {

                    imageselecetionflag = 1;

                    //driver_image.setImageDrawable(Drawable.createFromPath(mCurrentPhotoPath));
                    //vehicle_image.setImageDrawable(Drawable.createFromPath(mCurrentPhotoPath));
                    //otherVehicleImage.setImageDrawable(Drawable.createFromPath(mCurrentPhotoPath));

                    switch (requestCode) {

                        case REQUEST_TAKE_PHOTO_DRIVER:
                            //Uri capturedImage = data.getData();
                            driver_image.setImageDrawable(Drawable.createFromPath(mCurrentPhotoPath));
                            break;
                        case REQUEST_TAKE_PHOTO_VEHICLE:
                            vehicle_image.setImageDrawable(Drawable.createFromPath(mCurrentPhotoPath));
                            break;
                        case REQUEST_TAKE_PHOTO_OTHER:
                            otherVehicleImage.setImageDrawable(Drawable.createFromPath(mCurrentPhotoPath));
                            break;
                    }

                /*driver_image.setBackground(Drawable.createFromPath(mCurrentPhotoPath));
                vehicle_image.setBackground(Drawable.createFromPath(mCurrentPhotoPath));
                otherVehicleImage.setBackground(Drawable.createFromPath(mCurrentPhotoPath));*/

                }
                break;
            }
            case 0: {
                super.onActivityResult(requestCode, resultCode, data);
                imageselecetionflag = 1;
                switch (requestCode) {
                    case SELECT_PHOTO_DRIVER:
                        if (resultCode == RESULT_OK) {
                            //Toast.makeText(SellActivity.this,"Image selected, click on upload button",Toast.LENGTH_SHORT).show();
                            selectimage = data.getData();
                            realpath = getRealPathFromURI(selectimage);
                            //Toast.makeText(SellActivity.this,realpath,Toast.LENGTH_SHORT).show();
                            driver_image.setImageDrawable(Drawable.createFromPath(realpath));
                            break;
                        }

                    case SELECT_PHOTO_VEHICLE:
                        if (resultCode == RESULT_OK) {
                            //Toast.makeText(SellActivity.this,"Image selected, click on upload button",Toast.LENGTH_SHORT).show();
                            selectimage = data.getData();
                            realpath = getRealPathFromURI(selectimage);
                            //Toast.makeText(SellActivity.this,realpath,Toast.LENGTH_SHORT).show();
                            //driver_image.setBackground(Drawable.createFromPath(realpath));
                            vehicle_image.setImageDrawable(Drawable.createFromPath(realpath));
                            //otherVehicleImage.setBackground(Drawable.createFromPath(realpath));
                            break;
                        }

                    case SELECT_PHOTO_OTHER:
                        if (resultCode == RESULT_OK) {
                            //Toast.makeText(SellActivity.this,"Image selected, click on upload button",Toast.LENGTH_SHORT).show();
                            selectimage = data.getData();
                            realpath = getRealPathFromURI(selectimage);
                            //Toast.makeText(SellActivity.this,realpath,Toast.LENGTH_SHORT).show();
                            //driver_image.setBackground(Drawable.createFromPath(realpath));
                            //vehicle_image.setBackground(Drawable.createFromPath(realpath));
                            otherVehicleImage.setImageDrawable(Drawable.createFromPath(realpath));
                            break;
                        }
                }
                break;
            }
        }

    }


    public void uploadImage(String categoty){

        //final int uploadFlag = 1;



        if(imageselecetionflag == 0){
            Toast.makeText(add_new.this,"Capture or select an image to upload",Toast.LENGTH_SHORT).show();
            return;
        }

/*
        if (categoryFlag ==1)
            selectedcategory = "Driver";
        else if (categoryFlag ==2)
            selectedcategory = "Vehicle";
        else if(categoryFlag == 3)
            selectedcategory = "Other";
*/
        selectedcategory = categoty;
        String categoryfolder = "gs://pm-app-35121.appspot.com/" + selectedcategory;


        imagestore = FirebaseStorage.getInstance();
        imagestoreref = imagestore.getReferenceFromUrl(categoryfolder);

        if(flag ==1){
            selectimage = Uri.fromFile(new File(mCurrentPhotoPath));

        }

        else {
            selectimage = Uri.fromFile(new File(realpath));

        }

        imagename = selectimage.getLastPathSegment();

        imageref = imagestoreref.child(selectimage.getLastPathSegment());
        uploadprogressdialog = new ProgressDialog(this);
        uploadprogressdialog.setMax(100);
        uploadprogressdialog.setMessage("Uploading...");
        uploadprogressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        uploadprogressdialog.show();
        uploadprogressdialog.setCancelable(false);

        uploadpic = imageref.putFile(selectimage);

        uploadpic.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                //sets and increments value of progressbar
                uploadprogressdialog.incrementProgressBy((int) progress);
            }
        });




        uploadpic.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(add_new.this,"Error in uploading!",Toast.LENGTH_SHORT).show();
                imageuploadflag =0;
                uploadprogressdialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                //downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(add_new.this,"Upload successful", Toast.LENGTH_SHORT).show();
                imageuploadflag =1;

                //Get Image Url
                StorageReference storageRef = imagestore.getReference();
                storageRef.child(selectedcategory+"/"+imagename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Uri imageuri = taskSnapshot.getDownloadUrl();
                        imageurl = uri.toString();
                        //imageurl = "gs://help-nith.appspot.com/Book/imagesGBWA-20180201224903.jpg";
                        //Toast.makeText(SellActivity.this,imageurl,Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(SellActivity.this,"URL Not Found",Toast.LENGTH_SHORT).show();
                        imageurl = "https://firebasestorage.googleapis.com/v0/b/help-nith.appspot.com/o/ic_action_camera.png?alt=media&token=6465bb03-a35c-4a10-b30b-2ee6962b25f6";
                    }
                });

                uploadprogressdialog.dismiss();

                /*
                Picasso.with(SellActivity.this).load(downloadUrl)
                        .placeholder(R.drawable.place_holder)
                        .into(thumbview);
                        */
            }
        });
    }


    public void saveDriver(View view){

            if(imageuploadflag ==0){
                //Toast.makeText(this, "Saving Data Without Image", Toast.LENGTH_SHORT).show();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(add_new.this);
                builder.setMessage("Save data without Image?")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                driverDetail driverDetail = new driverDetail();
                                driverDetail.driverName = driver_name.getText().toString();
                                driverDetail.driverAdhaar = driver_aadhar_no.getText().toString();
                                driverDetail.driverCity = driver_city_prefer.getText().toString();
                                driverDetail.driverLicense = driver_license.getText().toString();
                                driverDetail.driverPhone = driver_phone_no.getText().toString();
                                //TODO:
                                driverDetail.driverImage = imageurl;
                                driverDetail.reqStatus = "Pending";
                                driverDetail.userId = account.getId();
                                firebaseAuth = FirebaseAuth.getInstance();
                                databaseReference = FirebaseDatabase.getInstance().getReference("Driver");
                                DatabaseReference newDriver = databaseReference.push();
                                newDriver.setValue(driverDetail);
                                Toast.makeText(add_new.this,"DriverSaved",Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                android.app.AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("No Image is uploaded");
                alertDialog.show();
            }
            else{

                driverDetail driverDetail = new driverDetail();
                driverDetail.driverName = driver_name.getText().toString();
                driverDetail.driverAdhaar = driver_aadhar_no.getText().toString();
                driverDetail.driverCity = driver_city_prefer.getText().toString();
                driverDetail.driverLicense = driver_license.getText().toString();
                driverDetail.driverPhone = driver_phone_no.getText().toString();
                //TODO:
                driverDetail.driverImage = imageurl;
                driverDetail.reqStatus = "Pending";
                driverDetail.userId = account.getId();
                firebaseAuth = FirebaseAuth.getInstance();
                databaseReference = FirebaseDatabase.getInstance().getReference("Driver");
                DatabaseReference newDriver = databaseReference.push();
                newDriver.setValue(driverDetail);
                Toast.makeText(add_new.this,"DriverSaved",Toast.LENGTH_SHORT).show();
                finish();

            }


    }

    public void saveVehicle(View view) {

        if(imageuploadflag ==0){

            // Toast.makeText(this, "Saving Data Without Image", Toast.LENGTH_SHORT).show();

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(add_new.this);
            builder.setMessage("Save data without Image?")
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            vehicleDetail vehicleDetail = new vehicleDetail();

                            vehicleDetail.vehicleType = vehicleType;
                            vehicleDetail.vehicleAvail = vehicleAvail;
                            vehicleDetail.vehiclePlateNo = license_plate_no.getText().toString();
                            //TODO:
                            vehicleDetail.vehicleImage = imageurl;
                            vehicleDetail.reqStatus = "Pending";
                            vehicleDetail.reqStatus = account.getId();

                            firebaseAuth = FirebaseAuth.getInstance();
                            databaseReference = FirebaseDatabase.getInstance().getReference("Vehicle");
                            DatabaseReference newVehicle = databaseReference.push();
                            newVehicle.setValue(vehicleDetail);

                            Toast.makeText(add_new.this,"VehicleSaved",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            android.app.AlertDialog alertDialog = builder.create();
            alertDialog.setTitle("No Image is uploaded");
            alertDialog.show();

        }
        else{
            vehicleDetail vehicleDetail = new vehicleDetail();

            vehicleDetail.vehicleType = vehicleType;
            vehicleDetail.vehicleAvail = vehicleAvail;
            vehicleDetail.vehiclePlateNo = license_plate_no.getText().toString();
            //TODO:
            vehicleDetail.vehicleImage = imageurl;
            vehicleDetail.reqStatus = "Pending";
            vehicleDetail.userId = account.getId();

            firebaseAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference("Vehicle");
            DatabaseReference newVehicle = databaseReference.push();
            newVehicle.setValue(vehicleDetail);

            Toast.makeText(add_new.this,"VehicleSaved",Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.pilgrim_radio:
                if(checked){
                    selectedTourType = "Pilgrimage";
                }
                break;
            case R.id.sightseen_radio:
                if(checked){
                    selectedTourType = "Sightseen";
                }
                break;
        }
    }

    public void saveOther(View view){


        if(imageuploadflag ==0){

            //Toast.makeText(this, "Saving Data Without Image", Toast.LENGTH_SHORT).show();

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(add_new.this);
            builder.setMessage("Save data without Image?")
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            otherDetail otherDetail = new otherDetail();
                            otherDetail.otherVehicleType = otherVehicleType;
                            otherDetail.otherVehicleLicensePlateNo =  other_licesnse_plate_no.getText().toString();
                            otherDetail.tourType = selectedTourType;
                            otherDetail.otherVehicleImage = imageurl;
                            otherDetail.reqStatus = "Pending";
                            otherDetail.userId = account.getId();

                            firebaseAuth = FirebaseAuth.getInstance();
                            databaseReference = FirebaseDatabase.getInstance().getReference("Other");
                            DatabaseReference newOther = databaseReference.push();
                            newOther.setValue(otherDetail);
                            Toast.makeText(add_new.this,"new Other Saved",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
            android.app.AlertDialog alertDialog = builder.create();
            alertDialog.setTitle("No Image is uploaded");
            alertDialog.show();

        }
        else{
            otherDetail otherDetail = new otherDetail();
            otherDetail.otherVehicleType = otherVehicleType;
            otherDetail.otherVehicleLicensePlateNo =  other_licesnse_plate_no.getText().toString();
            otherDetail.tourType = selectedTourType;
            otherDetail.otherVehicleImage = imageurl;
            otherDetail.reqStatus = "Pending";
            otherDetail.userId = account.getId();

            firebaseAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference("Other");
            DatabaseReference newOther = databaseReference.push();
            newOther.setValue(otherDetail);
            Toast.makeText(add_new.this,"new Other Saved",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    public void cancel(View view)
    {
        //Intent intent = new Intent(add_new.this, MainActivity.class);
        //startActivity(intent);
        finish();
    }

/*

    public void captureDriverImage(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) { b0
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                                         */
/*
                                            Uri photoURI = FileProvider.getUriForFile(this,
                                            "com.example.android.fileprovider",
                                            photoFile);
                                         *//*

                //TODO: flag =1;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                flag = 1;
                //TODO: imageselectionflag =1;
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }

*/
/*
    public void selectImage(View view){

        String[] galleryPermissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (EasyPermissions.hasPermissions(add_new.this, galleryPermissions)) {

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            imageselecetionflag = 1;
            flag = 0;
            startActivityForResult(photoPickerIntent, SELECT_PHOTO_DRIVER);

        }
        else {
            EasyPermissions.requestPermissions(this, "Access for storage",101, galleryPermissions);
        }

    }

    */

    /*
    public void getDriverImage(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("");
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                                         *//*
                                            Uri photoURI = FileProvider.getUriForFile(this,
                                            "com.example.android.fileprovider",
                                            photoFile);
                                         *//*
                        //TODO: flag =1;
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        flag = 1;
                        //TODO: imageselectionflag =1;
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }


            }
        }).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String[] galleryPermissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

                if (EasyPermissions.hasPermissions(add_new.this, galleryPermissions)) {

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    //TODO: flag =0;
                    //TODO: imageselectionflag =1;
                    flag = 0;
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);

                }
                else {
                    EasyPermissions.requestPermissions(this, "Access for storage",101, galleryPermissions);
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
*/


    public class vehicleTypeSelectionListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            vehicleType = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //Do Nothing
        }
    }

    public class otherVehicleTypeSelectionListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            otherVehicleType = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //Do Nothing
        }
    }

    public class vehicleAvailSelectionListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            vehicleAvail = parent.getItemAtPosition(position).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //Do Nothing
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



}
