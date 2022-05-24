package com.ocean.firebasechatappdemo.activities;

import static com.ocean.firebasechatappdemo.Util.showLongToast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ocean.firebasechatappdemo.FirebaseCommonDB;
import com.ocean.firebasechatappdemo.FirebaseDB;
import com.ocean.firebasechatappdemo.R;
import com.ocean.firebasechatappdemo.SessionManager;
import com.ocean.firebasechatappdemo.Util;
import com.ocean.firebasechatappdemo.databinding.ActivitySubmitDetailsBinding;
import com.ocean.firebasechatappdemo.model.UserModel;

import java.util.Objects;
import java.util.regex.Pattern;

public class SubmitDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySubmitDetailsBinding binding;

    private FirebaseAuth firebaseAuth;
    private String imageInBase64;
    private boolean isUpdate = false;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Bitmap bitmap;
    private SessionManager sessionManager;
    UserModel userModel;
    String TAG = "SUBMITDETAILSACTIVITY";
    String userName,userPhoneNum,userEmail,userJobStatus,userImage,userID,contactList;
    private FirebaseFirestore firestoreDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubmitDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userModel = new UserModel();
        firebaseAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(getApplicationContext());

//        if (firebaseAuth.getCurrentUser() != null){ //TODO: logged in session without shared preference, that is inbuilt firebase logged in session
//
//            startActivity(new Intent(SubmitDetailsActivity.this, HomeActivity.class));
//            finish();
//        }
        if (sessionManager.notLoggedIn()){
            sessionManager.loggedIn();
            startActivity(new Intent(SubmitDetailsActivity.this, HomeActivity.class));
            finish();
        }

        binding.btnCaptureImage.setOnClickListener(this);
        binding.btnSubmitDetails.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_submit_details:

                //TODO: verify edit text, verify logged in phone no is same as input sender's phone no., submit_details is creating sender_user in firestore.
                validationOfEditText();

                //CollectionReference collectionReference = firestoreDB.collection("userList");

                userModel.setUserName(Objects.requireNonNull(binding.etSenderName.getText().toString()));
                userModel.setUserEmail(Objects.requireNonNull(binding.etSenderEmail.getText().toString()));
                userModel.setUserJobStatus(Objects.requireNonNull(binding.etSenderJobStatus.getText().toString()));
                userModel.setUserImage(Objects.requireNonNull(Util.convertImageToString(binding.submitSendersProfileImage)));
                userModel.setUserPhoneNum(Objects.requireNonNull(binding.etSenderPhoneNum.getText().toString()));

//                collectionReference.add(userModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//
//                        Log.d(TAG, "onSuccess: " + documentReference);
//                        Toast.makeText(SubmitDetailsActivity.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                        Log.d(TAG, "onFailure: " + e);
//                        Toast.makeText(SubmitDetailsActivity.this, "Failed to submit, Please try later" + e, Toast.LENGTH_SHORT).show();
//
//                    }
//                });
                firestoreDB.collection("users").add(userModel)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {

                                Log.d(TAG, "onSuccess: " + documentReference);
                                Toast.makeText(SubmitDetailsActivity.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
                                sessionManager.loggedIn();
                                startActivity(new Intent(SubmitDetailsActivity.this, HomeActivity.class));
                                finish();
//
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.d(TAG, "onFailure: " + e);
                                Toast.makeText(SubmitDetailsActivity.this, "Failed to submit, Please try later" + e, Toast.LENGTH_SHORT).show();

                            }
                        });


                break;

            case R.id.btnCaptureImage:

                //TODO: selfcheck permission, capture, show captured image

                /*  if (ContextCompat.checkSelfPermission(SubmitDetailsActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    callCamera();
                }else {
                    ActivityCompat.requestPermissions(SubmitDetailsActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }   */

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater layoutInflater = getLayoutInflater();
                View dialogView = layoutInflater.inflate(R.layout.dialog_image_picker_cam, null);
                builder.setCancelable(true);
                builder.setView(dialogView);

                ImageView cam_cap = dialogView.findViewById(R.id.imageView_cam_cap);
                ImageView folder_cap = dialogView.findViewById(R.id.imageView_folder_cap);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                cam_cap.setOnClickListener(view1 -> {
                    cameraPermission();
                    alertDialog.dismiss();
                });
                folder_cap.setOnClickListener(view1 -> {
                    Intent folderIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityIfNeeded(folderIntent, 102);
                    alertDialog.dismiss();
                });

                break;
        }

    }

    private void callCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null){
            startActivityIfNeeded(cameraIntent,101);
        }
    }

    private void cameraPermission(){
        if (ContextCompat.checkSelfPermission(SubmitDetailsActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SubmitDetailsActivity.this, new String[]{Manifest.permission.CAMERA}, 101);
        }else callCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO: DO UR JOB
                callCamera();
            } else {
                showLongToast(this,"Permission Denied!");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK){
            bitmap = (Bitmap) data.getExtras().get("data");
            imageInBase64 = Util.convertBitmaptoBase64(bitmap);

            //TODO: setting image view with bitmap
            binding.submitSendersProfileImage.setImageBitmap(bitmap);

        }else if (requestCode == 102 && resultCode == RESULT_OK){
            assert data != null;
            Uri selectedImage = data.getData();
            binding.submitSendersProfileImage.setImageURI(selectedImage);
        }
    }

    private void validationOfEditText(){

        if(binding.submitSendersProfileImage.getDrawable() == null){
            //TODO: set error on empty image view
            Toast.makeText(this, "Profile Image is empty", Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(binding.etSenderName.getText().toString())){
            binding.etSenderName.setError("Name is empty !!!");
        }

        if (TextUtils.isEmpty(binding.etSenderPhoneNum.getText().toString())){
            binding.etSenderPhoneNum.setError("Phone Number is empty !!!");
        }else if (binding.etSenderPhoneNum.getText().toString().length() < 10){
            binding.etSenderPhoneNum.setError("Phone number is invalid");
        }

        if (TextUtils.isEmpty(binding.etSenderEmail.getText().toString())){
            binding.etSenderEmail.setError("Email can't be empty !!!");
        }else if (!TextUtils.isEmpty(binding.etSenderEmail.getText().toString()) && !binding.etSenderEmail.getText().toString().matches(emailPattern)){
            binding.etSenderEmail.setError("Invalid email address !!!");
        }

        if (TextUtils.isEmpty(binding.etSenderJobStatus.getText().toString())){
            binding.etSenderJobStatus.setError("Job status is empty !!!");
        }

//        if ("+91" +binding.etSenderPhoneNum.getText().toString() != sessionManager.getUserPhoneNum()){
//            Toast.makeText(this, "Logged-In phone number does not match with above given number...", Toast.LENGTH_SHORT).show();
//        }

    }
}

