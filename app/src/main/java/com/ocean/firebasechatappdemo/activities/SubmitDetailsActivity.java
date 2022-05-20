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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ocean.firebasechatappdemo.R;
import com.ocean.firebasechatappdemo.Util;
import com.ocean.firebasechatappdemo.databinding.ActivitySubmitDetailsBinding;

import java.util.regex.Pattern;

public class SubmitDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySubmitDetailsBinding binding;

    private FirebaseAuth firebaseAuth;
    private String imageInBase64;
    private boolean isUpdate = false;
    private String sender_name, sender_phone_num, sender_email, sender_job_status;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubmitDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

//        if (firebaseAuth.getCurrentUser() != null){ //TODO: logged in session without shared preference, that is inbuilt firebase logged in session
//
//            startActivity(new Intent(SubmitDetailsActivity.this, HomeActivity.class));
//            finish();
//        }

        binding.btnCaptureImage.setOnClickListener(this);
        binding.btnSubmitDetails.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_submit_details:

                //TODO: verify edit text, verify logged in phone no is same as input sender's phone no., submit_details is creating sender_user in firestore.
                validationOfEditText();
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

    }
}

